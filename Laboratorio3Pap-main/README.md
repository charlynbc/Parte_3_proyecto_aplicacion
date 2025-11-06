# Laboratorio 3 PaP – SOAP + Dispositivo Móvil Responsive (Tarea 3)

Implementación completa de la **Tarea 3** con arquitectura distribuida mediante Web Services SOAP y dispositivo móvil con diseño responsive.

## 📋 Requerimientos Cumplidos

✅ **Servidor Central SOAP** (puerto 8081) con servicios:
- `AuthService`: Login con validación real contra BD (nickname o email)
- `ActividadesService`: Consultas de actividades, salidas e inscripciones

✅ **Dispositivo Móvil Web Responsive** (RWD con Bootstrap 5):
- Inicio de Sesión
- Cierre de Sesión
- Consulta de Actividad Turística (con lista de salidas)

✅ **Arquitectura Distribuida**:
- Comunicación exclusiva vía Web Services SOAP
- Sin uso directo de JAR del Laboratorio1 en clientes
- Interoperabilidad garantizada

## 🏗️ Módulos del Proyecto

### 1. `central-ws/` (WAR - Puerto 8081)
**Servidor Central SOAP** con Metro JAX-WS / Jakarta para Tomcat 10.1

#### Servicios Expuestos:
- **AuthService**:
  - `login(identifier, password)` - Validación contra BD (soporta nickname o email)
  - `ping()` - Verificación de servicio
  
- **ActividadesService**:
  - `listarActividades()` - Lista todas las actividades con sus salidas
  - `obtenerActividad(id)` - Detalle de una actividad específica
  - `obtenerSalida(id)` - Detalle de una salida específica
  - `inscribirTurista(...)` - Registra inscripción de turista
  - `listarInscripcionesPorTurista(nickname)` - Inscripciones de un turista

#### Persistencia:
- JPA/EclipseLink + MySQL vía JNDI `jdbc/railway`
- Entidades del dominio: Usuario, Turista, Proveedor, Actividad, Salida, Inscripcion

### 2. `client-ws/` (JAR)
**Stubs generados por wsimport** para consumo desde clientes

- Generados en paquete: `uy.edu.pa.central.client`
- WSDLs consumidos desde `http://localhost:8081/central-ws/services`
- Se instala en repositorio local Maven para uso en otros módulos

### 3. `mobile-web/` (WAR - Puerto 8080) ⭐ NUEVO
**Dispositivo Móvil con Responsive Web Design**

#### Casos de Uso Implementados:
1. **Inicio de Sesión** (`/mobile/login`)
   - Formulario responsive con Bootstrap 5
   - Valida con AuthService SOAP
   - Soporta nickname o email

2. **Cierre de Sesión** (`/mobile/logout`)
   - Invalida sesión HTTP
   - Redirige al login

3. **Consulta de Actividad Turística** (`/mobile/actividades`)
   - Listado responsive con cards (Grid Bootstrap)
   - Imágenes adaptativas
   - Muestra precio, ubicación y número de salidas

4. **Detalle de Actividad** (`/mobile/actividad-detalle?id=...`)
   - Información completa de la actividad
   - Lista de todas las salidas disponibles
   - Diseño hero con imagen principal

#### Características Responsive:
- **Móvil** (< 576px): 1 columna, navbar colapsable
- **Tablet** (576px - 992px): 2 columnas
- **Desktop** (> 992px): 3 columnas
- Bootstrap 5.3 + Bootstrap Icons

## 🚀 Build y Deployment

### Prerequisitos
- Java 17 (mandatorio)
- Maven 3.9+
- Tomcat 10.1 (dos instancias: 8081 para Central, 8080 para Mobile Web)
- MySQL (configurado con JNDI o variables de entorno)

### 1. Compilar y Desplegar el Servidor Central (8081)

```bash
cd Laboratorio3Pap-main/central-ws
mvn clean package -DskipTests

# Opción A: Script automático
cd ../..
./deploy-central.sh

# Opción B: Manual
cp Laboratorio3Pap-main/central-ws/target/central-ws.war $TOMCAT_CENTRAL/webapps/
```

**Verificar WSDLs**:
- http://localhost:8081/central-ws/services/AuthService?wsdl
- http://localhost:8081/central-ws/services/ActividadesService?wsdl

### 2. Generar e Instalar Stubs de Cliente

```bash
cd Laboratorio3Pap-main/client-ws
mvn clean install -DskipTests
```

Esto genera los stubs en `target/generated-sources/wsimport/` y los instala en `~/.m2/repository/`.

### 3. Compilar y Desplegar Mobile Web (8080)

```bash
cd Laboratorio3Pap-main/mobile-web
mvn clean package -DskipTests

# Copiar WAR a Tomcat (puerto 8080)
cp target/mobile-web.war $TOMCAT_WEB/webapps/
```

### 4. Acceder a la Aplicación

**Dispositivo Móvil**: http://localhost:8080/mobile-web/

- Se redirige automáticamente a `/mobile/login`
- Probar en diferentes resoluciones (usar DevTools de Chrome)

## 📱 Testing del Dispositivo Móvil

### Usuarios de Prueba
Usa los usuarios que tengas en tu BD. Ejemplo:
- Nickname: `turista1`, `proveedor1`
- Email: `turista1@example.com`

### Verificar Responsive Design

