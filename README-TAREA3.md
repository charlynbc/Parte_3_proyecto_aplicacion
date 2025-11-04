# Turismo.uy - Tarea 3: Arquitectura Distribuida con Web Services

## 📋 Descripción del Proyecto

Este proyecto implementa una plataforma de turismo para Uruguay utilizando una **arquitectura distribuida basada en Web Services**, cumpliendo con los requisitos de la Tarea 3 de Programación de Aplicaciones. La solución separa la lógica de negocio del servidor web mediante servicios SOAP, implementando además un diseño responsive para dispositivos móviles.

## 🏗️ Arquitectura Distribuida

### Componentes Principales

```
┌─────────────────────┐    Web Services     ┌─────────────────────┐
│   Servidor Web      │◄──────SOAP──────────►│  Servidor Central   │
│   (Puerto 8080)     │                      │   (Puerto 9128)     │
│                     │                      │                     │
│ • Interfaz Usuario  │                      │ • Lógica Negocio    │
│ • Servlets/JSP      │                      │ • JPA/Hibernate     │
│ • Bootstrap UI      │                      │ • Base de Datos     │
│ • Cliente WS        │                      │ • Servicios SOAP    │
└─────────────────────┘                      └─────────────────────┘
```

### 1. Servidor Central (Puerto 9128)
**Responsabilidades:**
- **Lógica de Negocio**: Manejo de usuarios, actividades turísticas y reservas
- **Persistencia de Datos**: JPA/Hibernate con base de datos MySQL
- **Web Services SOAP**: Publicación de servicios para comunicación externa
- **Controladores**: IControladorUsuario, IControladorActividad

**Servicios Web Publicados:**
```java
@WebService
public class WSUsuario {
    @WebMethod
    public boolean existeUsuario(String nickname);
    
    @WebMethod 
    public DataUsuario obtenerUsuario(String nickname);
    
    @WebMethod
    public DataUsuario obtenerUsuarioPorEmail(String email);
    
    @WebMethod
    public String obtenerTipoUsuario(String nickname);
    
    @WebMethod
    public boolean verificarCredenciales(String nickname, String password);
}
```

### 2. Servidor Web (Puerto 8080)
**Responsabilidades:**
- **Interfaz de Usuario**: JSP con diseño responsive usando Bootstrap 5.3.2
- **Servlets HTTP**: Manejo de requests/responses y sesiones web
- **Cliente Web Services**: Consumo de servicios del servidor central
- **Sin Acceso Directo a BD**: Toda comunicación via Web Services

**Servlets Principales:**
- `LoginServlet`: Autenticación distribuida via Web Services
- `RegisterServlet`: Registro de usuarios (Turista/Proveedor)
- `DashboardServlet`: Panel principal responsive
- `ProfileServlet`: Gestión de perfiles de usuario

## 🔧 Tecnologías Implementadas

### Backend
- **Java 11**: Plataforma base de desarrollo
- **Maven 3.8+**: Gestión de dependencias y construcción
- **Jakarta EE 9**: Especificaciones empresariales
- **JAX-WS**: Framework para Web Services SOAP
- **JPA 2.2 / Hibernate**: Persistencia de datos
- **Apache Tomcat 10.1**: Servidor de aplicaciones web

### Frontend Responsive
- **Bootstrap 5.3.2**: Framework CSS responsive mobile-first
- **Bootstrap Icons**: Iconografía moderna
- **JSP/Servlets**: Tecnología de presentación
- **CSS Grid + Flexbox**: Layouts responsive avanzados
- **Media Queries**: Breakpoints para diferentes dispositivos

### Base de Datos
- **MySQL 8.0**: Sistema de gestión de base de datos relacional
- **Modelo de Datos**: Usuarios (Turista/Proveedor), Actividades, Reservas

## 🚀 Diseño Responsive Implementado

### Características Mobile-First
1. **Navegación Adaptable**: Navbar colapsable en dispositivos móviles
2. **Grid Responsive**: Sistema de 12 columnas de Bootstrap con breakpoints
3. **Componentes Móviles**: Cards, formularios y botones optimizados para touch
4. **Tipografía Escalable**: Tamaños relativos y espaciado adaptativo
5. **Imágenes Flexibles**: Contenido multimedia que se ajusta al viewport

