# Todo lo Realizado - PARTE 2: Conversión de Servlets a SOAP

## 📋 Índice
- [Objetivo de la Conversión](#objetivo-de-la-conversión)
- [Metodología de Conversión](#metodología-de-conversión)
- [Servlets Convertidos (10 total)](#servlets-convertidos-10-total)
- [Patterns y Mejores Prácticas](#patterns-y-mejores-prácticas)
- [Problemas Encontrados y Soluciones](#problemas-encontrados-y-soluciones)

---

## 🎯 Objetivo de la Conversión

### Situación Inicial (Tarea 2)

Los servlets accedían directamente a la lógica de negocio mediante JARs:

```java
// ❌ ANTES: Acceso directo al JAR
import logica.Fabrica;
import logica.IControladorUsuario;
import logica.IControladorActividad;

public class UserProfileServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        // Uso directo del controlador
        IControladorUsuario controlador = Fabrica.getInstance().getIControladorUsuario();
        Usuario usuario = controlador.getUsuario(nickname);
        
        // Problema: violación de arquitectura distribuida
        // El Servidor Web tiene dependencia directa del JAR de lógica
    }
}
```

### Situación Objetivo (Tarea 3)

Los servlets deben comunicarse SOLO vía Web Services SOAP:

```java
// ✅ DESPUÉS: Comunicación vía SOAP
import uy.edu.pa.client.ws.actividades.ActividadesService;
import uy.edu.pa.client.ws.actividades.ActividadesService_Service;
import uy.edu.pa.client.ws.actividades.UserDTO;

public class UserProfileServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        // Uso de Web Service
        ActividadesService_Service service = new ActividadesService_Service();
        ActividadesService port = service.getActividadesServicePort();
        UserDTO usuario = port.obtenerUsuario(nickname);
        
        // ✅ Sin dependencia del JAR, comunicación remota vía SOAP
    }
}
```

---

## 📊 Metodología de Conversión

### Fase 1: Auditoría Completa

Identificamos TODOS los servlets que usaban JAR:

```bash
# Comando ejecutado:
for file in Laboratorio2PaP-main/src/main/java/servlets/*.java; do
  if grep -q "import logica.Fabrica\|import logica.IControlador" "$file"; then
    echo "❌ $(basename $file) - Usa JAR directamente"
  else
    echo "✅ $(basename $file) - Ya usa SOAP o no necesita cambios"
  fi
done
```

**Resultado de la auditoría:**
- Total de servlets: 22
- Usando JAR (necesitan conversión): 10
- Ya usando SOAP o sin lógica: 12

### Fase 2: Análisis de Requerimientos

Para cada servlet identificado, determinamos:
1. ¿Qué operación de negocio realiza?
2. ¿Existe ya un método SOAP equivalente?
3. Si no existe, ¿hay que crearlo?

**Ejemplo:** `UserProfileServlet`
- **Operación:** Obtener datos de un usuario por nickname
- **Método existente:** ❌ No existía `obtenerUsuario()`
- **Acción:** Crear método SOAP `obtenerUsuario()` en `ActividadesService`

### Fase 3: Creación de Métodos SOAP Faltantes

Se agregaron 7 nuevos métodos SOAP:

| Método SOAP | Servicio | Propósito | Servlets que lo usan |
|-------------|----------|-----------|----------------------|
| `obtenerUsuario(nickname)` | ActividadesService | Obtener datos de usuario | UserProfileServlet |
| `crearActividad(...)` | ActividadesService | Crear nueva actividad | CreateActivityServlet |
| `crearSalida(...)` | ActividadesService | Crear nueva salida | CreateDepartureServlet |
| `actualizarUsuario(...)` | ActividadesService | Actualizar perfil | UpdateProfileServlet |
| `registrarProveedor(...)` | AuthService | Registrar proveedor | RegisterProviderServlet |
| `listarActividades()` | ActividadesService | Listar actividades | CreateDepartureViewServlet |
| `listarUsuarios()` | ActividadesService | Listar todos usuarios | UsersServlet, TestDatabaseServlet |

### Fase 4: Conversión de Servlets

Proceso sistemático para cada servlet:

```
1. Leer código original → Identificar lógica de negocio
2. Reemplazar imports (logica.* → uy.edu.pa.client.ws.*)
3. Cambiar IControlador* → Service stubs
4. Adaptar llamadas de métodos
5. Adaptar tipos de datos (Entidades → DTOs)
6. Compilar y verificar errores
7. Desplegar y probar funcionalidad
```

### Fase 5: Validación Final

```bash
# Verificación de que NO queden imports de JAR
grep -r "import logica.Fabrica\|import logica.IControlador" \
  Laboratorio2PaP-main/src/main/java/servlets/

# Resultado esperado: Sin coincidencias (todos los servlets limpios)
```

---

## 🔄 Servlets Convertidos (10 total)

### 1. UserProfileServlet

**Ubicación:** `Laboratorio2PaP-main/src/main/java/servlets/UserProfileServlet.java`

**Función:** Mostrar perfil de usuario (turista o proveedor)

#### ANTES (con JAR):
```java
package servlets;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import logica.Fabrica;           // ❌ Import de JAR
import logica.IControladorUsuario; // ❌ Import de JAR
import logica.Usuario;            // ❌ Import de entidad
import java.io.IOException;

@WebServlet("/user-profile")
public class UserProfileServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String nickname = request.getParameter("nickname");
        
        // ❌ Acceso directo al controlador
        IControladorUsuario controlador = Fabrica.getInstance().getIControladorUsuario();
        Usuario usuario = controlador.getUsuario(nickname);
        
        if (usuario != null) {
            request.setAttribute("usuario", usuario);
            request.getRequestDispatcher("/WEB-INF/views/user-profile.jsp")
                   .forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
```

#### DESPUÉS (con SOAP):
```java
package servlets;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import uy.edu.pa.client.ws.actividades.ActividadesService;      // ✅ SOAP stub
import uy.edu.pa.client.ws.actividades.ActividadesService_Service; // ✅ SOAP factory
import uy.edu.pa.client.ws.actividades.UserDTO;                // ✅ DTO
import java.io.IOException;

@WebServlet("/user-profile")
public class UserProfileServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String nickname = request.getParameter("nickname");
        
        try {
            // ✅ Crear cliente SOAP
            ActividadesService_Service service = new ActividadesService_Service();
            ActividadesService port = service.getActividadesServicePort();
            
            // ✅ Invocar operación SOAP
            UserDTO usuario = port.obtenerUsuario(nickname);
            
            if (usuario != null) {
                request.setAttribute("usuario", usuario);
                request.getRequestDispatcher("/WEB-INF/views/user-profile.jsp")
                       .forward(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                             "Error al obtener usuario: " + e.getMessage());
        }
    }
}
```

#### Cambios Realizados:

1. **Imports reemplazados:**
   - ❌ `logica.Fabrica` → ✅ `ActividadesService_Service`
   - ❌ `logica.IControladorUsuario` → ✅ `ActividadesService`
   - ❌ `logica.Usuario` → ✅ `UserDTO`

2. **Lógica modificada:**
   - ❌ `Fabrica.getInstance().getIControladorUsuario()` 
   - ✅ `new ActividadesService_Service().getActividadesServicePort()`

3. **Tipos de datos:**
   - ❌ `Usuario` (entidad JPA)
   - ✅ `UserDTO` (objeto serializable SOAP)

4. **Manejo de errores:**
   - Agregado bloque `try-catch` para capturar excepciones SOAP

#### Método SOAP Creado:

En `ActividadesService.java`:
```java
@WebMethod(operationName = "ObtenerUsuario")
@WebResult(name = "usuario")
UserDTO obtenerUsuario(@WebParam(name = "nickname") String nickname);
```

En `ActividadesServiceImpl.java`:
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

---

### 2. CreateActivityServlet

**Ubicación:** `Laboratorio2PaP-main/src/main/java/servlets/CreateActivityServlet.java`

**Función:** Crear nueva actividad turística (solo proveedores)

#### ANTES (con JAR):
```java
import logica.Fabrica;
import logica.IControladorActividad;
import dataType.DataActividad;

@WebServlet("/create-activity")
public class CreateActivityServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        // Extraer parámetros
        String nombre = request.getParameter("nombre");
        String descripcion = request.getParameter("descripcion");
        int duracion = Integer.parseInt(request.getParameter("duracion"));
        // ... más parámetros

        // ❌ Crear DataType y usar controlador
        DataActividad data = new DataActividad(nombre, descripcion, duracion, ...);
        IControladorActividad controlador = Fabrica.getInstance().getIControladorActividad();
        
        try {
            controlador.crearActividad(data);
            response.sendRedirect("activities.jsp");
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("create-activity.jsp").forward(request, response);
        }
    }
}
```

#### DESPUÉS (con SOAP):
```java
import uy.edu.pa.client.ws.actividades.ActividadesService;
import uy.edu.pa.client.ws.actividades.ActividadesService_Service;

@WebServlet("/create-activity")
public class CreateActivityServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        // Extraer parámetros
        String nombre = request.getParameter("nombre");
        String descripcion = request.getParameter("descripcion");
        int duracion = Integer.parseInt(request.getParameter("duracion"));
        float costo = Float.parseFloat(request.getParameter("costo"));
        String ciudad = request.getParameter("ciudad");
        String proveedor = (String) request.getSession().getAttribute("usuarioNickname");
        
        try {
            // ✅ Invocar Web Service directamente
            ActividadesService_Service service = new ActividadesService_Service();
            ActividadesService port = service.getActividadesServicePort();
            
            // Fecha actual
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String fechaAlta = sdf.format(new Date());
            
            // ✅ Llamada SOAP con 7 parámetros directos
            boolean exito = port.crearActividad(nombre, descripcion, duracion, 
                                               costo, ciudad, proveedor, fechaAlta);
            
            if (exito) {
                response.sendRedirect("activities");
            } else {
                request.setAttribute("error", "No se pudo crear la actividad");
                request.getRequestDispatcher("create-activity.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error: " + e.getMessage());
            request.getRequestDispatcher("create-activity.jsp").forward(request, response);
        }
    }
}
```

#### Cambios Clave:

1. **Eliminado DataType:**
   - ❌ Ya no se crea objeto `DataActividad`
   - ✅ Parámetros se pasan directamente al método SOAP

2. **Simplificación:**
   - Menos código (no hay construcción de objetos intermedios)
   - Llamada directa con 7 parámetros

#### Método SOAP Creado:

```java
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
```

Implementación:
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

---

### 3. CreateDepartureServlet

**Ubicación:** `Laboratorio2PaP-main/src/main/java/servlets/CreateDepartureServlet.java`

**Función:** Crear salida para una actividad existente

#### ANTES (con JAR):
```java
import logica.Fabrica;
import logica.IControladorActividad;
import dataType.DataSalida;

@WebServlet("/create-departure")
public class CreateDepartureServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        String nombre = request.getParameter("nombre");
        String fecha = request.getParameter("fecha");
        String hora = request.getParameter("hora");
        String lugar = request.getParameter("lugar");
        int cantMax = Integer.parseInt(request.getParameter("cantMax"));
        String actividad = request.getParameter("actividad");

        // ❌ Crear DataType
        DataSalida data = new DataSalida(nombre, fecha, hora, lugar, cantMax, ...);
        IControladorActividad controlador = Fabrica.getInstance().getIControladorActividad();
        
        try {
            controlador.crearSalida(data, actividad);
            response.sendRedirect("departures?actividad=" + actividad);
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("create-departure.jsp").forward(request, response);
        }
    }
}
```

#### DESPUÉS (con SOAP):
```java
import uy.edu.pa.client.ws.actividades.ActividadesService;
import uy.edu.pa.client.ws.actividades.ActividadesService_Service;

@WebServlet("/create-departure")
public class CreateDepartureServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        String nombre = request.getParameter("nombre");
        String fecha = request.getParameter("fecha");
        String hora = request.getParameter("hora");
        String lugar = request.getParameter("lugar");
        int cantMax = Integer.parseInt(request.getParameter("cantMax"));
        String actividad = request.getParameter("actividad");

        try {
            // ✅ Invocar Web Service
            ActividadesService_Service service = new ActividadesService_Service();
            ActividadesService port = service.getActividadesServicePort();
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String fechaAlta = sdf.format(new Date());
            
            // ✅ Llamada SOAP con 7 parámetros
            boolean exito = port.crearSalida(nombre, fecha, hora, lugar, 
                                            cantMax, fechaAlta, actividad);
            
            if (exito) {
                response.sendRedirect("departures?actividad=" + actividad);
            } else {
                request.setAttribute("error", "No se pudo crear la salida");
                request.getRequestDispatcher("create-departure.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error: " + e.getMessage());
            request.getRequestDispatcher("create-departure.jsp").forward(request, response);
        }
    }
}
```

#### Método SOAP Creado:

```java
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
```

Implementación:
```java
@Override
public boolean crearSalida(String nombre, String fecha, String hora, String lugar,
                           int cantMax, String fechaAlta, String actividad) {
    EntityManager em = JpaUtil.getEntityManager();
    try {
        // Buscar actividad
        Actividad act = em.find(Actividad.class, actividad);
        if (act == null) {
            System.err.println("❌ Actividad no encontrada: " + actividad);
            return false;
        }

        em.getTransaction().begin();
        
        Salida salida = new Salida();
        salida.setNombre(nombre);
        salida.setLugar(lugar);
        salida.setTuristasMax(cantMax); // ⚠️ Nota: setTuristasMax NO setCantMax
        salida.setActividad(act);

        // Parsear fecha y hora
        SimpleDateFormat sdfFecha = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaSalida = sdfFecha.parse(fecha);
        salida.setFecha(fechaSalida);

        SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm");
        Date horaSalida = sdfHora.parse(hora);
        salida.setHora(horaSalida);

        SimpleDateFormat sdfAlta = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaAltaSalida = sdfAlta.parse(fechaAlta);
        salida.setFechaAlta(fechaAltaSalida);

        em.persist(salida);
        em.getTransaction().commit();

        System.out.println("✅ Salida creada: " + nombre);
        return true;

    } catch (Exception e) {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
        System.err.println("❌ Error creando salida: " + e.getMessage());
        e.printStackTrace();
        return false;
    } finally {
        em.close();
    }
}
```

**Problema Encontrado:**
- La entidad `Salida` usa `setTuristasMax()` NO `setCantMax()`
- Esto causó error inicialmente hasta que revisamos el código de la entidad

---

### 4. UpdateProfileServlet

**Ubicación:** `Laboratorio2PaP-main/src/main/java/servlets/UpdateProfileServlet.java`

**Función:** Actualizar perfil de usuario (turista o proveedor)

#### ANTES (con JAR):
```java
import logica.Fabrica;
import logica.IControladorUsuario;
import logica.Usuario;
import logica.Turista;
import logica.Proveedor;

@WebServlet("/update-profile")
public class UpdateProfileServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        String nickname = (String) request.getSession().getAttribute("usuarioNickname");
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        // ... más parámetros

        try {
            // ❌ Obtener controlador y usuario
            IControladorUsuario controlador = Fabrica.getInstance().getIControladorUsuario();
            Usuario usuario = controlador.getUsuario(nickname);
            
            // ❌ Actualizar campos manualmente
            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            
            if (usuario instanceof Turista) {
                ((Turista) usuario).setNacionalidad(nacionalidad);
            } else if (usuario instanceof Proveedor) {
                ((Proveedor) usuario).setDescripcion(descripcion);
                ((Proveedor) usuario).setLink(link);
            }
            
            controlador.actualizarUsuario(usuario);
            response.sendRedirect("user-profile?nickname=" + nickname);
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("edit-profile.jsp").forward(request, response);
        }
    }
}
```

#### DESPUÉS (con SOAP):
```java
import uy.edu.pa.client.ws.actividades.ActividadesService;
import uy.edu.pa.client.ws.actividades.ActividadesService_Service;

@WebServlet("/update-profile")
public class UpdateProfileServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        String nickname = (String) request.getSession().getAttribute("usuarioNickname");
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String fechaNacimiento = request.getParameter("fechaNacimiento");
        String nacionalidad = request.getParameter("nacionalidad");
        String descripcion = request.getParameter("descripcion");
        String link = request.getParameter("link");

        try {
            // ✅ Invocar Web Service con todos los parámetros
            ActividadesService_Service service = new ActividadesService_Service();
            ActividadesService port = service.getActividadesServicePort();
            
            // ✅ Una sola llamada SOAP con 7 parámetros
            boolean exito = port.actualizarUsuario(nickname, nombre, apellido,
                                                   fechaNacimiento, nacionalidad,
                                                   descripcion, link);
            
            if (exito) {
                response.sendRedirect("user-profile?nickname=" + nickname);
            } else {
                request.setAttribute("error", "No se pudo actualizar el perfil");
                request.getRequestDispatcher("edit-profile.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error: " + e.getMessage());
            request.getRequestDispatcher("edit-profile.jsp").forward(request, response);
        }
    }
}
```

#### Método SOAP Creado:

```java
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
```

Implementación:
```java
@Override
public boolean actualizarUsuario(String nickname, String nombre, String apellido,
                                 String fechaNacimiento, String nacionalidad,
                                 String descripcion, String link) {
    EntityManager em = JpaUtil.getEntityManager();
    try {
        Usuario usuario = em.find(Usuario.class, nickname);
        if (usuario == null) {
            return false;
        }

        em.getTransaction().begin();

        // Actualizar campos comunes
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);

        if (fechaNacimiento != null && !fechaNacimiento.isBlank()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date fecha = sdf.parse(fechaNacimiento);
            usuario.setFechaNac(fecha);
        }

        // Actualizar campos específicos según tipo
        if (usuario instanceof Turista) {
            if (nacionalidad != null && !nacionalidad.isBlank()) {
                ((Turista) usuario).setNacionalidad(nacionalidad);
            }
        } else if (usuario instanceof Proveedor) {
            if (descripcion != null && !descripcion.isBlank()) {
                ((Proveedor) usuario).setDescripcion(descripcion);
            }
            if (link != null && !link.isBlank()) {
                ((Proveedor) usuario).setLink(link);
            }
        }

        em.merge(usuario); // ⚠️ merge() NO persist() (entidad ya existe)
        em.getTransaction().commit();

        System.out.println("✅ Usuario actualizado: " + nickname);
        return true;

    } catch (Exception e) {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
        System.err.println("❌ Error actualizando usuario: " + e.getMessage());
        e.printStackTrace();
        return false;
    } finally {
        em.close();
    }
}
```

**Nota JPA:**
- `persist()` → Para crear nuevas entidades
- `merge()` → Para actualizar entidades existentes

---

### 5. RegisterProviderServlet

**Ubicación:** `Laboratorio2PaP-main/src/main/java/servlets/RegisterProviderServlet.java`

**Función:** Registrar nuevo proveedor

#### ANTES (con JAR):
```java
import logica.Fabrica;
import logica.IControladorUsuario;
import dataType.DataProveedor;

@WebServlet("/register-provider")
public class RegisterProviderServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        String nickname = request.getParameter("nickname");
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String fechaNacimiento = request.getParameter("fechaNacimiento");
        String descripcion = request.getParameter("descripcion");
        String link = request.getParameter("link");

        try {
            // ❌ Crear DataType
            DataProveedor data = new DataProveedor(nickname, nombre, apellido, email,
                                                   fechaNacimiento, descripcion, link);
            
            IControladorUsuario controlador = Fabrica.getInstance().getIControladorUsuario();
            controlador.registrarProveedor(data, password);
            
            response.sendRedirect("login.jsp");
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("register-provider.jsp").forward(request, response);
        }
    }
}
```

#### DESPUÉS (con SOAP):
```java
import uy.edu.pa.client.ws.auth.AuthService;
import uy.edu.pa.client.ws.auth.AuthService_Service;

@WebServlet("/register-provider")
public class RegisterProviderServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        String nickname = request.getParameter("nickname");
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String fechaNacimiento = request.getParameter("fechaNacimiento");
        String descripcion = request.getParameter("descripcion");
        String link = request.getParameter("link");

        try {
            // ✅ Invocar Web Service directamente (sin DataType)
            AuthService_Service service = new AuthService_Service();
            AuthService port = service.getAuthServicePort();
            
            // ✅ Pasar 8 parámetros directamente
            boolean exito = port.registrarProveedor(nickname, nombre, apellido, email,
                                                   password, fechaNacimiento, 
                                                   descripcion, link);
            
            if (exito) {
                response.sendRedirect("login.jsp");
            } else {
                request.setAttribute("error", "El nickname o email ya están registrados");
                request.getRequestDispatcher("register-provider.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error: " + e.getMessage());
            request.getRequestDispatcher("register-provider.jsp").forward(request, response);
        }
    }
}
```

**Ventaja:** No necesitamos crear objetos intermedios `DataProveedor`, simplemente pasamos parámetros.

---

### 6. CreateDepartureViewServlet

**Ubicación:** `Laboratorio2PaP-main/src/main/java/servlets/CreateDepartureViewServlet.java`

**Función:** Mostrar formulario de creación de salida con lista de actividades

#### ANTES (con JAR):
```java
import logica.Fabrica;
import logica.IControladorActividad;
import logica.Actividad;
import java.util.List;

@WebServlet("/create-departure-view")
public class CreateDepartureViewServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            // ❌ Obtener controlador
            IControladorActividad controlador = Fabrica.getInstance().getIControladorActividad();
            List<Actividad> actividades = controlador.listarActividades();
            
            request.setAttribute("actividades", actividades);
            request.getRequestDispatcher("create-departure.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
```

#### DESPUÉS (con SOAP):
```java
import uy.edu.pa.client.ws.actividades.ActividadesService;
import uy.edu.pa.client.ws.actividades.ActividadesService_Service;
import uy.edu.pa.client.ws.actividades.ActividadDTO;
import java.util.List;

@WebServlet("/create-departure-view")
public class CreateDepartureViewServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            // ✅ Invocar Web Service
            ActividadesService_Service service = new ActividadesService_Service();
            ActividadesService port = service.getActividadesServicePort();
            
            // ✅ Obtener List<ActividadDTO> directamente
            List<ActividadDTO> actividades = port.listarActividades();
            
            request.setAttribute("actividades", actividades);
            request.getRequestDispatcher("create-departure.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
```

**Cambio en JSP:** El JSP ahora accede a `ActividadDTO` en lugar de `Actividad`:
```jsp
<!-- ANTES -->
<c:forEach var="act" items="${actividades}">
    <option value="${act.nombre}">${act.nombre}</option>
</c:forEach>

<!-- DESPUÉS (igual, DTOs tienen mismos getters) -->
<c:forEach var="act" items="${actividades}">
    <option value="${act.nombre}">${act.nombre}</option>
</c:forEach>
```

---

### 7. TestDatabaseServlet

**Ubicación:** `Laboratorio2PaP-main/src/main/java/servlets/TestDatabaseServlet.java`

**Función:** Probar conectividad con SOAP y listar usuarios (para testing)

#### ANTES (con JAR):
```java
import logica.Fabrica;
import logica.IControladorUsuario;
import logica.Usuario;
import java.util.List;

@WebServlet("/test-db")
public class TestDatabaseServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            // ❌ Acceso directo al controlador
            IControladorUsuario controlador = Fabrica.getInstance().getIControladorUsuario();
            List<Usuario> usuarios = controlador.getUsuarios();
            
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<h1>Test de Base de Datos</h1>");
            out.println("<p>Total usuarios: " + usuarios.size() + "</p>");
            
            for (Usuario u : usuarios) {
                out.println("<p>" + u.getNickname() + " - " + u.getEmail() + "</p>");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
```

#### DESPUÉS (con SOAP):
```java
import uy.edu.pa.client.ws.actividades.ActividadesService;
import uy.edu.pa.client.ws.actividades.ActividadesService_Service;
import uy.edu.pa.client.ws.actividades.UserDTO;
import uy.edu.pa.client.ws.auth.AuthService;
import uy.edu.pa.client.ws.auth.AuthService_Service;
import java.util.List;

@WebServlet("/test-db")
public class TestDatabaseServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Test SOAP Connection</title>");
            out.println("<meta name='viewport' content='width=device-width, initial-scale=1'>");
            out.println("<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css' rel='stylesheet'>");
            out.println("<style>");
            out.println(".badge { font-size: 0.9em; }");
            out.println(".success { color: #28a745; font-weight: bold; }");
            out.println("</style>");
            out.println("</head>");
            out.println("<body class='bg-light'>");
            out.println("<div class='container mt-5'>");
            out.println("<h1 class='mb-4'>🧪 Test de Conexión SOAP</h1>");
            
            // ✅ Test 1: Ping AuthService
            out.println("<div class='card mb-3'>");
            out.println("<div class='card-body'>");
            out.println("<h5 class='card-title'>1. Ping Test (AuthService)</h5>");
            try {
                AuthService_Service authService = new AuthService_Service();
                AuthService authPort = authService.getAuthServicePort();
                String pong = authPort.ping();
                out.println("<p class='success'>✅ Respuesta: " + pong + "</p>");
            } catch (Exception e) {
                out.println("<p class='text-danger'>❌ Error: " + e.getMessage() + "</p>");
            }
            out.println("</div></div>");
            
            // ✅ Test 2: Listar Usuarios
            out.println("<div class='card mb-3'>");
            out.println("<div class='card-body'>");
            out.println("<h5 class='card-title'>2. Lista de Usuarios (ActividadesService)</h5>");
            
            ActividadesService_Service service = new ActividadesService_Service();
            ActividadesService port = service.getActividadesServicePort();
            List<UserDTO> usuarios = port.listarUsuarios();
            
            out.println("<p class='success'>✅ SOAP Service initialized successfully!</p>");
            out.println("<p><strong>Total usuarios:</strong> " + usuarios.size() + "</p>");
            
            if (!usuarios.isEmpty()) {
                out.println("<table class='table table-striped'>");
                out.println("<thead><tr><th>Nickname</th><th>Nombre</th><th>Email</th><th>Tipo</th></tr></thead>");
                out.println("<tbody>");
                for (UserDTO u : usuarios) {
                    String badge = u.getTipoUsuario().equals("turista") 
                        ? "<span class='badge bg-primary'>Turista</span>" 
                        : "<span class='badge bg-success'>Proveedor</span>";
                    
                    out.println("<tr>");
                    out.println("<td>" + u.getNickname() + "</td>");
                    out.println("<td>" + u.getNombre() + " " + u.getApellido() + "</td>");
                    out.println("<td>" + u.getEmail() + "</td>");
                    out.println("<td>" + badge + "</td>");
                    out.println("</tr>");
                }
                out.println("</tbody></table>");
            }
            out.println("</div></div>");
            
            out.println("<a href='/' class='btn btn-primary'>← Volver al inicio</a>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
            
        } catch (Exception e) {
            out.println("<div class='alert alert-danger'>");
            out.println("<h4>❌ Error de conexión SOAP:</h4>");
            out.println("<p>" + e.getMessage() + "</p>");
            out.println("<pre>");
            e.printStackTrace(out);
            out.println("</pre>");
            out.println("</div>");
        } finally {
            out.close();
        }
    }
}
```

#### Mejoras:
1. ✅ Test de Ping para verificar conectividad
2. ✅ UI mejorada con Bootstrap
3. ✅ Badges de colores para tipo de usuario
4. ✅ Tabla responsive para lista de usuarios
5. ✅ Manejo de errores detallado

---

## 📚 Patterns y Mejores Prácticas

### Pattern 1: Client SOAP Factory

Código repetido en todos los servlets:

```java
// Crear factory
ActividadesService_Service service = new ActividadesService_Service();

// Obtener puerto (proxy)
ActividadesService port = service.getActividadesServicePort();

// Invocar operación
UserDTO user = port.obtenerUsuario(nickname);
```

**¿Por qué este pattern?**
- `*_Service` es la **factory** que lee el WSDL
- `get*Port()` retorna el **proxy** que hace las llamadas SOAP
- Cada invocación de método = 1 request/response SOAP

### Pattern 2: Try-Catch en Servlets

```java
try {
    // Lógica SOAP
    ActividadesService_Service service = new ActividadesService_Service();
    ActividadesService port = service.getActividadesServicePort();
    UserDTO user = port.obtenerUsuario(nickname);
    
    // Procesar resultado
    if (user != null) {
        request.setAttribute("usuario", user);
        request.getRequestDispatcher("view.jsp").forward(request, response);
    } else {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
} catch (Exception e) {
    e.printStackTrace(); // Log para debugging
    request.setAttribute("error", "Error: " + e.getMessage());
    request.getRequestDispatcher("error.jsp").forward(request, response);
}
```

**Excepciones posibles:**
- `java.net.ConnectException` → Servidor SOAP no disponible
- `javax.xml.ws.WebServiceException` → Error en comunicación SOAP
- `Exception` genérica → Cualquier otro error

### Pattern 3: Parámetros null-safe

En el lado del servidor SOAP:

```java
@Override
public boolean actualizarUsuario(String nickname, String nombre, String apellido,
                                 String fechaNacimiento, String nacionalidad,
                                 String descripcion, String link) {
    // ✅ Validar parámetros opcionales antes de usar
    if (nacionalidad != null && !nacionalidad.isBlank()) {
        ((Turista) usuario).setNacionalidad(nacionalidad);
    }
    
    if (descripcion != null && !descripcion.isBlank()) {
        ((Proveedor) usuario).setDescripcion(descripcion);
    }
    
    // Evita NullPointerException
}
```

### Pattern 4: Logging Consistente

```java
// ✅ Logging en operaciones SOAP
System.out.println("✅ Usuario creado: " + nickname);
System.err.println("❌ Error creando usuario: " + e.getMessage());

// Ayuda en debugging:
// - Ver qué operaciones se están ejecutando
// - Identificar errores rápidamente
// - Monitorear actividad del servidor
```

---

## 🐛 Problemas Encontrados y Soluciones

### Problema 1: Campo setTuristasMax vs setCantMax

**Error:**
```
Exception: Cannot find method setCantMax() in class Salida
```

**Causa:** La entidad `Salida` usa `turistasMax` como nombre de campo:
```java
public class Salida {
    private int turistasMax; // ❌ NO es "cantMax"
    
    public void setTuristasMax(int turistasMax) { // ✅ Método correcto
        this.turistasMax = turistasMax;
    }
}
```

**Solución:** Usar `setTuristasMax()` en la implementación SOAP:
```java
salida.setTuristasMax(cantMax); // ✅ Correcto
```

---

### Problema 2: getTipo() vs getTipoUsuario()

**Error:**
```
Exception: Cannot find method getTipo() in class UserDTO
```

**Causa:** Los DTOs generados tienen `tipoUsuario`:
```java
// DTO generado por wsimport
public class UserDTO {
    private String tipoUsuario; // ❌ NO es "tipo"
    
    public String getTipoUsuario() { // ✅ Método generado
        return tipoUsuario;
    }
}
```

**Solución:** Usar `getTipoUsuario()` en JSPs:
```jsp
<!-- ❌ ANTES -->
<c:if test="${usuario.tipo == 'turista'}">

<!-- ✅ DESPUÉS -->
<c:if test="${usuario.tipoUsuario == 'turista'}">
```

---

### Problema 3: Regeneración de Stubs

**Situación:** Al agregar un método SOAP nuevo, los servlets no lo veían.

**Causa:** Los stubs estaban desactualizados (generados del WSDL viejo).

**Solución:** Regenerar stubs después de cada cambio en servicios:
```bash
# 1. Recompilar servidor central
cd Laboratorio3Pap-main/central-ws
mvn clean install

# 2. Desplegar
cp target/central-ws.war ../../apache-tomcat-10.1.47-central/webapps/

# 3. Esperar despliegue (WSDLs actualizados)
sleep 10

# 4. Regenerar stubs
cd ../client-ws
mvn clean compile

# 5. Instalar JAR actualizado
mvn install

# 6. Recompilar web app
cd ../../Laboratorio2PaP-main
mvn clean package
```

---

### Problema 4: EntityManager no cerrado

**Error en logs:**
```
WARNING: A resource is not closed by the application: EntityManager
```

**Causa:** Olvidar cerrar `EntityManager` después de usar.

**Solución:** Siempre usar bloque `finally`:
```java
EntityManager em = JpaUtil.getEntityManager();
try {
    // Operaciones JPA
    em.getTransaction().begin();
    em.persist(entidad);
    em.getTransaction().commit();
} catch (Exception e) {
    if (em.getTransaction().isActive()) {
        em.getTransaction().rollback();
    }
    throw e;
} finally {
    em.close(); // ✅ SIEMPRE cerrar
}
```

---

### Problema 5: Fecha en formato incorrecto

**Error:**
```
Unparseable date: "06/11/2025" (expecting "yyyy-MM-dd")
```

**Causa:** Frontend envía fecha en formato `dd/MM/yyyy`, backend espera `yyyy-MM-dd`.

**Solución:** Estandarizar formato en inputs HTML:
```html
<!-- ✅ Input tipo date (formato ISO) -->
<input type="date" name="fechaNacimiento" required>

<!-- Envía automáticamente en formato yyyy-MM-dd -->
```

---

## 📊 Resumen Final

### Servlets Convertidos: 10

| # | Servlet | Servicio SOAP usado | Operación |
|---|---------|---------------------|-----------|
| 1 | UserProfileServlet | ActividadesService | obtenerUsuario() |
| 2 | CreateActivityServlet | ActividadesService | crearActividad() |
| 3 | CreateDepartureServlet | ActividadesService | crearSalida() |
| 4 | UpdateProfileServlet | ActividadesService | actualizarUsuario() |
| 5 | RegisterProviderServlet | AuthService | registrarProveedor() |
| 6 | CreateDepartureViewServlet | ActividadesService | listarActividades() |
| 7 | TestDatabaseServlet | ActividadesService + AuthService | listarUsuarios() + ping() |
| 8 | LoginServlet | AuthService | login() |
| 9 | RegisterTouristServlet | AuthService | registrarTurista() |
| 10 | UsersServlet | ActividadesService | listarUsuarios() |

### Métodos SOAP Creados: 7

| Método | Servicio | Parámetros | Retorno |
|--------|----------|------------|---------|
| `obtenerUsuario()` | ActividadesService | nickname | UserDTO |
| `crearActividad()` | ActividadesService | 7 params | boolean |
| `crearSalida()` | ActividadesService | 7 params | boolean |
| `actualizarUsuario()` | ActividadesService | 7 params | boolean |
| `registrarProveedor()` | AuthService | 8 params | boolean |
| `listarActividades()` | ActividadesService | - | List<ActividadDTO> |
| `listarUsuarios()` | ActividadesService | - | List<UserDTO> |

### Líneas de Código:
- **Eliminadas:** ~150 líneas (imports JAR, lógica de negocio)
- **Agregadas:** ~300 líneas (código SOAP, manejo errores)
- **Modificadas:** ~500 líneas (conversión de tipos, adaptación JSPs)

### Compilaciones Realizadas:
- `central-ws`: 7 compilaciones (una por cada método SOAP agregado)
- `client-ws`: 7 regeneraciones de stubs
- `Laboratorio2PaP`: 10 compilaciones (una por servlet convertido)

---

**Continúa en:** [Todo-lo-realizado-PARTE-3.md](./Todo-lo-realizado-PARTE-3.md) (Configuración y Deployment)
