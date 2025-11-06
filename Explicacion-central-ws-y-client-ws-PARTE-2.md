# Explicación central-ws y client-ws - PARTE 2: Client-WS

## 📋 Índice
- [¿Qué es client-ws?](#qué-es-client-ws)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Proceso de Generación de Stubs](#proceso-de-generación-de-stubs)
- [Clases Generadas Explicadas](#clases-generadas-explicadas)
- [Cómo Usar los Stubs](#cómo-usar-los-stubs)
- [Configuración y Archivos](#configuración-y-archivos)

---

## 🎯 ¿Qué es client-ws?

**client-ws** es un **proyecto Maven que genera clases Java (stubs)** para invocar servicios SOAP remotos.

### Analogía Simple

Imagina que `central-ws` es un **restaurante** y `client-ws` es el **menú traducido**:

- El restaurante (central-ws) tiene platos (operaciones SOAP)
- El menú original está en **XML** (WSDL)
- client-ws **traduce el menú a Java** (stubs)
- Con el menú traducido, puedes **pedir fácilmente** (invocar operaciones)

```
SIN client-ws (imposible):
┌──────────────────────────────────────────────┐
│ String xml = "<soap:Envelope>...</soap>";   │
│ HttpURLConnection conn = ...                │
│ conn.getOutputStream().write(xml.getBytes());│
│ // Parsear respuesta XML manualmente... 😱   │
└──────────────────────────────────────────────┘

CON client-ws (fácil):
┌──────────────────────────────────────────────┐
│ AuthService port = service.getAuthServicePort();│
│ UserDTO user = port.login("juan", "pass");  │
│ // ¡Listo! 😊                                │
└──────────────────────────────────────────────┘
```

---

## 📁 Estructura del Proyecto

### Ubicación en el Workspace
```
/workspaces/Parte_3_proyecto_aplicacion/
└── Laboratorio3Pap-main/
    └── client-ws/                      ← Proyecto Maven
        ├── pom.xml                     ← Configuración Maven + wsimport
        ├── src/
        │   └── main/
        │       └── java/
        │           └── (vacío)         ← No hay código fuente manual
        └── target/
            ├── client-ws-1.0.0.jar     ← JAR generado para distribuir
            ├── classes/                ← .class compilados
            └── generated-sources/       ← Código Java generado
                └── wsimport/
                    └── uy/edu/pa/client/ws/
                        ├── auth/                    ← Stubs de AuthService
                        │   ├── AuthService.java    ← Interface del servicio
                        │   ├── AuthService_Service.java  ← Factory
                        │   ├── Login.java          ← Request wrapper
                        │   ├── LoginResponse.java  ← Response wrapper
                        │   ├── UserDTO.java        ← DTO copiado
                        │   ├── Ping.java
                        │   ├── PingResponse.java
                        │   ├── RegistrarTurista.java
                        │   ├── RegistrarTuristaResponse.java
                        │   ├── RegistrarProveedor.java
                        │   ├── RegistrarProveedorResponse.java
                        │   ├── ObjectFactory.java  ← Helper JAXB
                        │   └── package-info.java   ← Metadata
                        └── actividades/             ← Stubs de ActividadesService
                            ├── ActividadesService.java
                            ├── ActividadesService_Service.java
                            ├── ListarActividades.java
                            ├── ListarActividadesResponse.java
                            ├── ObtenerActividad.java
                            ├── ObtenerActividadResponse.java
                            ├── CrearActividad.java
                            ├── CrearActividadResponse.java
                            ├── ActividadDTO.java
                            ├── SalidaDTO.java
                            ├── InscripcionDTO.java
                            ├── UserDTO.java
                            ├── ObjectFactory.java
                            ├── package-info.java
                            └── ... (28 clases más)
```

### Total de Archivos Generados

**AuthService stubs:** 13 clases  
**ActividadesService stubs:** 28 clases  
**Total:** 41 clases Java generadas automáticamente

---

## 🔨 Proceso de Generación de Stubs

### ¿Qué es wsimport?

**wsimport** es una herramienta que:
1. Lee un WSDL (XML que describe el servicio SOAP)
2. Genera clases Java para invocar ese servicio
3. Maneja toda la serialización/deserialización XML

### Diagrama del Proceso

```
┌─────────────────────────────────────────────────────────┐
│ PASO 1: Servidor Central publica WSDL                   │
│                                                          │
│ http://localhost:9128/central-ws/services/              │
│   AuthService?wsdl                                      │
│                                                          │
│ WSDL (XML):                                             │
│ <definitions name="AuthService">                        │
│   <portType name="AuthService">                         │
│     <operation name="Login">                            │
│       <input message="LoginRequest"/>                   │
│       <output message="LoginResponse"/>                 │
│     </operation>                                        │
│   </portType>                                           │
│ </definitions>                                          │
└────────────────────┬────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────┐
│ PASO 2: Maven ejecuta wsimport plugin                   │
│                                                          │
│ Comando: mvn clean compile                              │
│                                                          │
│ Plugin lee pom.xml:                                     │
│ <wsdlUrl>http://localhost:9128/.../AuthService?wsdl</wsdlUrl>│
│ <packageName>uy.edu.pa.client.ws.auth</packageName>    │
└────────────────────┬────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────┐
│ PASO 3: wsimport descarga WSDL y genera clases Java     │
│                                                          │
│ [INFO] jaxws:wsimport {execution: wsimport-auth}        │
│ [INFO] Processing: http://localhost:9128/.../AuthService?wsdl│
│ parsing WSDL...                                         │
│ Generating code...                                      │
│                                                          │
│ Clases generadas:                                       │
│ - AuthService.java          (interface)                 │
│ - AuthService_Service.java  (factory)                   │
│ - Login.java                (request wrapper)           │
│ - LoginResponse.java        (response wrapper)          │
│ - UserDTO.java              (DTO)                       │
│ - ...                                                    │
└────────────────────┬────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────┐
│ PASO 4: Maven compila las clases generadas              │
│                                                          │
│ javac uy/edu/pa/client/ws/auth/*.java                  │
│                                                          │
│ Resultado: .class en target/classes/                    │
└────────────────────┬────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────┐
│ PASO 5: Maven empaqueta en JAR                          │
│                                                          │
│ Comando: mvn package                                    │
│                                                          │
│ Resultado: target/client-ws-1.0.0.jar                   │
│                                                          │
│ Contenido del JAR:                                      │
│ uy/edu/pa/client/ws/auth/*.class                       │
│ uy/edu/pa/client/ws/actividades/*.class               │
└────────────────────┬────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────┐
│ PASO 6: Instalar JAR en repositorio local Maven         │
│                                                          │
│ Comando: mvn install                                    │
│                                                          │
│ Copia JAR a:                                            │
│ ~/.m2/repository/uy/edu/pa/client-ws/1.0.0/            │
│   client-ws-1.0.0.jar                                   │
│                                                          │
│ Ahora otros proyectos Maven pueden usar este JAR!      │
└─────────────────────────────────────────────────────────┘
```

### Comandos Ejecutados

```bash
# 1. Ir al proyecto client-ws
cd Laboratorio3Pap-main/client-ws

# 2. Limpiar compilaciones anteriores
mvn clean

# 3. Generar stubs y compilar
mvn compile

# Salida esperada:
# [INFO] --- jaxws-maven-plugin:4.0.0:wsimport (wsimport-auth) @ client-ws ---
# [INFO] Processing: http://localhost:9128/central-ws/services/AuthService?wsdl
# parsing WSDL...
# Generating code...
# [INFO] 
# [INFO] --- jaxws-maven-plugin:4.0.0:wsimport (wsimport-actividades) @ client-ws ---
# [INFO] Processing: http://localhost:9128/central-ws/services/ActividadesService?wsdl
# parsing WSDL...
# Generating code...
# [INFO] BUILD SUCCESS

# 4. Empaquetar como JAR
mvn package

# Resultado: target/client-ws-1.0.0.jar

# 5. Instalar en repositorio Maven local
mvn install

# Copia JAR a ~/.m2/repository/uy/edu/pa/client-ws/1.0.0/
```

---

## 📝 Configuración Maven (pom.xml)

**Ubicación:** `Laboratorio3Pap-main/client-ws/pom.xml`

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
                             http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>uy.edu.pa</groupId>
    <artifactId>client-ws</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>

    <name>Client Web Services</name>
    <description>Cliente SOAP generado con wsimport</description>

    <properties>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencies>
        <!-- Metro JAX-WS Runtime (necesario para que funcionen los stubs) -->
        <dependency>
            <groupId>com.sun.xml.ws</groupId>
            <artifactId>jaxws-rt</artifactId>
            <version>4.0.2</version>
        </dependency>

        <!-- JAX-WS API -->
        <dependency>
            <groupId>jakarta.xml.ws</groupId>
            <artifactId>jakarta.xml.ws-api</artifactId>
            <version>4.0.0</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <!-- Plugin JAX-WS para generar stubs -->
            <plugin>
                <groupId>com.sun.xml.ws</groupId>
                <artifactId>jaxws-maven-plugin</artifactId>
                <version>4.0.0</version>
                <executions>
                    
                    <!-- EJECUCIÓN 1: Generar stubs de AuthService -->
                    <execution>
                        <id>wsimport-auth</id>
                        <goals>
                            <goal>wsimport</goal>
                        </goals>
                        <configuration>
                            <!-- URL del WSDL -->
                            <wsdlUrls>
                                <wsdlUrl>http://localhost:9128/central-ws/services/AuthService?wsdl</wsdlUrl>
                            </wsdlUrls>
                            
                            <!-- Package donde generar clases -->
                            <packageName>uy.edu.pa.client.ws.auth</packageName>
                            
                            <!-- Guardar código fuente generado -->
                            <keep>true</keep>
                            
                            <!-- Directorio de salida -->
                            <sourceDestDir>${project.build.directory}/generated-sources/wsimport</sourceDestDir>
                        </configuration>
                    </execution>
                    
                    <!-- EJECUCIÓN 2: Generar stubs de ActividadesService -->
                    <execution>
                        <id>wsimport-actividades</id>
                        <goals>
                            <goal>wsimport</goal>
                        </goals>
                        <configuration>
                            <wsdlUrls>
                                <wsdlUrl>http://localhost:9128/central-ws/services/ActividadesService?wsdl</wsdlUrl>
                            </wsdlUrls>
                            <packageName>uy.edu.pa.client.ws.actividades</packageName>
                            <keep>true</keep>
                            <sourceDestDir>${project.build.directory}/generated-sources/wsimport</sourceDestDir>
                        </configuration>
                    </execution>
                    
                </executions>
            </plugin>
        </plugins>
    </build>
</project>
```

### Explicación de Configuración

**1. `<wsdlUrls>`**
```xml
<wsdlUrl>http://localhost:9128/central-ws/services/AuthService?wsdl</wsdlUrl>
```
- URL desde donde descargar el WSDL
- **IMPORTANTE:** El servidor central-ws DEBE estar corriendo

**2. `<packageName>`**
```xml
<packageName>uy.edu.pa.client.ws.auth</packageName>
```
- Package Java donde generar las clases
- Organiza los stubs por servicio

**3. `<keep>`**
```xml
<keep>true</keep>
```
- `true` → Guardar archivos .java generados
- `false` → Solo generar .class (no recomendado)

**4. `<sourceDestDir>`**
```xml
<sourceDestDir>${project.build.directory}/generated-sources/wsimport</sourceDestDir>
```
- Directorio donde guardar código generado
- `${project.build.directory}` = `target/`

---

## 📚 Clases Generadas Explicadas

### Tipos de Clases Generadas

Para cada servicio SOAP, wsimport genera:

1. **Interface del servicio** (AuthService.java)
2. **Factory del servicio** (AuthService_Service.java)
3. **Request wrappers** (Login.java, RegistrarTurista.java, etc.)
4. **Response wrappers** (LoginResponse.java, etc.)
5. **DTOs** (UserDTO.java, ActividadDTO.java, etc.)
6. **Helpers** (ObjectFactory.java, package-info.java)

---

### 1. Interface del Servicio (AuthService.java)

**Ubicación:** `target/generated-sources/wsimport/uy/edu/pa/client/ws/auth/AuthService.java`

**¿Qué es?**
Interface Java que define las operaciones del servicio.

**Código generado (extracto):**
```java
package uy.edu.pa.client.ws.auth;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.ws.Action;
import jakarta.xml.ws.RequestWrapper;
import jakarta.xml.ws.ResponseWrapper;

@WebService(name = "AuthService", targetNamespace = "http://ws.central.pa.edu.uy/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface AuthService {

    /**
     * Operación Ping
     * @return String "pong"
     */
    @WebMethod(operationName = "Ping")
    @WebResult(name = "pong", targetNamespace = "")
    @RequestWrapper(localName = "Ping", 
                    targetNamespace = "http://ws.central.pa.edu.uy/", 
                    className = "uy.edu.pa.client.ws.auth.Ping")
    @ResponseWrapper(localName = "PingResponse", 
                     targetNamespace = "http://ws.central.pa.edu.uy/", 
                     className = "uy.edu.pa.client.ws.auth.PingResponse")
    @Action(input = "http://ws.central.pa.edu.uy/AuthService/PingRequest", 
            output = "http://ws.central.pa.edu.uy/AuthService/PingResponse")
    public String ping();

    /**
     * Operación Login
     */
    @WebMethod(operationName = "Login")
    @WebResult(name = "user", targetNamespace = "")
    @RequestWrapper(localName = "Login", 
                    targetNamespace = "http://ws.central.pa.edu.uy/", 
                    className = "uy.edu.pa.client.ws.auth.Login")
    @ResponseWrapper(localName = "LoginResponse", 
                     targetNamespace = "http://ws.central.pa.edu.uy/", 
                     className = "uy.edu.pa.client.ws.auth.LoginResponse")
    public UserDTO login(
        @WebParam(name = "identifier", targetNamespace = "") String identifier,
        @WebParam(name = "password", targetNamespace = "") String password
    ) throws Exception_Exception;

    // ... más operaciones
}
```

**¿Para qué sirve?**
- Define el contrato del servicio
- Se usa para crear el proxy (port)
- No se implementa directamente (eso lo hace Metro internamente)

---

### 2. Factory del Servicio (AuthService_Service.java)

**Ubicación:** `target/generated-sources/wsimport/uy/edu/pa/client/ws/auth/AuthService_Service.java`

**¿Qué es?**
Clase que crea instancias del servicio (proxy SOAP).

**Código generado (simplificado):**
```java
package uy.edu.pa.client.ws.auth;

import java.net.MalformedURLException;
import java.net.URL;
import jakarta.xml.ws.Service;
import jakarta.xml.ws.WebEndpoint;
import jakarta.xml.ws.WebServiceClient;
import jakarta.xml.ws.WebServiceFeature;
import javax.xml.namespace.QName;

@WebServiceClient(name = "AuthService", 
                  targetNamespace = "http://ws.central.pa.edu.uy/", 
                  wsdlLocation = "http://localhost:9128/central-ws/services/AuthService?wsdl")
public class AuthService_Service extends Service {

    // URL del WSDL
    private final static URL AUTHSERVICE_WSDL_LOCATION;
    
    // QName del servicio
    private final static QName AUTHSERVICE_QNAME = 
        new QName("http://ws.central.pa.edu.uy/", "AuthService");

    static {
        URL url = null;
        try {
            url = new URL("http://localhost:9128/central-ws/services/AuthService?wsdl");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        AUTHSERVICE_WSDL_LOCATION = url;
    }

    /**
     * Constructor por defecto
     */
    public AuthService_Service() {
        super(AUTHSERVICE_WSDL_LOCATION, AUTHSERVICE_QNAME);
    }

    /**
     * Constructor con URL personalizada
     */
    public AuthService_Service(URL wsdlLocation) {
        super(wsdlLocation, AUTHSERVICE_QNAME);
    }

    /**
     * Obtiene el puerto del servicio (proxy SOAP)
     * @return AuthService proxy
     */
    @WebEndpoint(name = "AuthServicePort")
    public AuthService getAuthServicePort() {
        return super.getPort(
            new QName("http://ws.central.pa.edu.uy/", "AuthServicePort"), 
            AuthService.class
        );
    }

    /**
     * Obtiene el puerto con features personalizados
     */
    @WebEndpoint(name = "AuthServicePort")
    public AuthService getAuthServicePort(WebServiceFeature... features) {
        return super.getPort(
            new QName("http://ws.central.pa.edu.uy/", "AuthServicePort"), 
            AuthService.class, 
            features
        );
    }
}
```

**¿Cómo se usa?**
```java
// 1. Crear factory (lee WSDL, configura endpoint)
AuthService_Service service = new AuthService_Service();

// 2. Obtener port (proxy que hace llamadas SOAP)
AuthService port = service.getAuthServicePort();

// 3. Invocar operaciones (como si fueran métodos locales)
String pong = port.ping();
UserDTO user = port.login("juan123", "pass123");
```

---

### 3. Request Wrappers (Login.java)

**Ubicación:** `target/generated-sources/wsimport/uy/edu/pa/client/ws/auth/Login.java`

**¿Qué es?**
Clase que encapsula los parámetros de la petición SOAP.

**Código generado:**
```java
package uy.edu.pa.client.ws.auth;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;

/**
 * Clase Java para Login complex type.
 * 
 * Mapeo XML:
 * <Login>
 *   <identifier>valor</identifier>
 *   <password>valor</password>
 * </Login>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Login", propOrder = {
    "identifier",
    "password"
})
public class Login {

    protected String identifier;
    protected String password;

    /**
     * Obtiene identifier
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Establece identifier
     */
    public void setIdentifier(String value) {
        this.identifier = value;
    }

    /**
     * Obtiene password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Establece password
     */
    public void setPassword(String value) {
        this.password = value;
    }
}
```

**¿Para qué sirve?**
- Metro usa esta clase para serializar parámetros a XML
- Cuando llamas `port.login("juan", "pass")`, Metro:
  1. Crea objeto `Login`
  2. Setea `identifier = "juan"`, `password = "pass"`
  3. Serializa a XML:
     ```xml
     <Login>
       <identifier>juan</identifier>
       <password>pass</password>
     </Login>
     ```

---

### 4. Response Wrappers (LoginResponse.java)

**Ubicación:** `target/generated-sources/wsimport/uy/edu/pa/client/ws/auth/LoginResponse.java`

**¿Qué es?**
Clase que encapsula el resultado de la respuesta SOAP.

**Código generado:**
```java
package uy.edu.pa.client.ws.auth;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;

/**
 * Clase Java para LoginResponse complex type.
 * 
 * Mapeo XML:
 * <LoginResponse>
 *   <user>...</user>
 * </LoginResponse>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LoginResponse", propOrder = {
    "user"
})
public class LoginResponse {

    protected UserDTO user;

    /**
     * Obtiene user
     */
    public UserDTO getUser() {
        return user;
    }

    /**
     * Establece user
     */
    public void setUser(UserDTO value) {
        this.user = value;
    }
}
```

**¿Para qué sirve?**
- Metro usa esta clase para deserializar XML a objetos
- Cuando recibes respuesta SOAP:
  ```xml
  <LoginResponse>
    <user>
      <nickname>juan123</nickname>
      <nombre>Juan</nombre>
    </user>
  </LoginResponse>
  ```
- Metro:
  1. Parsea XML
  2. Crea objeto `LoginResponse`
  3. Crea objeto `UserDTO` con datos
  4. Setea `loginResponse.user = userDTO`
  5. Retorna `userDTO` al caller

---

### 5. DTOs (UserDTO.java)

**Ubicación:** `target/generated-sources/wsimport/uy/edu/pa/client/ws/auth/UserDTO.java`

**¿Qué es?**
Copia del DTO definido en el servidor, generada desde el WSDL.

**Código generado (extracto):**
```java
package uy.edu.pa.client.ws.auth;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "userDTO", propOrder = {
    "apellido",
    "descripcion",
    "email",
    "fechaNacimiento",
    "link",
    "nacionalidad",
    "nickname",
    "nombre",
    "tipoUsuario"
})
public class UserDTO {

    protected String apellido;
    protected String descripcion;
    protected String email;
    protected String fechaNacimiento;
    protected String link;
    protected String nacionalidad;
    protected String nickname;
    protected String nombre;
    protected String tipoUsuario;

    // Getters y Setters...
    
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String value) {
        this.nickname = value;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String value) {
        this.nombre = value;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String value) {
        this.tipoUsuario = value;
    }

    // ... más getters/setters
}
```

**Diferencias con el DTO del servidor:**
- Orden alfabético de campos (por JAXB)
- Anotaciones `@Xml*` agregadas
- Funcionalmente equivalente

---

### 6. Helpers (ObjectFactory.java)

**Ubicación:** `target/generated-sources/wsimport/uy/edu/pa/client/ws/auth/ObjectFactory.java`

**¿Qué es?**
Factory para crear instancias de los objetos XML.

**Código generado (extracto):**
```java
package uy.edu.pa.client.ws.auth;

import jakarta.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class ObjectFactory {

    /**
     * Crea una nueva ObjectFactory
     */
    public ObjectFactory() {
    }

    /**
     * Crea instancia de Login
     */
    public Login createLogin() {
        return new Login();
    }

    /**
     * Crea instancia de LoginResponse
     */
    public LoginResponse createLoginResponse() {
        return new LoginResponse();
    }

    /**
     * Crea instancia de UserDTO
     */
    public UserDTO createUserDTO() {
        return new UserDTO();
    }

    // ... más métodos create
}
```

**¿Para qué sirve?**
- JAXB (serialización XML) usa esta factory para crear objetos
- Normalmente no se usa directamente en código de usuario

---

## 💡 Cómo Usar los Stubs

### Ejemplo Completo: LoginServlet

```java
package servlets;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;

// Imports de stubs generados
import uy.edu.pa.client.ws.auth.AuthService;         // Interface
import uy.edu.pa.client.ws.auth.AuthService_Service; // Factory
import uy.edu.pa.client.ws.auth.UserDTO;             // DTO

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 1. Obtener parámetros del formulario
        String identifier = request.getParameter("identifier");
        String password = request.getParameter("password");

        try {
            // 2. Crear factory del servicio SOAP
            //    - Lee WSDL (http://localhost:9128/.../AuthService?wsdl)
            //    - Configura endpoint
            AuthService_Service service = new AuthService_Service();

            // 3. Obtener puerto (proxy SOAP)
            //    - Crea proxy que implementa interface AuthService
            //    - Maneja serialización/deserialización XML
            AuthService port = service.getAuthServicePort();

            // 4. Invocar operación SOAP
            //    Lo que pasa internamente:
            //    a) Metro crea objeto Login
            //    b) Setea identifier y password
            //    c) Serializa a XML SOAP
            //    d) Envía HTTP POST a servidor
            //    e) Recibe respuesta XML
            //    f) Deserializa a UserDTO
            //    g) Retorna UserDTO
            UserDTO user = port.login(identifier, password);

            // 5. Procesar resultado
            if (user != null) {
                // Login exitoso
                HttpSession session = request.getSession();
                session.setAttribute("usuario", user);
                session.setAttribute("usuarioNickname", user.getNickname());
                session.setAttribute("usuarioTipo", user.getTipoUsuario());
                
                // Redirigir según tipo de usuario
                if ("proveedor".equals(user.getTipoUsuario())) {
                    response.sendRedirect("dashboard-proveedor.jsp");
                } else {
                    response.sendRedirect("dashboard-turista.jsp");
                }
            } else {
                // Login fallido
                request.setAttribute("error", "Credenciales inválidas");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }

        } catch (Exception e) {
            // Error de conexión o servidor
            e.printStackTrace();
            request.setAttribute("error", "Error de conexión: " + e.getMessage());
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
```

### Ejemplo 2: Listar Actividades

```java
package servlets;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;
import java.util.List;

// Imports de stubs
import uy.edu.pa.client.ws.actividades.ActividadesService;
import uy.edu.pa.client.ws.actividades.ActividadesService_Service;
import uy.edu.pa.client.ws.actividades.ActividadDTO;

@WebServlet("/activities")
public class ActivitiesServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            // 1. Crear factory
            ActividadesService_Service service = new ActividadesService_Service();
            
            // 2. Obtener puerto
            ActividadesService port = service.getActividadesServicePort();
            
            // 3. Invocar operación
            List<ActividadDTO> actividades = port.listarActividades();
            
            // 4. Pasar datos a JSP
            request.setAttribute("actividades", actividades);
            request.getRequestDispatcher("/WEB-INF/views/activities.jsp")
                   .forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                             "Error al obtener actividades");
        }
    }
}
```

### Ejemplo 3: Crear Actividad

```java
package servlets;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import uy.edu.pa.client.ws.actividades.ActividadesService;
import uy.edu.pa.client.ws.actividades.ActividadesService_Service;

