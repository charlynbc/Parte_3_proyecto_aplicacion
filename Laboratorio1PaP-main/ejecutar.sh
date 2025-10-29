#!/bin/bash
javac -cp "lib/*:." -d bin $(find src -name "*.java")
if [ $? -ne 0 ]; then
    echo "Error en la compilación"
    exit 1
fi
java -cp "lib/*:bin" presentacion.LineaComando

