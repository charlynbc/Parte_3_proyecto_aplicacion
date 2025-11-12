#!/bin/bash

echo "🚀 Iniciando Tomcat Web (Aplicación Responsive) - Puerto 8080"

if [ ! -d "apache-tomcat-10.1.47" ]; then
    echo "❌ Error: apache-tomcat-10.1.47 no encontrado"
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

cd apache-tomcat-10.1.47

# Dar permisos
chmod +x bin/*.sh

# Iniciar
./bin/startup.sh

echo ""
echo "✅ Tomcat Web iniciado"
echo "📊 Ver logs: tail -f apache-tomcat-10.1.47/logs/catalina.out"
echo "🌐 Aplicación disponible en:"
echo "   http://localhost:8080/Laboratorio2PaP/"
