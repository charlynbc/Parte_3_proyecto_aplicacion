#!/usr/bin/env bash
set -euo pipefail

# Directorio del proyecto (raíz)
ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
TOMCAT_DIR="$ROOT_DIR/apache-tomcat-10.1.47"
LOG_FILE="$TOMCAT_DIR/logs/catalina.out"
BASE_URL="http://localhost:8080"
APP_CTXT="/Laboratorio2PaP"

color() { local c="$1"; shift; echo -e "\033[${c}m$*\033[0m"; }
info() { color "1;34" "[INFO] $*"; }
ok()   { color "1;32" "[ OK ] $*"; }
warn() { color "1;33" "[WARN] $*"; }
err()  { color "1;31" "[ERR ] $*"; }

start() {
  info "Iniciando Tomcat..."
  "$TOMCAT_DIR/bin/startup.sh" >/dev/null 2>&1 || true
  # Esperar a que responda el root
  for i in {1..60}; do
    if curl -s -o /dev/null -w "%{http_code}" "$BASE_URL/" | grep -q "^200$"; then
      ok "Tomcat arriba en $BASE_URL/"
      return 0
    fi
    sleep 1
  done
  err "Timeout esperando a Tomcat en $BASE_URL/"
  return 1
}

stop() {
  info "Deteniendo Tomcat..."
  "$TOMCAT_DIR/bin/shutdown.sh" >/dev/null 2>&1 || true
  # Esperar a que deje de responder
  for i in {1..30}; do
    if ! curl -s -o /dev/null -w "%{http_code}" "$BASE_URL/" | grep -q "^200$"; then
      ok "Tomcat detenido"
      return 0
    fi
    sleep 1
  done
  warn "Puede que Tomcat siga activo; revisa el proceso o el puerto 8080."
}

status() {
  if curl -s -o /dev/null -w "%{http_code}" "$BASE_URL/" | grep -q "^200$"; then
    ok "Tomcat respondiendo en $BASE_URL/"
  else
    warn "Tomcat no responde en $BASE_URL/"
    return 1
  fi
}

health() {
  local fail=0
  info "Chequeo raíz Tomcat"
  if curl -s -o /dev/null -w "%{http_code}" "$BASE_URL/" | grep -q "^200$"; then ok "Root 200"; else err "Root NO 200"; fail=1; fi

  info "Chequeo init-data ($APP_CTXT/init-data)"
  if curl -s -o /dev/null -w "%{http_code}" "$BASE_URL$APP_CTXT/init-data" | grep -q "^200$"; then ok "init-data 200"; else err "init-data NO 200"; fail=1; fi

  info "Chequeo users ($APP_CTXT/users)"
  if curl -s -o /dev/null -w "%{http_code}" "$BASE_URL$APP_CTXT/users" | grep -q "^200$"; then ok "users 200"; else err "users NO 200"; fail=1; fi

  if [[ $fail -ne 0 ]]; then
    warn "Revisando últimas 80 líneas de logs: $LOG_FILE"
    tail -n 80 "$LOG_FILE" || true
    return 1
  fi
}

logs() {
  info "Mostrando últimas 200 líneas de $LOG_FILE"
  tail -n 200 "$LOG_FILE" || true
}

usage() {
  cat <<EOF
Uso: scripts/tomcat.sh <comando>

Comandos:
  start     Arranca Tomcat y espera a que responda
  stop      Detiene Tomcat y espera a que caiga
  status    Verifica si Tomcat responde en $BASE_URL/
  health    Chequea root, $APP_CTXT/init-data y $APP_CTXT/users
  logs      Muestra el tail de catalina.out
EOF
}

cmd="${1:-}" || true
case "$cmd" in
  start)  start ;;
  stop)   stop ;;
  status) status ;;
  health) health ;;
  logs)   logs ;;
  *) usage; exit 1;;
esac
