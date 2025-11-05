#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$ROOT_DIR/Laboratorio2PaP-main"
TOMCAT_DIR="$ROOT_DIR/apache-tomcat-10.1.47"
WAR_NAME="Laboratorio2PaP.war"
APP_NAME="Laboratorio2PaP"

# Asegurar Java 17 para Maven y Tomcat
export JAVA_HOME="/usr/lib/jvm/java-17-openjdk-amd64"
export PATH="$JAVA_HOME/bin:$PATH"

info() { echo -e "\033[1;34m[DEPLOY]\033[0m $*"; }
warn() { echo -e "\033[1;33m[WARN]\033[0m $*"; }
err()  { echo -e "\033[1;31m[ERR ]\033[0m $*"; }

# If a .env file is present in the repository root, load and export its variables
ENV_FILE="$ROOT_DIR/.env"
if [[ -f "$ENV_FILE" ]]; then
  info "Cargando variables de entorno desde $ENV_FILE"
  # export all variables defined in the file
  set -o allexport
  # shellcheck disable=SC1090
  source "$ENV_FILE"
  set +o allexport
  # Si DB_URL quedó vacío tras cargar .env, eliminarlo del entorno para evitar que la JVM lo vea como definido
  if [[ -v DB_URL ]] && [[ -z "${DB_URL}" ]]; then
    unset DB_URL
  fi
  if [[ -v DB_USER ]] && [[ -z "${DB_USER}" ]]; then
    unset DB_USER
  fi
  if [[ -v DB_PASSWORD ]] && [[ -z "${DB_PASSWORD}" ]]; then
    unset DB_PASSWORD
  fi
fi

if [[ ! -d "$PROJECT_DIR" ]]; then
  err "No se encontró el directorio del proyecto Maven: $PROJECT_DIR"; exit 1;
fi
if [[ ! -d "$TOMCAT_DIR" ]]; then
  err "No se encontró Tomcat en: $TOMCAT_DIR"; exit 1;
fi

info "Compilando WAR (skip tests)"
pushd "$PROJECT_DIR" >/dev/null
java -version || true
mvn -q -DskipTests clean package
popd >/dev/null

WAR_PATH="$PROJECT_DIR/target/$WAR_NAME"
if [[ ! -f "$WAR_PATH" ]]; then
  err "WAR no generado: $WAR_PATH"; exit 1;
fi

# Parchear Laboratorio1.jar para reemplazar META-INF/persistence.xml por una versión JNDI (config/persistence.override.xml)
OVERRIDE_XML="$ROOT_DIR/config/persistence.override.xml"
if [[ -f "$OVERRIDE_XML" ]]; then
  info "Aplicando override de persistence.xml en Laboratorio1.jar (JNDI jdbc/railway)"
  TMP_DIR="$(mktemp -d)"
  unzip -q "$WAR_PATH" -d "$TMP_DIR"
  if [[ -f "$TMP_DIR/WEB-INF/lib/Laboratorio1.jar" ]]; then
    mkdir -p "$TMP_DIR/META-INF"
    cp "$OVERRIDE_XML" "$TMP_DIR/META-INF/persistence.xml"
    (cd "$TMP_DIR" && zip -q -u "WEB-INF/lib/Laboratorio1.jar" META-INF/persistence.xml)
    # Reempaquetar el WAR con el JAR modificado
    (cd "$TMP_DIR" && zip -q -r "$WAR_PATH" .)
  else
    warn "No se encontró WEB-INF/lib/Laboratorio1.jar dentro del WAR; se omite override"
  fi
  rm -rf "$TMP_DIR"
else
  warn "No existe $OVERRIDE_XML; se omite override de persistence.xml"
fi

info "Deteniendo Tomcat"
"$TOMCAT_DIR/bin/shutdown.sh" || true
sleep 2 || true

info "Limpiando despliegue previo"
rm -rf "$TOMCAT_DIR/webapps/$APP_NAME" \
       "$TOMCAT_DIR/webapps/$WAR_NAME" \
       "$TOMCAT_DIR/work/Catalina/localhost/$APP_NAME"* || true

info "Copiando WAR a webapps"
cp "$WAR_PATH" "$TOMCAT_DIR/webapps/"

info "Iniciando Tomcat"
"$TOMCAT_DIR/bin/startup.sh"

# Mostrar un resumen de logs sin bloquear la terminal
sleep 2 || true
LOG_FILE="$TOMCAT_DIR/logs/catalina.out"
if [[ -f "$LOG_FILE" ]]; then
  info "Últimas líneas de catalina.out:"
  tail -n 120 "$LOG_FILE" || true
else
  warn "No se encontró catalina.out aún. Revisa más tarde en: $LOG_FILE"
fi

info "Despliegue completado. URL: http://localhost:8080/$APP_NAME"
