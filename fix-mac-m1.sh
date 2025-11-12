#!/bin/bash

# Script para verificar y corregir configuración Mac M1
# Tarea 3 - Programación de Aplicaciones 2025

set -e

GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

echo -e "${BLUE}╔═══════════════════════════════════════════════════════════╗${NC}"
echo -e "${BLUE}║   FIX MAC M1 - Tarea 3                                    ║${NC}"
echo -e "${BLUE}╚═══════════════════════════════════════════════════════════╝${NC}"
echo ""

# 1. Verificar Java 17 ARM64
echo -e "${BLUE}[1/7] Verificando Java 17 ARM64...${NC}"
if [ -x "/usr/libexec/java_home" ]; then
    JAVA_HOME_DETECTED=$(/usr/libexec/java_home -v 17 2>/dev/null || echo "")
    if [ -n "$JAVA_HOME_DETECTED" ]; then
        JAVA_ARCH=$(file "$JAVA_HOME_DETECTED/bin/java" | grep -o "arm64\|x86_64")
        if [ "$JAVA_ARCH" = "arm64" ]; then
            echo -e "${GREEN}✅ Java 17 ARM64 detectado: $JAVA_HOME_DETECTED${NC}"
            export JAVA_HOME="$JAVA_HOME_DETECTED"
        else
            echo -e "${RED}❌ Java detectado pero es $JAVA_ARCH (necesita arm64)${NC}"
            echo -e "${YELLOW}Instalar: brew install --cask temurin17${NC}"
            exit 1
        fi
    else
        echo -e "${RED}❌ Java 17 no encontrado${NC}"
        echo -e "${YELLOW}Instalar: brew install --cask temurin17${NC}"
        exit 1
    fi
else
    echo -e "${YELLOW}⚠️  No estás en macOS, usando JAVA_HOME del sistema${NC}"
fi

# 2. Configurar JAVA_HOME en shell
echo -e "${BLUE}[2/7] Configurando JAVA_HOME...${NC}"
if [ -n "$JAVA_HOME_DETECTED" ]; then
    # Detectar shell
    if [ -n "$ZSH_VERSION" ] || [ "$SHELL" = "/bin/zsh" ]; then
        SHELL_RC="$HOME/.zshrc"
    else
        SHELL_RC="$HOME/.bash_profile"
    fi
    
    if ! grep -q "JAVA_HOME.*java_home" "$SHELL_RC" 2>/dev/null; then
        echo "" >> "$SHELL_RC"
        echo "# Java 17 para Tarea 3" >> "$SHELL_RC"
        echo 'export JAVA_HOME=$(/usr/libexec/java_home -v 17)' >> "$SHELL_RC"
        echo 'export PATH=$JAVA_HOME/bin:$PATH' >> "$SHELL_RC"
        echo -e "${GREEN}✅ JAVA_HOME agregado a $SHELL_RC${NC}"
        echo -e "${YELLOW}⚠️  Ejecutar: source $SHELL_RC${NC}"
    else
        echo -e "${GREEN}✅ JAVA_HOME ya configurado en $SHELL_RC${NC}"
    fi
fi

