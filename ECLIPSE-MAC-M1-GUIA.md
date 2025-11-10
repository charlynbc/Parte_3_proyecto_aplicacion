# 🍎 Eclipse en Mac M1 - Guía Completa

## ⚠️ IMPORTANTE: Estás en la rama `para_mac`

Esta rama tiene todos los ajustes necesarios para Mac M1 (Apple Silicon).

---

## 📋 Requisitos Previos

### 1. Instalar Java 17 (ARM64 nativo para M1)

```bash
# Opción A: Usar Homebrew (RECOMENDADO)
brew install --cask temurin17

# Opción B: Descargar manualmente
# https://adoptium.net/temurin/releases/?version=17&os=mac&arch=aarch64
```

### 2. Instalar Maven

```bash
brew install maven
```

### 3. Configurar JAVA_HOME en tu shell

```bash
# Para Zsh (shell por defecto en Mac)
echo 'export JAVA_HOME=$(/usr/libexec/java_home -v 17)' >> ~/.zshrc
echo 'export PATH=$JAVA_HOME/bin:$PATH' >> ~/.zshrc
source ~/.zshrc

# Verificar
java -version  # Debe mostrar versión 17.x
mvn -version   # Debe mostrar Java 17
```

---

## 🚀 Setup Rápido con Script Automático

Si quieres configurar TODO automáticamente (Java, Maven, Tomcat, scripts):

```bash
cd /ruta/a/Parte_3_proyecto_aplicacion
git checkout para_mac
chmod +x setup-mac-m1.sh
./setup-mac-m1.sh
```

El script instalará y configurará todo en 5-10 minutos.

---

## 📦 Compilar el Proyecto ANTES de Abrir en Eclipse

Eclipse necesita que los artefactos Maven estén compilados primero, especialmente el `client-ws` (stubs SOAP).

### Paso 1: Compilar el Servidor Central (SOAP)

```bash
cd Laboratorio3Pap-main/central-ws
mvn clean install -DskipTests
```

**Resultado esperado:**
- `BUILD SUCCESS`
- WAR generado en: `target/central-ws.war`

### Paso 2: Iniciar el Servidor Central

El servidor debe estar corriendo para generar los stubs del cliente.

```bash
# Desde la raíz del proyecto
./start-tomcat-central-mac.sh

# O manualmente
cd apache-tomcat-10.1.47-central
bin/catalina.sh start

# Esperar 10 segundos y verificar
curl http://localhost:9128/central-ws/services/AuthService?wsdl
```

**Debe responder con XML del WSDL** (no error 404).

### Paso 3: Generar Stubs del Cliente (client-ws)

⚠️ **CRÍTICO**: Este paso instala el JAR `client-ws` en tu repositorio Maven local (~/.m2). Sin esto, `Laboratorio2PaP` tendrá errores de dependencias.

```bash
cd Laboratorio3Pap-main/client-ws
mvn clean compile install
```

**Resultado esperado:**
- `BUILD SUCCESS`
- Verás mensajes: `Processing: http://localhost:9128/central-ws/services/AuthService?wsdl`
- JAR instalado en: `~/.m2/repository/uy/edu/pa/client-ws/1.0.0-SNAPSHOT/`

### Paso 4: Compilar la Aplicación Web

```bash
cd Laboratorio2PaP-main
mvn clean package
```

**Resultado esperado:**
- `BUILD SUCCESS`
- WAR generado en: `target/Laboratorio2PaP.war`

---

## 🔧 Configurar Eclipse

### 1. Instalar Eclipse IDE para Java EE

```bash
# Descargar desde: https://www.eclipse.org/downloads/packages/
# Buscar: "Eclipse IDE for Enterprise Java and Web Developers"
# Versión ARM64 (Apple Silicon)
```

### 2. Configurar Java 17 en Eclipse

1. **Eclipse → Preferences → Java → Installed JREs**
2. Clic en **"Add..."**
3. Seleccionar **"Standard VM"** → Next
4. **JRE home:** `/Library/Java/JavaVirtualMachines/temurin-17.jdk/Contents/Home`
5. Nombre: `Java 17 (Temurin ARM64)`
6. **Marcar como JRE por defecto** ✅
7. Apply and Close

### 3. Configurar Maven en Eclipse

1. **Eclipse → Preferences → Maven**
2. Verificar que **"User Settings"** apunte a: `~/.m2/settings.xml` (o usar el default)
3. **Update Settings**: Asegúrate de que está habilitado
4. Apply and Close

