# 🍎 Pasos para Mac M1 - Guía Rápida

## 📋 Requisitos Previos

Antes de empezar, necesitas tener instalado:
- ☕ **Java 17** (ARM64 nativo para M1)
- 📦 **Maven**
- 💻 **Eclipse IDE for Enterprise Java**

---

## 🚀 Opción 1: Instalación Automática (RECOMENDADO)

### Paso 1: Clonar el repositorio

```bash
git clone https://github.com/charlynbc/Parte_3_proyecto_aplicacion.git
cd Parte_3_proyecto_aplicacion
git checkout para_mac
```

### Paso 2: Ejecutar script de setup automático

```bash
chmod +x setup-mac-m1.sh
./setup-mac-m1.sh
```

Este script instalará automáticamente:
- ☕ Java 17 (Temurin ARM64)
- 📦 Maven
- 🔧 Configurará ambos Tomcat
- 📝 Creará scripts de build y deploy

**Tiempo:** 5-10 minutos (incluye descargas)

### Paso 3: Ir directamente al Paso 5 (compilar proyectos)

Si usaste el script automático, salta directo al **Paso 5** más abajo.

---

## 🛠️ Opción 2: Instalación Manual

### Paso 1: Instalar Homebrew (si no lo tienes)

```bash
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
```

### Paso 2: Instalar Java 17

```bash
brew install --cask temurin17
```

### Paso 3: Instalar Maven

```bash
brew install maven
```

### Paso 4: Configurar JAVA_HOME

```bash
# Para Zsh (shell por defecto en Mac)
echo 'export JAVA_HOME=$(/usr/libexec/java_home -v 17)' >> ~/.zshrc
echo 'export PATH=$JAVA_HOME/bin:$PATH' >> ~/.zshrc
source ~/.zshrc

# Verificar instalación
java -version    # Debe mostrar: openjdk version "17.x.x"
mvn -version     # Debe mostrar: Apache Maven 3.x.x y Java version: 17.x.x
```

---

## 📦 Paso 5: Compilar los Proyectos (CRÍTICO)

⚠️ **IMPORTANTE:** Debes compilar desde terminal ANTES de abrir Eclipse.

### 5.1. Compilar el Servidor Central (SOAP)

```bash
cd Laboratorio3Pap-main/central-ws
mvn clean install -DskipTests
```

**Resultado esperado:**
```
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```

### 5.2. Iniciar el Servidor Central

El servidor debe estar corriendo para generar los stubs del cliente.

```bash
# Volver a la raíz del proyecto
cd ../..

# Iniciar Tomcat Central
cd apache-tomcat-10.1.47-central/bin
./catalina.sh start
```

**Esperar 10-15 segundos** para que arranque completamente.

### 5.3. Verificar que el servidor está corriendo

```bash
# Verificar WSDL (debe devolver XML, no error 404)
curl http://localhost:9128/central-ws/services/AuthService?wsdl
```

Si ves XML con `<definitions>`, está funcionando ✅

### 5.4. Generar Stubs del Cliente (CRÍTICO PARA ECLIPSE)

```bash
# Volver a la raíz
cd ../..

# Compilar client-ws
cd Laboratorio3Pap-main/client-ws
mvn clean compile install
```

**Resultado esperado:**
```
[INFO] Processing: http://localhost:9128/central-ws/services/AuthService?wsdl
[INFO] Processing: http://localhost:9128/central-ws/services/ActividadesService?wsdl
[INFO] Compiling 41 source files...
[INFO] BUILD SUCCESS
```

**¿Por qué es crítico?**
- Este paso genera código Java desde los WSDLs (wsimport)
- Instala el JAR `client-ws-1.0.0-SNAPSHOT.jar` en `~/.m2/repository/`
- Sin este JAR, Eclipse mostrará errores rojos en `Laboratorio2PaP`

### 5.5. Compilar la Aplicación Web

```bash
# Volver a la raíz
cd ../..

# Compilar Laboratorio2PaP
cd Laboratorio2PaP-main
mvn clean package
```

**Resultado esperado:**
```
[INFO] Building war: .../target/Laboratorio2PaP.war
[INFO] BUILD SUCCESS
```

---

## 💻 Paso 6: Abrir en Eclipse

### 6.1. Importar Proyectos Maven

1. Abrir **Eclipse**

