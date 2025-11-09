#!/bin/bash
# Script de configuración para Mac M1 (Apple Silicon)
# Este script configura el entorno completo para ejecutar el proyecto en Mac M1

set -e  # Detener en caso de error

echo "=========================================="
echo "   CONFIGURACIÓN PARA MAC M1 (ARM64)"
echo "=========================================="
echo ""

# Colores para output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Función para imprimir con color
print_success() {
    echo -e "${GREEN}✓ $1${NC}"
}

print_warning() {
    echo -e "${YELLOW}⚠ $1${NC}"
}

print_error() {
    echo -e "${RED}✗ $1${NC}"
}

# Detectar arquitectura
ARCH=$(uname -m)
if [[ "$ARCH" != "arm64" ]]; then
    print_warning "Este script está optimizado para Mac M1 (ARM64)"
    print_warning "Arquitectura detectada: $ARCH"
    read -p "¿Deseas continuar de todos modos? (s/n): " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Ss]$ ]]; then
        exit 1
    fi
fi

echo ""
echo "PASO 1: Verificando Homebrew..."
echo "================================"

# Verificar si Homebrew está instalado
if ! command -v brew &> /dev/null; then
    print_error "Homebrew no está instalado"
    echo "Instalando Homebrew..."
    /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
    
    # Configurar PATH para M1
    if [[ "$ARCH" == "arm64" ]]; then
        echo 'eval "$(/opt/homebrew/bin/brew shellenv)"' >> ~/.zprofile
        eval "$(/opt/homebrew/bin/brew shellenv)"
    fi
    print_success "Homebrew instalado"
else
    print_success "Homebrew ya está instalado ($(brew --version | head -n 1))"
fi

echo ""
echo "PASO 2: Instalando Java 17 (Temurin - ARM64)..."
echo "================================================"

# Instalar OpenJDK 17 (Temurin) optimizado para ARM64
if ! brew list --cask temurin17 &> /dev/null; then
    echo "Instalando Temurin OpenJDK 17..."
    brew install --cask temurin17
    print_success "Java 17 instalado"
else
    print_success "Java 17 ya está instalado"
fi

# Configurar JAVA_HOME
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
echo "JAVA_HOME configurado: $JAVA_HOME"

# Agregar a shell profile si no existe
SHELL_PROFILE=""
if [[ -f ~/.zshrc ]]; then
    SHELL_PROFILE=~/.zshrc
elif [[ -f ~/.bash_profile ]]; then
    SHELL_PROFILE=~/.bash_profile
fi

if [[ -n "$SHELL_PROFILE" ]] && ! grep -q "JAVA_HOME" "$SHELL_PROFILE"; then
    echo "" >> "$SHELL_PROFILE"
    echo "# Java 17 Configuration" >> "$SHELL_PROFILE"
    echo 'export JAVA_HOME=$(/usr/libexec/java_home -v 17)' >> "$SHELL_PROFILE"
    echo 'export PATH=$JAVA_HOME/bin:$PATH' >> "$SHELL_PROFILE"
    print_success "JAVA_HOME agregado a $SHELL_PROFILE"
fi

# Verificar Java
java -version
print_success "Java 17 configurado correctamente"

echo ""
echo "PASO 3: Instalando Maven..."
echo "============================"

if ! command -v mvn &> /dev/null; then
    echo "Instalando Maven..."
    brew install maven
    print_success "Maven instalado"
else
    print_success "Maven ya está instalado ($(mvn -version | head -n 1))"
fi

echo ""
echo "PASO 4: Configurando Tomcat para Mac M1..."
echo "==========================================="

# Actualizar setenv.sh para Tomcat Central
cat > apache-tomcat-10.1.47-central/bin/setenv.sh << 'EOF'
#!/bin/bash
# Configuración de Java 17 para Tomcat Central (Mac M1)
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
export PATH=$JAVA_HOME/bin:$PATH

# Opciones específicas para Mac M1
export CATALINA_OPTS="-Djava.awt.headless=true"
EOF

chmod +x apache-tomcat-10.1.47-central/bin/setenv.sh
print_success "setenv.sh configurado para Tomcat Central"

