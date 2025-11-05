#!/bin/bash

export JAVA_HOME="/usr/lib/jvm/java-17-openjdk-amd64"
export PATH="$JAVA_HOME/bin:$PATH"

echo "🛑 Deteniendo servidores distribuidos..."

echo "📡 Deteniendo Servidor Central..."
pkill -f "publicadores.WebServices" 2>/dev/null || true
pkill -f "servidor-central" 2>/dev/null || true

echo "🌐 Deteniendo Tomcat..."
./apache-tomcat-10.1.47/bin/shutdown.sh 2>/dev/null || true

sleep 2

echo "🧹 Limpiando archivos temporales..."
rm -f central-server.log 2>/dev/null || true

echo "✅ Servidores detenidos correctamente"