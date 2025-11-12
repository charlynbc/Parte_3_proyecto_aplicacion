#!/bin/bash

echo "🚀 Iniciando Tomcat Central (Web Services) - Puerto 9128"

if [ ! -d "apache-tomcat-10.1.47-central" ]; then
    echo "❌ Error: apache-tomcat-10.1.47-central no encontrado"
    exit 1
fi

# Configurar JAVA_HOME para macOS
if [[ "$OSTYPE" == "darwin"* ]]; then
    export JAVA_HOME=$(/usr/libexec/java_home -v 17 2>/dev/null)
    if [ -z "$JAVA_HOME" ]; then
        echo "❌ Java 17 no encontrado. Instalar con: brew install --cask temurin17"
        exit 1
    fi
    echo "✅ Java 17 detectado: $JAVA_HOME"
fi

cd apache-tomcat-10.1.47-central

# Dar permisos
chmod +x bin/*.sh

# Iniciar
./bin/startup.sh

echo ""
echo "✅ Tomcat Central iniciado"
echo "📊 Ver logs: tail -f apache-tomcat-10.1.47-central/logs/catalina.out"
echo "🌐 WSDLs disponibles en:"
echo "   http://localhost:9128/central-ws/services/AuthService?wsdl"
echo "   http://localhost:9128/central-ws/services/ActividadesService?wsdl"
