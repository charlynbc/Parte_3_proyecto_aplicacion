# 🍎 Rama para_mac - Guía Rápida

## ✅ ¿Qué se hizo?

Se creó una rama completa con **adaptaciones específicas para Mac M1** (Apple Silicon), manteniendo compatibilidad con Linux.

## 📦 Archivos Creados/Modificados

### Nuevos Archivos

1. **`setup-mac-m1.sh`** - Script de instalación automática
   - Instala Homebrew (si no existe)
   - Instala Java 17 (Temurin ARM64)
   - Instala Maven
   - Configura ambos Tomcat
   - Crea todos los scripts necesarios

2. **`README-MAC-M1.md`** - Documentación completa
   - Guía paso a paso
   - Instalación manual y automática
   - Troubleshooting
   - URLs y comandos útiles

3. **`DIFERENCIAS-LINUX-MAC.md`** - Comparativa
   - Diferencias técnicas entre Linux y Mac
   - Tabla comparativa completa
   - Problemas comunes y soluciones

### Scripts Generados Automáticamente

El script `setup-mac-m1.sh` crea automáticamente:

- `build-and-deploy-mac.sh` - Compilar y desplegar todo
- `start-tomcat-central-mac.sh` - Iniciar servidor SOAP (9128)
- `start-tomcat-web-mac.sh` - Iniciar servidor Web (8080)
- `stop-tomcat-mac.sh` - Detener ambos servidores

### Archivos Modificados

1. **`apache-tomcat-10.1.47-central/bin/setenv.sh`**
   - Detecta automáticamente macOS vs Linux
   - Usa `/usr/libexec/java_home` en Mac
   - Usa ruta fija en Linux

2. **`apache-tomcat-10.1.47/bin/setenv.sh`**
   - Mismas adaptaciones que el anterior

## 🚀 Cómo Usar en Mac M1

### Opción 1: Setup Automático (RECOMENDADO)

```bash
# 1. Clonar y cambiar a la rama para_mac
git clone https://github.com/charlynbc/Parte_3_proyecto_aplicacion.git
cd Parte_3_proyecto_aplicacion
git checkout para_mac

# 2. Ejecutar setup (instala todo automáticamente)
chmod +x setup-mac-m1.sh
./setup-mac-m1.sh

# 3. Compilar y desplegar todo
./build-and-deploy-mac.sh

# 4. Abrir en navegador
open http://localhost:8080/Laboratorio2PaP/
```

**Tiempo total:** 5-10 minutos (incluye descargas)

### Opción 2: Instalación Manual

```bash
# 1. Instalar Homebrew
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"

# 2. Instalar Java 17
brew install --cask temurin17

# 3. Instalar Maven
brew install maven

# 4. Configurar JAVA_HOME en ~/.zshrc
echo 'export JAVA_HOME=$(/usr/libexec/java_home -v 17)' >> ~/.zshrc
echo 'export PATH=$JAVA_HOME/bin:$PATH' >> ~/.zshrc
source ~/.zshrc

# 5. Clonar proyecto
git clone https://github.com/charlynbc/Parte_3_proyecto_aplicacion.git
cd Parte_3_proyecto_aplicacion
git checkout para_mac

# 6. Usar build-and-deploy-mac.sh (creado por setup)
./setup-mac-m1.sh  # Crea los scripts
./build-and-deploy-mac.sh
```

## 🎯 Características Específicas para Mac M1

✅ **Java 17 Nativo ARM64**
- Mejor rendimiento que Rosetta 2
- Instalación vía Homebrew (Temurin)

✅ **Detección Automática de Sistema Operativo**
- Scripts funcionan en Mac y Linux
- Detectan automáticamente el JAVA_HOME correcto

✅ **Scripts Optimizados**
- Compilación y deployment en un solo comando
- Inicio/parada simplificados
- Verificación automática de servicios

✅ **Configuración de Tomcat para macOS**
- CATALINA_OPTS con `-Djava.awt.headless=true`
- Rutas adaptadas a macOS

✅ **Documentación Completa**
- Guía de instalación paso a paso
- Troubleshooting de problemas comunes
- Tabla comparativa Linux vs Mac