@WebServlet("/create-activity")
public class CreateActivityServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Obtener parámetros
        String nombre = request.getParameter("nombre");
        String descripcion = request.getParameter("descripcion");
        int duracion = Integer.parseInt(request.getParameter("duracion"));
        float costo = Float.parseFloat(request.getParameter("costo"));
        String ciudad = request.getParameter("ciudad");
        String proveedor = (String) request.getSession().getAttribute("usuarioNickname");
        
        try {
            // Crear cliente SOAP
            ActividadesService_Service service = new ActividadesService_Service();
            ActividadesService port = service.getActividadesServicePort();
            
            // Fecha actual
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String fechaAlta = sdf.format(new Date());
            
            // Invocar operación SOAP con 7 parámetros
            boolean exito = port.crearActividad(
                nombre,
                descripcion,
                duracion,
                costo,
                ciudad,
                proveedor,
                fechaAlta
            );
            
            if (exito) {
                response.sendRedirect("activities?success=1");
            } else {
                request.setAttribute("error", "No se pudo crear la actividad");
                request.getRequestDispatcher("create-activity.jsp")
                       .forward(request, response);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error: " + e.getMessage());
            request.getRequestDispatcher("create-activity.jsp")
                   .forward(request, response);
        }
    }
}
```

---

## 🔄 Ciclo de Vida Completo de una Invocación SOAP

```
┌──────────────────────────────────────────────────────────────┐
│ CLIENTE (Servidor Web - Puerto 8080)                         │
│                                                               │
│ LoginServlet:                                                │
│   1. AuthService_Service service = new AuthService_Service();│
│      → Lee WSDL desde URL hardcodeada                        │
│      → Configura endpoint: localhost:9128                    │
│                                                               │
│   2. AuthService port = service.getAuthServicePort();        │
│      → Crea proxy dinámico (java.lang.reflect.Proxy)        │
│      → Proxy implementa interface AuthService                │
│                                                               │
│   3. UserDTO user = port.login("juan", "pass");              │
│      ↓                                                        │
│      Metro JAX-WS Runtime (Cliente):                         │
│      a) Intercepta llamada (proxy)                           │
│      b) Crea objeto Login:                                   │
│         Login req = new Login();                             │
│         req.setIdentifier("juan");                           │
│         req.setPassword("pass");                             │
│      c) Serializa a XML usando JAXB:                         │
│         <soap:Envelope>                                      │
│           <soap:Body>                                        │
│             <Login>                                          │
│               <identifier>juan</identifier>                  │
│               <password>pass</password>                      │
│             </Login>                                         │
│           </soap:Body>                                       │
│         </soap:Envelope>                                     │
│      d) Envía HTTP POST a:                                   │
│         http://localhost:9128/central-ws/services/AuthService│
│                                                               │
└───────────────────────┬──────────────────────────────────────┘
                        │ HTTP (XML)
                        ▼