### 4. Importar Proyectos

#### Opción A: Importar como Maven Projects (RECOMENDADO)

1. **File → Import... → Maven → Existing Maven Projects**
2. **Root Directory:** Navegar a `/ruta/a/Parte_3_proyecto_aplicacion`
3. Eclipse detectará automáticamente:
   - `Laboratorio3Pap-main/central-ws/pom.xml`
   - `Laboratorio3Pap-main/client-ws/pom.xml`
   - `Laboratorio2PaP-main/pom.xml`
4. ✅ **Marcar los 3 proyectos**
5. **Finish**

Eclipse descargará todas las dependencias automáticamente.

#### Opción B: Importar Proyectos Existentes

Si ya tienes archivos `.project`:

1. **File → Import... → General → Existing Projects into Workspace**
2. Seleccionar cada carpeta de proyecto
3. Finish

---

## 🐱 Configurar Tomcat en Eclipse

### 1. Añadir Servidor Tomcat Central (Puerto 9128)

1. **Window → Show View → Servers** (o View → Servers)
2. Clic derecho en el panel → **New → Server**
3. **Server type:** Apache Tomcat v10.1
4. **Next**
5. **Tomcat installation directory:** 
   ```
   /ruta/a/Parte_3_proyecto_aplicacion/apache-tomcat-10.1.47-central
   ```
6. **JRE:** Seleccionar **Java 17 (Temurin ARM64)**
7. **Finish**

### 2. Configurar Puerto del Servidor Central

1. En el panel **Servers**, doble clic en **Tomcat v10.1 Server at localhost**
2. En la pestaña **Overview:**
   - **HTTP/1.1:** Cambiar de `8080` a `9128`
3. **File → Save**

### 3. Añadir Servidor Tomcat Web (Puerto 8080)

Repetir los pasos anteriores, pero usando:
- **Tomcat installation directory:** 
  ```
  /ruta/a/Parte_3_proyecto_aplicacion/apache-tomcat-10.1.47
  ```
- **Puerto:** Dejar en `8080`

### 4. Desplegar Proyectos

1. Clic derecho en **Tomcat Central (9128)** → **Add and Remove...**
2. Mover `central-ws` de **Available** → **Configured**
3. **Finish**

4. Clic derecho en **Tomcat Web (8080)** → **Add and Remove...**
5. Mover `Laboratorio2PaP` de **Available** → **Configured**
6. **Finish**

---

## ▶️ Ejecutar el Proyecto

### Orden de Inicio (IMPORTANTE)

1. **Iniciar Tomcat Central primero:**
   - Clic derecho en **Tomcat Central** → **Start**
   - Esperar a que la consola muestre: `Server startup in [XXXX] ms`

2. **Verificar WSDLs:**
   ```bash
   curl http://localhost:9128/central-ws/services/AuthService?wsdl
   ```

3. **Iniciar Tomcat Web:**
   - Clic derecho en **Tomcat Web** → **Start**
   - Esperar a que la consola muestre: `Server startup in [XXXX] ms`

4. **Abrir en navegador:**
   ```
   http://localhost:8080/Laboratorio2PaP/
   ```

---

## 🛠️ Solución de Problemas en Eclipse

### ❌ Error: "The import uy.edu.pa.central.client cannot be resolved"

**Causa:** El JAR `client-ws` no está instalado en `~/.m2/`

**Solución:**
```bash
# Terminal (fuera de Eclipse)
cd Laboratorio3Pap-main/client-ws
mvn clean compile install

# Luego en Eclipse:
# Clic derecho en Laboratorio2PaP → Maven → Update Project
```

---

### ❌ Error: "Project facet Java version 17 is not supported"

**Causa:** Eclipse no reconoce Java 17 o está configurado para otra versión.

**Solución:**

1. **Instalar Java 17 Support en Eclipse:**
   - Help → Check for Updates
   - O instalar: **Eclipse Marketplace → Buscar "Java 17"**

2. **Cambiar Compiler Level del Proyecto:**
   - Clic derecho en `Laboratorio2PaP` → **Properties**
   - **Java Compiler**
   - ✅ Enable project specific settings
   - **Compiler compliance level:** 17
   - Apply

---

### ❌ Error: "Archive for required library cannot be found"

**Causa:** Dependencias Maven no descargadas.

**Solución:**

1. **Clic derecho en el proyecto → Maven → Update Project...**
2. ✅ Marcar **Force Update of Snapshots/Releases**
3. OK

