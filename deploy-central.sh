#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CENTRAL_DIR="$ROOT_DIR/Laboratorio3Pap-main/central-ws"
CENTRAL_TOMCAT="$ROOT_DIR/apache-tomcat-10.1.47-central"

info(){ echo -e "\033[1;34m[DEPLOY-CENTRAL]\033[0m $*"; }
err(){ echo -e "\033[1;31m[ERR ]\033[0m $*"; }

if [[ ! -d "$CENTRAL_DIR" ]]; then err "No se encontró $CENTRAL_DIR"; exit 1; fi

info "Compilando WAR central-ws (skip tests)"
mvn -q -DskipTests package -f "$CENTRAL_DIR/pom.xml"
WAR="$CENTRAL_DIR/target/central-ws.war"
if [[ ! -f "$WAR" ]]; then err "No se generó el WAR: $WAR"; exit 1; fi

# Parchear Laboratorio1.jar dentro del WAR para usar JNDI (jdbc/railway)
OVERRIDE_XML="$ROOT_DIR/config/persistence.override.xml"
if [[ -f "$OVERRIDE_XML" ]]; then
  info "Aplicando override de persistence.xml en Laboratorio1.jar (JNDI jdbc/railway) para central-ws"
  TMP_DIR="$(mktemp -d)"
  unzip -q "$WAR" -d "$TMP_DIR"
  LAB1_PATH=$(ls "$TMP_DIR/WEB-INF/lib"/Laboratorio1*.jar 2>/dev/null | head -n1 || true)
  if [[ -n "${LAB1_PATH}" && -f "${LAB1_PATH}" ]]; then
    mkdir -p "$TMP_DIR/META-INF"
    cp "$OVERRIDE_XML" "$TMP_DIR/META-INF/persistence.xml"
    (cd "$TMP_DIR" && zip -q -u "${LAB1_PATH#${TMP_DIR}/}" META-INF/persistence.xml)
    # Reempaquetar WAR
    (cd "$TMP_DIR" && zip -q -r "$WAR" .)
  else
    info "No se encontró Laboratorio1*.jar en el WAR; se omite override"
  fi
  rm -rf "$TMP_DIR"
fi

info "Arrancando instancia Tomcat Central (8081)"
"$ROOT_DIR/scripts/tomcat-central.sh" start || true
sleep 2 || true

info "Copiando WAR a webapps (8081)"
cp -f "$WAR" "$CENTRAL_TOMCAT/webapps/"

info "Esperando despliegue y comprobando WSDL"
sleep 3
HTTP=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:8081/central-ws/services/AuthService?wsdl || true)
echo "WSDL AuthService -> HTTP $HTTP"
if [[ "$HTTP" != "200" ]]; then
  info "Revisar logs con: scripts/tomcat-central.sh logs"
  exit 1
fi

info "Despliegue Central OK: http://localhost:8081/central-ws/services/AuthService?wsdl"