2. **File → Import...**

3. Seleccionar **Maven → Existing Maven Projects**
   - ⚠️ NO uses "General → Existing Projects"
   - ⚠️ NO uses "Git → Projects from Git"

4. Clic en **Next**

5. **Browse...** → Navegar a la carpeta donde clonaste el proyecto:
   ```
   /Users/tu-usuario/ruta/Parte_3_proyecto_aplicacion
   ```

6. Eclipse detectará automáticamente **3 proyectos**:
   ```
   ☑️ /Laboratorio3Pap-main/central-ws/pom.xml
   ☑️ /Laboratorio3Pap-main/client-ws/pom.xml
   ☑️ /Laboratorio2PaP-main/pom.xml
   ```

7. **✅ Marcar los 3 proyectos**

8. Clic en **Finish**

Eclipse comenzará a:
- Descargar dependencias Maven (primera vez tarda 2-5 min)
- Configurar el classpath automáticamente
- Compilar los proyectos

---

## 🐱 Paso 7: Configurar Tomcat en Eclipse

### 7.1. Abrir vista de Servers

**Window → Show View → Servers** (o **View → Servers**)

### 7.2. Añadir Tomcat Central (Puerto 9128)

1. Clic derecho en el panel Servers → **New → Server**

2. **Server type:** Apache → **Tomcat v10.1 Server**

3. Clic en **Next**

4. **Tomcat installation directory:** Clic en **Browse...**
   ```
   /Users/tu-usuario/ruta/Parte_3_proyecto_aplicacion/apache-tomcat-10.1.47-central
   ```

5. **JRE:** Seleccionar **Java 17** (debe aparecer si lo instalaste correctamente)

6. Clic en **Finish**

### 7.3. Configurar Puerto 9128 para Tomcat Central

1. En el panel **Servers**, **doble clic** en **Tomcat v10.1 Server at localhost**

2. En la pestaña **Overview**:
   - Buscar **Ports**
   - **HTTP/1.1:** Cambiar de `8080` a `9128`

3. **File → Save** (o Cmd+S)

### 7.4. Añadir Tomcat Web (Puerto 8080)

1. Clic derecho en panel Servers → **New → Server**

2. **Server type:** Apache → **Tomcat v10.1 Server**

3. Clic en **Next**

4. **Tomcat installation directory:**
   ```
   /Users/tu-usuario/ruta/Parte_3_proyecto_aplicacion/apache-tomcat-10.1.47
   ```

5. **JRE:** Java 17

6. Clic en **Finish**

7. Verificar que el puerto sea **8080** (es el default, no cambiar)

### 7.5. Desplegar Proyectos en los Servidores

**Para Tomcat Central:**
1. Clic derecho en **Tomcat v10.1 at localhost (9128)** → **Add and Remove...**
2. Seleccionar **central-ws** en la lista **Available**
3. Clic en **Add >** (moverlo a **Configured**)
4. Clic en **Finish**

**Para Tomcat Web:**
1. Clic derecho en **Tomcat v10.1 at localhost (8080)** → **Add and Remove...**
2. Seleccionar **Laboratorio2PaP** en la lista **Available**
3. Clic en **Add >**
4. Clic en **Finish**

---

## ▶️ Paso 8: Ejecutar el Proyecto

### Orden de Inicio (IMPORTANTE ⚠️)

#### 8.1. Iniciar Tomcat Central (primero)

1. En el panel **Servers**, clic derecho en **Tomcat v10.1 at localhost (9128)**
2. Seleccionar **Start**
3. Esperar a que la consola muestre:
   ```
   Server startup in [XXXX] ms
   ```

#### 8.2. Verificar WSDLs

Abrir navegador o terminal:
```bash
curl http://localhost:9128/central-ws/services/AuthService?wsdl
```

Debe devolver XML (no error 404) ✅

#### 8.3. Iniciar Tomcat Web (segundo)

1. Clic derecho en **Tomcat v10.1 at localhost (8080)**
2. Seleccionar **Start**
3. Esperar a que la consola muestre:
   ```
   Server startup in [XXXX] ms
   ```

#### 8.4. Abrir la Aplicación

Abrir navegador:
```
http://localhost:8080/Laboratorio2PaP/
```

**Deberías ver la página de inicio del proyecto** ✅

---

## 🎯 URLs Importantes

