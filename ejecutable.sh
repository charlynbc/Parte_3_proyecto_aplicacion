#!/bin/bash
# Script para compilar todo el proyecto y ejecutar los Tomcats

set -e

export JAVA_HOME=$(/usr/libexec/java_home -v 17)
export PATH=$JAVA_HOME/bin:$PATH

echo "=========================================="
echo "   COMPILACIÓN Y EJECUCIÓN COMPLETA"
echo "=========================================="
echo ""

# Detener servidores si están corriendo
echo "Deteniendo servidores existentes..."
./apache-tomcat-10.1.47-central/bin/catalina.sh stop 2>/dev/null || true
./apache-tomcat-10.1.47/bin/catalina.sh stop 2>/dev/null || true
sleep 3

# Limpiar deployments anteriores
echo "Limpiando deployments anteriores..."
rm -rf apache-tomcat-10.1.47-central/webapps/central-ws*
rm -rf apache-tomcat-10.1.47/webapps/Laboratorio2PaP*

# PASO 1: Compilar central-ws
echo ""
echo "PASO 1: Compilando central-ws..."
cd Laboratorio3Pap-main/central-ws
mvn clean install -DskipTests
cd ../..

# Desplegar central-ws
cp Laboratorio3Pap-main/central-ws/target/central-ws.war apache-tomcat-10.1.47-central/webapps/

# PASO 2: Iniciar Tomcat Central
echo ""
echo "PASO 2: Iniciando Tomcat Central..."
./apache-tomcat-10.1.47-central/bin/catalina.sh start
echo "Esperando a que central-ws se despliegue..."
sleep 10

# Verificar WSDLs
if ! curl -s http://localhost:9128/central-ws/services/AuthService?wsdl > /dev/null; then
    echo "Error: AuthService WSDL no disponible"
    exit 1
fi

if ! curl -s http://localhost:9128/central-ws/services/ActividadesService?wsdl > /dev/null; then
    echo "Error: ActividadesService WSDL no disponible"
    exit 1
fi

# PASO 3: Generar stubs (client-ws)
echo ""
echo "PASO 3: Generando stubs..."
cd Laboratorio3Pap-main/client-ws
mvn clean compile install
cd ../..

# PASO 4: Compilar Laboratorio2PaP
echo ""
echo "PASO 4: Compilando Laboratorio2PaP..."
cd Laboratorio2PaP-main
mvn clean package
cd ..

# Desplegar Laboratorio2PaP
cp Laboratorio2PaP-main/target/Laboratorio2PaP.war apache-tomcat-10.1.47/webapps/

# PASO 5: Iniciar Tomcat Web
echo ""
echo "PASO 5: Iniciando Tomcat Web..."
./apache-tomcat-10.1.47/bin/catalina.sh start
echo "Esperando a que Laboratorio2PaP se despliegue..."
sleep 15

# Verificar aplicación
if ! curl -s http://localhost:8080/Laboratorio2PaP/ > /dev/null; then
    echo "Error: Aplicación web no disponible"
    exit 1
fi

echo ""
echo "=========================================="
echo "   ✓ EJECUCIÓN COMPLETADA"
echo "=========================================="
echo ""
echo "URLs disponibles:"
echo "  • Aplicación Web: http://localhost:8080/Laboratorio2PaP/"
echo "  • AuthService WSDL: http://localhost:9128/central-ws/services/AuthService?wsdl"
echo "  • ActividadesService WSDL: http://localhost:9128/central-ws/services/ActividadesService?wsdl"
echo ""
echo "Para detener: ./stop-tomcat-mac.sh"