### Breakpoints Implementados
```css
/* Extra Small (xs) - Móviles */
@media (max-width: 575.98px) { ... }

/* Small (sm) - Móviles landscape */
@media (min-width: 576px) { ... }

/* Medium (md) - Tablets */
@media (min-width: 768px) { ... }

/* Large (lg) - Laptops */
@media (min-width: 992px) { ... }

/* Extra Large (xl) - Desktops */
@media (min-width: 1200px) { ... }
```

### Páginas Responsive Creadas
- ✅ `login-responsive.jsp` - Login adaptativo con validación
- ✅ `dashboard-responsive.jsp` - Panel principal con grid responsive
- ✅ `activities-responsive.jsp` - Lista de actividades con cards responsive
- ✅ `profile-responsive.jsp` - Perfil de usuario mobile-friendly
- ✅ `register-tourist-responsive.jsp` - Registro de turista adaptativo
- ✅ `register-provider-responsive.jsp` - Registro de proveedor responsive

## 🔄 Comunicación Web Services

### Flujo de Autenticación
```
1. Usuario → Login Form (servidor-web)
2. LoginServlet → WSUsuarioClient
3. WSUsuarioClient → SOAP Request (servidor-central:9128)
4. WSUsuario → Verificación en BD
5. Respuesta SOAP → WSUsuarioClient
6. Sesión HTTP creada → Redirect Dashboard
```

### Ventajas de la Arquitectura Distribuida
- **Separación de Responsabilidades**: UI separada de lógica de negocio
- **Escalabilidad**: Servidores independientes pueden escalarse por separado
- **Mantenibilidad**: Cambios en lógica no afectan la interfaz
- **Reutilización**: Web Services pueden ser consumidos por múltiples clientes
- **Interoperabilidad**: SOAP permite integración con diferentes tecnologías

## 📱 Funcionalidades Responsive

### Dashboard Móvil
- **Navegación por pestañas**: Fácil acceso en pantallas pequeñas
- **Cards apilables**: Información organizada en tarjetas responsivas
- **Botones táctiles**: Tamaño optimizado para dedos (44px mínimo)
- **Menús contextuales**: Dropdowns adaptados para móvil

### Formularios Adaptativos
- **Inputs flotantes**: Labels que se mueven dinámicamente
- **Validación en tiempo real**: Feedback inmediato al usuario
- **Teclado contextual**: type="email", type="tel" para móviles
- **Botones de acción primaria**: Destacados y fáciles de presionar

### Sistema de Grid Responsive
```html
<!-- Ejemplo de grid adaptativo -->
<div class="row">
    <div class="col-12 col-md-6 col-lg-4">
        <!-- 1 columna en móvil, 2 en tablet, 3 en desktop -->
    </div>
</div>
```

## 🔒 Seguridad Implementada

### Autenticación Distribuida
- **Sesiones HTTP**: Manejo seguro de sesiones en servidor web
- **Validación de Credenciales**: Via Web Services con encriptación
- **Control de Acceso**: Middleware de autenticación en servlets
- **Logout Seguro**: Invalidación completa de sesión

### Validación de Datos
- **Client-side**: JavaScript con Bootstrap validation
- **Server-side**: Validación en servlets antes de Web Services
- **Sanitización**: Prevención de XSS e inyección de código

## 📊 Casos de Uso Implementados

### Para Turistas
1. **Registro**: Formulario responsive con validación
2. **Login**: Autenticación distribuida via Web Services
3. **Dashboard**: Panel personalizado mobile-friendly
4. **Explorar Actividades**: Lista responsive con filtros
5. **Ver Perfil**: Información personal adaptativa

### Para Proveedores
1. **Registro Empresarial**: Formulario extendido responsive
2. **Gestión de Actividades**: CRUD responsive de actividades
3. **Dashboard Proveedor**: Panel de control móvil
4. **Estadísticas**: Visualización adaptativa de datos

## 🧪 Testing de Arquitectura Distribuida

### Scripts de Prueba Automatizados
- `test-distributed-architecture.sh`: Prueba completa de arquitectura
- `stop-servers.sh`: Parada ordenada de servicios
- `deploy-tomcat10.sh`: Despliegue automatizado