| Servicio | URL |
|----------|-----|
| **Aplicación Web** | http://localhost:8080/Laboratorio2PaP/ |
| **Login** | http://localhost:8080/Laboratorio2PaP/login |
| **Registro** | http://localhost:8080/Laboratorio2PaP/register |
| **WSDL AuthService** | http://localhost:9128/central-ws/services/AuthService?wsdl |
| **WSDL ActividadesService** | http://localhost:9128/central-ws/services/ActividadesService?wsdl |

---

## 🛠️ Solución de Problemas Comunes

### ❌ Error: "The import uy.edu.pa.central.client cannot be resolved"

**Causa:** No compilaste `client-ws` antes de abrir Eclipse.

**Solución:**
```bash
# Terminal (fuera de Eclipse)
cd Laboratorio3Pap-main/client-ws
mvn clean compile install

# Luego en Eclipse:
# Clic derecho en Laboratorio2PaP → Maven → Update Project → OK
```

---

### ❌ Error: "Project facet Java version 17 is not supported"

**Causa:** Eclipse no tiene soporte para Java 17 o está mal configurado.

**Solución:**

1. **Eclipse → Preferences → Java → Installed JREs**
2. Verificar que Java 17 esté listado
3. Si no está, clic en **Add...** → **Standard VM** → **Next**
4. **JRE home:**
   ```
   /Library/Java/JavaVirtualMachines/temurin-17.jdk/Contents/Home
   ```
5. **✅ Marcar como default**
6. **Apply and Close**

Luego:
1. Clic derecho en el proyecto → **Properties**
2. **Java Compiler**
3. ✅ Enable project specific settings
4. **Compiler compliance level:** 17
5. **Apply and Close**

---

### ❌ Error: "Server Tomcat v10.1 at localhost failed to start"

**Causa:** Puerto ya en uso o servidor mal configurado.

**Solución:**

1. **Verificar si hay otro proceso usando el puerto:**
   ```bash
   lsof -i :9128  # Para Tomcat Central
   lsof -i :8080  # Para Tomcat Web
   ```

2. **Si hay un proceso, matarlo:**
   ```bash
   kill -9 <PID>
   ```

3. **O usar los scripts de parada:**
   ```bash
   # Detener ambos servidores
   ./stop-tomcat-mac.sh
   ```

4. **Limpiar y reintentar en Eclipse:**
   - Clic derecho en el servidor → **Clean...**
   - Clic derecho → **Start**

---

### ❌ Error: "Archive for required library cannot be found"

**Causa:** Dependencias Maven no descargadas correctamente.

**Solución:**

1. **En Eclipse:**
   - Clic derecho en el proyecto → **Maven → Update Project...**
   - ✅ Marcar **Force Update of Snapshots/Releases**
   - **OK**

2. **O desde terminal:**
   ```bash
   cd Laboratorio2PaP-main
   mvn dependency:purge-local-repository
   mvn clean install
   ```

---

## ✅ Checklist Final

Antes de decir "terminé", verifica:

- [ ] Java 17 instalado (`java -version`)
- [ ] Maven instalado (`mvn -version`)
- [ ] `JAVA_HOME` configurado en `~/.zshrc`
- [ ] `central-ws` compilado
- [ ] Tomcat Central iniciado (puerto 9128)
- [ ] WSDLs accesibles (curl devuelve XML)
- [ ] `client-ws` compilado e instalado
- [ ] JAR en `~/.m2/repository/uy/edu/pa/client-ws/1.0.0-SNAPSHOT/`
- [ ] `Laboratorio2PaP` compilado
- [ ] Proyectos importados en Eclipse sin errores rojos
- [ ] Ambos Tomcat configurados en Eclipse
- [ ] Aplicación abre en http://localhost:8080/Laboratorio2PaP/

---

## 📚 Documentación Adicional

- **ECLIPSE-MAC-M1-GUIA.md** - Guía detallada de Eclipse
- **RAMA-PARA-MAC-README.md** - Resumen de la rama
- **DIFERENCIAS-LINUX-MAC.md** - Comparativa técnica
- **Como-usarlo-en-Eclipse.md** - Guía general de Eclipse

---

**Última actualización:** Noviembre 2025  
**Rama:** `para_mac`  
**Versión:** Tarea 3 - Arquitectura Distribuida SOAP
