# Preguntas y Respuestas - Tarea 3 Programación de Aplicaciones

## Información General
- **Curso**: Programación de Aplicaciones 2025
- **Tarea**: 3
- **Estudiante**: [Tu Nombre]
- **Fecha de Entrega**: 12 de noviembre 2025
- **Objetivos**: Interoperabilidad vía Web Services, Diseño Responsive

## Arquitectura Implementada
La aplicación sigue una arquitectura distribuida con tres componentes principales:
- **Servidor Central**: Centraliza la lógica de negocio y datos, expone servicios SOAP.
- **Servidor Web**: Interfaz web que consume los servicios SOAP del servidor central.
- **Cliente Web Responsive**: Interfaz adaptable a dispositivos móviles usando diseño responsive.

## Estructura del Proyecto y Tecnologías Detalladas

### Carpetas Principales
- **`Laboratorio3Pap-main/`**: Contiene el servidor central SOAP.
  - **`central-ws/`**: Proyecto Maven (WAR) que expone servicios JAX-WS (AuthService, ActividadesService).
    - **Tecnologías**: Java 17, Jakarta EE 10, JAX-WS, JPA (EclipseLink), MySQL.
    - **Archivos clave**:
      - `src/main/java/uy/edu/pa/central/ws/AuthServiceImpl.java`: Implementa login/logout vía SOAP.
      - `src/main/java/uy/edu/pa/central/ws/ActividadesServiceImpl.java`: Maneja consultas de actividades.
      - `src/main/java/uy/edu/pa/central/persistence/JpaUtil.java`: Utilidad para EntityManager (conecta a BD).
      - `src/main/resources/META-INF/persistence.xml`: Configuración JPA (unidad "emf", MySQL).
      - `pom.xml`: Dependencias JAX-WS, EclipseLink, MySQL driver.
  - **`client-ws/`**: Proyecto Maven (JAR) que genera stubs SOAP desde el WSDL del central.
    - **Tecnologías**: Maven plugin jaxws-maven-plugin para generar clases cliente.
    - **Archivos clave**: `pom.xml` con configuración de wsimport.

- **`Laboratorio2PaP-main/`**: Aplicación web que consume SOAP.
  - **Tecnologías**: Java 17, Jakarta EE 10, JSP/JSTL, Servlets, CSS responsive.
  - **Estructura**:
    - `src/main/java/servlets/`: Servlets como LoginServlet.java (maneja login vía SOAP), LogoutServlet.java (invalida sesión).
    - `src/main/java/logica/`: Clases de negocio (Usuario, Turista, Proveedor).
    - `src/main/java/persistence/JpaUtil.java`: Stub (retorna null) ya que BD está en central.
    - `src/main/webapp/WEB-INF/`: JSPs (login.jsp, dashboard.jsp, etc.).
    - `src/main/webapp/css/`: Hojas de estilo responsive (styles.css, navbar.css, etc.).
    - `src/main/webapp/js/`: Scripts JavaScript (si los hay).
    - `pom.xml`: Dependencias client-ws (stubs), JSTL, Jakarta EE.

- **`apache-tomcat-10.1.47/`**: Instancia Tomcat para el servidor web (puerto 8080).
  - `webapps/Laboratorio2PaP/`: Despliegue del WAR web.
  - `conf/server.xml`: Configurado para puerto 8080.

- **`apache-tomcat-10.1.47-central/`**: Instancia Tomcat para el servidor central (puerto 9128).
  - `webapps/central-ws/`: Despliegue del WAR SOAP.
  - `conf/server.xml`: Configurado para puerto 9128.

- **`Laboratorio1PaP-main/`**: Lógica de negocio compartida (JAR).
  - `src/`: Clases como Usuario, Actividad, etc.
  - `pom.xml`: Proyecto simple Java.

- **Archivos de Configuración y Guías**:
  - `ECLIPSE-MAC-M1-GUIA.md`: Guía específica para Mac M1.
  - `setup-mac-m1.sh`: Script para configurar Java, Maven, Tomcat en Mac.
  - `Preguntas_y_Respuestas.md`: Este documento.

### Tecnologías por Componente
- **Lenguaje**: Java 17 (ARM64 en Mac M1).
- **Framework Web**: Jakarta EE 10 (Servlets 6.0, JSP 3.1, JSTL 3.0).
- **Web Services**: JAX-WS (SOAP) para interoperabilidad.
- **Persistencia**: JPA 3.1 con EclipseLink, BD MySQL.
- **Build**: Maven 3.9 (gestión dependencias, plugins para WAR/JAR).
- **Servidor**: Apache Tomcat 10.1.47 (compatible Jakarta).
- **Frontend**: JSP con JSTL, CSS3 responsive (media queries, flexbox).
- **Control de Versiones**: Git (rama para_mac para Mac M1).

