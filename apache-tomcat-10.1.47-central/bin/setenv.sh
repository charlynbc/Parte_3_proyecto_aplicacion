#!/bin/bash
# Configuración automática para macOS y Linux

if [[ "$OSTYPE" == "darwin"* ]]; then
    # macOS
    export JAVA_HOME=$(/usr/libexec/java_home -v 17)
else
    # Linux
    export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
fi

export CATALINA_OPTS="$CATALINA_OPTS -Xms512m -Xmx1024m"

# Configurar URL pública para Codespaces si está disponible
if [ ! -z "$CODESPACE_NAME" ] && [ ! -z "$GITHUB_CODESPACES_PORT_FORWARDING_DOMAIN" ]; then
    export CATALINA_OPTS="$CATALINA_OPTS -DCODESPACE_NAME=$CODESPACE_NAME"
    export CATALINA_OPTS="$CATALINA_OPTS -DGITHUB_CODESPACES_PORT_FORWARDING_DOMAIN=$GITHUB_CODESPACES_PORT_FORWARDING_DOMAIN"
    echo "🌐 Codespace detectado: $CODESPACE_NAME"
fi