**Chrome DevTools** (F12):
1. Click en icono "Toggle device toolbar" (Ctrl+Shift+M)
2. Probar con:
   - iPhone SE (375px)
   - iPad (768px)
   - Desktop (1920px)

### Flujo de Prueba
1. Acceder a http://localhost:8080/mobile-web/
2. Login con usuario turista
3. Ver lista de actividades (responsive cards)
4. Click en "Ver Detalle" de una actividad
5. Ver información completa con salidas
6. Verificar navbar colapsable en móvil
7. Cerrar sesión

## 📦 Contrato de Servicios (DTOs)

### AuthService
```java
UserDTO {
    String nickname;
    String nombre;
    String email;
    String tipoUsuario; // "turista" | "proveedor"
}
```

### ActividadesService
```java
ActividadDTO {
    String id; // nombre
    String descripcion;
    String lugar;
    float costo;
    String imagen; // URL
    List<SalidaDTO> salidas;
}

SalidaDTO {
    String id; // nombre
    String fecha; // dd/MM/yyyy
    String hora;  // HH:mm
    String lugar;
}

InscripcionDTO {
    String salidaNombre;
    String actividadNombre;
    String fechaSalida;
    String horaSalida;
    String lugar;
    int cantidad;
    float costo;
    String fechaInscripcion;
    String imagen;
}
```

## 🔧 Configuración

### Variables de Entorno (Servidor Central)
```bash
export DB_URL="jdbc:mysql://localhost:3306/turismouy"
export DB_USER="root"
export DB_PASSWORD="password"
export DB_LOG_LEVEL="INFO"
```

O configurar JNDI en `$TOMCAT_CENTRAL/conf/context.xml`:
```xml
<Resource name="jdbc/railway" 
          auth="Container"
          type="javax.sql.DataSource"
          maxTotal="20" 
          maxIdle="10"
          maxWaitMillis="10000"
          username="root" 
          password="password"
          driverClassName="com.mysql.cj.jdbc.Driver"
          url="jdbc:mysql://localhost:3306/turismouy"/>
```

### Ajustar URL del Servidor Central

Si el Central no está en `localhost:8081`, editar:

**client-ws/pom.xml**:
```xml
<properties>
    <wsdl.baseUrl>http://TU_HOST:8081/central-ws/services</wsdl.baseUrl>
</properties>
```

Luego regenerar stubs: `mvn clean install`

## 📊 Arquitectura Tarea 3

```
┌─────────────────┐
│  Navegador Web  │
│   (Móvil/PC)    │
└────────┬────────┘
         │ HTTP
         ▼
┌─────────────────┐
│   Mobile Web    │  Puerto 8080
│  (Tomcat 10.1)  │  
│   WAR: JSP +    │
│    Servlets     │
└────────┬────────┘
         │ SOAP/XML
         │ (client-ws stubs)
         ▼
┌─────────────────┐
│ Servidor Central│  Puerto 8081
│  (Tomcat 10.1)  │
│   JAX-WS Metro  │
│  AuthService    │
│  ActividadesService
└────────┬────────┘
         │ JPA/JDBC
         ▼
┌─────────────────┐
│  MySQL Database │
│  (turismouy)    │
└─────────────────┘
```

## ✅ Checklist Tarea 3

- [x] Servidor Central expone servicios SOAP (JAX-WS)
- [x] AuthService con validación real contra BD
- [x] ActividadesService con consultas reales
- [x] Cliente SOAP (stubs generados con wsimport)
- [x] Dispositivo Móvil con RWD (Bootstrap 5)
- [x] Caso de uso: Inicio de Sesión
- [x] Caso de uso: Cierre de Sesión
- [x] Caso de uso: Consulta de Actividad Turística
- [x] Consulta de Salidas dentro de Actividad
- [x] Sin uso directo de JAR Laboratorio1 en clientes
- [x] Arquitectura distribuida funcional
- [x] Interoperabilidad vía estándares (SOAP/WSDL)

## 🔍 Troubleshooting

### Error: "Connection refused" al consumir SOAP
- Verificar que Tomcat Central (8081) esté ejecutándose
- Probar WSDLs manualmente en navegador
- Revisar firewall/puertos

### Error: "ClassNotFoundException" en mobile-web
- Verificar que client-ws esté instalado: `mvn install` en client-ws
- Limpiar y recompilar mobile-web: `mvn clean package`

### Diseño no responsive
- Verificar que los archivos JSP incluyan Bootstrap CSS/JS
- Probar en Chrome DevTools con diferentes dispositivos
- Revisar consola del navegador para errores

### Login falla con "Usuario no encontrado"
- Verificar datos en la base de datos
- Revisar logs del Servidor Central (Tomcat 8081)
- Verificar que la contraseña esté encriptada correctamente

## 📚 Tecnologías Utilizadas

- **Java 17**
- **Jakarta EE 9** (Servlet 5.0, JSP 3.0)
- **JAX-WS / Metro 4.0.2** (SOAP)
- **Tomcat 10.1**
- **Bootstrap 5.3** (Responsive Design)
- **JSTL 2.0**
- **JPA / EclipseLink 4.0**
- **MySQL 8**
- **Maven 3.9**

---

**Última actualización**: Noviembre 2025  
**Autor**: Equipo PaP  
**Curso**: Programación de Aplicaciones 2025
