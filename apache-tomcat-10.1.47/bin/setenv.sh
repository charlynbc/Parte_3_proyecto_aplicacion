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
