#!/usr/bin/env bash
set -euo pipefail

# --- Descubrir directorios de Central y Web (ajustá si querés forzar) ---
CANDIDATES_CENTRAL=("Laboratorio1PaP-main" "parte 3/servidor-central" "servidor-central" "central")
CANDIDATES_WEB=("Laboratorio2PaP-main" "parte 3/servidor-web" "servidor-web" "web")

find_dir() {
  local -n arr=$1
  for d in "${arr[@]}"; do
    if [ -f "$d/pom.xml" ]; then
      echo "$d"; return 0
    fi
  done
  return 1
}

CENTRAL_DIR=$(find_dir CANDIDATES_CENTRAL || true)
WEB_DIR=$(find_dir CANDIDATES_WEB || true)

if [ -z "${CENTRAL_DIR:-}" ] || [ -z "${WEB_DIR:-}" ]; then
  echo "❌ No pude localizar CENTRAL o WEB. Ajustá rutas en el script."
  echo "   CENTRAL candidates: ${CANDIDATES_CENTRAL[*]}"
  echo "   WEB candidates:     ${CANDIDATES_WEB[*]}"
  exit 1
fi

echo "📁 Central: $CENTRAL_DIR"
echo "📁 Web:     $WEB_DIR"

# --- 1) Compilar ambos con Maven (sin tests) ---
echo "🏗️ Compilando Central..."
mvn -q -f "$CENTRAL_DIR/pom.xml" -DskipTests=true package
echo "✅ Central OK"

echo "🏗️ Compilando Web..."
mvn -q -f "$WEB_DIR/pom.xml" -DskipTests=true package
echo "✅ Web OK"

# --- 2) Verificar que el Web NO depende del JAR del Central ---
echo "🔎 Revisando pom del Web por dependencia al Central..."
if grep -Rni --color=always -E "(Laboratorio1PaP|servidor-central|artifactId>.*central<|groupId>.*central<)" "$WEB_DIR/pom.xml"; then
  echo "❌ Encontré algo que parece dependencia al Central en el pom del Web."
  exit 2
else
  echo "✅ pom del Web sin dependencia al Central"
fi

# --- 3) Verificar que en el Web NO existan imports a logica.* ---
echo "🔎 Buscando imports 'logica.*' en código del Web..."
if grep -Rni --include="*.java" -E "import\s+.*logica\." "$WEB_DIR/src" 2>/dev/null; then
  echo "❌ Hay imports a 'logica.*' en el Web. Deben reemplazarse por soapclient.*"
  exit 3
else
  echo "✅ Sin imports 'logica.*' en el Web"
fi

# --- 4) Recordatorio de flujo manual a probar ---
echo "🧪 Próxima prueba manual:"
echo "   1) Levantar el WS del Central (WSPublisher) -> http://localhost:9000/central?wsdl"
echo "   2) Desplegar el Web en Tomcat -> abrir /login.jsp"
echo "   3) Flujo: /login.jsp → /home.jsp → /actividad?nombre=... → /salida?nombre=... → /logout"
echo "   4) Ver en DevTools (Toggle device toolbar) que se vea bien en móvil"
echo "📄 README.md: recordá mantenerlo actualizado."
echo "🎉 Checklist automatizado OK."
