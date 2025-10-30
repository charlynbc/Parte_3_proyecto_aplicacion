# Laboratorio 2 PaP - Sistema de Inscripciones

## Descripción
Sistema web de inscripciones desarrollado con Java EE/Jakarta EE, desplegado en Apache Tomcat 10.1 con integración a base de datos actualizada.

## Tecnologías Utilizadas
- **Java 11+**
- **Apache Tomcat 10.1**
- **Jakarta EE** (Servlets, JSP)
- **Maven** para gestión de dependencias
- **MySQL/PostgreSQL** para base de datos
- **HTML/CSS/JavaScript** para frontend

## Estructura del Proyecto
```
Laboratorio2PaP-main/
├── src/main/java/
│   ├── servlets/
│   │   ├── LoginServlet.java
│   │   ├── InscripcionServlet.java
│   │   ├── ActivitiesServlet.java
│   │   └── LogoutServlet.java
│   ├── models/
│   ├── utils/
│   └── filters/
├── src/main/webapp/
│   ├── WEB-INF/
│   │   ├── jsp/
│   │   └── web.xml
│   ├── css/
│   └── js/
└── target/
    └── Laboratorio2PaP.war
```

## Funcionalidades
- ✅ Sistema de autenticación de usuarios
- ✅ Gestión de actividades
- ✅ Proceso de inscripciones
- ✅ Dashboard de usuario
- ✅ Panel de administración
- ✅ Validaciones de negocio
- ✅ Manejo de sesiones

## Compilación y Despliegue

### Prerrequisitos
- Java 11 o superior
- Maven 3.6+
- Apache Tomcat 10.1
- Base de datos configurada

### Comandos de Compilación
```bash
# Compilar proyecto
mvn clean compile

# Generar WAR
mvn package -DskipTests

# Desplegar en Tomcat
cp target/Laboratorio2PaP.war /opt/tomcat/webapps/
```

### Iniciar Aplicación
```bash
# Iniciar Tomcat
/opt/tomcat/bin/startup.sh

# Verificar despliegue
tail -f /opt/tomcat/logs/catalina.out
```

## Acceso a la Aplicación
🌐 **URL Principal:** http://localhost:8080/Laboratorio2PaP

### Rutas Disponibles
- `/` - Página principal
- `/login` - Autenticación de usuarios
- `/dashboard` - Panel de usuario
- `/actividades` - Lista de actividades
- `/inscripcion` - Proceso de inscripción
- `/admin` - Panel de administración

## Base de Datos
El sistema está configurado para trabajar con la nueva estructura de base de datos que incluye:

### Tablas Principales
- `usuarios` - Información de usuarios
- `actividades` - Catálogo de actividades
- `inscripciones` - Registro de inscripciones
- `categorias` - Categorías de actividades

## Configuración
Variables de entorno requeridas:
```
DB_URL=jdbc:mysql://autorack.proxy.rlwy.net:13046/railway?serverTimezone=UTC&autoReconnect=true&allowPublicKeyRetrieval=true&useSSL=true
DB_USER=root
DB_PASSWORD=OqlJKLJHzKixCFCeqgVSKjNJzpAXspeQ
DB_MAX_POOL=20
DB_LOGIN_TIMEOUT=30
```

## Desarrollo
Para desarrollo local:

```bash
# Modo desarrollo con hot-reload
mvn tomcat7:run

# O usar el script de despliegue
./deploy-tomcat10.sh
```

## Logs y Monitoreo
```bash
# Ver logs de aplicación
tail -f /opt/tomcat/logs/catalina.out

# Ver logs específicos
tail -f /opt/tomcat/logs/localhost.*.log

# Verificar estado
ps aux | grep tomcat
netstat -tln | grep :8080
```

## Solución de Problemas

### Errores Comunes
1. **Puerto 8080 ocupado**: Verificar otros procesos
2. **Error de despliegue**: Revisar logs de Tomcat
3. **Conexión DB**: Verificar configuración de base de datos

### Comandos Útiles
```bash
# Reiniciar Tomcat
/opt/tomcat/bin/shutdown.sh && sleep 5 && /opt/tomcat/bin/startup.sh

# Limpiar caché
rm -rf /opt/tomcat/work/Catalina/localhost/Laboratorio2PaP

# Verificar versión
/opt/tomcat/bin/version.sh
```

## Contribución
1. Fork del repositorio
2. Crear rama feature (`git checkout -b feature/nueva-funcionalidad`)
3. Commit cambios (`git commit -am 'Agregar nueva funcionalidad'`)
4. Push a la rama (`git push origin feature/nueva-funcionalidad`)
5. Crear Pull Request

## Licencia
Proyecto académico - Laboratorio 2 PaP

## Contacto
Para soporte técnico, revisar los logs o crear un issue en el repositorio.

---
**Última actualización:** Proyecto actualizado para nueva base de datos con Tomcat 10.1