# 3. Dar permisos a scripts Tomcat
echo -e "${BLUE}[3/7] Configurando permisos de scripts...${NC}"
if [ -d "apache-tomcat-10.1.47-central/bin" ]; then
    chmod +x apache-tomcat-10.1.47-central/bin/*.sh
    echo -e "${GREEN}✅ Permisos configurados en Tomcat Central${NC}"
fi

if [ -d "apache-tomcat-10.1.47/bin" ]; then
    chmod +x apache-tomcat-10.1.47/bin/*.sh
    echo -e "${GREEN}✅ Permisos configurados en Tomcat Web${NC}"
fi

chmod +x *.sh 2>/dev/null || true
echo -e "${GREEN}✅ Permisos configurados en scripts del proyecto${NC}"

# 4. Verificar y corregir setenv.sh para macOS
echo -e "${BLUE}[4/7] Verificando setenv.sh...${NC}"
for tomcat_dir in apache-tomcat-10.1.47-central apache-tomcat-10.1.47; do
    if [ -d "$tomcat_dir/bin" ]; then
        SETENV_FILE="$tomcat_dir/bin/setenv.sh"
        if [ -f "$SETENV_FILE" ]; then
            # Verificar si detecta macOS
            if grep -q "Darwin" "$SETENV_FILE"; then
                echo -e "${GREEN}✅ $SETENV_FILE detecta macOS${NC}"
            else
                echo -e "${YELLOW}⚠️  Actualizando $SETENV_FILE para macOS...${NC}"
                cat > "$SETENV_FILE" << 'EOF'
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
EOF
                chmod +x "$SETENV_FILE"
                echo -e "${GREEN}✅ $SETENV_FILE actualizado${NC}"
            fi
        else
            echo -e "${YELLOW}⚠️  Creando $SETENV_FILE...${NC}"
            cat > "$SETENV_FILE" << 'EOF'
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
EOF
            chmod +x "$SETENV_FILE"
            echo -e "${GREEN}✅ $SETENV_FILE creado${NC}"
        fi
    fi
done

# 5. Verificar dependencias en pom.xml
echo -e "${BLUE}[5/7] Verificando dependencias Maven...${NC}"
if [ -f "Laboratorio2PaP-main/pom.xml" ]; then
    # Verificar que NO use central-ws.jar directamente
    if grep -q "<artifactId>central-ws</artifactId>" "Laboratorio2PaP-main/pom.xml" && \
       ! grep -q "client-ws" "Laboratorio2PaP-main/pom.xml"; then
        echo -e "${RED}❌ CRÍTICO: Laboratorio2PaP usa central-ws.jar directamente${NC}"
        echo -e "${YELLOW}   Debe usar SOLO client-ws para Web Services${NC}"
    else
        echo -e "${GREEN}✅ Dependencias correctas (usa client-ws)${NC}"
    fi
fi

# 6. Verificar Bootstrap en JSP
echo -e "${BLUE}[6/7] Verificando Bootstrap en JSP...${NC}"
JSP_COUNT=$(find Laboratorio2PaP-main/src/main/webapp -name "*.jsp" 2>/dev/null | wc -l)
if [ "$JSP_COUNT" -gt 0 ]; then
    BOOTSTRAP_COUNT=$(grep -r "bootstrap" Laboratorio2PaP-main/src/main/webapp/*.jsp 2>/dev/null | wc -l)
    if [ "$BOOTSTRAP_COUNT" -gt 0 ]; then
        echo -e "${GREEN}✅ Bootstrap encontrado en JSP${NC}"
    else
        echo -e "${YELLOW}⚠️  Bootstrap NO encontrado en JSP${NC}"
        echo -e "${YELLOW}   Agregar CDN de Bootstrap en las páginas${NC}"
    fi
    
    # Verificar meta viewport
    VIEWPORT_COUNT=$(grep -r "viewport" Laboratorio2PaP-main/src/main/webapp/*.jsp 2>/dev/null | wc -l)
    if [ "$VIEWPORT_COUNT" -gt 0 ]; then
        echo -e "${GREEN}✅ Meta viewport encontrado (responsive)${NC}"
    else
        echo -e "${YELLOW}⚠️  Meta viewport NO encontrado${NC}"
        echo -e "${YELLOW}   Agregar: <meta name='viewport' content='width=device-width, initial-scale=1.0'>${NC}"
    fi
fi

# 7. Verificar puertos disponibles
echo -e "${BLUE}[7/7] Verificando puertos...${NC}"
if lsof -Pi :9128 -sTCP:LISTEN -t >/dev/null 2>&1; then
    echo -e "${YELLOW}⚠️  Puerto 9128 en uso (Tomcat Central ya corriendo)${NC}"
else
    echo -e "${GREEN}✅ Puerto 9128 disponible${NC}"
fi

if lsof -Pi :8080 -sTCP:LISTEN -t >/dev/null 2>&1; then
    echo -e "${YELLOW}⚠️  Puerto 8080 en uso (Tomcat Web ya corriendo)${NC}"
else
    echo -e "${GREEN}✅ Puerto 8080 disponible${NC}"
fi

echo ""
echo -e "${GREEN}╔═══════════════════════════════════════════════════════════╗${NC}"
echo -e "${GREEN}║   Configuración verificada para Mac M1                   ║${NC}"
echo -e "${GREEN}╚═══════════════════════════════════════════════════════════╝${NC}"
echo ""
echo -e "${BLUE}Próximos pasos:${NC}"
echo -e "  1. source ~/.zshrc  # Recargar configuración"
echo -e "  2. ./verificar-tarea3.sh  # Verificar todo el proyecto"
echo -e "  3. Compilar y ejecutar según ECLIPSE-MAC-M1-GUIA.md"
echo ""
