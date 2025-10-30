# Laboratorio 2 PaP - Sistema de Inscripciones

## Descripción
Sistema web de inscripciones desarrollado con Java EE/Jakarta EE, desplegado en Apache Tomcat 10.1 con integración a base de datos actualizada.

## Tecnologías Utilizadas
- **Java 11+**
- **Apache Tomcat 10.1**
- **Jakarta EE** (Servlets, JSP)
- **Maven** para gestión de dependencias
- **MySQL** (Railway) para base de datos
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
# Compilar y empaquetar (sin tests)
mvn -DskipTests package -f Laboratorio2PaP-main/pom.xml

# Desplegar con script (recomendado)
./deploy-tomcat10.sh

# Alternativa: copiar WAR manualmente
cp Laboratorio2PaP-main/target/Laboratorio2PaP.war apache-tomcat-10.1.47/webapps/
```

### Iniciar Aplicación
Tomcat se inicia/recicla automáticamente con el script de despliegue. Para ver logs:

```bash
tail -f apache-tomcat-10.1.47/logs/catalina.out
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
El sistema usa MySQL (Railway). Entidades principales y tablas relacionadas:

- `usuarios`
- `turista`
- `proveedores`
- `actividad`
- `salida`
- `inscripcion`

## Configuración
Variables de entorno requeridas (no commitear credenciales reales):

```
# Conexión directa JDBC (preferida)
DB_URL=jdbc:mysql://<host>.proxy.rlwy.net:<port>/railway?serverTimezone=UTC&allowPublicKeyRetrieval=true&useSSL=true
DB_USER=<usuario>
DB_PASSWORD=<password>

# Opcional: tamaño de pool / timeouts
DB_MAX_POOL=20
DB_LOGIN_TIMEOUT=30

# Opcional SOLO para inicializar esquema (ver siguiente sección)
# Valores posibles: none | create-tables | drop-and-create-tables | create-or-extend-tables
DB_DDL=create-or-extend-tables
```

Si no se define `DB_URL`, la app intentará usar un DataSource JNDI (`jdbc/railway`) de Tomcat si existe.

### Inicialización de esquema (DDL)
Cuando la base está vacía (p. ej., un nuevo Railway), se puede pedir a EclipseLink que cree las tablas automáticamente:

1) Definir temporalmente `DB_DDL=create-or-extend-tables` en el entorno.
2) Desplegar la app y revisar `catalina.out`. Verás mensajes `ddl:` y posibles advertencias de claves foráneas duplicadas si ya existen.
3) Una vez creado el esquema, QUITAR `DB_DDL` (o poner `none`) para evitar que en cada arranque se reintente alterar tablas y se llenen los logs.

Nota: repetir el arranque con `DB_DDL` activo puede generar advertencias tipo "Duplicate foreign key constraint name". Es esperado si el esquema ya está creado.

## Desarrollo
Para desarrollo local usa el script de despliegue (incluye build y redeploy en Tomcat 10):

```bash
./deploy-tomcat10.sh
```

## Logs y Monitoreo
```bash
# Ver logs de aplicación
tail -f apache-tomcat-10.1.47/logs/catalina.out

# Ver logs específicos
tail -f apache-tomcat-10.1.47/logs/localhost.*.log

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