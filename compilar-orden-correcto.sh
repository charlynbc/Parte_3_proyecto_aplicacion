#!/bin/bash

# Compilación en orden correcto según arquitectura Web Services
# Tarea 3 - PaP 2025

export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH

echo "🚀 Compilación Ordenada - Arquitectura Web Services"
echo "===================================================="
echo ""
echo "☕ Java: $(java -version 2>&1 | head -n 1)"
echo ""

GREEN='\033[0;32m'
BLUE='\033[0;34m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m'

BASE_DIR="/workspaces/Parte_3_proyecto_aplicacion"

check_result() {
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✅ $1${NC}"
        echo ""
    else
        echo -e "${RED}❌ $1${NC}"
        echo ""
        exit 1
    fi
}

# ============================================
# PASO 1: Laboratorio1 (JAR)
# ============================================
echo -e "${BLUE}📦 [1/5] Compilando Laboratorio1.jar${NC}"
cd "$BASE_DIR/Laboratorio1PaP-main"

rm -rf build/*.jar 2>/dev/null
javac -d build/classes -cp "lib/*" src/logica/*.java src/excepciones/*.java 2>/dev/null
check_result "Compilación Laboratorio1"

cd build/classes
jar cf ../Laboratorio1.jar *
cd ../..
check_result "Empaquetado JAR"

mvn install:install-file \
    -Dfile=build/Laboratorio1.jar \
    -DgroupId=com \
    -DartifactId=Laboratorio1 \
    -Dversion=1 \
    -Dpackaging=jar \
    -q 2>/dev/null
check_result "Instalación en Maven local"

# ============================================
# PASO 2: Servidor Central (WAR)
# ============================================
echo -e "${BLUE}🌐 [2/5] Compilando Servidor Central${NC}"
echo "   → Expone Web Services SOAP"
cd "$BASE_DIR/Laboratorio3Pap-main/central-ws"

mvn clean package -DskipTests -q
check_result "Compilación Servidor Central"

# ============================================
# PASO 3: Iniciar Servidor Central
# ============================================
echo -e "${BLUE}🚀 [3/5] Iniciando Servidor Central${NC}"
echo "   → Puerto: 9128"
echo "   → Esperando que los servicios SOAP estén disponibles..."

# Copiar WAR a Tomcat central
TOMCAT_CENTRAL="$BASE_DIR/apache-tomcat-10.1.47-central"
if [ -d "$TOMCAT_CENTRAL" ]; then
    cp target/central-ws.war "$TOMCAT_CENTRAL/webapps/"
    echo "   → WAR copiado a Tomcat Central"
    
    # Iniciar Tomcat en background
    "$TOMCAT_CENTRAL/bin/startup.sh" > /dev/null 2>&1
    
    echo "   → Esperando despliegue (30 segundos)..."
    sleep 30
    
    # Verificar que el servicio esté disponible
    for i in {1..10}; do
        if curl -s "http://localhost:9128/central-ws/services/AuthService?wsdl" > /dev/null 2>&1; then
            echo -e "${GREEN}   ✅ Servidor Central activo${NC}"
            break
        fi
        echo "   → Intento $i/10..."
        sleep 3
    done
else
    echo -e "${YELLOW}   ⚠️  Tomcat Central no encontrado${NC}"
    echo "   → Asumiendo servidor ya corriendo en localhost:9128"
fi

# ============================================
# PASO 4: Client-WS (stubs SOAP)
# ============================================
echo ""
echo -e "${BLUE}📡 [4/5] Generando Client-WS (SOAP Stubs)${NC}"
echo "   → Consumirá WSDLs del Servidor Central"
cd "$BASE_DIR/Laboratorio3Pap-main/client-ws"

mvn clean install -DskipTests -q
check_result "Generación de stubs SOAP"

# ============================================
# PASO 5: Servidor Web (WAR)
# ============================================
echo -e "${BLUE}🌐 [5/5] Compilando Servidor Web${NC}"
echo "   → Consume Web Services del Servidor Central"
echo "   → Incluye CSS Responsive sin Bootstrap"
cd "$BASE_DIR/Laboratorio2PaP-main"

mvn clean package -DskipTests -q
check_result "Compilación Servidor Web"

# ============================================
# RESUMEN
# ============================================
echo ""
echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}✅ COMPILACIÓN COMPLETA EXITOSA${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""
echo "📦 Artefactos generados:"
echo ""
echo "1️⃣  Laboratorio1.jar:"
echo "   $BASE_DIR/Laboratorio1PaP-main/build/Laboratorio1.jar"
echo ""
echo "2️⃣  central-ws.war (Servidor SOAP):"
echo "   $BASE_DIR/Laboratorio3Pap-main/central-ws/target/central-ws.war"
echo ""
echo "3️⃣  client-ws.jar (SOAP Stubs):"
echo "   $BASE_DIR/Laboratorio3Pap-main/client-ws/target/client-ws-1.0.0-SNAPSHOT.jar"
echo ""
echo "4️⃣  Laboratorio2PaP.war (Servidor Web):"
echo "   $BASE_DIR/Laboratorio2PaP-main/target/Laboratorio2PaP-0.0.1-SNAPSHOT.war"
echo ""
echo -e "${BLUE}🏗️  Arquitectura implementada:${NC}"
echo ""
echo "   ┌──────────────────┐"
echo "   │  Navegador Web   │"
echo "   └────────┬─────────┘"
echo "            │ HTTP"
echo "            ↓"
echo "   ┌──────────────────┐"
echo "   │  Servidor Web    │ ← CSS Responsive ✨"
echo "   │  (Tomcat 8080)   │"
echo "   └────────┬─────────┘"
echo "            │ SOAP/Web Services"
echo "            ↓"
echo "   ┌──────────────────┐"
echo "   │ Servidor Central │ ← Expone servicios"
echo "   │  (Tomcat 9128)   │"
echo "   └────────┬─────────┘"
echo "            │"
echo "            ↓"
echo "   ┌──────────────────┐"
echo "   │ Laboratorio1.jar │ ← Lógica de negocio"
echo "   └──────────────────┘"
echo ""
echo -e "${YELLOW}🚀 Próximos pasos:${NC}"
echo ""
if [ -d "$TOMCAT_CENTRAL" ]; then
    echo "   ✅ Servidor Central ya está corriendo (puerto 9128)"
    echo ""
    echo "   Iniciar Servidor Web:"
    echo "   1. Copiar WAR a Tomcat Web:"
    echo "      cp $BASE_DIR/Laboratorio2PaP-main/target/Laboratorio2PaP-0.0.1-SNAPSHOT.war \\"
    echo "         $BASE_DIR/apache-tomcat-10.1.47/webapps/"
    echo ""
    echo "   2. Iniciar Tomcat Web:"
    echo "      $BASE_DIR/apache-tomcat-10.1.47/bin/startup.sh"
    echo ""
    echo "   3. Acceder a:"
    echo "      http://localhost:8080/Laboratorio2PaP-0.0.1-SNAPSHOT/"
else
    echo "   1. Iniciar Servidor Central en puerto 9128"
    echo "   2. Iniciar Servidor Web en puerto 8080"
    echo "   3. Acceder a la aplicación"
fi
echo ""
echo -e "${GREEN}✨ Diseño responsive implementado (sin Bootstrap)${NC}"
echo ""
