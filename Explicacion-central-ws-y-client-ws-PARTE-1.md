# Explicación central-ws y client-ws - PARTE 1: Central-WS

## 📋 Índice
- [¿Qué es central-ws?](#qué-es-central-ws)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Tecnologías Utilizadas](#tecnologías-utilizadas)
- [Componentes Principales](#componentes-principales)
- [Servicios SOAP Expuestos](#servicios-soap-expuestos)
- [Cómo Funciona Internamente](#cómo-funciona-internamente)

---

## 🎯 ¿Qué es central-ws?

**central-ws** es el **Servidor Central de Web Services SOAP** del proyecto. Es una aplicación Java que:

1. **Expone servicios SOAP** para que otras aplicaciones los consuman
2. **Maneja toda la lógica de negocio** (usuarios, actividades, inscripciones)
3. **Se conecta a la base de datos MySQL** usando JPA
4. **Corre en Tomcat en el puerto 9128**

### Analogía Simple

Piensa en `central-ws` como un **banco**:
- El banco tiene **cajas** (servicios SOAP)
- Las cajas ofrecen **operaciones** (login, registrar usuario, crear actividad)
- Los clientes (Servidor Web) hacen **peticiones** a las cajas
- Las cajas procesan las peticiones y **retornan resultados**

```
┌─────────────────────────────────────────────────┐
│           SERVIDOR CENTRAL (central-ws)         │
│              Puerto 9128                        │
│                                                 │
│  ┌───────────────────────────────────────┐    │
│  │  AuthService                          │    │
│  │  - Ping()                             │◄───┼── Cliente hace petición SOAP
│  │  - Login(user, pass)                  │    │
│  │  - RegistrarTurista(...)              │────┼──► Respuesta SOAP
│  │  - RegistrarProveedor(...)            │    │
│  └───────────────────────────────────────┘    │
│                                                 │
│  ┌───────────────────────────────────────┐    │
│  │  ActividadesService                   │    │
│  │  - ListarActividades()                │    │
│  │  - CrearActividad(...)                │    │
│  │  - InscribirTurista(...)              │    │
│  │  - ... (7 operaciones más)            │    │
│  └───────────────────────────────────────┘    │
│                                                 │
│              ▼ JPA/JDBC ▼                       │
│         [Base de Datos MySQL]                  │
└─────────────────────────────────────────────────┘
```

---

## 📁 Estructura del Proyecto

### Ubicación en el Workspace
```
/workspaces/Parte_3_proyecto_aplicacion/
└── Laboratorio3Pap-main/
    └── central-ws/                     ← Proyecto Maven
        ├── pom.xml                     ← Configuración Maven
        ├── src/
        │   └── main/
        │       ├── java/
        │       │   └── uy/edu/pa/central/
        │       │       ├── persistence/
        │       │       │   └── JpaUtil.java
        │       │       └── ws/
        │       │           ├── AuthService.java
        │       │           ├── AuthServiceImpl.java
        │       │           ├── ActividadesService.java
        │       │           ├── ActividadesServiceImpl.java
        │       │           └── dtos/
        │       │               ├── UserDTO.java
        │       │               ├── ActividadDTO.java
        │       │               ├── SalidaDTO.java
        │       │               └── InscripcionDTO.java
        │       ├── resources/
        │       │   └── META-INF/
        │       │       └── persistence.xml
        │       └── webapp/
        │           └── WEB-INF/
        │               ├── web.xml
        │               └── sun-jaxws.xml
        └── target/
            └── central-ws.war          ← WAR generado para desplegar
```

### Árbol de Paquetes Java

```
uy.edu.pa.central
│
├── persistence/               # Capa de Persistencia
│   └── JpaUtil.java          # Gestor de EntityManager
│
└── ws/                       # Capa de Web Services
    ├── AuthService.java           # Interface SOAP de autenticación
    ├── AuthServiceImpl.java       # Implementación de autenticación
    ├── ActividadesService.java    # Interface SOAP de actividades
    ├── ActividadesServiceImpl.java # Implementación de actividades
    └── dtos/                      # Data Transfer Objects
        ├── UserDTO.java
        ├── ActividadDTO.java
        ├── SalidaDTO.java
        └── InscripcionDTO.java
```

---

## 🔧 Tecnologías Utilizadas

### 1. Java 17
**¿Qué es?** Versión del lenguaje Java  
**¿Para qué se usa?** Lenguaje de programación base del proyecto

**Ejemplo:**
```java
public class MiClase {
    public String metodo(String parametro) {
        return "Resultado: " + parametro;
    }
}
```

---

### 2. Jakarta EE 9
**¿Qué es?** Plataforma empresarial de Java (antes Java EE)  
**¿Para qué se usa?** Provee APIs estándar para aplicaciones empresariales

**APIs utilizadas:**
- **Jakarta Servlet 5.0** → Para manejar peticiones HTTP
- **Jakarta XML Web Services (JAX-WS) 4.0** → Para crear servicios SOAP
- **Jakarta Persistence (JPA) 3.1** → Para acceso a base de datos

---

### 3. Metro JAX-WS 4.0.2
**¿Qué es?** Implementación de referencia de JAX-WS  
**¿Para qué se usa?** Motor que hace funcionar los servicios SOAP

**Lo que hace Metro:**
1. Lee las anotaciones `@WebService`, `@WebMethod`
2. Genera automáticamente el WSDL (contrato del servicio)
3. Serializa objetos Java a XML
4. Deserializa XML a objetos Java
5. Maneja el protocolo SOAP

**Ejemplo de lo que hace Metro:**

```java
// Tu código:
@WebService
public class AuthServiceImpl {
    @WebMethod
    public String ping() {
        return "pong";
    }
}

// Metro convierte esto automáticamente en un servicio SOAP accesible:
// http://localhost:9128/central-ws/services/AuthService

// Y genera WSDL automáticamente:
// http://localhost:9128/central-ws/services/AuthService?wsdl
```

**Request SOAP (XML que Metro procesa):**
```xml
<soap:Envelope>
  <soap:Body>
    <Ping/>
  </soap:Body>
</soap:Envelope>
```

**Response SOAP (XML que Metro genera):**
```xml
<soap:Envelope>
  <soap:Body>
    <PingResponse>
      <pong>pong</pong>
    </PingResponse>
  </soap:Body>
</soap:Envelope>
```

---

### 4. JPA (Jakarta Persistence API) 3.1
**¿Qué es?** API estándar para mapear objetos Java a base de datos  
**¿Para qué se usa?** CRUD (Create, Read, Update, Delete) en base de datos sin escribir SQL

**Ejemplo:**

```java
// Sin JPA (SQL manual):
String sql = "SELECT * FROM Usuario WHERE nickname = ?";
PreparedStatement stmt = connection.prepareStatement(sql);
stmt.setString(1, "juan123");
ResultSet rs = stmt.executeQuery();
// ... parsear ResultSet manualmente

// Con JPA (simple):
Usuario usuario = entityManager.find(Usuario.class, "juan123");
// ¡Listo! Objeto completo con todos sus datos
```

---

### 5. EclipseLink 4.0.2
**¿Qué es?** Implementación de JPA (proveedor JPA)  
**¿Para qué se usa?** Motor que ejecuta las operaciones JPA

**Lo que hace EclipseLink:**
1. Lee las anotaciones `@Entity`, `@Id`, `@Column`
2. Genera SQL automáticamente
3. Ejecuta queries en la base de datos
4. Convierte filas de BD a objetos Java
5. Maneja transacciones

**Ejemplo:**
```java
// Tu código:
@Entity
@Table(name = "Usuario")
public class Usuario {
    @Id
    private String nickname;
    
    @Column(nullable = false)
    private String nombre;
}

// EclipseLink genera automáticamente SQL:
// SELECT nickname, nombre FROM Usuario WHERE nickname = ?
```

---

### 6. MySQL Connector/J 8.0.33
**¿Qué es?** Driver JDBC para MySQL  
**¿Para qué se usa?** Conectar Java con base de datos MySQL

**Conexión:**
```
Java Application (central-ws)
       ↓ (JDBC)
MySQL Connector/J Driver
       ↓ (TCP/IP)
MySQL Server (Railway Cloud)
```

---

### 7. Maven 3.9+
**¿Qué es?** Herramienta de gestión de proyectos y dependencias  
**¿Para qué se usa?** Compilar, descargar librerías, empaquetar WAR

**Comandos principales:**
```bash
mvn clean           # Limpia target/
mvn compile         # Compila .java → .class
mvn package         # Crea WAR
mvn install         # Instala en repositorio local
```

**Ejemplo de dependencia en pom.xml:**
```xml
<dependency>
    <groupId>com.sun.xml.ws</groupId>
    <artifactId>jaxws-rt</artifactId>
    <version>4.0.2</version>
</dependency>
```

Maven descarga automáticamente `jaxws-rt-4.0.2.jar` y todas sus dependencias.

---

## 🧩 Componentes Principales

### 1. JpaUtil.java - Gestor de EntityManager

**Ubicación:** `src/main/java/uy/edu/pa/central/persistence/JpaUtil.java`

**¿Qué hace?**
- Crea y gestiona `EntityManagerFactory` (pool de conexiones a BD)
- Provee `EntityManager` para cada operación
- Implementa patrón Singleton (una sola instancia)

**Código completo:**
```java
package uy.edu.pa.central.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JpaUtil {
    
    // Variable estática compartida (Singleton)
    private static EntityManagerFactory emf = null;
    
    // Constructor privado (no se puede hacer new JpaUtil())
    private JpaUtil() {}
    
    // Inicializa factory una sola vez
    private static void initFactory() {
        if (emf == null) {
            try {
                // Lee persistence.xml
                emf = Persistence.createEntityManagerFactory("CentralWS-PU");
                System.out.println("✅ EntityManagerFactory inicializado");
            } catch (Exception e) {
                System.err.println("❌ Error: " + e.getMessage());
                throw new ExceptionInInitializerError(e);
            }
        }
    }
    
    // Método público para obtener EntityManager
    public static EntityManager getEntityManager() {
        if (emf == null) {
            initFactory();
        }
        return emf.createEntityManager();
    }
    
    // Cerrar factory al finalizar app
    public static void closeFactory() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
```

**¿Cómo se usa?**
```java
// En cualquier servicio:
EntityManager em = JpaUtil.getEntityManager();
try {
    em.getTransaction().begin();
    Usuario usuario = em.find(Usuario.class, "juan123");
    em.getTransaction().commit();
} finally {
    em.close(); // IMPORTANTE: siempre cerrar
}
```

**Diagrama de flujo:**
```
Primera vez:
  JpaUtil.getEntityManager()
    → emf es null
    → initFactory()
      → Lee persistence.xml
      → Crea EntityManagerFactory
      → Conecta pool a MySQL
    → createEntityManager()
    → Retorna EntityManager

Siguientes veces:
  JpaUtil.getEntityManager()
    → emf ya existe
    → createEntityManager() (rápido)
    → Retorna EntityManager
```

---

### 2. AuthService.java - Interface SOAP de Autenticación

**Ubicación:** `src/main/java/uy/edu/pa/central/ws/AuthService.java`

**¿Qué hace?**
Define el contrato (operaciones) del servicio de autenticación

**Código completo:**
```java
package uy.edu.pa.central.ws;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import uy.edu.pa.central.ws.dtos.UserDTO;

@WebService(name = "AuthService", targetNamespace = "http://ws.central.pa.edu.uy/")
public interface AuthService {

    /**
     * Health check del servicio
     * @return "pong" si está activo
     */
    @WebMethod(operationName = "Ping")
    @WebResult(name = "pong")
    String ping();

    /**
     * Autenticar usuario
     * @param identifier Nickname o email
     * @param password Contraseña
     * @return UserDTO con datos del usuario, o null si falla
     */
    @WebMethod(operationName = "Login")
    @WebResult(name = "user")
    UserDTO login(@WebParam(name = "identifier") String identifier,
                  @WebParam(name = "password") String password) throws Exception;

    /**
     * Registrar nuevo turista
     * @return true si se registró, false si ya existe
     */
    @WebMethod(operationName = "RegistrarTurista")
    @WebResult(name = "exito")
    boolean registrarTurista(
        @WebParam(name = "nickname") String nickname,
        @WebParam(name = "nombre") String nombre,
        @WebParam(name = "apellido") String apellido,
        @WebParam(name = "email") String email,
        @WebParam(name = "password") String password,
        @WebParam(name = "fechaNacimiento") String fechaNacimiento,
        @WebParam(name = "nacionalidad") String nacionalidad
    ) throws Exception;

    /**
     * Registrar nuevo proveedor
     * @return true si se registró, false si ya existe
     */
    @WebMethod(operationName = "RegistrarProveedor")
    @WebResult(name = "exito")
    boolean registrarProveedor(
        @WebParam(name = "nickname") String nickname,
        @WebParam(name = "nombre") String nombre,
        @WebParam(name = "apellido") String apellido,
        @WebParam(name = "email") String email,
        @WebParam(name = "password") String password,
        @WebParam(name = "fechaNacimiento") String fechaNacimiento,
        @WebParam(name = "descripcion") String descripcion,
        @WebParam(name = "link") String link
    ) throws Exception;
}
```

**Explicación de Anotaciones:**

1. **@WebService**
```java
@WebService(name = "AuthService", targetNamespace = "http://ws.central.pa.edu.uy/")
```
- Marca la interface como servicio SOAP
- `name` → Nombre del servicio en WSDL
- `targetNamespace` → Namespace XML único (como un package)

2. **@WebMethod**
```java
@WebMethod(operationName = "Login")
```
- Marca el método como operación SOAP
- `operationName` → Nombre de la operación en WSDL

3. **@WebResult**
```java
@WebResult(name = "user")
```
- Define nombre del elemento de retorno en XML
- Sin esto, sería `<return>`, con esto es `<user>`

4. **@WebParam**
```java
@WebParam(name = "identifier") String identifier
```
- Define nombre del parámetro en XML
- Sin esto, sería `<arg0>`, con esto es `<identifier>`

**WSDL Generado (extracto):**
```xml
<definitions targetNamespace="http://ws.central.pa.edu.uy/"
             name="AuthService">
  
  <portType name="AuthService">
    
    <operation name="Ping">
      <output message="tns:PingResponse"/>
    </operation>
    
    <operation name="Login">
      <input message="tns:Login"/>
      <output message="tns:LoginResponse"/>
    </operation>
    
  </portType>
  
</definitions>
```

---

### 3. AuthServiceImpl.java - Implementación de Autenticación

**Ubicación:** `src/main/java/uy/edu/pa/central/ws/AuthServiceImpl.java`

**¿Qué hace?**
Implementa la lógica real de cada operación definida en `AuthService`

**Estructura:**
```java
@WebService(
    serviceName = "AuthService",
    endpointInterface = "uy.edu.pa.central.ws.AuthService",
    portName = "AuthServicePort",
    targetNamespace = "http://ws.central.pa.edu.uy/"
)
public class AuthServiceImpl implements AuthService {
    
    @Override
    public String ping() {
        // Implementación
    }
    
    @Override
    public UserDTO login(String identifier, String password) throws Exception {
        // Implementación
    }
    
    @Override
    public boolean registrarTurista(...) throws Exception {
        // Implementación
    }
    
    @Override
    public boolean registrarProveedor(...) throws Exception {
        // Implementación
    }
}
```

**Método Login (completo con explicaciones):**
```java
@Override
public UserDTO login(String identifier, String password) throws Exception {
    
    // 1. Validar entrada
    if (identifier == null || identifier.isBlank() || 
        password == null || password.isBlank()) {
        throw new Exception("Credenciales inválidas");
    }

    // 2. Obtener EntityManager
    EntityManager em = JpaUtil.getEntityManager();
    
    try {
        // 3. Buscar usuario por nickname O email
        TypedQuery<Usuario> query = em.createQuery(
            "SELECT u FROM Usuario u WHERE u.nickname = :id OR u.email = :id",
            Usuario.class
        );
        query.setParameter("id", identifier);
        List<Usuario> usuarios = query.getResultList();

        // 4. Verificar si existe
        if (usuarios.isEmpty()) {
            return null; // Usuario no encontrado
        }

        Usuario usuario = usuarios.get(0);

        // 5. Validar contraseña
        if (!usuario.getPassword().equals(password)) {
            return null; // Contraseña incorrecta
        }

        // 6. Construir DTO para retornar
        UserDTO dto = new UserDTO();
        dto.setNickname(usuario.getNickname());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setEmail(usuario.getEmail());

        // 7. Determinar tipo de usuario (polimorfismo)
        if (usuario instanceof Turista) {
            dto.setTipoUsuario("turista");
            dto.setNacionalidad(((Turista) usuario).getNacionalidad());
        } else if (usuario instanceof Proveedor) {
            dto.setTipoUsuario("proveedor");
            dto.setDescripcion(((Proveedor) usuario).getDescripcion());
            dto.setLink(((Proveedor) usuario).getLink());
        }

        // 8. Formatear fecha
        if (usuario.getFechaNac() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            dto.setFechaNacimiento(sdf.format(usuario.getFechaNac()));
        }

        // 9. Retornar DTO
        return dto;
        
    } finally {
        // 10. SIEMPRE cerrar EntityManager
        em.close();
    }
}
```

**Flujo paso a paso:**
```
1. Cliente envía SOAP Request:
   <Login>
     <identifier>juan123</identifier>
     <password>pass123</password>
   </Login>

2. Metro deserializa XML → Llama login("juan123", "pass123")

3. AuthServiceImpl:
   - Valida parámetros ✓
   - Obtiene EntityManager ✓
   - Ejecuta query JPA ✓
   - Valida contraseña ✓
   - Crea UserDTO ✓
   - Cierra EntityManager ✓

4. Metro serializa UserDTO → XML

5. Cliente recibe SOAP Response:
   <LoginResponse>
     <user>
       <nickname>juan123</nickname>
       <nombre>Juan</nombre>
       <email>juan@example.com</email>
       <tipoUsuario>turista</tipoUsuario>
     </user>
   </LoginResponse>
```

---

### 4. DTOs (Data Transfer Objects)

**Ubicación:** `src/main/java/uy/edu/pa/central/ws/dtos/`

**¿Qué son los DTOs?**
Clases Java simples (POJO) que solo contienen datos, sin lógica de negocio.

**¿Por qué DTOs?**
- ✅ Las entidades JPA no se pueden serializar directamente a SOAP
- ✅ Controlar exactamente qué datos se envían al cliente
- ✅ Evitar exponer toda la estructura de la base de datos
- ✅ Independientes de la capa de persistencia

**Entidad JPA vs DTO:**

```java
// ❌ Entidad JPA (NO enviar por SOAP)
@Entity
@Table(name = "Usuario")
public class Usuario {
    @Id
    private String nickname;
    
    @Column
    private String password; // ¡Nunca enviar password!
    
    @OneToMany(mappedBy = "usuario")
    private List<Actividad> actividades; // ¡Lazy loading problem!
    
    // ... muchos más campos y relaciones
}

// ✅ DTO (SÍ enviar por SOAP)
public class UserDTO {
    private String nickname;
    private String nombre;
    private String email;
    // Solo campos necesarios, sin password ni relaciones
}
```

**UserDTO.java completo:**
```java
package uy.edu.pa.central.ws.dtos;

public class UserDTO {
    
    // Campos comunes
    private String nickname;
    private String nombre;
    private String apellido;
    private String email;
    private String fechaNacimiento; // String, no Date (fácil serialización)
    private String tipoUsuario;     // "turista" o "proveedor"
    
    // Campos específicos de Turista
    private String nacionalidad;
    
    // Campos específicos de Proveedor
    private String descripcion;
    private String link;

    // Constructor vacío (requerido por JAXB)
    public UserDTO() {}

    // Getters y Setters (todos públicos)
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
```

**Características de los DTOs:**

1. **Constructor vacío obligatorio**
```java
public UserDTO() {}
```
JAXB (serialización XML) necesita poder crear instancias sin parámetros.

2. **Todos los campos String** (cuando sea posible)
```java
private String fechaNacimiento; // String, no Date
```
Simplifica la serialización/deserialización. Formato: "yyyy-MM-dd"

3. **Getters y Setters públicos**
```java
public String getNombre() { return nombre; }
public void setNombre(String nombre) { this.nombre = nombre; }
```
JAXB usa reflection para acceder a los campos vía getters/setters.

4. **Sin lógica de negocio**
```java
// ❌ NO hacer esto en DTO:
public boolean esValido() {
    return nombre != null && !nombre.isEmpty();
}

// ✅ DTOs son solo contenedores de datos
```

---

## 🌐 Servicios SOAP Expuestos

### AuthService

**URL del Servicio:** `http://localhost:9128/central-ws/services/AuthService`  
**URL del WSDL:** `http://localhost:9128/central-ws/services/AuthService?wsdl`

**Operaciones:**

| Operación | Parámetros | Retorno | Descripción |
|-----------|-----------|---------|-------------|
| `Ping` | Ninguno | String ("pong") | Health check |
| `Login` | identifier, password | UserDTO o null | Autenticar usuario |
| `RegistrarTurista` | 7 campos | boolean | Registrar turista |
| `RegistrarProveedor` | 8 campos | boolean | Registrar proveedor |

**Ejemplo de uso desde cliente:**
```java
// 1. Crear factory
AuthService_Service service = new AuthService_Service();

// 2. Obtener puerto (proxy)
AuthService port = service.getAuthServicePort();

// 3. Invocar operación
UserDTO user = port.login("juan123", "password123");

if (user != null) {
    System.out.println("Bienvenido " + user.getNombre());
} else {
    System.out.println("Credenciales inválidas");
}
```

---

### ActividadesService

**URL del Servicio:** `http://localhost:9128/central-ws/services/ActividadesService`  
**URL del WSDL:** `http://localhost:9128/central-ws/services/ActividadesService?wsdl`

**Operaciones:**

| Operación | Parámetros | Retorno | Descripción |
|-----------|-----------|---------|-------------|
| `ListarActividades` | - | List\<ActividadDTO\> | Todas las actividades |
| `ObtenerActividad` | id | ActividadDTO | Una actividad específica |
| `ObtenerSalida` | id | SalidaDTO | Una salida específica |
| `InscribirTurista` | 6 campos | boolean | Inscribir a salida |
| `ListarInscripcionesPorTurista` | nickname | List\<InscripcionDTO\> | Inscripciones de turista |
| `ListarUsuarios` | - | List\<UserDTO\> | Todos los usuarios |
| `ObtenerUsuario` | nickname | UserDTO | Un usuario específico |
| `CrearActividad` | 7 campos | boolean | Crear actividad |
| `CrearSalida` | 7 campos | boolean | Crear salida |
| `ActualizarUsuario` | 7 campos | boolean | Actualizar perfil |

**Ejemplo de uso:**
```java
// 1. Crear factory
ActividadesService_Service service = new ActividadesService_Service();

// 2. Obtener puerto
ActividadesService port = service.getActividadesServicePort();

// 3. Listar actividades
List<ActividadDTO> actividades = port.listarActividades();

for (ActividadDTO act : actividades) {
    System.out.println(act.getNombre() + " - $" + act.getCosto());
}

// 4. Crear nueva actividad
boolean exito = port.crearActividad(
    "Rafting en el río",           // nombre
    "Aventura extrema",            // descripcion
    3,                             // duracion (horas)
    50.0f,                         // costo
    "Salto",                       // ciudad
    "proveedor123",                // proveedor
    "2025-11-06"                   // fechaAlta
);

if (exito) {
    System.out.println("Actividad creada!");
}
```

---

## ⚙️ Cómo Funciona Internamente

### Ciclo de Vida de una Petición SOAP

```
1. Cliente envía HTTP POST a:
   http://localhost:9128/central-ws/services/AuthService
   
   Body (XML):
   <soap:Envelope>
     <soap:Body>
       <Login>
         <identifier>juan123</identifier>
         <password>pass123</password>
       </Login>
     </soap:Body>
   </soap:Envelope>

2. Tomcat recibe petición HTTP
   → Delega a WSServlet (configurado en web.xml)

3. WSServlet (Metro)
   → Lee sun-jaxws.xml
   → Encuentra endpoint: AuthServiceImpl
   → Deserializa XML a objetos Java
   → Invoca: authServiceImpl.login("juan123", "pass123")

4. AuthServiceImpl
   → Obtiene EntityManager (JpaUtil)
   → Ejecuta query JPA
   → EclipseLink traduce a SQL:
      SELECT * FROM Usuario WHERE nickname = 'juan123' OR email = 'juan123'
   → MySQL Connector ejecuta query
   → EclipseLink convierte ResultSet a Usuario
   → Valida password
   → Construye UserDTO
   → Retorna UserDTO

5. Metro (WSServlet)
   → Serializa UserDTO a XML
   → Construye SOAP Response

6. Tomcat envía HTTP Response
   
   Body (XML):
   <soap:Envelope>
     <soap:Body>
       <LoginResponse>
         <user>
           <nickname>juan123</nickname>
           <nombre>Juan</nombre>
         </user>
       </LoginResponse>
     </soap:Body>
   </soap:Envelope>

7. Cliente recibe response
   → Metro (cliente) deserializa XML
   → Retorna UserDTO al código Java
```

---

### Arquitectura de Capas en central-ws

```
┌─────────────────────────────────────────────────────┐
│              CAPA DE PRESENTACIÓN                    │
│                   (SOAP/XML)                         │
│  - Metro JAX-WS Runtime                             │
│  - WSServlet                                         │
│  - Serialización/Deserialización XML                │
└────────────────────┬────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────┐
│              CAPA DE SERVICIOS                       │
│                (Web Services)                        │
│  - AuthServiceImpl                                   │
│  - ActividadesServiceImpl                           │
│  - Lógica de negocio                                │
│  - Validaciones                                      │
│  - Construcción de DTOs                             │
└────────────────────┬────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────┐
│              CAPA DE PERSISTENCIA                    │
│                    (JPA)                             │
│  - JpaUtil                                           │
│  - EntityManager                                     │
│  - EclipseLink (JPA Provider)                       │
└────────────────────┬────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────┐
│              CAPA DE DATOS                           │
│                   (JDBC)                             │
│  - MySQL Connector/J                                 │
│  - Conexiones TCP/IP                                │
└────────────────────┬────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────┐
│               BASE DE DATOS                          │
│                Railway MySQL                         │
│  - Tablas (Usuario, Actividad, Salida, etc.)       │
└─────────────────────────────────────────────────────┘
```

---

**Continúa en:** [Explicacion-central-ws-y-client-ws-PARTE-2.md](./Explicacion-central-ws-y-client-ws-PARTE-2.md)
