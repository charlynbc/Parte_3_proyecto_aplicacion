#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
BASE_TOMCAT="$ROOT_DIR/apache-tomcat-10.1.47"
CENTRAL_TOMCAT="$ROOT_DIR/apache-tomcat-10.1.47-central"

info(){ echo -e "\033[1;34m[CENTRAL]\033[0m $*"; }
warn(){ echo -e "\033[1;33m[WARN]\033[0m $*"; }
err(){ echo -e "\033[1;31m[ERR ]\033[0m $*"; }

cmd=${1:-status}

ensure_base(){
  if [[ ! -d "$BASE_TOMCAT" ]]; then err "No existe base Tomcat: $BASE_TOMCAT"; exit 1; fi
  if [[ ! -d "$CENTRAL_TOMCAT" ]]; then
    info "Creando instancia Tomcat Central en 8081..."
    cp -a "$BASE_TOMCAT" "$CENTRAL_TOMCAT"
    sed -i 's/port="8005"/port="8006"/g' "$CENTRAL_TOMCAT/conf/server.xml"
    sed -i 's/port="8080" protocol="HTTP\/1.1"/port="8081" protocol="HTTP\/1.1"/g' "$CENTRAL_TOMCAT/conf/server.xml"
    sed -i 's/redirectPort="8443"/redirectPort="8444"/g' "$CENTRAL_TOMCAT/conf/server.xml"
    if grep -q 'protocol="AJP/1.3"' "$CENTRAL_TOMCAT/conf/server.xml"; then
      sed -i 's/port="8009" protocol="AJP\/1.3"/port="8010" protocol="AJP\/1.3"/g' "$CENTRAL_TOMCAT/conf/server.xml"
    fi
  fi
}

start(){ ensure_base; info "Iniciando Tomcat Central (8081)"; "$CENTRAL_TOMCAT/bin/startup.sh"; }
stop(){ if [[ -d "$CENTRAL_TOMCAT" ]]; then info "Deteniendo Tomcat Central"; "$CENTRAL_TOMCAT/bin/shutdown.sh" || true; else warn "No hay instancia central"; fi }
status(){ ps -ef | grep -i '[o]rg.apache.catalina.startup.Bootstrap' | grep "$CENTRAL_TOMCAT" || true; }
logs(){ tail -n 120 "$CENTRAL_TOMCAT/logs/catalina.out" || echo "no logs"; }

case "$cmd" in
  start) start;;
  stop) stop;;
  restart) stop; sleep 2; start;;
  status) status;;
  logs) logs;;
  *) echo "Uso: $0 {start|stop|restart|status|logs}"; exit 1;;
esac
