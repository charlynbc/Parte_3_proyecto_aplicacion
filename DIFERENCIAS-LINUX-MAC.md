# Diferencias entre Linux y Mac M1 - Guía de Migración

## 📋 Resumen de Cambios

Esta rama `para_mac` contiene todas las adaptaciones necesarias para ejecutar el proyecto en **Mac M1** (Apple Silicon / ARM64).

## 🔄 Principales Diferencias

### 1. **Java Installation**

| Aspecto | Linux | Mac M1 |
|---------|-------|--------|
| **Package Manager** | `apt` | Homebrew |
| **Instalación** | `sudo apt install openjdk-17-jdk` | `brew install --cask temurin17` |
| **JAVA_HOME** | `/usr/lib/jvm/java-17-openjdk-amd64` | `$(/usr/libexec/java_home -v 17)` |
| **Arquitectura** | x86_64 / amd64 | ARM64 / aarch64 |

### 2. **Detección de Java**

**Linux:**
```bash
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
```

**Mac:**
```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
```

> macOS tiene una utilidad `java_home` que localiza automáticamente la instalación de Java.

### 3. **Scripts Modificados**

#### `setenv.sh` (ambos Tomcat)

**ANTES (Linux):**
```bash
#!/bin/bash
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH
```

**DESPUÉS (Multiplataforma):**
```bash
#!/bin/bash
# Detectar sistema operativo
if [[ "$OSTYPE" == "darwin"* ]]; then
    # macOS
    export JAVA_HOME=$(/usr/libexec/java_home -v 17 2>/dev/null)
    if [ -z "$JAVA_HOME" ]; then
        echo "Error: Java 17 no encontrado"
        exit 1
    fi
else
    # Linux
    export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
fi

export PATH=$JAVA_HOME/bin:$PATH
export CATALINA_OPTS="-Djava.awt.headless=true"
```

### 4. **Nuevos Scripts para Mac**

| Script | Descripción |
|--------|-------------|
| `setup-mac-m1.sh` | Instalación automática de todo (Java, Maven, Homebrew) |
| `build-and-deploy-mac.sh` | Compilar y desplegar todo automáticamente |
| `start-tomcat-central-mac.sh` | Iniciar Tomcat Central (puerto 9128) |
| `start-tomcat-web-mac.sh` | Iniciar Tomcat Web (puerto 8080) |
| `stop-tomcat-mac.sh` | Detener ambos Tomcat |

### 5. **Gestión de Procesos**

| Tarea | Linux | Mac |
|-------|-------|-----|
| **Ver procesos Java** | `ps aux \| grep java` | `ps aux \| grep java` (igual) |
| **Ver puerto ocupado** | `netstat -tulpn \| grep 8080` | `lsof -i :8080` |
| **Matar proceso** | `kill -9 <PID>` | `kill -9 <PID>` (igual) |

### 6. **Homebrew vs APT**

**Linux (APT):**
```bash
sudo apt update
sudo apt install openjdk-17-jdk maven
```

**Mac (Homebrew):**
```bash
brew install --cask temurin17
brew install maven
```

### 7. **Configuración del Shell**

| Shell Config | Linux | Mac M1 |
|--------------|-------|--------|
| **Shell por defecto** | bash | zsh |
| **Archivo de config** | `~/.bashrc` o `~/.bash_profile` | `~/.zshrc` |
| **Homebrew PATH** | N/A | `/opt/homebrew/bin` (ARM64) |

**Configuración en Mac:**
```bash
# En ~/.zshrc
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
export PATH=$JAVA_HOME/bin:$PATH

# PATH de Homebrew (solo ARM64)
eval "$(/opt/homebrew/bin/brew shellenv)"
```

### 8. **Diferencias en Tomcat**

Las configuraciones de Tomcat son idénticas, pero:

- **Linux:** No requiere `CATALINA_OPTS` especiales
- **Mac:** Requiere `-Djava.awt.headless=true` para evitar problemas de GUI

## 🚀 Cómo Usar Esta Rama

### Clonar el Proyecto en Mac

```bash
git clone https://github.com/charlynbc/Parte_3_proyecto_aplicacion.git
cd Parte_3_proyecto_aplicacion
git checkout para_mac
```

### Instalación Rápida

