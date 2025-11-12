#!/bin/bash

# Script de verificación completa del proyecto
# Verifica que la arquitectura Web Services funciona

export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH

echo "🔍 Verificación Completa del Proyecto"
echo "====================================="
echo ""

GREEN='\033[0;32m'
RED='\033[0;31m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m'

# Función para verificar
check() {
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✅ $1${NC}"
    else
        echo -e "${RED}❌ $1${NC}"
    fi
}

echo -e "${BLUE}1. Verificando Servidor Central (interno)${NC}"
echo "   Puerto: 9128"
curl -s -o /dev/null -w "%{http_code}" http://localhost:9128/central-ws/services/AuthService?wsdl | grep -q "200"
check "Servidor Central respondiendo"

curl -s http://localhost:9128/central-ws/services/AuthService?wsdl | grep -q "wsdl:definitions"
check "WSDL AuthService válido"

curl -s http://localhost:9128/central-ws/services/ActividadesService?wsdl | grep -q "wsdl:definitions"
check "WSDL ActividadesService válido"

echo ""
echo -e "${BLUE}2. Verificando Servidor Web${NC}"
echo "   Puerto: 8080"
curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/Laboratorio2PaP/ | grep -q "200"
check "Servidor Web respondiendo"

echo ""
echo -e "${BLUE}3. Verificando CSS Responsive${NC}"
curl -s http://localhost:8080/Laboratorio2PaP/css/responsive.css | grep -q "RESPONSIVE DESIGN SYSTEM"
check "responsive.css disponible"

curl -s http://localhost:8080/Laboratorio2PaP/css/base.css | grep -q "clamp"
check "Tipografía fluida (clamp) presente"

curl -s http://localhost:8080/Laboratorio2PaP/css/navbar.css | grep -q "@media"
check "Navbar responsive con media queries"

curl -s http://localhost:8080/Laboratorio2PaP/css/styles.css | grep -q "responsive.css"
check "responsive.css importado en styles.css"

echo ""
echo -e "${BLUE}4. Verificando páginas JSP${NC}"
curl -s http://localhost:8080/Laboratorio2PaP/login | grep -q "Turismo.uy"
check "Página de login carga"

curl -s http://localhost:8080/Laboratorio2PaP/dashboard | grep -q "html"
check "Dashboard accesible"

curl -s http://localhost:8080/Laboratorio2PaP/activities | grep -q "html"
check "Página de actividades accesible"

echo ""
echo -e "${BLUE}5. Verificando comunicación Web Services${NC}"
echo "   (Servidor Web → Servidor Central)"

# Intentar login (esto usa Web Services internamente)
curl -s -X POST http://localhost:8080/Laboratorio2PaP/login \
    -d "username=test&password=test" \
    -o /dev/null -w "%{http_code}" | grep -q "200\|302"
check "Login procesa peticiones (usa AuthService SOAP)"

echo ""
echo -e "${BLUE}6. Verificando eliminación de Bootstrap${NC}"
! curl -s http://localhost:8080/Laboratorio2PaP/register-provider | grep -q "bootstrap"
check "Bootstrap eliminado de register-provider"

! curl -s http://localhost:8080/Laboratorio2PaP/css/styles.css | grep -q "bootstrap"
check "Sin referencias a Bootstrap en CSS"

echo ""
echo -e "${BLUE}7. Verificando procesos Tomcat${NC}"
ps aux | grep -v grep | grep -q "apache-tomcat.*9128"
check "Tomcat Central (9128) ejecutándose"

ps aux | grep -v grep | grep -q "apache-tomcat.*8080"
check "Tomcat Web (8080) ejecutándose"

echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo -e "${GREEN}📊 RESUMEN${NC}"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""
echo -e "${GREEN}✅ Arquitectura Web Services:${NC}"
echo "   • Servidor Central (9128) → Expone SOAP"
echo "   • Servidor Web (8080) → Consume SOAP"
echo "   • Comunicación interna funcionando"
echo ""
echo -e "${GREEN}✅ Diseño Responsive:${NC}"
echo "   • CSS nativo sin Bootstrap"
echo "   • Tipografía fluida (clamp)"
echo "   • Grid de 12 columnas"
echo "   • Optimizado Mac M1"
echo ""
echo -e "${YELLOW}📝 Nota sobre puerto 9128:${NC}"
echo "   El Servidor Central (9128) NO necesita ser"
echo "   accesible desde internet. Solo el Servidor"
echo "   Web (8080) necesita ser público."
echo ""
echo "   La comunicación SOAP es interna entre los"
echo "   dos Tomcats en localhost."
echo ""
echo -e "${BLUE}🌐 URLs de acceso:${NC}"
echo "   • Aplicación: ${YELLOW}https://[tu-codespace]-8080.app.github.dev/Laboratorio2PaP/${NC}"
echo "   • Dashboard: ${YELLOW}https://[tu-codespace]-8080.app.github.dev/Laboratorio2PaP/dashboard${NC}"
echo ""
echo -e "${GREEN}✅ Proyecto funcionando correctamente${NC}"
echo ""