### Configuraciones Importantes
- **JAVA_HOME**: `/Library/Java/JavaVirtualMachines/temurin-17.jdk/Contents/Home` en Mac.
- **Persistencia**: Unidad JPA "emf" en persistence.xml, conecta a MySQL local.
- **Tomcat**: Dos instancias separadas para evitar conflictos.
- **Sesiones**: HttpSession en servlets, invalidada en logout.
- **Responsive**: CSS custom con breakpoints para móvil (<768px), tablet, desktop.

## 1. ¿Por qué se implementó una arquitectura distribuida con Web Services?
- **Interoperabilidad**: Los Web Services permiten comunicación entre aplicaciones heterogéneas (Java, .NET, etc.) sin depender de tecnologías específicas.
- **Escalabilidad**: Separa responsabilidades: el servidor central maneja datos y lógica, el web maneja la presentación.
- **Mantenibilidad**: Cambios en un componente no afectan a los otros.
- **Estándares**: SOAP se basa en XML y HTTP, ampliamente soportado.

## 2. ¿Cómo se implementó la comunicación vía Web Services?
- **Servidor Central**: Usa JAX-WS para exponer servicios SOAP (AuthService y ActividadesService).
- **Cliente**: Maven genera stubs automáticamente desde el WSDL del servidor central.
- **Protocolo**: SOAP sobre HTTP, puerto 9128 para central.
- **Ventaja**: No se usan .jar locales; todo se consume vía servicios remotos.

## 3. ¿Por qué se eligió SOAP en lugar de REST?
- **Requisito del curso**: La tarea especifica Web Services, que típicamente son SOAP.
- **Tipos de datos complejos**: SOAP maneja mejor objetos complejos con XML schema.
- **Seguridad**: WS-Security para autenticación avanzada (aunque no implementado aquí).
- **Herramientas**: JAX-WS genera contratos automáticamente.

## 4. ¿Cómo se implementó el inicio de sesión (Login)?
- **Lógica**: El servlet `LoginServlet` recibe credenciales, las envía vía SOAP al `AuthService.login()`.
- **Validación**: El servidor central consulta la base de datos JPA y retorna un `UserDTO`.
- **Sesión**: Si válido, crea `HttpSession` con atributos: username, nombre, email, tipoUsuario.
- **Redirección**: A dashboard si ok, o muestra error.

## 5. ¿Cómo se implementó el cierre de sesión (Logout)?
- **Lógica**: `LogoutServlet` invalida la `HttpSession` actual.
- **Seguridad**: Elimina todos los atributos de sesión.
- **Redirección**: A página de login.
- **Razón**: Previene acceso no autorizado después de logout.

## 6. ¿Cómo se maneja la base de datos?
- **Tecnología**: JPA con EclipseLink, conectado a MySQL (driver incluido).
- **Configuración**: `persistence.xml` define la unidad de persistencia.
- **Acceso**: `JpaUtil.getEntityManager()` crea EntityManagers.
- **Entidades**: `Usuario`, `Turista`, `Proveedor`, etc. (herencia JPA).
- **Nota**: En esta iteración, el servidor web no accede directamente a BD; todo vía SOAP.

## 7. ¿Por qué se usó JPA en lugar de JDBC directo?
- **Productividad**: Abstracción de SQL, mapeo objeto-relacional automático.
- **Mantenibilidad**: Cambios en esquema se reflejan en entidades.
- **Portabilidad**: Funciona con diferentes BD (MySQL, PostgreSQL).
- **Consultas**: JPQL para queries type-safe.

## 8. ¿Cómo se implementó el diseño responsive?
- **Framework**: Se usó CSS personalizado en lugar de Bootstrap (aunque el requerimiento lo sugiere).
- **Técnicas**: Media queries, flexbox, grid layout.
- **Viewport**: `<meta name="viewport" content="width=device-width, initial-scale=1.0">`.
- **Componentes**: Navbar responsive, forms adaptables, cards móviles.
- **Razón**: Control total sobre el diseño, pero Bootstrap sería más rápido para prototipos.

## 9. ¿Por qué no se usó Bootstrap como indica la tarea?
- **Decisión de diseño**: Se prefirió CSS personalizado para tener control total sobre el diseño y aprender responsive design desde cero, sin depender de frameworks predefinidos.
- **Ventajas**: 
  - Menos dependencias externas, lo que reduce el tamaño del proyecto.
  - Diseño único y adaptado específicamente a las necesidades de Turismo.uy.
  - Mayor flexibilidad para personalizaciones avanzadas.
