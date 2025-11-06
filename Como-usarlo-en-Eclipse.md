# Cómo Usar el Proyecto en Eclipse - Guía Completa

## 📋 Índice
- [Requisitos Previos](#requisitos-previos)
- [Descargar e Instalar Software](#descargar-e-instalar-software)
- [Importar Proyectos en Eclipse](#importar-proyectos-en-eclipse)
- [Configurar Tomcat en Eclipse](#configurar-tomcat-en-eclipse)
- [Compilar y Desplegar](#compilar-y-desplegar)
- [Probar la Aplicación](#probar-la-aplicación)
- [Troubleshooting](#troubleshooting)

---

## 🎯 Requisitos Previos

Antes de comenzar, necesitas tener instalado:

| Software | Versión Mínima | ¿Para qué? |
|----------|----------------|------------|
| **Java JDK** | 17 o superior | Compilar y ejecutar código Java |
| **Eclipse IDE** | 2023-09 o superior | Entorno de desarrollo |
| **Apache Maven** | 3.9+ | Gestión de dependencias |
| **Apache Tomcat** | 10.1.x | Servidor de aplicaciones |
| **MySQL** (opcional) | 8.0+ | Base de datos (usamos Railway Cloud) |

---

## 📥 Descargar e Instalar Software

### 1. Instalar Java JDK 17

#### Windows:

1. **Descargar JDK 17:**
   - Ir a: https://adoptium.net/
   - Seleccionar:
     - Operating System: **Windows**
     - Architecture: **x64**
     - Package Type: **JDK**
     - Version: **17 - LTS**
   - Click en **Download**

2. **Instalar:**
   - Ejecutar el archivo descargado (`.msi`)
   - Seguir el asistente (Next → Next → Install)
   - Ruta por defecto: `C:\Program Files\Eclipse Adoptium\jdk-17.x.x.x-hotspot\`

3. **Configurar variable de entorno JAVA_HOME:**
   - Click derecho en **"Este equipo"** → **Propiedades**
   - **Configuración avanzada del sistema**
   - **Variables de entorno**
   - En **Variables del sistema**, click en **Nueva**:
     - Nombre: `JAVA_HOME`
     - Valor: `C:\Program Files\Eclipse Adoptium\jdk-17.0.16-hotspot`
   - Editar variable **Path**, agregar: `%JAVA_HOME%\bin`
   - Click **Aceptar**

4. **Verificar instalación:**
   ```cmd
   java -version
   # Salida esperada:
   # openjdk version "17.0.16" 2024-01-16
   # OpenJDK Runtime Environment Temurin-17.0.16+...
   ```

#### Linux/Mac:

```bash
# Ubuntu/Debian
sudo apt update
sudo apt install openjdk-17-jdk

# Mac (con Homebrew)
brew install openjdk@17

# Verificar
java -version
```

---

### 2. Instalar Eclipse IDE

1. **Descargar Eclipse:**
   - Ir a: https://www.eclipse.org/downloads/
   - Descargar **Eclipse IDE for Enterprise Java and Web Developers**
   - Versión recomendada: **2023-09** o superior

2. **Instalar:**
   - **Windows:** Ejecutar `eclipse-inst-win64.exe`
   - **Linux/Mac:** Extraer y ejecutar `eclipse-inst`
   - Seleccionar: **Eclipse IDE for Enterprise Java and Web Developers**
   - Elegir directorio de instalación
   - Click **Install**

3. **Iniciar Eclipse:**
   - Seleccionar workspace (carpeta para proyectos)
   - Recomendado: `C:\Users\TuUsuario\eclipse-workspace` (Windows)
   - Click **Launch**

---

### 3. Instalar Apache Maven

#### Opción A: Usar Maven integrado en Eclipse
Eclipse ya trae Maven integrado (m2e plugin). **No necesitas instalarlo por separado.**

Verificar en Eclipse:
- **Window** → **Preferences** → **Maven**
- Debe aparecer configuración de Maven

#### Opción B: Instalar Maven standalone (opcional)

**Windows:**
1. Descargar: https://maven.apache.org/download.cgi
2. Descargar `apache-maven-3.9.x-bin.zip`
3. Extraer en `C:\Program Files\Apache\maven`
4. Agregar a **Path**: `C:\Program Files\Apache\maven\bin`
5. Verificar:
   ```cmd
   mvn -version
   ```

**Linux/Mac:**
```bash
# Ubuntu/Debian
sudo apt install maven

# Mac
brew install maven

# Verificar
mvn -version
```

---

### 4. Descargar Apache Tomcat 10

1. **Descargar Tomcat:**
   - Ir a: https://tomcat.apache.org/download-10.cgi
   - Sección **10.1.47** (o versión más reciente de la serie 10.1.x)
   - **Windows:** Descargar `apache-tomcat-10.1.47-windows-x64.zip`
   - **Linux/Mac:** Descargar `apache-tomcat-10.1.47.tar.gz`

2. **Extraer:**
   - **Windows:** Extraer en `C:\Apache\Tomcat10`
   - **Linux/Mac:** Extraer en `/opt/tomcat10` o `~/tomcat10`

3. **Estructura de Tomcat:**
   ```
   apache-tomcat-10.1.47/
   ├── bin/          # Scripts para iniciar/detener
   ├── conf/         # Configuración
   ├── lib/          # Librerías
   ├── logs/         # Logs
   ├── temp/         # Archivos temporales
   ├── webapps/      # Aplicaciones desplegadas
   └── work/         # Trabajo temporal
   ```

4. **Configurar puertos (si es necesario):**
   
   Para este proyecto necesitamos **DOS instancias de Tomcat**:
   - **Tomcat 1 (Puerto 8080):** Servidor Web (Laboratorio2PaP)
   - **Tomcat 2 (Puerto 9128):** Servidor Central (central-ws)

   **Configurar segunda instancia:**
   - Copiar carpeta completa: `apache-tomcat-10.1.47` → `apache-tomcat-10.1.47-central`
   - Editar `apache-tomcat-10.1.47-central/conf/server.xml`:
     ```xml
     <!-- Cambiar puerto de apagado -->
     <Server port="8006" shutdown="SHUTDOWN">  <!-- era 8005 -->
     
     <!-- Cambiar puerto HTTP -->
     <Connector port="9128" protocol="HTTP/1.1"  <!-- era 8080 -->
                connectionTimeout="20000"
                redirectPort="8443" />
     
     <!-- Cambiar puerto AJP -->
     <Connector protocol="AJP/1.3"
                address="::1"
                port="8010"  <!-- era 8009 -->
                redirectPort="8443" />
     ```

---

## 📂 Importar Proyectos en Eclipse

### 1. Clonar o Descargar el Repositorio

**Opción A: Con Git (recomendado)**
```bash
git clone https://github.com/charlynbc/Parte_3_proyecto_aplicacion.git
cd Parte_3_proyecto_aplicacion
```

**Opción B: Descarga directa**
1. Ir a: https://github.com/charlynbc/Parte_3_proyecto_aplicacion
2. Click en **Code** → **Download ZIP**
3. Extraer en una carpeta accesible

### 2. Importar central-ws en Eclipse

1. **Abrir Eclipse**

2. **File** → **Import...**

3. Seleccionar **Maven** → **Existing Maven Projects**

4. Click **Next**

5. **Root Directory:** Buscar y seleccionar:
   ```
   /ruta/a/Parte_3_proyecto_aplicacion/Laboratorio3Pap-main/central-ws
   ```

6. Debe aparecer en **Projects:**
   ```
   ☑ /central-ws/pom.xml
   ```

7. Click **Finish**

8. **Esperar a que Maven descargue dependencias:**
   - Ver progreso en parte inferior derecha: "Updating Maven project..."
   - Puede tomar 2-5 minutos la primera vez

9. **Verificar importación:**
   - En **Project Explorer** debe aparecer: `central-ws`
   - Sin errores (sin ❌ roja en el nombre del proyecto)

---

### 3. Importar client-ws en Eclipse

1. **File** → **Import...**

2. **Maven** → **Existing Maven Projects**

3. **Root Directory:**
   ```
   /ruta/a/Parte_3_proyecto_aplicacion/Laboratorio3Pap-main/client-ws
   ```

4. Click **Finish**

**⚠️ IMPORTANTE:** Este proyecto NO compilará aún porque necesita que central-ws esté desplegado (para leer los WSDLs). Esto es normal.

---

### 4. Importar Laboratorio2PaP en Eclipse

1. **File** → **Import...**

2. **Maven** → **Existing Maven Projects**

3. **Root Directory:**
   ```
   /ruta/a/Parte_3_proyecto_aplicacion/Laboratorio2PaP-main
   ```

4. Click **Finish**

**⚠️ IMPORTANTE:** Este proyecto tampoco compilará aún porque necesita `client-ws.jar` instalado en Maven local.

---

## 🖥️ Configurar Tomcat en Eclipse

### 1. Agregar Servidor Tomcat (Puerto 8080)

1. **Window** → **Show View** → **Servers**
   - Se abre la vista **Servers** en la parte inferior

2. Click derecho en área vacía → **New** → **Server**

3. **Tipo de servidor:**
   - Expandir **Apache**
   - Seleccionar: **Tomcat v10.1 Server**
   - Server name: `Tomcat 10 - Puerto 8080`
   - Click **Next**

4. **Tomcat installation directory:**
   - Click **Browse...**
   - Seleccionar: `C:\Apache\Tomcat10\apache-tomcat-10.1.47`
   - Click **Next**

5. **Add projects:**
   - NO agregar nada aún
   - Click **Finish**

6. **Configurar puerto:**
   - Doble click en **Tomcat 10 - Puerto 8080** (en vista Servers)
   - Se abre **Overview**
   - En sección **Ports:**
     - HTTP/1.1: **8080** (dejar como está)
     - Server Shutdown Port: **8005**
   - **Server Locations:**
     - Seleccionar: **Use Tomcat installation (takes control of Tomcat installation)**
   - **Ctrl+S** para guardar

---

### 2. Agregar Servidor Tomcat Central (Puerto 9128)

1. Click derecho en vista **Servers** → **New** → **Server**

2. **Tipo:**
   - **Tomcat v10.1 Server**
   - Server name: `Tomcat 10 - Central (9128)`
   - Click **Next**

3. **Installation directory:**
   - Seleccionar: `C:\Apache\Tomcat10\apache-tomcat-10.1.47-central`
   - Click **Next**

4. Click **Finish**

5. **Configurar puerto:**
   - Doble click en **Tomcat 10 - Central (9128)**
   - **Ports:**
     - HTTP/1.1: **9128**
     - Server Shutdown Port: **8006**
   - **Server Locations:**
     - Seleccionar: **Use Tomcat installation**
   - **Ctrl+S** para guardar

---

## 🔨 Compilar y Desplegar

### Flujo de Despliegue

```
1. Compilar central-ws
2. Desplegar central-ws en Tomcat Central (puerto 9128)
3. Iniciar Tomcat Central
4. Generar stubs (client-ws) leyendo WSDLs
5. Instalar client-ws en Maven local
6. Compilar Laboratorio2PaP (usa client-ws.jar)
7. Desplegar Laboratorio2PaP en Tomcat Web (puerto 8080)
8. Iniciar Tomcat Web
```

---

### PASO 1: Compilar central-ws

**Opción A: Desde Eclipse (interfaz gráfica)**

1. Click derecho en proyecto **central-ws**

2. **Run As** → **Maven build...**

3. En **Goals:** escribir:
   ```
   clean install
   ```

4. Click **Run**

5. **Consola debe mostrar:**
   ```
   [INFO] BUILD SUCCESS
   [INFO] Total time: 5.231 s
   ```

6. **Verificar WAR generado:**
   - Expandir `central-ws` → `target`
   - Debe existir: `central-ws.war`

**Opción B: Desde Terminal**

```bash
cd /ruta/a/Parte_3_proyecto_aplicacion/Laboratorio3Pap-main/central-ws
mvn clean install
```

---

### PASO 2: Desplegar central-ws en Tomcat Central

**Opción A: Desde Eclipse**

1. En vista **Servers**, click derecho en **Tomcat 10 - Central (9128)**

2. **Add and Remove...**

3. Seleccionar **central-ws** de la lista izquierda

4. Click **Add >**

5. Debe aparecer en **Configured:**
   ```
   Configured:
     central-ws
   ```

6. Click **Finish**

**Opción B: Copiar WAR manualmente**

```bash
# Windows
copy "Laboratorio3Pap-main\central-ws\target\central-ws.war" "C:\Apache\Tomcat10\apache-tomcat-10.1.47-central\webapps\"

# Linux/Mac
cp Laboratorio3Pap-main/central-ws/target/central-ws.war /opt/tomcat10-central/webapps/
```

---

### PASO 3: Iniciar Tomcat Central

1. En vista **Servers**, click derecho en **Tomcat 10 - Central (9128)**

2. **Start**

3. **Consola debe mostrar:**
   ```
   INFO: Starting ProtocolHandler ["http-nio-9128"]
   INFO: Server startup in [3456] milliseconds
   ```

4. **Verificar deployment:**
   - Esperar 5-10 segundos
   - Debe aparecer en consola:
     ```
     INFO: Deployment of web application archive [...\central-ws.war] has finished in [4,123] ms
     ```

5. **Verificar WSDLs accesibles:**
   - Abrir navegador
   - Ir a: `http://localhost:9128/central-ws/services/AuthService?wsdl`
   - Debe mostrar XML (el WSDL)
   - Ir a: `http://localhost:9128/central-ws/services/ActividadesService?wsdl`
   - También debe mostrar XML

✅ **Si ves XML en ambas URLs, el servidor está funcionando correctamente.**

---

### PASO 4: Generar Stubs (client-ws)

**⚠️ IMPORTANTE:** central-ws DEBE estar corriendo antes de este paso.

**Opción A: Desde Eclipse**

1. Click derecho en proyecto **client-ws**

2. **Run As** → **Maven build...**

3. **Goals:**
   ```
   clean compile install
   ```

4. Click **Run**

5. **Consola debe mostrar:**
   ```
   [INFO] --- jaxws-maven-plugin:4.0.0:wsimport (wsimport-auth) @ client-ws ---
   [INFO] Processing: http://localhost:9128/central-ws/services/AuthService?wsdl
   parsing WSDL...
   Generating code...
   
   [INFO] --- jaxws-maven-plugin:4.0.0:wsimport (wsimport-actividades) @ client-ws ---
   [INFO] Processing: http://localhost:9128/central-ws/services/ActividadesService?wsdl
   parsing WSDL...
   Generating code...
   
   [INFO] BUILD SUCCESS
   ```

6. **Verificar clases generadas:**
   - Expandir `client-ws` → `target` → `generated-sources` → `wsimport`
   - Debe contener:
     - `uy/edu/pa/client/ws/auth/` (13 archivos .java)
     - `uy/edu/pa/client/ws/actividades/` (28 archivos .java)

7. **Verificar JAR instalado:**
   - Maven instaló el JAR en:
     ```
     C:\Users\TuUsuario\.m2\repository\uy\edu\pa\client-ws\1.0.0\client-ws-1.0.0.jar
     ```

**Opción B: Desde Terminal**

```bash
cd /ruta/a/Parte_3_proyecto_aplicacion/Laboratorio3Pap-main/client-ws
mvn clean compile install
```

---

### PASO 5: Compilar Laboratorio2PaP

**Opción A: Desde Eclipse**

1. Click derecho en proyecto **Laboratorio2PaP**

2. **Maven** → **Update Project...**
   - Esto fuerza a Eclipse a recargar dependencias

3. Click **OK**

4. Click derecho en **Laboratorio2PaP** → **Run As** → **Maven build...**

5. **Goals:**
   ```
   clean package
   ```

6. Click **Run**

7. **Consola debe mostrar:**
   ```
   [INFO] Building war: ...\Laboratorio2PaP\target\Laboratorio2PaP.war
   [INFO] BUILD SUCCESS
   [INFO] Total time: 7.214 s
   ```

8. **Verificar WAR:**
   - Expandir `Laboratorio2PaP` → `target`
   - Debe existir: `Laboratorio2PaP.war`

**Opción B: Desde Terminal**

```bash
cd /ruta/a/Parte_3_proyecto_aplicacion/Laboratorio2PaP-main
mvn clean package
```

---

### PASO 6: Desplegar Laboratorio2PaP en Tomcat Web

**Desde Eclipse:**

1. Vista **Servers**, click derecho en **Tomcat 10 - Puerto 8080**

2. **Add and Remove...**

3. Agregar **Laboratorio2PaP**

4. Click **Finish**

**Manualmente:**

```bash
# Windows
copy "Laboratorio2PaP-main\target\Laboratorio2PaP.war" "C:\Apache\Tomcat10\apache-tomcat-10.1.47\webapps\"

# Linux/Mac
cp Laboratorio2PaP-main/target/Laboratorio2PaP.war /opt/tomcat10/webapps/
```

---

### PASO 7: Iniciar Tomcat Web

1. Vista **Servers**, click derecho en **Tomcat 10 - Puerto 8080**

2. **Start**

3. **Consola debe mostrar:**
   ```
   INFO: Starting ProtocolHandler ["http-nio-8080"]
   INFO: Deployment of web application archive [...\Laboratorio2PaP.war] has finished in [8,456] ms
   INFO: Server startup in [9234] milliseconds
   ```

4. **Verificar aplicación:**
   - Abrir navegador
   - Ir a: `http://localhost:8080/Laboratorio2PaP/`
   - Debe mostrar la página de inicio

✅ **Si ves la página de inicio, todo está funcionando!**

---

## 🧪 Probar la Aplicación

### Verificar Estado de Servidores

**En Eclipse:**
- Vista **Servers**
- Ambos servidores deben mostrar estado **Started** con círculo verde ●

**URLs para verificar:**

| Descripción | URL | Resultado esperado |
|-------------|-----|-------------------|
| **Aplicación Web** | http://localhost:8080/Laboratorio2PaP/ | Página de inicio |
| **Login** | http://localhost:8080/Laboratorio2PaP/login.jsp | Formulario de login |
| **AuthService WSDL** | http://localhost:9128/central-ws/services/AuthService?wsdl | XML del WSDL |
| **ActividadesService WSDL** | http://localhost:9128/central-ws/services/ActividadesService?wsdl | XML del WSDL |
| **Test DB** | http://localhost:8080/Laboratorio2PaP/test-db | Lista de usuarios |

---

### Flujo de Prueba Completo

#### 1. Registrar Turista

1. Ir a: `http://localhost:8080/Laboratorio2PaP/register-tourist.jsp`

2. Llenar formulario:
   - **Nickname:** `juan123`
   - **Nombre:** `Juan`
   - **Apellido:** `Pérez`
   - **Email:** `juan@example.com`
   - **Contraseña:** `password123`
   - **Fecha de Nacimiento:** `1990-05-15`
   - **Nacionalidad:** `Uruguay`

3. Click **Registrar**

4. **Resultado esperado:**
   - Redirige a `login.jsp`
   - Mensaje: "Registro exitoso"

5. **Verificar en consola de Tomcat Central:**
   ```
   ✅ Turista registrado: juan123
   ```

---

#### 2. Login

1. Ir a: `http://localhost:8080/Laboratorio2PaP/login.jsp`

2. Ingresar:
   - **Usuario:** `juan123`
   - **Contraseña:** `password123`

3. Click **Iniciar Sesión**

4. **Resultado esperado:**
   - Redirige a dashboard de turista
   - Muestra nombre del usuario en navbar

---

#### 3. Registrar Proveedor

1. **Logout** (si estás logueado)

2. Ir a: `http://localhost:8080/Laboratorio2PaP/register-provider.jsp`

3. Llenar formulario:
   - **Nickname:** `proveedor123`
   - **Nombre:** `María`
   - **Apellido:** `González`
   - **Email:** `maria@example.com`
   - **Contraseña:** `password123`
   - **Fecha de Nacimiento:** `1985-08-20`
   - **Descripción:** `Organizador de tours`
   - **Sitio Web:** `https://tours.com`

4. Click **Registrar**

5. **Login con proveedor123**

---

#### 4. Crear Actividad (como Proveedor)

1. Estar logueado como **proveedor123**

2. Ir a: `http://localhost:8080/Laboratorio2PaP/create-activity.jsp`

3. Llenar formulario:
   - **Nombre:** `Rafting en el río Uruguay`
   - **Descripción:** `Aventura extrema en rápidos`
   - **Duración:** `4` (horas)
   - **Costo:** `50.00`
   - **Ciudad:** `Salto`

4. Click **Crear Actividad**

5. **Resultado esperado:**
   - Redirige a lista de actividades
   - Aparece la nueva actividad

6. **Verificar en consola Central:**
   ```
   ✅ Actividad creada: Rafting en el río Uruguay
   ```

---

#### 5. Crear Salida (como Proveedor)

1. Ir a: `http://localhost:8080/Laboratorio2PaP/create-departure-view`

2. Seleccionar **Actividad:** `Rafting en el río Uruguay`

3. Llenar:
   - **Nombre de la Salida:** `Salida Noviembre 2025`
   - **Fecha:** `2025-11-15`
   - **Hora:** `09:00`
   - **Lugar:** `Puerto de Salto`
   - **Cant. Máx. Turistas:** `10`

4. Click **Crear Salida**

5. **Resultado esperado:**
   - Salida creada
   - Aparece en listado de salidas

---

#### 6. Inscribirse a Salida (como Turista)

1. **Logout** y login como **juan123**

2. Ver actividades disponibles

3. Seleccionar **Rafting en el río Uruguay**

4. Ver salidas disponibles

5. **Inscribirse** a `Salida Noviembre 2025`

6. Ingresar:
   - **Cantidad de personas:** `2`

7. Click **Inscribirse**

8. **Resultado esperado:**
   - Inscripción registrada
   - Aparece en "Mis Inscripciones"

---

### Ver Logs en Eclipse

**Logs en tiempo real:**

1. Vista **Console** (parte inferior)

2. Ver salida de servidor:
   - Dropdown a la derecha de la consola
   - Seleccionar: **Tomcat 10 - Puerto 8080** o **Tomcat 10 - Central (9128)**

3. **Logs importantes:**
   ```
   # Servidor Central
   ✅ EntityManagerFactory inicializado correctamente
   ✅ Turista registrado: juan123
   ✅ Proveedor registrado: proveedor123
   ✅ Actividad creada: Rafting en el río Uruguay
   ✅ Salida creada: Salida Noviembre 2025
   ✅ Inscripción creada: juan123 → Salida Noviembre 2025
   
   # Servidor Web
   AuthService_Service initialized
   ActividadesService_Service initialized
   ```

---

## 🔧 Troubleshooting

### Problema 1: "Port 8080 already in use"

**Síntoma:**
```
java.net.BindException: Address already in use: bind
```

**Causa:** Otro proceso está usando el puerto 8080.

**Solución:**

**Windows:**
```cmd
# Ver qué proceso usa el puerto
netstat -ano | findstr :8080

# Matar proceso (reemplazar PID)
taskkill /F /PID 1234
```

**Linux/Mac:**
```bash
# Ver proceso
lsof -i :8080

# Matar proceso
kill -9 PID
```

**O cambiar puerto en Eclipse:**
1. Doble click en servidor Tomcat
2. Cambiar **HTTP/1.1** a `8081`
3. Guardar y reiniciar

---

### Problema 2: "BUILD FAILURE - Cannot resolve dependencies"

**Síntoma:**
```
[ERROR] Failed to execute goal on project Laboratorio2PaP: 
Could not resolve dependencies for project uy.edu.pa:Laboratorio2PaP:war:1.0.0: 
Could not find artifact uy.edu.pa:client-ws:jar:1.0.0
```

**Causa:** `client-ws.jar` no está instalado en Maven local.

**Solución:**
1. Verificar que **Tomcat Central esté corriendo** (puerto 9128)
2. Compilar e instalar `client-ws`:
   ```bash
   cd client-ws
   mvn clean install
   ```
3. Verificar instalación:
   ```bash
   ls ~/.m2/repository/uy/edu/pa/client-ws/1.0.0/
   # Debe existir: client-ws-1.0.0.jar
   ```

---

### Problema 3: "WSDL not found - java.net.ConnectException"

**Síntoma:**
```
[ERROR] Failed to execute goal com.sun.xml.ws:jaxws-maven-plugin:4.0.0:wsimport
Caused by: java.net.ConnectException: Connection refused: connect
```

**Causa:** Tomcat Central (puerto 9128) NO está corriendo.

**Solución:**
1. Iniciar Tomcat Central en Eclipse
2. Esperar a que se despliegue `central-ws.war` (5-10 segundos)
3. Verificar WSDL en navegador:
   ```
   http://localhost:9128/central-ws/services/AuthService?wsdl
   ```
4. Si muestra XML, reintentar compilar `client-ws`

---

### Problema 4: "The superclass jakarta.servlet.http.HttpServlet was not found"

**Síntoma:** Error de compilación en servlets.

**Causa:** Eclipse no tiene configurado Tomcat como runtime.

**Solución:**
1. Click derecho en proyecto **Laboratorio2PaP**
2. **Properties** → **Project Facets**
3. Verificar:
   - ☑ **Dynamic Web Module** 5.0
   - ☑ **Java** 17
4. Click en **Runtimes** (derecha)
5. Seleccionar: ☑ **Apache Tomcat v10.1**
6. Click **Apply and Close**

---

### Problema 5: "HTTP Status 500 - Error instantiating servlet class"

**Síntoma:** Error 500 al acceder a un servlet.

**Causa:** Servlet usa clases de `client-ws` pero el JAR no está en `WEB-INF/lib`.

**Solución:**
1. Recompilar `Laboratorio2PaP`:
   ```bash
   mvn clean package
   ```
2. Verificar que `client-ws.jar` está en WAR:
   ```bash
   # Extraer WAR y verificar
   unzip -l target/Laboratorio2PaP.war | grep client-ws
   # Debe aparecer: WEB-INF/lib/client-ws-1.0.0.jar
   ```
3. Redesplegar en Tomcat

---

### Problema 6: "EntityManagerFactory error - No Persistence provider"

**Síntoma:**
```
❌ Error al inicializar EntityManagerFactory: No Persistence provider for EntityManager named CentralWS-PU
```

**Causa:** Falta `persistence.xml` o está mal ubicado.

**Solución:**
1. Verificar ubicación:
   ```
   central-ws/src/main/resources/META-INF/persistence.xml
   ```
2. Verificar que esté en WAR:
   ```bash
   unzip -l target/central-ws.war | grep persistence.xml
   # Debe aparecer: WEB-INF/classes/META-INF/persistence.xml
   ```
3. Recompilar:
   ```bash
   mvn clean install
   ```

---

### Problema 7: "MySQL Connection Error"

**Síntoma:**
```
Communications link failure
The last packet sent successfully to the server was 0 milliseconds ago
```

**Causa:** No hay conectividad con Railway MySQL.

**Solución:**
1. Verificar conexión a internet
2. Ping al servidor:
   ```bash
   ping switchback.proxy.rlwy.net
   ```
3. Verificar credenciales en `persistence.xml`:
   ```xml
   <property name="jakarta.persistence.jdbc.url" 
             value="jdbc:mysql://switchback.proxy.rlwy.net:40439/railway?useSSL=false&amp;serverTimezone=UTC"/>
   <property name="jakarta.persistence.jdbc.user" value="root"/>
   <property name="jakarta.persistence.jdbc.password" value="gxgmXGPBKKiKEYMcXlEqcECWjYkkdMKy"/>
   ```
4. Probar conexión con MySQL Workbench o DBeaver

---

### Problema 8: "Clase no encontrada después de cambios"

**Síntoma:** Cambios en código no se reflejan en aplicación.

**Causa:** Eclipse no recompila automáticamente o Tomcat no se reinició.

**Solución:**
1. **Limpiar proyecto:**
   - Click derecho en proyecto → **Clean...**
   - Seleccionar proyecto → **Clean**

2. **Rebuild:**
   - **Project** → **Build Project**

3. **Restart servidor:**
   - Click derecho en servidor Tomcat → **Clean...**
   - Click derecho → **Restart**

4. **Hot Deploy (sin reiniciar):**
   - Guardar archivo Java
   - Eclipse compila automáticamente
   - Tomcat detecta cambio y redespliega (puede tardar 5-10 seg)

---

## 📊 Resumen de URLs

### Servidor Central (Puerto 9128)

| Descripción | URL |
|-------------|-----|
| **AuthService WSDL** | http://localhost:9128/central-ws/services/AuthService?wsdl |
| **ActividadesService WSDL** | http://localhost:9128/central-ws/services/ActividadesService?wsdl |
| **Test SOAP Ping** | Usar herramienta SOAP (SoapUI, Postman) |

### Servidor Web (Puerto 8080)

| Descripción | URL |
|-------------|-----|
| **Home** | http://localhost:8080/Laboratorio2PaP/ |
| **Login** | http://localhost:8080/Laboratorio2PaP/login.jsp |
| **Registro Turista** | http://localhost:8080/Laboratorio2PaP/register-tourist.jsp |
| **Registro Proveedor** | http://localhost:8080/Laboratorio2PaP/register-provider.jsp |
| **Actividades** | http://localhost:8080/Laboratorio2PaP/activities |
| **Mi Perfil** | http://localhost:8080/Laboratorio2PaP/user-profile?nickname=juan123 |
| **Crear Actividad** | http://localhost:8080/Laboratorio2PaP/create-activity.jsp |
| **Crear Salida** | http://localhost:8080/Laboratorio2PaP/create-departure-view |
| **Test Base de Datos** | http://localhost:8080/Laboratorio2PaP/test-db |

---

## ✅ Checklist de Verificación

Antes de comenzar a programar, verifica:

- [ ] Java JDK 17 instalado (`java -version`)
- [ ] Eclipse IDE for Enterprise Java instalado
- [ ] Maven instalado o integrado en Eclipse
- [ ] Tomcat 10.1.47 descargado (2 instancias)
- [ ] Puertos 8080 y 9128 libres
- [ ] Proyectos importados en Eclipse sin errores
- [ ] `central-ws` compilado (`mvn clean install`)
- [ ] Tomcat Central iniciado (puerto 9128)
- [ ] WSDLs accesibles en navegador
- [ ] `client-ws` compilado e instalado
- [ ] `Laboratorio2PaP` compilado
- [ ] Tomcat Web iniciado (puerto 8080)
- [ ] Aplicación accesible en http://localhost:8080/Laboratorio2PaP/

---

## 🎓 Consejos para Desarrollo

### 1. Workflow Recomendado

```
1. Hacer cambios en código
2. Guardar (Ctrl+S)
3. Eclipse compila automáticamente
4. Si es central-ws:
   a. Restart Tomcat Central
   b. Esperar 10 seg
   c. Recompilar client-ws (mvn install)
   d. Recompilar Laboratorio2PaP
   e. Restart Tomcat Web
5. Si es servlet (Laboratorio2PaP):
   a. Restart Tomcat Web (o esperar hot deploy)
6. Refresh navegador (F5)
```

### 2. Atajos de Teclado Eclipse

| Atajo | Acción |
|-------|--------|
| **Ctrl+S** | Guardar |
| **Ctrl+Shift+O** | Organizar imports |
| **Ctrl+Shift+F** | Formatear código |
| **Ctrl+Space** | Autocompletar |
| **F3** | Ir a definición |
| **Ctrl+Shift+R** | Buscar archivo |
| **Ctrl+H** | Buscar en proyecto |

### 3. Debugging

**Iniciar en modo Debug:**
1. Vista **Servers**
2. Click derecho en servidor → **Debug**

**Colocar breakpoints:**
1. Doble click en margen izquierdo del editor (aparece punto azul ●)
2. Ejecutar acción que llame a ese código
3. Eclipse pausa ejecución
4. Ver variables en vista **Variables**
5. **F6** → Siguiente línea
6. **F8** → Continuar ejecución

---

**¡Listo para programar!** 🚀

Si tienes problemas, revisa la sección de Troubleshooting o consulta los logs de Tomcat en la vista Console de Eclipse.