### Verificaciones Implementadas
1. **Compilación independiente** de ambos servidores
2. **Publicación de Web Services** en puerto 9128
3. **Despliegue en Tomcat** en puerto 8080
4. **Conectividad SOAP** entre servidores
5. **Funcionalidad end-to-end** de autenticación

## 📚 Patrones de Diseño Utilizados

### Arquitectónicos
- **Service-Oriented Architecture (SOA)**: Comunicación via Web Services
- **Model-View-Controller (MVC)**: Separación de capas en servidor web
- **Data Access Object (DAO)**: Abstracción de acceso a datos
- **Facade Pattern**: WSUsuarioClient como fachada de servicios

### Responsive Design
- **Mobile-First**: Diseño primero para móviles, luego desktop
- **Progressive Enhancement**: Funcionalidad básica en todos los dispositivos
- **Graceful Degradation**: Experiencia óptima en navegadores modernos
- **Component-Based**: Reutilización de componentes Bootstrap

## 🔧 Configuración Técnica

### Dependencias Maven - Servidor Central
```xml
<dependencies>
    <!-- Web Services -->
    <dependency>
        <groupId>jakarta.xml.ws</groupId>
        <artifactId>jakarta.xml.ws-api</artifactId>
        <version>3.0.1</version>
    </dependency>
    
    <!-- JPA/Hibernate -->
    <dependency>
        <groupId>org.hibernate</groupId>
        <artifactId>hibernate-core</artifactId>
        <version>5.6.15.Final</version>
    </dependency>
    
    <!-- MySQL Driver -->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>8.0.33</version>
    </dependency>
</dependencies>
```

### Dependencias Maven - Servidor Web
```xml
<dependencies>
    <!-- Servlets API -->
    <dependency>
        <groupId>jakarta.servlet</groupId>
        <artifactId>jakarta.servlet-api</artifactId>
        <version>5.0.0</version>
    </dependency>
    
    <!-- Web Services Client -->
    <dependency>
        <groupId>jakarta.xml.ws</groupId>
        <artifactId>jakarta.xml.ws-api</artifactId>
        <version>3.0.1</version>
    </dependency>
    
    <!-- JSTL -->
    <dependency>
        <groupId>jakarta.servlet.jsp.jstl</groupId>
        <artifactId>jakarta.servlet.jsp.jstl-api</artifactId>
        <version>2.0.0</version>
    </dependency>
</dependencies>
```

## 📋 Checklist de Implementación Tarea 3

### ✅ Requisitos Cumplidos
- [x] **Arquitectura Distribuida**: Separación servidor-web / servidor-central
- [x] **Web Services SOAP**: Comunicación entre servidores via JAX-WS
- [x] **Sin Dependencias JAR**: Eliminadas dependencias directas entre proyectos
- [x] **Responsive Web Design**: Bootstrap 5.3.2 con diseño mobile-first
- [x] **Funcionalidad Completa**: Login, registro, dashboard, perfil
- [x] **Base de Datos**: MySQL mantenida como solicitado
- [x] **Testing**: Scripts automatizados de prueba de arquitectura
- [x] **Documentación**: READMEs comprehensivos con setup e implementación

### 🔄 Flujo de Comunicación Implementado
```
Frontend (JSP) → Servlet → WSClient → SOAP → WSService → Controller → JPA → DB
     ↓              ↓         ↓         ↓         ↓          ↓        ↓
   Bootstrap    HTTP     Web Service  SOAP    Business   ORM    MySQL
  Responsive  Request    Client Call  Call     Logic    Layer  Database
```

## 🚀 Próximos Pasos
1. **Testing Extensivo**: Pruebas de carga y estrés en arquitectura distribuida
2. **Optimización**: Cacheable responses y connection pooling
3. **Monitoreo**: Logging distribuido y métricas de performance
4. **Seguridad Avanzada**: OAuth2 y JWT para APIs
5. **API REST**: Complementar SOAP con endpoints REST para móviles

---

**Desarrollado para Tarea 3 - Programación de Aplicaciones**  
*Arquitectura Distribuida con Web Services y Responsive Design*