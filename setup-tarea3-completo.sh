#!/bin/bash

# Script de Setup Completo - Tarea 3
# Programación de Aplicaciones 2025

set -e

GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

echo -e "${BLUE}╔═══════════════════════════════════════════════════════════╗${NC}"
echo -e "${BLUE}║   SETUP COMPLETO - TAREA 3 - Mac M1                      ║${NC}"
echo -e "${BLUE}║   Programación de Aplicaciones 2025                      ║${NC}"
echo -e "${BLUE}╚═══════════════════════════════════════════════════════════╝${NC}"
echo ""

# 1. Verificar Java 17
echo -e "${BLUE}[1/10] Verificando Java 17...${NC}"
if command -v java >/dev/null 2>&1; then
    JAVA_VER=$(java -version 2>&1 | head -1)
    echo -e "${GREEN}✅ $JAVA_VER${NC}"
else
    echo -e "${RED}❌ Java no encontrado${NC}"
    echo -e "${YELLOW}Instalar: brew install --cask temurin17${NC}"
    exit 1
fi

# 2. Verificar Maven
echo -e "${BLUE}[2/10] Verificando Maven...${NC}"
if command -v mvn >/dev/null 2>&1; then
    MVN_VER=$(mvn -version | head -1)
    echo -e "${GREEN}✅ $MVN_VER${NC}"
else
    echo -e "${RED}❌ Maven no encontrado${NC}"
    echo -e "${YELLOW}Instalar: brew install maven${NC}"
    exit 1
fi