- **Desventajas**: 
  - Mayor tiempo de desarrollo, ya que requiere escribir CSS manualmente.
  - Posible inconsistencia si no se maneja bien el responsive.
- **Alternativa**: Bootstrap se podría integrar fácilmente agregando enlaces CDN en los JSP, pero se optó por no usarlo para demostrar habilidades en CSS puro.
- **Implementación actual**: Se usaron media queries, flexbox y grid CSS para lograr responsividad en móviles, tablets y desktop.

## 10. ¿Cómo se estructuró el proyecto Maven?
- **Módulos**:
  - `central-ws`: WAR con servicios SOAP.
  - `client-ws`: JAR con stubs generados.
  - `Laboratorio2PaP`: WAR web que consume SOAP.
- **Dependencias**: Jakarta EE 10, JSTL, EclipseLink, MySQL driver.
- **Plugins**: Compiler, WAR, Tomcat7-Maven-Plugin.

## 11. ¿Cómo se desplegó en Tomcat?
- **Dos instancias**: Una para central (puerto 9128), otra para web (puerto 8080).
- **Configuración**: context.xml para BD, web.xml para servlets.
- **Separación**: Central no tiene interfaz web, solo servicios.

## 12. ¿Qué casos de uso se implementaron?
- **Inicio de Sesión**: SOAP + sesión HTTP.
- **Cierre de Sesión**: Invalidar sesión.
- **Consulta de Actividad Turística**: SOAP para obtener datos.

## 13. ¿Cómo se manejaron los errores?
- **Excepciones**: Try-catch en servlets, mensajes de error en JSP.
- **Validación**: Client-side (HTML5) y server-side.
- **Logging**: Console logs en Tomcat.

## 14. ¿Qué tecnologías se usaron y por qué?
- **Java 17**: LTS, compatible con Jakarta EE.
- **Jakarta EE**: Estándar moderno para web.
- **Maven**: Gestión de dependencias y build.
- **Tomcat 10**: Servidor compatible con Jakarta.
- **JSP/JSTL**: Templates del lado servidor.

## 15. ¿Cómo se probó la aplicación?
- **Compilación**: `mvn clean install` en cada módulo.
- **Despliegue**: Iniciar Tomcat central, luego web.
- **Funcionalidad**: Login/logout, consultas vía web.
- **Integración**: Verificar WSDL accesible.

## 16. ¿Qué desafíos se enfrentaron?
- **Configuración Mac M1**: Java 17 ARM64, paths específicos.
- **Generación de stubs**: Asegurar WSDL accesible antes de compilar cliente.
- **Sesiones**: Manejo correcto de HttpSession.
- **Responsive**: CSS media queries para móviles.

## 17. ¿Cómo se asegura la seguridad?
- **Sesiones**: HttpSession con timeout.
- **Validación**: Input sanitization.
- **Contraseñas**: Comparación directa (en producción: hash + salt).
- **SOAP**: Sin encriptación adicional.

## 18. ¿Qué se entregaría al profesor?
- **Código fuente**: Repositorio GitHub.
- **WARs**: central-ws.war, Laboratorio2PaP.war.
- **Documentación**: Este README, guías de instalación.
- **Demostración**: Aplicación corriendo en Tomcat.

## 19. ¿Qué se implementó en Tarea 3 respecto a las entregas anteriores (Tarea 1 y Tarea 2)?

### Comparación con Tarea 1 (Estación de Trabajo Swing + Servidor Central Básico)
- **Tarea 1**: Solo interfaz Swing para administrador, servidor central con BD HSQLDB, casos de uso básicos (alta/consulta usuarios, actividades, salidas, inscripciones). Arquitectura monolítica en un nodo.
- **Cambios en Tarea 3**:
  - Se mantiene el servidor central, pero ahora expone servicios SOAP (JAX-WS) en lugar de ser usado como JAR local.
  - BD cambia a MySQL (en lugar de HSQLDB).
  - Se agrega distribución: servidor central separado (puerto 9128), servidor web separado (puerto 8080).
  - Interfaz web responsive en lugar de solo Swing.
  - Web Services para interoperabilidad entre componentes.