O desde terminal:
```bash
mvn dependency:purge-local-repository
mvn clean install
```

---

### ❌ Error: "Unable to ping server at localhost:9128"

**Causa:** Tomcat Central no está corriendo o puerto incorrecto.

**Solución:**

1. **Verificar que Tomcat Central esté iniciado**
   ```bash
   lsof -i :9128
   # Debe mostrar un proceso Java
   ```

2. **Si no hay nada en el puerto, iniciar manualmente:**
   ```bash
   cd apache-tomcat-10.1.47-central
   bin/catalina.sh run  # Usar 'run' para ver logs en consola
   ```

3. **Verificar logs:**
   ```bash
   tail -f apache-tomcat-10.1.47-central/logs/catalina.out
   ```

---

### ❌ Error: "java.lang.ClassNotFoundException: jakarta.servlet.jsp.jstl.core.Config"

**Causa:** JSTL no está incluido en el WAR.

**Solución:**

Ya está configurado correctamente en `Laboratorio2PaP-main/pom.xml`:

```xml
<dependency>
  <groupId>org.glassfish.web</groupId>
  <artifactId>jakarta.servlet.jsp.jstl</artifactId>
  <version>3.0.1</version>
  <!-- Sin scope=provided, se incluye en el WAR -->
</dependency>
```

Recompilar:
```bash
cd Laboratorio2PaP-main
mvn clean package
```

---

## 📌 Diferencias Clave: Mac vs Linux

| Aspecto | Linux (Codespace) | Mac M1 (esta rama) |
|---------|-------------------|-------------------|
| **Java Home** | `/usr/lib/jvm/java-17-openjdk-amd64` | `/Library/Java/JavaVirtualMachines/temurin-17.jdk/Contents/Home` |
| **Arquitectura** | x86_64 (AMD64) | arm64 (Apple Silicon) |
| **Shell** | bash | zsh (por defecto) |
| **Comando Java Home** | Ruta fija | `/usr/libexec/java_home -v 17` |
| **setenv.sh** | Usa ruta Linux | Detecta macOS automáticamente |
| **Homebrew** | No disponible | Requerido |

---

## 🔗 URLs Importantes

| Servicio | URL | Puerto |
|----------|-----|--------|
| **SOAP AuthService WSDL** | http://localhost:9128/central-ws/services/AuthService?wsdl | 9128 |
| **SOAP ActividadesService WSDL** | http://localhost:9128/central-ws/services/ActividadesService?wsdl | 9128 |
| **Aplicación Web** | http://localhost:8080/Laboratorio2PaP/ | 8080 |
| **Login** | http://localhost:8080/Laboratorio2PaP/login | 8080 |
| **Registro** | http://localhost:8080/Laboratorio2PaP/register | 8080 |

---

## 🎯 Checklist de Verificación

Antes de abrir Eclipse, verifica que:

- [ ] Java 17 instalado (ARM64 nativo para M1)
- [ ] Maven instalado
- [ ] `JAVA_HOME` configurado en `~/.zshrc`
- [ ] `central-ws` compilado (`mvn clean install`)
- [ ] Tomcat Central iniciado (puerto 9128)
- [ ] WSDLs accesibles (curl devuelve XML)
- [ ] `client-ws` compilado e instalado (`mvn clean compile install`)
- [ ] JAR en `~/.m2/repository/uy/edu/pa/client-ws/1.0.0-SNAPSHOT/`
- [ ] `Laboratorio2PaP` compilado (`mvn clean package`)

Si todos estos pasos están ✅, Eclipse debería importar el proyecto sin errores.

---

## 📚 Documentación Adicional

- **README-MAC-M1.md** - Guía completa de instalación
- **DIFERENCIAS-LINUX-MAC.md** - Comparativa técnica
- **RAMA-PARA-MAC-README.md** - Resumen de la rama
- **Como-usarlo-en-Eclipse.md** - Guía general de Eclipse

---

## 🆘 Soporte

Si encuentras errores específicos en Eclipse:

1. **Copiar el mensaje de error completo**
2. Verificar logs de Tomcat:
   ```bash
   tail -100 apache-tomcat-10.1.47-central/logs/catalina.out
   ```
3. Verificar dependencias Maven:
   ```bash
   cd Laboratorio2PaP-main
   mvn dependency:tree
   ```

---

**Última actualización:** Noviembre 2025  
**Versión del proyecto:** Tarea 3 - Arquitectura Distribuida SOAP
