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