```bash
# 1. Ejecutar setup automático
chmod +x setup-mac-m1.sh
./setup-mac-m1.sh

# 2. Compilar y desplegar todo
./build-and-deploy-mac.sh
```

### Uso Diario

```bash
# Iniciar servidores (si ya están compilados)
./start-tomcat-central-mac.sh
./start-tomcat-web-mac.sh

# Detener servidores
./stop-tomcat-mac.sh
```

## ⚙️ Compatibilidad

### Mac M1/M2/M3 (Apple Silicon)

✅ **Completamente soportado**
- Java 17 nativo para ARM64
- Mejor rendimiento que Rosetta 2
- Scripts optimizados

### Mac Intel (x86_64)

✅ **Soportado**
- Los scripts funcionan igual
- Java se instala para x86_64
- Homebrew en `/usr/local` en lugar de `/opt/homebrew`

### Linux (Ubuntu/Debian)

✅ **Soportado** (rama principal `main`)
- Scripts `setenv.sh` detectan automáticamente el SO
- Usa APT en lugar de Homebrew

## 🔍 Verificación del Sistema

### En Mac

```bash
# Ver arquitectura
uname -m
# Salida esperada: arm64

# Ver Java
java -version
# Salida esperada: openjdk version "17.0.x"

# Ver ubicación de Java
/usr/libexec/java_home -v 17
# Salida esperada: /Library/Java/JavaVirtualMachines/temurin-17.jdk/Contents/Home

# Ver Maven
mvn -version
```

### En Linux

```bash
# Ver arquitectura
uname -m
# Salida esperada: x86_64

# Ver Java
java -version

# Ver ubicación de Java
which java
# Salida esperada: /usr/bin/java
```

## 📊 Tabla Comparativa Completa

| Característica | Linux (main) | Mac M1 (para_mac) |
|----------------|--------------|-------------------|
| **Package Manager** | APT | Homebrew |
| **Java Path** | Fijo (`/usr/lib/jvm/...`) | Dinámico (`java_home`) |
| **Shell** | bash | zsh |
| **Arquitectura** | x86_64 | ARM64 |
| **Tomcat Config** | Estándar | + CATALINA_OPTS |
| **Scripts de inicio** | Manuales | Automatizados |
| **Setup automático** | No | ✅ `setup-mac-m1.sh` |
| **Deployment automático** | No | ✅ `build-and-deploy-mac.sh` |

## 🐛 Problemas Comunes en Mac

### 1. "java: command not found"

**Solución:**
```bash
brew install --cask temurin17
echo 'export JAVA_HOME=$(/usr/libexec/java_home -v 17)' >> ~/.zshrc
source ~/.zshrc
```

### 2. "Permission denied" al ejecutar .sh

**Solución:**
```bash
chmod +x *.sh
```

### 3. "Port already in use"

**Solución:**
```bash
# Ver qué usa el puerto
lsof -i :8080
lsof -i :9128

# Matar proceso
kill -9 <PID>

# O usar script
./stop-tomcat-mac.sh
```

### 4. Homebrew no encontrado

**Solución:**
```bash
# Instalar Homebrew
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"

# Configurar PATH
echo 'eval "$(/opt/homebrew/bin/brew shellenv)"' >> ~/.zprofile
eval "$(/opt/homebrew/bin/brew shellenv)"
```

## 📚 Documentación Adicional

- `README-MAC-M1.md` - Guía completa de instalación en Mac
- `Como-usarlo-en-Eclipse.md` - Guía de Eclipse (multiplataforma)
- `Todo-lo-realizado-PARTE-*.md` - Documentación técnica del proyecto

## 🔄 Merge con Main

Si quieres llevar estos cambios a la rama principal:

```bash
git checkout main
git merge para_mac
```

Los scripts `setenv.sh` son compatibles con ambos sistemas operativos, por lo que el merge es seguro.

## ✅ Checklist de Migración

Para migrar de Linux a Mac M1:

- [ ] Instalar Homebrew
- [ ] Instalar Java 17 (Temurin)
- [ ] Instalar Maven
- [ ] Clonar rama `para_mac`
- [ ] Ejecutar `./setup-mac-m1.sh`
- [ ] Ejecutar `./build-and-deploy-mac.sh`
- [ ] Verificar en http://localhost:8080/Laboratorio2PaP/

---

**¡Todo listo para ejecutar en Mac M1!** 🚀