┌──────────────────────────────────────────────────────────────┐
│ SERVIDOR (Central WS - Puerto 9128)                          │
│                                                               │
│ Tomcat:                                                      │
│   → Recibe HTTP POST                                         │
│   → Delega a WSServlet                                       │
│                                                               │
│ Metro JAX-WS Runtime (Servidor):                             │
│   1. Lee XML del request body                                │
│   2. Deserializa XML a objeto Login usando JAXB              │
│   3. Extrae parámetros: identifier="juan", password="pass"   │
│   4. Busca endpoint: AuthServiceImpl                         │
│   5. Invoca: authServiceImpl.login("juan", "pass")           │
│                                                               │
│ AuthServiceImpl:                                             │
│   6. Obtiene EntityManager                                   │
│   7. Query JPA: SELECT * FROM Usuario WHERE ...              │
│   8. Valida contraseña                                       │
│   9. Construye UserDTO                                       │
│  10. return userDTO;                                         │
│                                                               │
│ Metro JAX-WS Runtime (Servidor):                             │
│  11. Crea objeto LoginResponse:                              │
│      LoginResponse resp = new LoginResponse();               │
│      resp.setUser(userDTO);                                  │
│  12. Serializa a XML usando JAXB:                            │
│      <soap:Envelope>                                         │
│        <soap:Body>                                           │
│          <LoginResponse>                                     │
│            <user>                                            │
│              <nickname>juan</nickname>                       │
│              <nombre>Juan</nombre>                           │
│              <tipoUsuario>turista</tipoUsuario>              │
│            </user>                                           │
│          </LoginResponse>                                    │
│        </soap:Body>                                          │
│      </soap:Envelope>                                        │
│  13. Envía HTTP 200 OK con XML en body                       │
│                                                               │
└───────────────────────┬──────────────────────────────────────┘
                        │ HTTP (XML)
                        ▼
