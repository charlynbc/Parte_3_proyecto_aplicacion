#!/bin/bash

echo "=============================================="
echo "🚀 Prueba de Arquitectura Distribuida - Tarea 3"
echo "=============================================="

# Función para mostrar estado
function show_status() {
    echo ""
    echo "📊 Estado actual de los servidores:"
    echo "  📡 Servidor Central (Puerto 9128): $(curl -s http://localhost:9128/webservices/usuario?wsdl > /dev/null && echo "✅ Activo" || echo "❌ Inactivo")"
    echo "  🌐 Servidor Web (Puerto 8080): $(curl -s http://localhost:8080/Laboratorio2PaP > /dev/null && echo "✅ Activo" || echo "❌ Inactivo")"
    echo ""
}

# Función para limpiar procesos anteriores
function cleanup() {
    echo "🧹 Limpiando procesos anteriores..."
    pkill -f "servidor-central" 2>/dev/null || true
    pkill -f "WebServices" 2>/dev/null || true
    ./apache-tomcat-10.1.47/bin/shutdown.sh 2>/dev/null || true
    sleep 2
}

# Función para compilar proyectos
function compile_projects() {
    echo "🔨 Compilando servidor-central..."
    mvn clean package -f "parte 3/servidor-central/pom.xml" -q
    if [ $? -ne 0 ]; then
        echo "❌ Error compilando servidor-central"
        exit 1
    fi
    
    echo "🔨 Compilando servidor-web..."
    mvn clean package -f "parte 3/servidor-web/pom.xml" -q -DskipTests
    if [ $? -ne 0 ]; then
        echo "❌ Error compilando servidor-web"
        exit 1
    fi
    
    echo "✅ Compilación exitosa"
}

# Función para iniciar servidor central
function start_central_server() {
    echo "🚀 Iniciando Servidor Central (Web Services)..."
    cd "parte 3/servidor-central"
    
    # Ejecutar Web Services usando Maven exec para tener el classpath correcto
    mvn exec:java -Dexec.mainClass="publicadores.WebServices" -Dexec.args="" -q > ../../central-server.log 2>&1 &
    CENTRAL_PID=$!
    cd ../..
    
    echo "📝 PID del Servidor Central: $CENTRAL_PID"
    
    # Esperar que el servidor esté listo
    echo "⏳ Esperando que el Servidor Central esté listo..."
    for i in {1..15}; do
        if curl -s http://localhost:9128/webservices/usuario?wsdl > /dev/null; then
            echo "✅ Servidor Central listo en puerto 9128"
            return 0
        fi
        sleep 3
        echo "   Intento $i/15..."
    done
    
    echo "❌ Timeout esperando Servidor Central"
    return 1
}

# Función para iniciar servidor web
function start_web_server() {
    echo "🚀 Iniciando Servidor Web (Tomcat)..."
    
    # Copiar WAR a Tomcat
    cp "parte 3/servidor-web/target/Laboratorio2PaP.war" apache-tomcat-10.1.47/webapps/
    
    # Iniciar Tomcat
    ./apache-tomcat-10.1.47/bin/startup.sh > /dev/null 2>&1
    
    # Esperar que Tomcat esté listo
    echo "⏳ Esperando que Tomcat esté listo..."
    for i in {1..15}; do
        if curl -s http://localhost:8080/Laboratorio2PaP > /dev/null; then
            echo "✅ Servidor Web listo en puerto 8080"
            return 0
        fi
        sleep 3
        echo "   Intento $i/15..."
    done
    
    echo "❌ Timeout esperando Servidor Web"
    return 1
}

# Función para probar Web Services
function test_webservices() {
    echo ""
    echo "🧪 Probando Web Services..."
    
    echo "📝 Verificando WSDL del servicio Usuario:"
    if curl -s "http://localhost:9128/webservices/usuario?wsdl" | grep -q "WSUsuario"; then
        echo "  ✅ WSDL Usuario disponible"
    else
        echo "  ❌ WSDL Usuario no disponible"
    fi
    
    echo "📝 Verificando WSDL del servicio Actividad:"
    if curl -s "http://localhost:9128/webservices/actividad?wsdl" | grep -q "WSActividad"; then
        echo "  ✅ WSDL Actividad disponible"
    else
        echo "  ❌ WSDL Actividad no disponible"
    fi
    
    echo "📝 Verificando WSDL del servicio Salida:"
    if curl -s "http://localhost:9128/webservices/salida?wsdl" | grep -q "WSSalida"; then
        echo "  ✅ WSDL Salida disponible"
    else
        echo "  ❌ WSDL Salida no disponible"
    fi
    
    echo "📝 Verificando WSDL del servicio Inscripcion:"
    if curl -s "http://localhost:9128/webservices/inscripcion?wsdl" | grep -q "WSInscripcion"; then
        echo "  ✅ WSDL Inscripcion disponible"
    else
        echo "  ❌ WSDL Inscripcion no disponible"
    fi
}

# Función para probar aplicación web
function test_webapp() {
    echo ""
    echo "🌐 Probando Aplicación Web..."
    
    if curl -s "http://localhost:8080/Laboratorio2PaP" | grep -q "login"; then
        echo "  ✅ Página principal accesible"
    else
        echo "  ❌ Página principal no accesible"
    fi
    
    if curl -s "http://localhost:8080/Laboratorio2PaP/login" | grep -q "usuario"; then
        echo "  ✅ Página de login accesible"
    else
        echo "  ❌ Página de login no accesible"
    fi
}

# Función principal
function main() {
    cleanup
    compile_projects
    
    if start_central_server; then
        if start_web_server; then
            show_status
            test_webservices
            test_webapp
            
            echo ""
            echo "🎉 ¡Arquitectura distribuida funcionando!"
            echo ""
            echo "📋 URLs disponibles:"
            echo "  🌐 Aplicación Web: http://localhost:8080/Laboratorio2PaP"
            echo "  📡 Web Services: http://localhost:9128/webservices/"
            echo "  📄 WSDL Usuario: http://localhost:9128/webservices/usuario?wsdl"
            echo ""
            echo "📝 Logs disponibles:"
            echo "  📊 Servidor Central: central-server.log"
            echo "  🐱 Tomcat: apache-tomcat-10.1.47/logs/catalina.out"
            echo ""
            echo "🛑 Para detener los servidores, ejecuta: ./stop-servers.sh"
            
        else
            echo "❌ Error iniciando Servidor Web"
            exit 1
        fi
    else
        echo "❌ Error iniciando Servidor Central"
        exit 1
    fi
}

# Ejecutar función principal
main