# Actualizar setenv.sh para Tomcat Web
cat > apache-tomcat-10.1.47/bin/setenv.sh << 'EOF'
#!/bin/bash
# Configuración de Java 17 para Tomcat Web (Mac M1)
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
export PATH=$JAVA_HOME/bin:$PATH

# Opciones específicas para Mac M1
export CATALINA_OPTS="-Djava.awt.headless=true"
EOF

chmod +x apache-tomcat-10.1.47/bin/setenv.sh
print_success "setenv.sh configurado para Tomcat Web"

# Dar permisos de ejecución a todos los scripts de Tomcat
chmod +x apache-tomcat-10.1.47-central/bin/*.sh
chmod +x apache-tomcat-10.1.47/bin/*.sh
print_success "Permisos de ejecución configurados para Tomcat"

echo ""
echo "PASO 5: Creando scripts de inicio para Mac..."
echo "=============================================="

# Script para iniciar Tomcat Central
cat > start-tomcat-central-mac.sh << 'EOF'
#!/bin/bash
# Iniciar Tomcat Central (Puerto 9128) en Mac M1

export JAVA_HOME=$(/usr/libexec/java_home -v 17)
export PATH=$JAVA_HOME/bin:$PATH

echo "Iniciando Tomcat Central en puerto 9128..."
./apache-tomcat-10.1.47-central/bin/catalina.sh start

echo "Esperando a que el servidor inicie..."
sleep 5

# Verificar que inició correctamente
if curl -s http://localhost:9128 > /dev/null; then
    echo "✓ Tomcat Central iniciado correctamente"
    echo "  WSDLs disponibles en:"
    echo "  - http://localhost:9128/central-ws/services/AuthService?wsdl"
    echo "  - http://localhost:9128/central-ws/services/ActividadesService?wsdl"
else
    echo "✗ Error al iniciar Tomcat Central"
    echo "Ver logs en: apache-tomcat-10.1.47-central/logs/catalina.out"
fi
EOF

chmod +x start-tomcat-central-mac.sh
print_success "Script start-tomcat-central-mac.sh creado"

# Script para iniciar Tomcat Web
cat > start-tomcat-web-mac.sh << 'EOF'
#!/bin/bash
# Iniciar Tomcat Web (Puerto 8080) en Mac M1

export JAVA_HOME=$(/usr/libexec/java_home -v 17)
export PATH=$JAVA_HOME/bin:$PATH

echo "Iniciando Tomcat Web en puerto 8080..."
./apache-tomcat-10.1.47/bin/catalina.sh start

echo "Esperando a que el servidor inicie..."
sleep 8

# Verificar que inició correctamente
if curl -s http://localhost:8080/Laboratorio2PaP/ > /dev/null; then
    echo "✓ Tomcat Web iniciado correctamente"
    echo "  Aplicación disponible en: http://localhost:8080/Laboratorio2PaP/"
else
    echo "✗ Error al iniciar Tomcat Web"
    echo "Ver logs en: apache-tomcat-10.1.47/logs/catalina.out"
fi
EOF

chmod +x start-tomcat-web-mac.sh
print_success "Script start-tomcat-web-mac.sh creado"

# Script para detener ambos servidores
cat > stop-tomcat-mac.sh << 'EOF'
#!/bin/bash
# Detener todos los servidores Tomcat en Mac

export JAVA_HOME=$(/usr/libexec/java_home -v 17)
export PATH=$JAVA_HOME/bin:$PATH

echo "Deteniendo Tomcat Central..."
./apache-tomcat-10.1.47-central/bin/catalina.sh stop

echo "Deteniendo Tomcat Web..."
./apache-tomcat-10.1.47/bin/catalina.sh stop

echo "✓ Servidores detenidos"
EOF

chmod +x stop-tomcat-mac.sh
print_success "Script stop-tomcat-mac.sh creado"

# Script completo de compilación y deployment
cat > build-and-deploy-mac.sh << 'EOF'
#!/bin/bash
# Compilar y desplegar todo el proyecto en Mac M1

set -e

export JAVA_HOME=$(/usr/libexec/java_home -v 17)
export PATH=$JAVA_HOME/bin:$PATH

echo "=========================================="
echo "   COMPILACIÓN Y DEPLOYMENT (Mac M1)"
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
echo "================================="
cd Laboratorio3Pap-main/central-ws
mvn clean install -DskipTests
cd ../..

# Desplegar central-ws
echo "Desplegando central-ws..."
cp Laboratorio3Pap-main/central-ws/target/central-ws.war apache-tomcat-10.1.47-central/webapps/
echo "✓ central-ws.war copiado"

# PASO 2: Iniciar Tomcat Central
echo ""
echo "PASO 2: Iniciando Tomcat Central..."
echo "===================================="
./apache-tomcat-10.1.47-central/bin/catalina.sh start
echo "Esperando a que central-ws se despliegue..."
sleep 10

# Verificar WSDLs
echo "Verificando WSDLs..."
if curl -s http://localhost:9128/central-ws/services/AuthService?wsdl > /dev/null; then
    echo "✓ AuthService WSDL disponible"
else
    echo "✗ Error: AuthService WSDL no disponible"
    exit 1
fi

if curl -s http://localhost:9128/central-ws/services/ActividadesService?wsdl > /dev/null; then
    echo "✓ ActividadesService WSDL disponible"
else
    echo "✗ Error: ActividadesService WSDL no disponible"
    exit 1
fi

# PASO 3: Compilar client-ws (generar stubs)
echo ""
echo "PASO 3: Generando stubs (client-ws)..."
echo "======================================="
cd Laboratorio3Pap-main/client-ws
mvn clean compile install
cd ../..
echo "✓ client-ws compilado e instalado"

# PASO 4: Compilar Laboratorio2PaP
echo ""
echo "PASO 4: Compilando Laboratorio2PaP..."
echo "======================================"
cd Laboratorio2PaP-main
mvn clean package
cd ..
echo "✓ Laboratorio2PaP compilado"

# Desplegar Laboratorio2PaP
echo "Desplegando Laboratorio2PaP..."
cp Laboratorio2PaP-main/target/Laboratorio2PaP.war apache-tomcat-10.1.47/webapps/
echo "✓ Laboratorio2PaP.war copiado"

# PASO 5: Iniciar Tomcat Web
echo ""
echo "PASO 5: Iniciando Tomcat Web..."
echo "================================"
./apache-tomcat-10.1.47/bin/catalina.sh start
echo "Esperando a que Laboratorio2PaP se despliegue..."
sleep 15

# Verificar aplicación
echo "Verificando aplicación web..."
if curl -s http://localhost:8080/Laboratorio2PaP/ > /dev/null; then
    echo "✓ Aplicación web disponible"
else
    echo "✗ Error: Aplicación web no disponible"
    exit 1
fi

echo ""
echo "=========================================="
echo "   ✓ DEPLOYMENT COMPLETADO"
echo "=========================================="
echo ""
echo "URLs disponibles:"
echo "  • Aplicación Web: http://localhost:8080/Laboratorio2PaP/"
echo "  • AuthService WSDL: http://localhost:9128/central-ws/services/AuthService?wsdl"
echo "  • ActividadesService WSDL: http://localhost:9128/central-ws/services/ActividadesService?wsdl"
echo ""
echo "Para detener los servidores ejecuta:"
echo "  ./stop-tomcat-mac.sh"
echo ""
EOF

chmod +x build-and-deploy-mac.sh
print_success "Script build-and-deploy-mac.sh creado"

echo ""
echo "PASO 6: Creando guía README para Mac..."
echo "========================================"

cat > README-MAC-M1.md << 'EOF'
# Guía de Instalación y Ejecución en Mac M1 (Apple Silicon)

Esta guía te ayudará a configurar y ejecutar el proyecto en Mac M1 con Apple Silicon.

## 📋 Requisitos Previos

- Mac con chip M1, M2 o M3 (Apple Silicon)
- macOS 11.0 (Big Sur) o superior
- Conexión a Internet

## 🚀 Instalación Automática

### Opción 1: Setup Completo (Recomendado)

Ejecuta el script de configuración automática:

```bash
chmod +x setup-mac-m1.sh
./setup-mac-m1.sh
```

Este script instalará:
- ✅ Homebrew (si no está instalado)
- ✅ Java 17 (OpenJDK Temurin para ARM64)
- ✅ Maven
- ✅ Configurará ambos Tomcat
- ✅ Creará scripts de inicio/parada

### Opción 2: Instalación Manual

Si prefieres instalar manualmente:

#### 1. Instalar Homebrew

```bash
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
```

Después de instalar, configura el PATH:

```bash
echo 'eval "$(/opt/homebrew/bin/brew shellenv)"' >> ~/.zprofile
eval "$(/opt/homebrew/bin/brew shellenv)"
```

#### 2. Instalar Java 17 (Temurin)

```bash
brew install --cask temurin17
```

#### 3. Configurar JAVA_HOME

Agrega a tu `~/.zshrc` o `~/.bash_profile`:

```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
export PATH=$JAVA_HOME/bin:$PATH
```

Luego recarga:

```bash
source ~/.zshrc  # o source ~/.bash_profile
```

#### 4. Instalar Maven

```bash
brew install maven
```

#### 5. Verificar instalación

```bash
java -version    # Debe mostrar Java 17
mvn -version     # Debe mostrar Maven 3.9+
```

## 🏗️ Compilar y Desplegar

### Opción A: Script Automático (Recomendado)

```bash
./build-and-deploy-mac.sh
```

Este script:
1. ✅ Compila `central-ws`
2. ✅ Despliega y inicia Tomcat Central (puerto 9128)
3. ✅ Genera stubs desde WSDLs (`client-ws`)
4. ✅ Compila `Laboratorio2PaP`
5. ✅ Despliega y inicia Tomcat Web (puerto 8080)
6. ✅ Verifica que todo funcione

**Tiempo estimado:** 2-3 minutos

### Opción B: Manual (Paso a Paso)

#### 1. Compilar central-ws

```bash
cd Laboratorio3Pap-main/central-ws
mvn clean install -DskipTests
cd ../..
```

#### 2. Desplegar central-ws

```bash
cp Laboratorio3Pap-main/central-ws/target/central-ws.war \
   apache-tomcat-10.1.47-central/webapps/
```

#### 3. Iniciar Tomcat Central

```bash
./start-tomcat-central-mac.sh
```

Espera 10 segundos y verifica que los WSDLs estén disponibles:

```bash
curl http://localhost:9128/central-ws/services/AuthService?wsdl
```

#### 4. Generar stubs (client-ws)

```bash
cd Laboratorio3Pap-main/client-ws
mvn clean compile install
cd ../..
```

#### 5. Compilar Laboratorio2PaP

```bash
cd Laboratorio2PaP-main
mvn clean package
cd ..
```

#### 6. Desplegar Laboratorio2PaP

```bash
cp Laboratorio2PaP-main/target/Laboratorio2PaP.war \
   apache-tomcat-10.1.47/webapps/
```

#### 7. Iniciar Tomcat Web

```bash
./start-tomcat-web-mac.sh
```

## 🎮 Uso

### Iniciar Servidores

Si ya compilaste todo:

```bash
# Iniciar Tomcat Central (puerto 9128)
./start-tomcat-central-mac.sh

# Iniciar Tomcat Web (puerto 8080)
./start-tomcat-web-mac.sh
```

### Detener Servidores

```bash
./stop-tomcat-mac.sh
```

### URLs de Acceso

| Servicio | URL |
|----------|-----|
| **Aplicación Web** | http://localhost:8080/Laboratorio2PaP/ |
| **AuthService WSDL** | http://localhost:9128/central-ws/services/AuthService?wsdl |
| **ActividadesService WSDL** | http://localhost:9128/central-ws/services/ActividadesService?wsdl |

## 🔧 Troubleshooting

### Error: "Port already in use"

Si ves errores de puerto ocupado:

```bash
# Ver qué proceso usa el puerto 8080
lsof -i :8080

# Ver qué proceso usa el puerto 9128
lsof -i :9128

# Matar proceso por PID
kill -9 <PID>
```

O simplemente:

```bash
./stop-tomcat-mac.sh
```

### Error: "JAVA_HOME not set"

Verifica que Java 17 esté instalado:

```bash
/usr/libexec/java_home -v 17
```

Si no está instalado:

```bash
brew install --cask temurin17
```

### Error: "Cannot connect to MySQL"

El proyecto usa una base de datos MySQL en Railway Cloud. Verifica tu conexión a internet.

Si necesitas usar una base de datos local:

1. Instalar MySQL:
   ```bash
   brew install mysql
   brew services start mysql
   ```

2. Editar `Laboratorio3Pap-main/central-ws/src/main/resources/META-INF/persistence.xml`

### Ver Logs

```bash
# Logs de Tomcat Central
tail -f apache-tomcat-10.1.47-central/logs/catalina.out

# Logs de Tomcat Web
tail -f apache-tomcat-10.1.47/logs/catalina.out
```

## 📊 Verificación del Sistema

Script para verificar que todo está correcto:

```bash
# Verificar Java
java -version

# Verificar Maven
mvn -version

# Verificar que los servidores estén corriendo
ps aux | grep catalina

# Verificar puertos
lsof -i :8080
lsof -i :9128

# Test de conectividad
curl -I http://localhost:8080/Laboratorio2PaP/
curl -I http://localhost:9128/central-ws/services/AuthService?wsdl
```

## 🎯 Características Específicas de Mac M1

Este proyecto está optimizado para Mac M1 con:

- ✅ OpenJDK 17 nativo para ARM64 (mejor rendimiento)
- ✅ Scripts adaptados para macOS (`/usr/libexec/java_home`)
- ✅ Configuración de CATALINA_OPTS para Mac
- ✅ Permisos correctos para scripts `.sh`
- ✅ Homebrew en `/opt/homebrew` (ubicación ARM64)

## 📚 Estructura del Proyecto

```
Parte_3_proyecto_aplicacion/
├── Laboratorio3Pap-main/
│   ├── central-ws/           # Servidor SOAP (puerto 9128)
│   └── client-ws/            # Generador de stubs
├── Laboratorio2PaP-main/     # Aplicación Web (puerto 8080)
├── apache-tomcat-10.1.47-central/  # Tomcat para SOAP
├── apache-tomcat-10.1.47/    # Tomcat para Web
├── setup-mac-m1.sh           # Setup automático
├── build-and-deploy-mac.sh   # Compilar y desplegar todo
├── start-tomcat-central-mac.sh
├── start-tomcat-web-mac.sh
└── stop-tomcat-mac.sh
```

## ⚙️ Configuración Avanzada

### Cambiar Puerto de Tomcat Central

Editar `apache-tomcat-10.1.47-central/conf/server.xml`:

```xml
<Connector port="9128" protocol="HTTP/1.1" ... />
```

### Cambiar Puerto de Tomcat Web

Editar `apache-tomcat-10.1.47/conf/server.xml`:

```xml
<Connector port="8080" protocol="HTTP/1.1" ... />
```

### Aumentar Memoria JVM

Editar archivos `setenv.sh`:

```bash
export CATALINA_OPTS="-Xms512m -Xmx2048m -Djava.awt.headless=true"
```

## 🆘 Soporte

Si tienes problemas:

1. Verifica los logs en `apache-tomcat-*/logs/catalina.out`
2. Asegúrate de que Java 17 esté configurado correctamente
3. Revisa que los puertos 8080 y 9128 estén libres
4. Verifica conexión a internet (para base de datos Railway)

## 📝 Notas Importantes

- **Primera ejecución:** La primera vez tomará más tiempo (Maven descarga dependencias)
- **WSDLs:** Los WSDLs deben estar accesibles antes de compilar `client-ws`
- **Orden:** Siempre inicia Tomcat Central antes que Tomcat Web
- **Memoria:** Mac M1 puede ejecutar ambos Tomcat simultáneamente sin problemas

---

**¡Listo para usar en Mac M1!** 🚀
EOF

print_success "README-MAC-M1.md creado"

echo ""
echo "=========================================="
echo "   ✓ CONFIGURACIÓN COMPLETADA"
echo "=========================================="
echo ""
echo "Siguiente paso:"
echo ""
echo "Para compilar y desplegar todo:"
echo "  ./build-and-deploy-mac.sh"
echo ""
echo "Para ver la guía completa:"
echo "  cat README-MAC-M1.md"
echo ""
echo "¡Todo listo para ejecutar en Mac M1! 🚀"