┌──────────────────────────────────────────────────────────────┐
│ CLIENTE (Servidor Web - Puerto 8080)                         │
│                                                               │
│ Metro JAX-WS Runtime (Cliente):                              │
│   e) Recibe HTTP 200 OK                                      │
│   f) Lee XML del response body                               │
│   g) Deserializa XML a LoginResponse                         │
│   h) Extrae: userDTO = loginResponse.getUser()               │
│   i) Retorna userDTO al caller                               │
│                                                               │
│ LoginServlet:                                                │
│   4. if (user != null) {                                     │
│        session.setAttribute("usuario", user);                │
│        response.sendRedirect("dashboard.jsp");               │
│      }                                                        │
│                                                               │
└──────────────────────────────────────────────────────────────┘
```

---

## 🎯 Resumen

### ¿Qué hace client-ws?

1. **Lee WSDLs** de servicios SOAP remotos
2. **Genera clases Java** (stubs) automáticamente
3. **Empaqueta en JAR** para distribuir
4. **Simplifica invocación** de servicios SOAP

### Ventajas de usar client-ws

✅ **No escribir XML manualmente** - Metro lo hace automáticamente  
✅ **Type-safe** - Compilador verifica tipos  
✅ **Autocomplete** - IDE sugiere métodos y parámetros  
✅ **Documentación** - Javadoc generado desde WSDL  
✅ **Mantenible** - Si cambia WSDL, regenerar stubs  

### Flujo de Trabajo

```
1. Servidor publica WSDL
2. wsimport genera stubs desde WSDL
3. Stubs se empaquetan en JAR
4. JAR se agrega como dependencia
5. Código usa stubs como clases normales
```

---

**FIN DE LA PARTE 2**

📚 **Documentos relacionados:**
- [PARTE 1: Central-WS](./Explicacion-central-ws-y-client-ws-PARTE-1.md)
- [PARTE 2: Client-WS](./Explicacion-central-ws-y-client-ws-PARTE-2.md) ← Estás aquí
