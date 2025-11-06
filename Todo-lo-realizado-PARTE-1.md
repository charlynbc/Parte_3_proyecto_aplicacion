# Todo lo Realizado - PARTE 1: Arquitectura y Servicios SOAP

## 📋 Índice
- [Objetivo General](#objetivo-general)
- [Arquitectura Implementada](#arquitectura-implementada)
- [Servidor Central - Web Services SOAP](#servidor-central---web-services-soap)
- [DTOs y Transferencia de Datos](#dtos-y-transferencia-de-datos)
- [Cliente SOAP - Generación de Stubs](#cliente-soap---generación-de-stubs)

---

## 🎯 Objetivo General

### Requisito de la Letra (Tarea 3)
> **"Manejo puramente de Web Services sin usar .jar"**

La especificación de Tarea 3 requiere que:
- El **Servidor Web** (Laboratorio2PaP) NO debe usar directamente los JARs de lógica
- Toda comunicación debe ser vía **Web Services SOAP**
- Arquitectura distribuida de 3 capas con separación clara

### ¿Por qué esto es importante?
En la versión anterior (Tarea 2), los servlets importaban directamente:
```java
import logica.Fabrica;
import logica.IControladorUsuario;
import logica.IControladorActividad;

// Uso directo del JAR
IControladorUsuario controlador = Fabrica.getInstance().getIControladorUsuario();
List<Usuario> usuarios = controlador.getUsuarios();
```

**Problema:** Esto viola el principio de arquitectura distribuida porque:
- El Servidor Web tiene acceso directo a la lógica de negocio
- No hay separación real de capas
- No se pueden escalar independientemente
- No cumple con la especificación de Tarea 3

---

## 🏗️ Arquitectura Implementada

### Diagrama de la Arquitectura

```
┌─────────────────────────────────────────────────────────────┐
│                    CLIENTE (Navegador)                       │
│                   http://localhost:8080                      │
└────────────────────────────┬────────────────────────────────┘
                             │ HTTP GET/POST
                             │ (Formularios, Enlaces)
                             ▼
┌─────────────────────────────────────────────────────────────┐
│              SERVIDOR WEB - Laboratorio2PaP                  │
│                   Puerto 8080 (Tomcat 10)                    │
│  ┌─────────────────────────────────────────────────────┐   │
│  │              22 Servlets (Capa Web)                  │   │
│  │  - LoginServlet                                      │   │
│  │  - RegisterTouristServlet                            │   │
│  │  - CreateActivityServlet                             │   │
│  │  - UserProfileServlet                                │   │
│  │  - ... (18 más)                                      │   │
│  └────────────────┬────────────────────────────────────┘   │
│                   │ SOAP/XML sobre HTTP                      │
│                   │ (client-ws.jar - Stubs generados)        │
└───────────────────┼──────────────────────────────────────────┘
                    │
                    │ SOAP Request/Response
                    │ (XML con operaciones y datos)
                    ▼
┌─────────────────────────────────────────────────────────────┐
│           SERVIDOR CENTRAL - central-ws                      │
│                 Puerto 9128 (Tomcat 10)                      │
│  ┌─────────────────────────────────────────────────────┐   │
│  │         2 Web Services SOAP (Capa Negocio)           │   │
│  │                                                       │   │
│  │  AuthService (4 operaciones)                         │   │
│  │  ├─ Ping() → "pong"                                  │   │
│  │  ├─ Login(identifier, password) → UserDTO            │   │
│  │  ├─ RegistrarTurista(...) → boolean                  │   │
│  │  └─ RegistrarProveedor(...) → boolean                │   │
│  │                                                       │   │
│  │  ActividadesService (10 operaciones)                 │   │
│  │  ├─ ListarActividades() → List<ActividadDTO>         │   │
│  │  ├─ ObtenerActividad(id) → ActividadDTO              │   │
│  │  ├─ ObtenerSalida(id) → SalidaDTO                    │   │
│  │  ├─ InscribirTurista(...) → boolean                  │   │
│  │  ├─ ListarInscripcionesPorTurista() → List<...>     │   │
│  │  ├─ ListarUsuarios() → List<UserDTO>                 │   │
│  │  ├─ ObtenerUsuario(nickname) → UserDTO               │   │
│  │  ├─ CrearActividad(...) → boolean                    │   │
│  │  ├─ CrearSalida(...) → boolean                       │   │
│  │  └─ ActualizarUsuario(...) → boolean                 │   │
│  └────────────────┬────────────────────────────────────┘   │
│                   │ JPA/JDBC                                 │
└───────────────────┼──────────────────────────────────────────┘
                    │
                    │ SQL Queries
                    ▼
┌─────────────────────────────────────────────────────────────┐
│                  BASE DE DATOS MySQL                         │
│              Railway Cloud (Puerto 40439)                    │
│  switchback.proxy.rlwy.net:40439/railway                    │
│                                                              │
│  Tablas: Usuario, Turista, Proveedor, Actividad,            │
│          Salida, Inscripcion, Departamento, Categoria        │
└─────────────────────────────────────────────────────────────┘
```

### Flujo de Datos Ejemplo: Login de Usuario

```
1. Usuario ingresa credenciales en formulario → POST /login

2. LoginServlet (Puerto 8080)
   └─ Recibe HttpServletRequest
   └─ Extrae: identifier="juan123", password="pass123"
   └─ Crea cliente SOAP:
      AuthService_Service service = new AuthService_Service();
      AuthService port = service.getAuthServicePort();
   └─ Invoca: UserDTO user = port.login("juan123", "pass123");

3. SOAP Request (XML enviado al puerto 9128):
   <soap:Envelope>
     <soap:Body>
       <Login>
         <identifier>juan123</identifier>
         <password>pass123</password>
       </Login>
     </soap:Body>
   </soap:Envelope>

4. AuthServiceImpl (Puerto 9128)
   └─ Recibe petición SOAP
   └─ Busca en BD con JPA:
      EntityManager em = JpaUtil.getEntityManager();
      TypedQuery<Usuario> query = em.createQuery(
        "SELECT u FROM Usuario u WHERE u.nickname = :id OR u.email = :id",
        Usuario.class
      );
   └─ Valida contraseña
   └─ Construye UserDTO
   └─ Retorna respuesta SOAP

5. SOAP Response (XML recibido en puerto 8080):
   <soap:Envelope>
     <soap:Body>
       <LoginResponse>
         <user>
           <nickname>juan123</nickname>
           <nombre>Juan</nombre>
           <apellido>Pérez</apellido>
           <email>juan@example.com</email>
           <tipoUsuario>turista</tipoUsuario>
         </user>
       </LoginResponse>
     </soap:Body>
   </soap:Envelope>

6. LoginServlet procesa respuesta
   └─ Si user != null:
      request.getSession().setAttribute("usuario", user);
      response.sendRedirect("dashboard.jsp");
   └─ Si user == null:
      request.setAttribute("error", "Credenciales inválidas");
      request.getRequestDispatcher("login.jsp").forward(...);
```

---

## 🌐 Servidor Central - Web Services SOAP

### Ubicación del Código
```
Laboratorio3Pap-main/
└── central-ws/
    ├── pom.xml                          # Configuración Maven
    └── src/
        └── main/
            ├── java/
            │   └── uy/edu/pa/central/
            │       ├── persistence/
            │       │   └── JpaUtil.java      # Gestor EntityManager
            │       └── ws/
            │           ├── AuthService.java          # Interface SOAP Auth
            │           ├── AuthServiceImpl.java      # Implementación Auth
            │           ├── ActividadesService.java   # Interface SOAP Actividades
            │           ├── ActividadesServiceImpl.java # Implementación Actividades
            │           └── dtos/
            │               ├── UserDTO.java
            │               ├── ActividadDTO.java
            │               ├── SalidaDTO.java
            │               └── InscripcionDTO.java
            ├── resources/
            │   └── META-INF/
            │       └── persistence.xml       # Config JPA/EclipseLink
            └── webapp/
                └── WEB-INF/
                    ├── web.xml              # Descriptor Servlet
                    └── sun-jaxws.xml        # Config JAX-WS endpoints
```

---

## 📡 AuthService - Servicio de Autenticación

### 1. Interface AuthService.java

**Ubicación:** `Laboratorio3Pap-main/central-ws/src/main/java/uy/edu/pa/central/ws/AuthService.java`

```java
package uy.edu.pa.central.ws;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import uy.edu.pa.central.ws.dtos.UserDTO;

@WebService(name = "AuthService", targetNamespace = "http://ws.central.pa.edu.uy/")
public interface AuthService {

    @WebMethod(operationName = "Ping")
    @WebResult(name = "pong")
    String ping();

    @WebMethod(operationName = "Login")
    @WebResult(name = "user")
    UserDTO login(@WebParam(name = "identifier") String identifier,
                  @WebParam(name = "password") String password) throws Exception;

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

#### Explicación Detallada de Anotaciones:

1. **@WebService**
   - `name = "AuthService"`: Nombre del servicio en el WSDL
   - `targetNamespace = "http://ws.central.pa.edu.uy/"`: Namespace XML único

2. **@WebMethod**
   - Marca un método como operación SOAP expuesta
   - `operationName`: Nombre de la operación en el WSDL

3. **@WebResult**
   - Define el nombre del elemento de retorno en el XML
   - Ejemplo: `<pong>valor</pong>`

4. **@WebParam**
   - Define nombres de parámetros en el mensaje SOAP
   - Ejemplo: `<identifier>juan123</identifier>`

#### Operaciones Implementadas:

##### Operación 1: Ping()
```java
@WebMethod(operationName = "Ping")
@WebResult(name = "pong")
String ping();
```
- **Propósito:** Health check del servicio
- **Entrada:** Ninguna
- **Salida:** String "pong"
- **Uso:** Verificar que el servicio SOAP está activo

##### Operación 2: Login()
```java
@WebMethod(operationName = "Login")
@WebResult(name = "user")
UserDTO login(@WebParam(name = "identifier") String identifier,
              @WebParam(name = "password") String password) throws Exception;
```
- **Propósito:** Autenticar usuario (turista o proveedor)
- **Entrada:** 
  - `identifier`: Nickname o email del usuario
  - `password`: Contraseña en texto plano
- **Salida:** `UserDTO` con datos del usuario o `null` si fallan credenciales
- **Excepción:** `Exception` si hay error de BD o parámetros inválidos

##### Operación 3: RegistrarTurista()
```java
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
```
- **Propósito:** Crear nuevo turista en el sistema
- **Entrada:** 
  - 7 parámetros String (fechaNacimiento formato: "yyyy-MM-dd")
- **Salida:** `true` si registro exitoso, `false` si ya existe
- **Excepción:** `Exception` si hay error de validación o BD

##### Operación 4: RegistrarProveedor()
```java
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
```
- **Propósito:** Crear nuevo proveedor en el sistema
- **Entrada:** 
  - 8 parámetros String (incluye descripcion y link/sitioWeb)
- **Salida:** `true` si registro exitoso, `false` si ya existe
- **Excepción:** `Exception` si hay error de validación o BD

---

### 2. Implementación AuthServiceImpl.java

**Ubicación:** `Laboratorio3Pap-main/central-ws/src/main/java/uy/edu/pa/central/ws/AuthServiceImpl.java`

```java
package uy.edu.pa.central.ws;

import jakarta.jws.WebService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import uy.edu.pa.central.ws.dtos.UserDTO;
import uy.edu.pa.central.persistence.JpaUtil;
import logica.Usuario;
import logica.Turista;
import logica.Proveedor;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@WebService(
    serviceName = "AuthService",
    endpointInterface = "uy.edu.pa.central.ws.AuthService",
    portName = "AuthServicePort",
    targetNamespace = "http://ws.central.pa.edu.uy/"
)
public class AuthServiceImpl implements AuthService {

    @Override
    public String ping() {
        return "pong";
    }

    @Override
    public UserDTO login(String identifier, String password) throws Exception {
        if (identifier == null || identifier.isBlank() || password == null || password.isBlank()) {
            throw new Exception("Credenciales inválidas");
        }

        EntityManager em = JpaUtil.getEntityManager();
        try {
            // Buscar por nickname o email
            TypedQuery<Usuario> query = em.createQuery(
                "SELECT u FROM Usuario u WHERE u.nickname = :id OR u.email = :id",
                Usuario.class
            );
            query.setParameter("id", identifier);
            List<Usuario> usuarios = query.getResultList();

            if (usuarios.isEmpty()) {
                return null;
            }

            Usuario usuario = usuarios.get(0);

            // Validar contraseña
            if (!usuario.getPassword().equals(password)) {
                return null;
            }

            // Construir DTO
            UserDTO dto = new UserDTO();
            dto.setNickname(usuario.getNickname());
            dto.setNombre(usuario.getNombre());
            dto.setApellido(usuario.getApellido());
            dto.setEmail(usuario.getEmail());

            // Determinar tipo de usuario
            if (usuario instanceof Turista) {
                dto.setTipoUsuario("turista");
                dto.setNacionalidad(((Turista) usuario).getNacionalidad());
            } else if (usuario instanceof Proveedor) {
                dto.setTipoUsuario("proveedor");
                dto.setDescripcion(((Proveedor) usuario).getDescripcion());
                dto.setLink(((Proveedor) usuario).getLink());
            }

            if (usuario.getFechaNac() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                dto.setFechaNacimiento(sdf.format(usuario.getFechaNac()));
            }

            return dto;
        } finally {
            em.close();
        }
    }

    @Override
    public boolean registrarTurista(String nickname, String nombre, String apellido,
                                   String email, String password, String fechaNacimiento,
                                   String nacionalidad) throws Exception {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            // Verificar si ya existe
            TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(u) FROM Usuario u WHERE u.nickname = :nick OR u.email = :email",
                Long.class
            );
            query.setParameter("nick", nickname);
            query.setParameter("email", email);
            Long count = query.getSingleResult();

            if (count > 0) {
                return false; // Ya existe
            }

            // Crear nuevo turista
            em.getTransaction().begin();
            
            Turista turista = new Turista();
            turista.setNickname(nickname);
            turista.setNombre(nombre);
            turista.setApellido(apellido);
            turista.setEmail(email);
            turista.setPassword(password);
            turista.setNacionalidad(nacionalidad);

            // Parsear fecha
            if (fechaNacimiento != null && !fechaNacimiento.isBlank()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date fecha = sdf.parse(fechaNacimiento);
                turista.setFechaNac(fecha);
            }

            em.persist(turista);
            em.getTransaction().commit();

            System.out.println("✅ Turista registrado: " + nickname);
            return true;

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("❌ Error registrando turista: " + e.getMessage());
            throw new Exception("Error al registrar turista: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    @Override
    public boolean registrarProveedor(String nickname, String nombre, String apellido,
                                     String email, String password, String fechaNacimiento,
                                     String descripcion, String link) throws Exception {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            // Verificar si ya existe
            TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(u) FROM Usuario u WHERE u.nickname = :nick OR u.email = :email",
                Long.class
            );
            query.setParameter("nick", nickname);
            query.setParameter("email", email);
            Long count = query.getSingleResult();

            if (count > 0) {
                return false; // Ya existe
            }

            // Crear nuevo proveedor
            em.getTransaction().begin();
            
            Proveedor proveedor = new Proveedor();
            proveedor.setNickname(nickname);
            proveedor.setNombre(nombre);
            proveedor.setApellido(apellido);
            proveedor.setEmail(email);
            proveedor.setPassword(password);
            proveedor.setDescripcion(descripcion);
            proveedor.setLink(link);

            // Parsear fecha
            if (fechaNacimiento != null && !fechaNacimiento.isBlank()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date fecha = sdf.parse(fechaNacimiento);
                proveedor.setFechaNac(fecha);
            }

            em.persist(proveedor);
            em.getTransaction().commit();

            System.out.println("✅ Proveedor registrado: " + nickname);
            return true;

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("❌ Error registrando proveedor: " + e.getMessage());
            throw new Exception("Error al registrar proveedor: " + e.getMessage());
        } finally {
            em.close();
        }
    }
}
```

#### Explicación de la Implementación:

**Anotación @WebService de Implementación:**
```java
@WebService(
    serviceName = "AuthService",              // Nombre del servicio
    endpointInterface = "uy.edu.pa.central.ws.AuthService", // Interface que implementa
    portName = "AuthServicePort",             // Nombre del puerto SOAP
    targetNamespace = "http://ws.central.pa.edu.uy/" // Namespace
)
```

**Gestión de EntityManager (JPA):**
```java
EntityManager em = JpaUtil.getEntityManager();
try {
    // Operaciones de BD
    em.getTransaction().begin();
    em.persist(entidad);
    em.getTransaction().commit();
} catch (Exception e) {
    if (em.getTransaction().isActive()) {
        em.getTransaction().rollback(); // Revertir cambios si hay error
    }
    throw new Exception("Error: " + e.getMessage());
} finally {
    em.close(); // SIEMPRE cerrar EntityManager
}
```

**Pattern de Validación de Duplicados:**
```java
TypedQuery<Long> query = em.createQuery(
    "SELECT COUNT(u) FROM Usuario u WHERE u.nickname = :nick OR u.email = :email",
    Long.class
);
query.setParameter("nick", nickname);
query.setParameter("email", email);
Long count = query.getSingleResult();

if (count > 0) {
    return false; // Ya existe, no permitir registro
}
```

**Parseo de Fechas:**
```java
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
Date fecha = sdf.parse(fechaNacimiento); // String → Date
turista.setFechaNac(fecha);

// Para retornar:
String fechaStr = sdf.format(usuario.getFechaNac()); // Date → String
dto.setFechaNacimiento(fechaStr);
```

---

## 🎬 ActividadesService - Servicio de Actividades

### 1. Interface ActividadesService.java

**Ubicación:** `Laboratorio3Pap-main/central-ws/src/main/java/uy/edu/pa/central/ws/ActividadesService.java`

```java
package uy.edu.pa.central.ws;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import uy.edu.pa.central.ws.dtos.ActividadDTO;
import uy.edu.pa.central.ws.dtos.SalidaDTO;
import uy.edu.pa.central.ws.dtos.InscripcionDTO;
import uy.edu.pa.central.ws.dtos.UserDTO;
import java.util.List;

@WebService(name = "ActividadesService", targetNamespace = "http://ws.central.pa.edu.uy/")
public interface ActividadesService {

    // ============= CONSULTAS =============
    
    @WebMethod(operationName = "ListarActividades")
    @WebResult(name = "actividades")
    List<ActividadDTO> listarActividades();

    @WebMethod(operationName = "ObtenerActividad")
    @WebResult(name = "actividad")
    ActividadDTO obtenerActividad(@WebParam(name = "id") String id);

    @WebMethod(operationName = "ObtenerSalida")
    @WebResult(name = "salida")
    SalidaDTO obtenerSalida(@WebParam(name = "id") String id);

    @WebMethod(operationName = "ListarUsuarios")
    @WebResult(name = "usuarios")
    List<UserDTO> listarUsuarios();

    @WebMethod(operationName = "ObtenerUsuario")
    @WebResult(name = "usuario")
    UserDTO obtenerUsuario(@WebParam(name = "nickname") String nickname);

    @WebMethod(operationName = "ListarInscripcionesPorTurista")
    @WebResult(name = "inscripciones")
    List<InscripcionDTO> listarInscripcionesPorTurista(
        @WebParam(name = "turista") String turista
    );

    // ============= OPERACIONES =============

    @WebMethod(operationName = "InscribirTurista")
    @WebResult(name = "exito")
    boolean inscribirTurista(
        @WebParam(name = "actividad") String actividad,
        @WebParam(name = "salida") String salida,
        @WebParam(name = "turista") String turista,
        @WebParam(name = "cantidad") int cantidad,
        @WebParam(name = "fecha") String fecha,
        @WebParam(name = "costo") float costo
    );

    @WebMethod(operationName = "CrearActividad")
    @WebResult(name = "exito")
    boolean crearActividad(
        @WebParam(name = "nombre") String nombre,
        @WebParam(name = "descripcion") String descripcion,
        @WebParam(name = "duracion") int duracion,
        @WebParam(name = "costo") float costo,
        @WebParam(name = "ciudad") String ciudad,
        @WebParam(name = "proveedor") String proveedor,
        @WebParam(name = "fechaAlta") String fechaAlta
    );

    @WebMethod(operationName = "CrearSalida")
    @WebResult(name = "exito")
    boolean crearSalida(
        @WebParam(name = "nombre") String nombre,
        @WebParam(name = "fecha") String fecha,
        @WebParam(name = "hora") String hora,
        @WebParam(name = "lugar") String lugar,
        @WebParam(name = "cantMax") int cantMax,
        @WebParam(name = "fechaAlta") String fechaAlta,
        @WebParam(name = "actividad") String actividad
    );

    @WebMethod(operationName = "ActualizarUsuario")
    @WebResult(name = "exito")
    boolean actualizarUsuario(
        @WebParam(name = "nickname") String nickname,
        @WebParam(name = "nombre") String nombre,
        @WebParam(name = "apellido") String apellido,
        @WebParam(name = "fechaNacimiento") String fechaNacimiento,
        @WebParam(name = "nacionalidad") String nacionalidad,
        @WebParam(name = "descripcion") String descripcion,
        @WebParam(name = "link") String link
    );
}
```

#### Operaciones Implementadas (10 total):

##### Grupo 1: Consultas de Lectura

1. **ListarActividades()** - Obtener todas las actividades
2. **ObtenerActividad(id)** - Obtener una actividad específica
3. **ObtenerSalida(id)** - Obtener una salida específica
4. **ListarUsuarios()** - Obtener todos los usuarios
5. **ObtenerUsuario(nickname)** - Obtener un usuario específico
6. **ListarInscripcionesPorTurista(turista)** - Obtener inscripciones de un turista

##### Grupo 2: Operaciones de Escritura

7. **InscribirTurista(...)** - Registrar inscripción de turista a salida
8. **CrearActividad(...)** - Crear nueva actividad turística
9. **CrearSalida(...)** - Crear nueva salida para una actividad
10. **ActualizarUsuario(...)** - Actualizar datos de usuario

---

### 2. Implementación ActividadesServiceImpl.java (Extractos Clave)

**Ubicación:** `Laboratorio3Pap-main/central-ws/src/main/java/uy/edu/pa/central/ws/ActividadesServiceImpl.java`

#### Ejemplo 1: ListarActividades()

```java
@Override
public List<ActividadDTO> listarActividades() {
    EntityManager em = JpaUtil.getEntityManager();
    try {
        TypedQuery<Actividad> query = em.createQuery(
            "SELECT a FROM Actividad a",
            Actividad.class
        );
        List<Actividad> actividades = query.getResultList();

        List<ActividadDTO> dtos = new ArrayList<>();
        for (Actividad act : actividades) {
            ActividadDTO dto = new ActividadDTO();
            dto.setNombre(act.getNombre());
            dto.setDescripcion(act.getDescripcion());
            dto.setDuracion(act.getDuracion());
            dto.setCosto(act.getCosto());
            dto.setCiudad(act.getCiudad());
            dto.setEstado(act.getEstado());
            
            if (act.getProveedor() != null) {
                dto.setProveedorNickname(act.getProveedor().getNickname());
            }
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if (act.getFechaAlta() != null) {
                dto.setFechaAlta(sdf.format(act.getFechaAlta()));
            }
            
            dtos.add(dto);
        }
        return dtos;
    } finally {
        em.close();
    }
}
```

**¿Por qué DTOs?**
- No podemos retornar entidades JPA directamente (problemas de serialización SOAP)
- Evitamos exponer toda la estructura de la BD
- Control sobre qué datos se envían al cliente

#### Ejemplo 2: CrearActividad()

```java
@Override
public boolean crearActividad(String nombre, String descripcion, int duracion,
                              float costo, String ciudad, String proveedor,
                              String fechaAlta) {
    EntityManager em = JpaUtil.getEntityManager();
    try {
        // Buscar proveedor
        Proveedor prov = em.find(Proveedor.class, proveedor);
        if (prov == null) {
            System.err.println("❌ Proveedor no encontrado: " + proveedor);
            return false;
        }

        em.getTransaction().begin();
        
        Actividad actividad = new Actividad();
        actividad.setNombre(nombre);
        actividad.setDescripcion(descripcion);
        actividad.setDuracion(duracion);
        actividad.setCosto(costo);
        actividad.setCiudad(ciudad);
        actividad.setEstado("Ingresada"); // Estado por defecto
        actividad.setProveedor(prov);

        // Parsear fecha
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date fecha = sdf.parse(fechaAlta);
        actividad.setFechaAlta(fecha);

        em.persist(actividad);
        em.getTransaction().commit();

        System.out.println("✅ Actividad creada: " + nombre);
        return true;

    } catch (Exception e) {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
        System.err.println("❌ Error creando actividad: " + e.getMessage());
        return false;
    } finally {
        em.close();
    }
}
```

**Puntos Clave:**
- Validación de existencia de proveedor ANTES de crear
- Transacción con rollback automático en caso de error
- Estado "Ingresada" por defecto (según lógica de negocio)
- Logging detallado para debugging

#### Ejemplo 3: ObtenerUsuario()

```java
@Override
public UserDTO obtenerUsuario(String nickname) {
    EntityManager em = JpaUtil.getEntityManager();
    try {
        Usuario usuario = em.find(Usuario.class, nickname);
        if (usuario == null) {
            return null;
        }

        UserDTO dto = new UserDTO();
        dto.setNickname(usuario.getNickname());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setEmail(usuario.getEmail());

        // Type checking para campos específicos
        if (usuario instanceof Turista) {
            dto.setTipoUsuario("turista");
            dto.setNacionalidad(((Turista) usuario).getNacionalidad());
        } else if (usuario instanceof Proveedor) {
            dto.setTipoUsuario("proveedor");
            dto.setDescripcion(((Proveedor) usuario).getDescripcion());
            dto.setLink(((Proveedor) usuario).getLink());
        }

        if (usuario.getFechaNac() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            dto.setFechaNacimiento(sdf.format(usuario.getFechaNac()));
        }

        return dto;
    } finally {
        em.close();
    }
}
```

**Pattern de Polimorfismo:**
- `Usuario` es clase base
- `Turista` y `Proveedor` heredan de `Usuario`
- Usamos `instanceof` para determinar tipo y acceder a campos específicos

---

## 📦 DTOs y Transferencia de Datos

### ¿Qué son los DTOs?

**DTO = Data Transfer Object**

Son objetos simples (POJO) que solo contienen datos, sin lógica de negocio. Se usan para:
- Transferir datos entre capas (Web Service ↔ Servlet)
- Evitar exponer entidades JPA directamente
- Serialización/deserialización SOAP/XML

### UserDTO.java

**Ubicación:** `Laboratorio3Pap-main/central-ws/src/main/java/uy/edu/pa/central/ws/dtos/UserDTO.java`

```java
package uy.edu.pa.central.ws.dtos;

public class UserDTO {
    private String nickname;
    private String nombre;
    private String apellido;
    private String email;
    private String fechaNacimiento;
    private String tipoUsuario; // "turista" o "proveedor"
    
    // Campos específicos de Turista
    private String nacionalidad;
    
    // Campos específicos de Proveedor
    private String descripcion;
    private String link;

    // Constructor vacío (requerido por JAXB)
    public UserDTO() {}

    // Getters y Setters (todos públicos)
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    // ... (resto de getters/setters)
}
```

**Características:**
- Todos los campos son `String` (simplifica serialización)
- Constructor vacío obligatorio (JAXB lo requiere)
- Getters/setters públicos (JAXB los usa para XML)
- Sin lógica de negocio (solo datos)

### ActividadDTO.java

```java
package uy.edu.pa.central.ws.dtos;

public class ActividadDTO {
    private String nombre;
    private String descripcion;
    private int duracion;
    private float costo;
    private String ciudad;
    private String estado;
    private String fechaAlta;
    private String proveedorNickname;

    public ActividadDTO() {}

    // Getters y Setters
    // ...
}
```

### SalidaDTO.java

```java
package uy.edu.pa.central.ws.dtos;

public class SalidaDTO {
    private String nombre;
    private String fecha;
    private String hora;
    private String lugar;
    private int cantMax;
    private String fechaAlta;
    private String actividadNombre;

    public SalidaDTO() {}

    // Getters y Setters
    // ...
}
```

### InscripcionDTO.java

```java
package uy.edu.pa.central.ws.dtos;

public class InscripcionDTO {
    private String actividad;
    private String salida;
    private String turista;
    private int cantidad;
    private String fecha;
    private float costo;

    public InscripcionDTO() {}

    // Getters y Setters
    // ...
}
```

---

## 🔧 Cliente SOAP - Generación de Stubs

### ¿Qué son los Stubs?

Los **stubs** son clases Java generadas automáticamente que actúan como **proxy** para invocar servicios SOAP remotos.

**Sin stubs (imposible):**
```java
// ¿Cómo invoco el servicio SOAP manualmente?
String xml = "<soap:Envelope>...</soap:Envelope>"; // ❌ Tedioso y propenso a errores
HttpURLConnection conn = ...
// Escribir y parsear XML manualmente
```

**Con stubs (fácil):**
```java
// Stubs generados hacen todo el trabajo SOAP
AuthService_Service service = new AuthService_Service();
AuthService port = service.getAuthServicePort();
UserDTO user = port.login("juan123", "pass123"); // ✅ Simple como llamar un método local
```

### Generación de Stubs con Maven wsimport

**Ubicación:** `Laboratorio3Pap-main/client-ws/pom.xml`

```xml
<project>
    <groupId>uy.edu.pa</groupId>
    <artifactId>client-ws</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>

    <dependencies>
        <!-- Metro JAX-WS para cliente SOAP -->
        <dependency>
            <groupId>com.sun.xml.ws</groupId>
            <artifactId>jaxws-rt</artifactId>
            <version>4.0.2</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>com.sun.xml.ws</groupId>
                <artifactId>jaxws-maven-plugin</artifactId>
                <version>4.0.0</version>
                <executions>
                    <!-- Generar stubs para AuthService -->
                    <execution>
                        <id>wsimport-auth</id>
                        <goals>
                            <goal>wsimport</goal>
                        </goals>
                        <configuration>
                            <wsdlUrls>
                                <wsdlUrl>http://localhost:9128/central-ws/services/AuthService?wsdl</wsdlUrl>
                            </wsdlUrls>
                            <packageName>uy.edu.pa.client.ws.auth</packageName>
                            <keep>true</keep>
                            <sourceDestDir>${project.build.directory}/generated-sources/wsimport</sourceDestDir>
                        </configuration>
                    </execution>
                    
                    <!-- Generar stubs para ActividadesService -->
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

### Proceso de Generación

```bash
# 1. Asegurarse de que central-ws está corriendo en puerto 9128
cd Laboratorio3Pap-main/central-ws
mvn clean install
cp target/central-ws.war ../../apache-tomcat-10.1.47-central/webapps/

# 2. Verificar que WSDLs están accesibles
curl http://localhost:9128/central-ws/services/AuthService?wsdl
curl http://localhost:9128/central-ws/services/ActividadesService?wsdl

# 3. Generar stubs
cd ../client-ws
mvn clean compile

# Salida:
# [INFO] jaxws:wsimport args: [-keep, -s, .../generated-sources/wsimport, 
#                                -d, .../classes, -encoding, UTF-8, -Xnocompile,
#                                -p, uy.edu.pa.client.ws.auth,
#                                http://localhost:9128/central-ws/services/AuthService?wsdl]
# parsing WSDL...
# Generating code...

# 4. Empaquetar como JAR
mvn package

# Resultado: client-ws/target/client-ws-1.0.0.jar
```

### Clases Generadas (41 total)

```
client-ws/target/generated-sources/wsimport/
└── uy/edu/pa/client/ws/
    ├── auth/
    │   ├── AuthService.java           # Interface del servicio
    │   ├── AuthService_Service.java   # Factory para crear puerto
    │   ├── Login.java                 # Request wrapper
    │   ├── LoginResponse.java         # Response wrapper
    │   ├── Ping.java
    │   ├── PingResponse.java
    │   ├── RegistrarTurista.java
    │   ├── RegistrarTuristaResponse.java
    │   ├── RegistrarProveedor.java
    │   ├── RegistrarProveedorResponse.java
    │   ├── UserDTO.java               # DTO copiado
    │   ├── ObjectFactory.java         # JAXB helper
    │   └── package-info.java          # Metadata
    └── actividades/
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
        └── package-info.java
        └── ... (28 más)
```

### Ejemplo de Uso de Stubs en Servlet

```java
package servlets;

import jakarta.servlet.http.*;
import uy.edu.pa.client.ws.auth.AuthService;
import uy.edu.pa.client.ws.auth.AuthService_Service;
import uy.edu.pa.client.ws.auth.UserDTO;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. Obtener parámetros
        String identifier = request.getParameter("identifier");
        String password = request.getParameter("password");

        try {
            // 2. Crear cliente SOAP usando stubs generados
            AuthService_Service service = new AuthService_Service();
            AuthService port = service.getAuthServicePort();

            // 3. Invocar operación SOAP (parece método local, pero es remoto)
            UserDTO user = port.login(identifier, password);

            // 4. Procesar respuesta
            if (user != null) {
                HttpSession session = request.getSession();
                session.setAttribute("usuario", user);
                response.sendRedirect("dashboard.jsp");
            } else {
                request.setAttribute("error", "Credenciales inválidas");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error de conexión: " + e.getMessage());
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
```

**Flujo Interno (transparente para el programador):**
1. `new AuthService_Service()` → Lee WSDL, configura endpoint
2. `service.getAuthServicePort()` → Crea proxy SOAP
3. `port.login(...)` → Serializa parámetros a XML, envía HTTP POST, parsea respuesta
4. Retorna `UserDTO` deserializado desde XML

---

## 📊 Resumen de Cambios - Parte 1

### Archivos Creados

| Archivo | Ubicación | Propósito |
|---------|-----------|-----------|
| `AuthService.java` | `central-ws/src/.../ws/` | Interface SOAP auth |
| `AuthServiceImpl.java` | `central-ws/src/.../ws/` | Implementación auth con JPA |
| `ActividadesService.java` | `central-ws/src/.../ws/` | Interface SOAP actividades |
| `ActividadesServiceImpl.java` | `central-ws/src/.../ws/` | Implementación actividades con JPA |
| `UserDTO.java` | `central-ws/src/.../dtos/` | DTO para usuarios |
| `ActividadDTO.java` | `central-ws/src/.../dtos/` | DTO para actividades |
| `SalidaDTO.java` | `central-ws/src/.../dtos/` | DTO para salidas |
| `InscripcionDTO.java` | `central-ws/src/.../dtos/` | DTO para inscripciones |
| `JpaUtil.java` | `central-ws/src/.../persistence/` | Gestor EntityManager |
| `sun-jaxws.xml` | `central-ws/src/main/webapp/WEB-INF/` | Config endpoints JAX-WS |
| `persistence.xml` | `central-ws/src/main/resources/META-INF/` | Config JPA/EclipseLink |
| `client-ws/pom.xml` | `client-ws/` | Config Maven para generar stubs |
| 41 clases stub | `client-ws/target/generated-sources/` | Proxies SOAP generados |

### Servicios SOAP Expuestos

**AuthService** (Puerto 9128)
- Endpoint: `http://localhost:9128/central-ws/services/AuthService`
- WSDL: `http://localhost:9128/central-ws/services/AuthService?wsdl`
- Operaciones: 4 (Ping, Login, RegistrarTurista, RegistrarProveedor)

**ActividadesService** (Puerto 9128)
- Endpoint: `http://localhost:9128/central-ws/services/ActividadesService`
- WSDL: `http://localhost:9128/central-ws/services/ActividadesService?wsdl`
- Operaciones: 10 (Listar, Obtener, Inscribir, Crear, Actualizar)

### Tecnologías Utilizadas

- **JAX-WS 4.0** (Jakarta XML Web Services)
- **Metro 4.0.2** (Implementación JAX-WS)
- **JPA 3.1** (Jakarta Persistence API)
- **EclipseLink 4.0.2** (Proveedor JPA)
- **MySQL Connector 8.0.33**
- **Maven 3.9+**
- **Tomcat 10.1.47** (Jakarta EE 9+)

---

## ✅ Verificación de Funcionamiento

### Prueba 1: Health Check (Ping)

```bash
curl -X POST http://localhost:9128/central-ws/services/AuthService \
  -H "Content-Type: text/xml" \
  -d '<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
  <soap:Body>
    <Ping xmlns="http://ws.central.pa.edu.uy/"/>
  </soap:Body>
</soap:Envelope>'

# Respuesta esperada:
# <soap:Envelope>
#   <soap:Body>
#     <PingResponse>
#       <pong>pong</pong>
#     </PingResponse>
#   </soap:Body>
# </soap:Envelope>
```

### Prueba 2: Login

```bash
curl -X POST http://localhost:9128/central-ws/services/AuthService \
  -H "Content-Type: text/xml" \
  -d '<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
  <soap:Body>
    <Login xmlns="http://ws.central.pa.edu.uy/">
      <identifier>juan123</identifier>
      <password>password123</password>
    </Login>
  </soap:Body>
</soap:Envelope>'

# Respuesta esperada (si existe el usuario):
# <LoginResponse>
#   <user>
#     <nickname>juan123</nickname>
#     <nombre>Juan</nombre>
#     <apellido>Pérez</apellido>
#     <email>juan@example.com</email>
#     <tipoUsuario>turista</tipoUsuario>
#   </user>
# </LoginResponse>
```

### Prueba 3: Verificar WSDLs

```bash
# AuthService WSDL
curl http://localhost:9128/central-ws/services/AuthService?wsdl

# ActividadesService WSDL
curl http://localhost:9128/central-ws/services/ActividadesService?wsdl

# Ambos deben retornar XML válido con definiciones SOAP
```

---

## 🎓 Conceptos Clave Aprendidos

### 1. Arquitectura SOA (Service-Oriented Architecture)
- Separación de capas: Web → Services → Data
- Comunicación mediante contratos (WSDL)
- Independencia de tecnología

### 2. SOAP vs REST
| Característica | SOAP | REST |
|----------------|------|------|
| Protocolo | Solo HTTP(S) | HTTP, HTTPS, otros |
| Formato | XML exclusivamente | JSON, XML, HTML |
| Contrato | WSDL (estricto) | Opcional (OpenAPI) |
| Stateful | Sí (WS-Security, etc.) | No (stateless) |
| Complejidad | Alta | Baja |
| Uso | Enterprise, legacy | Moderno, microservicios |

### 3. JPA y EntityManager
- **EntityManager**: Gestor de persistencia (CRUD sobre BD)
- **Transacciones**: begin() → persist/merge/remove() → commit()
- **Rollback**: Revertir cambios en caso de error
- **Cerrar siempre**: `em.close()` en bloque `finally`

### 4. DTOs vs Entidades
| DTO | Entidad JPA |
|-----|-------------|
| Datos simples | Lógica + Datos |
| Serializable | Puede no ser |
| Sin anotaciones JPA | @Entity, @Id, @Column |
| Para transferencia | Para persistencia |

---

**Continúa en:** [Todo-lo-realizado-PARTE-2.md](./Todo-lo-realizado-PARTE-2.md) (Conversión de Servlets)