# 3. Configurar permisos
echo -e "${BLUE}[3/10] Configurando permisos...${NC}"
chmod +x *.sh 2>/dev/null || true
if [ -d "apache-tomcat-10.1.47-central/bin" ]; then
    chmod +x apache-tomcat-10.1.47-central/bin/*.sh
fi
if [ -d "apache-tomcat-10.1.47/bin" ]; then
    chmod +x apache-tomcat-10.1.47/bin/*.sh
fi
echo -e "${GREEN}✅ Permisos configurados${NC}"

# 4. Compilar Servidor Central
echo -e "${BLUE}[4/10] Compilando Servidor Central (Web Services)...${NC}"
cd Laboratorio3Pap-main/central-ws
mvn clean install -DskipTests
if [ $? -eq 0 ]; then
    echo -e "${GREEN}✅ Servidor Central compilado${NC}"
else
    echo -e "${RED}❌ Error compilando Servidor Central${NC}"
    exit 1
fi
cd ../..

# 5. Iniciar Tomcat Central
echo -e "${BLUE}[5/10] Iniciando Tomcat Central (puerto 9128)...${NC}"
if [ -d "apache-tomcat-10.1.47-central" ]; then
    cd apache-tomcat-10.1.47-central
    ./bin/startup.sh
    cd ..
    echo -e "${GREEN}✅ Tomcat Central iniciado${NC}"
    echo -e "${YELLOW}⏳ Esperando 15 segundos para que inicie completamente...${NC}"
    sleep 15
else
    echo -e "${RED}❌ Tomcat Central no encontrado${NC}"
    exit 1
fi

# 6. Verificar WSDLs
echo -e "${BLUE}[6/10] Verificando Web Services...${NC}"
if curl -s http://localhost:9128/central-ws/services/AuthService?wsdl | grep -q "wsdl:definitions"; then
    echo -e "${GREEN}✅ AuthService WSDL accesible${NC}"
else
    echo -e "${RED}❌ AuthService WSDL no accesible${NC}"
    echo -e "${YELLOW}Verificar logs: tail -f apache-tomcat-10.1.47-central/logs/catalina.out${NC}"
fi

if curl -s http://localhost:9128/central-ws/services/ActividadesService?wsdl | grep -q "wsdl:definitions"; then
    echo -e "${GREEN}✅ ActividadesService WSDL accesible${NC}"
else
    echo -e "${YELLOW}⚠️  ActividadesService WSDL no accesible (puede no estar implementado aún)${NC}"
fi

# 7. Generar Cliente SOAP
echo -e "${BLUE}[7/10] Generando stubs SOAP (client-ws)...${NC}"
cd Laboratorio3Pap-main/client-ws
mvn clean compile install
if [ $? -eq 0 ]; then
    echo -e "${GREEN}✅ Cliente SOAP generado${NC}"
else
    echo -e "${RED}❌ Error generando cliente SOAP${NC}"
    exit 1
fi
cd ../..

# 8. Compilar Servidor Web
echo -e "${BLUE}[8/10] Compilando Servidor Web (Bootstrap + Responsive)...${NC}"
cd Laboratorio2PaP-main
mvn clean package
if [ $? -eq 0 ]; then
    echo -e "${GREEN}✅ Servidor Web compilado${NC}"
else
    echo -e "${RED}❌ Error compilando Servidor Web${NC}"
    exit 1
fi
cd ..

# 9. Iniciar Tomcat Web
echo -e "${BLUE}[9/10] Iniciando Tomcat Web (puerto 8080)...${NC}"
if [ -d "apache-tomcat-10.1.47" ]; then
    cd apache-tomcat-10.1.47
    ./bin/startup.sh
    cd ..
    echo -e "${GREEN}✅ Tomcat Web iniciado${NC}"
    echo -e "${YELLOW}⏳ Esperando 10 segundos...${NC}"
    sleep 10
else
    echo -e "${RED}❌ Tomcat Web no encontrado${NC}"
    exit 1
fi

# 10. Verificación final
echo -e "${BLUE}[10/10] Verificación final...${NC}"

if lsof -Pi :9128 -sTCP:LISTEN -t >/dev/null 2>&1; then
    echo -e "${GREEN}✅ Puerto 9128 (Central) activo${NC}"
else
    echo -e "${RED}❌ Puerto 9128 no está escuchando${NC}"
fi

if lsof -Pi :8080 -sTCP:LISTEN -t >/dev/null 2>&1; then
    echo -e "${GREEN}✅ Puerto 8080 (Web) activo${NC}"
else
    echo -e "${RED}❌ Puerto 8080 no está escuchando${NC}"
fi

echo ""
echo -e "${GREEN}╔═══════════════════════════════════════════════════════════╗${NC}"
echo -e "${GREEN}║   ✅ SETUP COMPLETO - TAREA 3 LISTA                      ║${NC}"
echo -e "${GREEN}╚═══════════════════════════════════════════════════════════╝${NC}"
echo ""
echo -e "${BLUE}🌐 URLs de la aplicación:${NC}"
echo -e "  Aplicación Web: ${YELLOW}http://localhost:8080/Laboratorio2PaP/${NC}"
echo -e "  AuthService WSDL: ${YELLOW}http://localhost:9128/central-ws/services/AuthService?wsdl${NC}"
echo -e "  ActividadesService WSDL: ${YELLOW}http://localhost:9128/central-ws/services/ActividadesService?wsdl${NC}"
echo ""
echo -e "${BLUE}📝 Casos de Uso implementados:${NC}"
echo -e "  ✓ Inicio de Sesión (nick o email + contraseña)"
echo -e "  ✓ Cierre de Sesión"
echo -e "  ✓ Consulta de Actividad Turística (con imagen)"
echo ""
echo -e "${BLUE}📱 Responsive Design:${NC}"
echo -e "  ✓ Bootstrap 5 implementado"
echo -e "  ✓ Grid responsive (móvil, tablet, desktop)"
echo ""
echo -e "${BLUE}📊 Ver logs:${NC}"
echo -e "  tail -f apache-tomcat-10.1.47-central/logs/catalina.out"
echo -e "  tail -f apache-tomcat-10.1.47/logs/catalina.out"
echo ""
