# Plan de Migración: Lógica de Servlets → Web Services SOAP

## 🎯 Objetivo
Mover toda la lógica de negocio de los servlets al servidor Central (SOAP) para implementar correctamente la arquitectura de 3 capas:

```
Presentación (JSPs + Servlets) → Web Services (SOAP) → Lógica de Negocio (Laboratorio1.jar)
```

---

## 📊 Análisis de Servlets con Lógica a Migrar

### 1. **CreateActivityServlet** ⚠️ CRÍTICO
**Lógica actual:** Acceso directo a JPA/EntityManager
**Debe migrar a:** `ActividadesService.crearActividad()`

### 2. **CreateDepartureServlet** ⚠️ CRÍTICO (Causa el error 500)
**Lógica actual:** Acceso directo a JPA/EntityManager
**Debe migrar a:** `ActividadesService.crearSalida()`

### 3. **InscriptionServlet / InscripcionServlet** ⚠️ CRÍTICO
**Lógica actual:** Acceso directo a JPA para inscripciones
**Debe migrar a:** `ActividadesService.inscribirTurista()`

### 4. **UpdateProfileServlet** ⚠️
**Lógica actual:** Acceso directo a JPA para actualizar usuarios
**Debe migrar a:** `UsuariosService.actualizarPerfil()`

### 5. **RegisterProviderServlet** ✅ Ya usa SOAP
**Estado:** Ya consume `AuthService.registrarProveedor()`

### 6. **RegisterTouristServlet** ✅ Ya usa SOAP  
**Estado:** Ya consume `AuthService.registrarTurista()`

### 7. **LoginServlet** ✅ Ya usa SOAP
**Estado:** Ya consume `AuthService.login()`

### 8. **ActivityDetailServlet** ⚠️
**Lógica actual:** Consultas JPA directas
**Debe migrar a:** `ActividadesService.obtenerActividad()`

### 9. **UserProfileServlet** ⚠️
**Lógica actual:** Consultas JPA directas
**Debe migrar a:** `UsuariosService.obtenerUsuario()`

### 10. **ActivitiesServlet** ⚠️
**Lógica actual:** Consultas JPA directas
**Debe migrar a:** `ActividadesService.listarActividades()` (ya existe)

---

## 🛠️ Servicios SOAP a Crear/Extender

### **ActividadesService** (Central-WS)

```java
@WebMethod
boolean crearActividad(
    String nombre,
    String descripcion, 
    int duracion,
    double costo,
    String ciudad,
    String categoria,
    String fechaAlta,
    String proveedorNickname,
    byte[] imagen
) throws Exception;

@WebMethod
boolean crearSalida(
    String nombre,
    String fechaSalida,
    String horaSalida,
    String lugarSalida,
    int cuposMaximos,
    String nombreActividad,
    byte[] imagen
) throws Exception;

@WebMethod
boolean inscribirTurista(
    String turistaNickname,
    String salidaNombre,
    String fechaInscripcion,
    int cantidadTuristas
) throws Exception;

@WebMethod
ActividadDTO obtenerActividadPorNombre(String nombre) throws Exception;

@WebMethod
List<SalidaDTO> obtenerSalidasDeActividad(String nombreActividad) throws Exception;
```

### **UsuariosService** (Central-WS)

```java
@WebMethod
boolean actualizarPerfil(
    String nickname,
    String nombre,
    String apellido,
    String email,
    String fechaNacimiento,
    String nacionalidad,  // solo turistas
    String descripcion,   // solo proveedores
    String link          // solo proveedores
) throws Exception;

@WebMethod
UserDTO obtenerUsuarioPorNickname(String nickname) throws Exception;

@WebMethod
UserDTO obtenerUsuarioPorEmail(String email) throws Exception;

@WebMethod
List<UserDTO> listarUsuarios() throws Exception;
```

### **PaquetesService** (Central-WS) - Si es necesario

```java
@WebMethod
boolean crearPaquete(...) throws Exception;

@WebMethod
List<PaqueteDTO> listarPaquetes() throws Exception;
```

---

## 🔧 Orden de Implementación

### **Fase 1: Extender Servicios SOAP** (Central-WS)
1. Agregar métodos a `ActividadesService.java` (interfaz)
2. Implementar en `ActividadesServiceImpl.java`
3. Agregar métodos a `UsuariosService.java` (interfaz)
4. Implementar en `UsuariosServiceImpl.java`

### **Fase 2: Recompilar y Regenerar Stubs**
```bash
# 1. Compilar Central-WS
cd Laboratorio3Pap-main/central-ws
mvn clean package

# 2. Reiniciar servidor Central
./apache-tomcat-10.1.47-central/bin/shutdown.sh
cp Laboratorio3Pap-main/central-ws/target/central-ws.war \
   apache-tomcat-10.1.47-central/webapps/
./apache-tomcat-10.1.47-central/bin/startup.sh

# 3. Esperar despliegue (15 segundos)
sleep 15

# 4. Regenerar stubs de cliente
cd Laboratorio3Pap-main/client-ws
mvn clean install
```

### **Fase 3: Refactorizar Servlets**
1. `CreateDepartureServlet` → Usar `ActividadesService.crearSalida()`
2. `CreateActivityServlet` → Usar `ActividadesService.crearActividad()`
3. `InscriptionServlet` → Usar `ActividadesService.inscribirTurista()`
4. `UpdateProfileServlet` → Usar `UsuariosService.actualizarPerfil()`
5. `ActivityDetailServlet` → Usar `ActividadesService.obtenerActividadPorNombre()`
6. `UserProfileServlet` → Usar `UsuariosService.obtenerUsuarioPorNickname()`

### **Fase 4: Recompilar Aplicación Web**
```bash
# 1. Compilar Laboratorio2PaP
cd Laboratorio2PaP-main
mvn clean package

# 2. Desplegar
cp target/Laboratorio2PaP.war ../apache-tomcat-10.1.47/webapps/

# 3. Reiniciar servidor Web
../apache-tomcat-10.1.47/bin/shutdown.sh
../apache-tomcat-10.1.47/bin/startup.sh
```

---

## ✅ Beneficios de esta Arquitectura

1. **Separación de capas**: Presentación → Servicios → Negocio
2. **Reusabilidad**: Los mismos servicios SOAP pueden ser consumidos por desktop, mobile, etc.
3. **Mantenibilidad**: La lógica de negocio está centralizada
4. **Testeo**: Se pueden probar los servicios SOAP independientemente
5. **Escalabilidad**: Servidor Central y Servidor Web pueden escalar independientemente

---

## 🚨 Archivos a NO Modificar

- `TestDatabaseServlet.java` - Solo para debugging
- `InitDataServlet.java` - Seed de datos inicial (mantener JPA directo)
- `DashboardServlet.java` - Solo renderiza vista
- `LogoutServlet.java` - Solo maneja sesión

---

## 📝 DTOs Necesarios

Asegurar que existan en `central-ws/src/main/java/.../dtos/`:

- `ActividadDTO.java` ✅ (ya existe)
- `SalidaDTO.java` ⚠️ (verificar)
- `InscripcionDTO.java` ⚠️ (crear si no existe)
- `UserDTO.java` ✅ (ya existe)
- `TuristaDTO.java` ⚠️ (verificar)
- `ProveedorDTO.java` ⚠️ (verificar)

---

## 🎯 Siguiente Paso INMEDIATO

**Comenzar con el servlet que está causando el error 500:**

1. Crear `ActividadesService.crearSalida()` en Central-WS
2. Refactorizar `CreateDepartureServlet` para usar SOAP
3. Probar que funciona

¿Deseas que comience con esta implementación?
