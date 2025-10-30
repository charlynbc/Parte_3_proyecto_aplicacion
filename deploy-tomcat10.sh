#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$ROOT_DIR/Laboratorio2PaP-main"
TOMCAT_DIR="$ROOT_DIR/apache-tomcat-10.1.47"
WAR_NAME="Laboratorio2PaP.war"
APP_NAME="Laboratorio2PaP"

info() { echo -e "\033[1;34m[DEPLOY]\033[0m $*"; }
warn() { echo -e "\033[1;33m[WARN]\033[0m $*"; }
err()  { echo -e "\033[1;31m[ERR ]\033[0m $*"; }

if [[ ! -d "$PROJECT_DIR" ]]; then
  err "No se encontró el directorio del proyecto Maven: $PROJECT_DIR"; exit 1;
fi
if [[ ! -d "$TOMCAT_DIR" ]]; then
  err "No se encontró Tomcat en: $TOMCAT_DIR"; exit 1;
fi

info "Compilando WAR (skip tests)"
pushd "$PROJECT_DIR" >/dev/null
mvn -q -DskipTests clean package
popd >/dev/null

WAR_PATH="$PROJECT_DIR/target/$WAR_NAME"
if [[ ! -f "$WAR_PATH" ]]; then
  err "WAR no generado: $WAR_PATH"; exit 1;
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