### Comparación con Tarea 2 (Aplicación Web + Estación de Trabajo)
- **Tarea 2**: Agrega aplicación web con JSP/Servlets, login/logout web, casos de uso web (alta usuario, consulta, etc.). Servidor central como JAR en web y Swing. BD persistida. Estados en actividades (Agregada/Confirmada/Rechazada).
- **Cambios en Tarea 3**:
  - **Distribución**: Componentes ahora independientes y comunicados vía SOAP (no JAR compartido).
  - **Web Services Puros**: No se usan .jar del servidor central; todo consumo remoto vía WSDL.
  - **Responsive Design**: Interfaz web adaptable a móviles (RWD), aunque se usó CSS custom en lugar de Bootstrap.
  - **Actores**: Mantiene Visitante/Turista/Proveedor, pero enfocado en web móvil.
  - **Casos de Uso**: Simplificados a login/logout, consulta actividades (sin modificaciones ni altas web).
  - **Tecnologías**: JAX-WS para SOAP, stubs generados automáticamente.

### Lo Nuevo en Tarea 3
- Arquitectura distribuida real (3 nodos: central SOAP, web consumidor, móvil responsive).
- Interoperabilidad vía estándares (SOAP/XML/HTTP).
- Diseño responsive para dispositivos móviles.
- Comunicación remota pura (sin dependencias locales).

## 20. Explicación Detallada de Cada Requerimiento de Tarea 3

### Objetivos
- **Interoperabilidad de aplicaciones distribuidas vía Web Services**: Lograr que central y web se comuniquen sin acoplamiento.
- **Diseño Responsive**: Un diseño web que se adapte a móviles/tablets/desktop.

### Introducción
- Continúa desarrollo de Servidor Central y Servidor Web.
- Agrega Dispositivo Móvil como web responsive (no app nativa).
- Comunicación vía Web Services entre Web y Central (Figura 1 del enunciado).

### Visión
- Comunicación Web Services entre Web y Central.
- Incorpora RWD para casos de uso en dispositivos móviles.

### Actores
- **Administrador**: Solo Estación de Trabajo (Swing), no web.
- **Visitante**: Web sin login.
- **Proveedor/Turista**: Web con login.

### Requerimientos Funcionales
#### Dispositivo Móvil
- **Inicio de Sesión**: Login web con nickname/email + password, verifica vía SOAP, inicia sesión HttpSession.
- **Cierre de Sesión**: Logout invalida HttpSession, redirige a login.
- **Consulta de Actividad Turística**: Lista actividades confirmadas, muestra detalles + salidas vía SOAP.

### Requerimientos Especiales
#### Diseño del Dispositivo Móvil
- RWD: Un diseño web adaptable a cualquier dispositivo.
- Framework: Bootstrap o similar (se usó CSS custom con media queries).

### Se Pide
- **Interfaz Web para Dispositivo Móvil**: Desarrollo con HTML5/CSS3/JS, JSP/Servlets.
- **Manejo puro de Web Services**: Sin .jar, solo consumo remoto SOAP para todos los casos de uso de Tarea 2.
- **Implementación**: Login/logout, consultas responsive.

## 21. ¿Se puede agregar Swing a este proyecto?
Sí, se puede agregar una interfaz Swing para el Administrador del Sistema, convirtiéndola en un cliente que consume los servicios SOAP del Servidor Central, en lugar de usar el JAR local como en tareas anteriores.

### ¿Hace lo de la letra 1 y 2?
- **Tarea 1**: Swing básico con CRUD (alta/consulta usuarios, actividades, salidas, inscripciones) usando lógica local.
- **Tarea 2**: Swing mantiene admin functions + agrega aceptar/rechazar actividades (estado).
- **Swing en Tarea 3**: Puede hacer lo mismo, pero consumiendo SOAP remoto. Actualmente tiene login básico; se puede expandir a full admin (menús, internal frames, CRUD vía SOAP).

### Implementación Sugerida
- **Cliente Swing SOAP**: Aplicación Java con interfaz gráfica que llama a `AuthService` y `ActividadesService` vía stubs generados. Para full admin, agregar más servicios SOAP (ej. UserService, DepartureService).
- **Ventajas**: Mantiene la distribución (admin no necesita BD local), interoperabilidad.
- **Archivos a Agregar**:
  - Nuevo módulo Maven: `desktop/` con dependencias `client-ws`, Swing.
  - Clases: `AdminLoginFrame.java` (login SOAP), `AdminDashboard.java` (consultas/altas vía SOAP), menús con JInternalFrames.
- **Ejecución**: Corre como app standalone, conecta al puerto 9128 del central.
- **Razón**: Cumple con la arquitectura distribuida de Tarea 3, donde todos los clientes consumen servicios remotos.

Si se implementa, actualizaría el diagrama a 4 nodos: Central SOAP, Web, Móvil, Admin Swing.</content>
<parameter name="filePath">/workspaces/Parte_3_proyecto_aplicacion/Preguntas_y_Respuestas.md