## 📊 Comparación Rápida

| Aspecto | Linux (main) | Mac M1 (para_mac) |
|---------|--------------|-------------------|
| **Java** | APT | Homebrew (Temurin) |
| **JAVA_HOME** | `/usr/lib/jvm/...` | `$(/usr/libexec/java_home)` |
| **Setup** | Manual | ✅ Automatizado |
| **Scripts** | Genéricos | ✅ Optimizados para Mac |
| **Deployment** | Manual | ✅ Script único |

## 🔗 URLs Importantes

Después de ejecutar todo:

- **Aplicación Web:** http://localhost:8080/Laboratorio2PaP/
- **AuthService WSDL:** http://localhost:9128/central-ws/services/AuthService?wsdl
- **ActividadesService WSDL:** http://localhost:9128/central-ws/services/ActividadesService?wsdl

## 📚 Documentación

Lee estos archivos para más detalles:

1. **README-MAC-M1.md** - Guía completa de instalación
2. **DIFERENCIAS-LINUX-MAC.md** - Diferencias técnicas
3. **Como-usarlo-en-Eclipse.md** - Guía de Eclipse (funciona en Mac)
4. **Todo-lo-realizado-PARTE-*.md** - Documentación del proyecto

## 🐛 Troubleshooting Rápido

### Java no encontrado
```bash
brew install --cask temurin17
```

### Puerto ocupado
```bash
./stop-tomcat-mac.sh
```

### Ver logs
```bash
tail -f apache-tomcat-10.1.47/logs/catalina.out
tail -f apache-tomcat-10.1.47-central/logs/catalina.out
```

### Permisos denegados
```bash
chmod +x *.sh
```

## ✨ Ventajas de Esta Rama

1. **Setup en Un Comando** - `./setup-mac-m1.sh` hace todo
2. **Deployment en Un Comando** - `./build-and-deploy-mac.sh` compila todo
3. **Compatibilidad Total** - Funciona en Mac M1, M2, M3
4. **También Funciona en Linux** - Scripts detectan el SO automáticamente
5. **Documentación Completa** - Guías para cada escenario

## 🔄 Volver a Main (Linux)

Si necesitas volver a la rama principal:

```bash
git checkout main
```

O crear una nueva rama basada en main:

```bash
git checkout -b mi-rama main
```

## 📈 Estructura del Proyecto

```
Parte_3_proyecto_aplicacion/
├── setup-mac-m1.sh                    # ⭐ Setup automático
├── build-and-deploy-mac.sh            # ⭐ Build todo
├── start-tomcat-central-mac.sh        # ⭐ Inicia SOAP server
├── start-tomcat-web-mac.sh            # ⭐ Inicia Web server
├── stop-tomcat-mac.sh                 # ⭐ Detiene todo
├── README-MAC-M1.md                   # ⭐ Guía Mac
├── DIFERENCIAS-LINUX-MAC.md           # ⭐ Comparativa
├── Laboratorio3Pap-main/
│   ├── central-ws/                    # Servidor SOAP
│   └── client-ws/                     # Generador stubs
├── Laboratorio2PaP-main/              # App Web
├── apache-tomcat-10.1.47-central/     # Tomcat SOAP (9128)
│   └── bin/setenv.sh                  # ⭐ Detecta Mac/Linux
└── apache-tomcat-10.1.47/             # Tomcat Web (8080)
    └── bin/setenv.sh                  # ⭐ Detecta Mac/Linux
```

## 🎓 Resumen para el Usuario

**Si tienes Mac M1:**
1. `git checkout para_mac`
2. `./setup-mac-m1.sh`
3. `./build-and-deploy-mac.sh`
4. ¡Listo! 🎉

**Todo está automatizado y documentado.**

---

## 📞 Enlaces

- **Rama en GitHub:** https://github.com/charlynbc/Parte_3_proyecto_aplicacion/tree/para_mac
- **Crear Pull Request:** https://github.com/charlynbc/Parte_3_proyecto_aplicacion/pull/new/para_mac

---

**¡Proyecto completamente adaptado para Mac M1!** 🚀🍎
