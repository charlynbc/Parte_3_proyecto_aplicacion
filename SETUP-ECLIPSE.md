# 🛠️ Setup e Instalación - Turismo.uy Tarea 3

## 📋 Requisitos del Sistema

### Software Necesario
- **Java Development Kit (JDK) 11 o superior**
- **Apache Maven 3.8+**
- **Eclipse IDE 2023-06 o superior** (con Jakarta EE support)
- **Apache Tomcat 10.1.47** (incluido en el proyecto)
- **MySQL 8.0+** o compatible
- **Git** para control de versiones

### Verificación de Requisitos
```bash
# Verificar Java
java -version
# Esperado: openjdk version "11.0.x" o superior

# Verificar Maven
mvn -version
# Esperado: Apache Maven 3.8.x o superior

# Verificar MySQL
mysql --version
# Esperado: mysql Ver 8.0.x o superior
```

## 🔧 Configuración del Entorno de Desarrollo

### 1. Setup de Eclipse IDE

#### Instalación de Plugins Necesarios
1. **Jakarta EE Tools**:
   - `Help` → `Eclipse Marketplace`
   - Buscar "Jakarta EE" 
   - Instalar "Jakarta EE Developer Tools"

2. **Maven Integration**:
   - Generalmente incluido, verificar en `Help` → `About Eclipse IDE` → `Installation Details`

3. **Server Tools**:
   - `Help` → `Install New Software`
   - Usar update site de Eclipse para "Web, XML, Java EE and OSGi Enterprise Development"

#### Configuración de JDK en Eclipse
```
Window → Preferences → Java → Installed JREs
→ Add → Standard VM → Next
→ JRE home: [ruta a JDK 11]
→ JRE name: "JDK-11"
→ Finish → Apply and Close
```

#### Configuración de Maven en Eclipse
```
Window → Preferences → Maven
→ User Settings: [verificar archivo settings.xml]
→ Installations: [verificar instalación de Maven]
→ Apply and Close
```

### 2. Configuración de Tomcat en Eclipse

#### Agregar Tomcat Server
```
Window → Preferences → Server → Runtime Environments
→ Add → Apache Tomcat v10.0 → Next
→ Tomcat installation directory: [proyecto]/apache-tomcat-10.1.47
→ JRE: JDK-11
→ Finish → Apply and Close
```

#### Configurar Server en Eclipse
```
File → New → Other → Server → Server → Next
→ Apache Tomcat v10.0 Server → Next
→ Server name: "Tomcat 10.1 - Turismo.uy"
→ Finish
```

## 📁 Importación del Proyecto en Eclipse

### 1. Clonar el Repositorio
```bash
git clone [URL_DEL_REPOSITORIO]
cd Parte_3_proyecto_aplicacion
```

### 2. Importar Proyectos Maven

#### Servidor Central
```
File → Import → Existing Maven Projects
→ Root Directory: [proyecto]/parte 3/servidor-central
→ Select all projects → Finish
```

#### Servidor Web
```
File → Import → Existing Maven Projects  
→ Root Directory: [proyecto]/parte 3/servidor-web
→ Select all projects → Finish
```

### 3. Configuración de Build Path

#### Para ambos proyectos:
```
Right-click proyecto → Properties → Java Build Path
→ Libraries → Modulepath/Classpath → Add Library
→ Server Runtime → Apache Tomcat v10.0 → Finish
→ Apply and Close
```

## 🗄️ Configuración de Base de Datos

### 1. Crear Base de Datos MySQL
```sql
-- Conectar como root
mysql -u root -p

-- Crear base de datos
CREATE DATABASE turismo_uy_tarea3;

-- Crear usuario para la aplicación
CREATE USER 'turismo_user'@'localhost' IDENTIFIED BY 'turismo_pass';

-- Otorgar permisos
GRANT ALL PRIVILEGES ON turismo_uy_tarea3.* TO 'turismo_user'@'localhost';
FLUSH PRIVILEGES;

-- Usar la base de datos
USE turismo_uy_tarea3;
```

### 2. Configurar persistence.xml
Ubicación: `servidor-central/src/main/resources/META-INF/persistence.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<persistence xmlns="http://java.sun.com/xml/ns/persistence"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://java.sun.com/xml/ns/persistence 
             http://java.sun.com/xml/ns/persistence/persistence_2_0.xsd"
             version="2.0">
    
    <persistence-unit name="turismo-uy">
        <provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>
        
        <!-- Entidades -->
        <class>entities.Usuario</class>
        <class>entities.Turista</class>
        <class>entities.Proveedor</class>
        <class>entities.Actividad</class>
        
        <properties>
            <!-- Configuración de base de datos -->
            <property name="javax.persistence.jdbc.driver" value="com.mysql.cj.jdbc.Driver"/>
            <property name="javax.persistence.jdbc.url" value="jdbc:mysql://localhost:3306/turismo_uy_tarea3"/>
            <property name="javax.persistence.jdbc.user" value="turismo_user"/>
            <property name="javax.persistence.jdbc.password" value="turismo_pass"/>
            
            <!-- Configuración de Hibernate -->
            <property name="hibernate.dialect" value="org.hibernate.dialect.MySQL8Dialect"/>
            <property name="hibernate.hbm2ddl.auto" value="update"/>
            <property name="hibernate.show_sql" value="true"/>
            <property name="hibernate.format_sql" value="true"/>
        </properties>
    </persistence-unit>
</persistence>
```

## 🚀 Compilación y Despliegue

### 1. Compilación con Maven

#### Servidor Central
```bash
cd parte\ 3/servidor-central
mvn clean compile
mvn exec:java -Dexec.mainClass="main.PublicadorWS"
```

#### Servidor Web
```bash
cd parte\ 3/servidor-web  
mvn clean package
```

### 2. Despliegue Automático
```bash
# Desde la raíz del proyecto
chmod +x deploy-tomcat10.sh
./deploy-tomcat10.sh
```

### 3. Despliegue Manual en Eclipse

#### Servidor Central (Web Services)
```
Right-click servidor-central → Run As → Java Application
→ Main class: main.PublicadorWS
→ Run
```

#### Servidor Web (Tomcat)
```
Right-click servidor-web → Run As → Run on Server
→ Select Tomcat 10.1 server
→ Add proyecto to configured projects
→ Finish
```

## 🔍 Verificación de la Instalación

### 1. Verificar Web Services (Puerto 9128)
```bash
# Verificar que el servicio esté corriendo
curl http://localhost:9128/wsUsuario?wsdl

# Debe retornar el WSDL del servicio
```

### 2. Verificar Aplicación Web (Puerto 8080)
```bash
# Verificar aplicación web
curl http://localhost:8080/Laboratorio2PaP/

# O abrir en navegador
# http://localhost:8080/Laboratorio2PaP/login
```

### 3. Test de Arquitectura Distribuida
```bash
# Ejecutar script de testing
chmod +x test-distributed-architecture.sh
./test-distributed-architecture.sh
```

## 🐛 Troubleshooting Común

### Problema: Puerto 9128 ocupado
```bash
# Verificar qué proceso usa el puerto
netstat -tulpn | grep 9128

# Matar proceso si es necesario  
kill -9 [PID]
```

### Problema: Error de compilación Maven
```bash
# Limpiar e instalar dependencias
mvn clean install -U

# Refrescar proyecto en Eclipse
Right-click proyecto → Maven → Reload Projects
```

### Problema: Error de conexión a BD
```sql
-- Verificar conexión MySQL
mysql -u turismo_user -p turismo_uy_tarea3

-- Verificar permisos
SHOW GRANTS FOR 'turismo_user'@'localhost';
```

### Problema: ClassNotFoundException en Tomcat
```
# Verificar que el JAR esté en WEB-INF/lib
# Recompilar con:
mvn clean package

# Verificar deployment descriptor en web.xml
```

### Problema: Web Services no accesibles
```bash
# Verificar que PublicadorWS esté corriendo
ps aux | grep PublicadorWS

# Verificar logs en consola Eclipse
# Revisar firewall/antivirus
```

## 📝 Configuración de Eclipse para Desarrollo

### 1. Workspace Setup
```
File → Switch Workspace → Other
→ Browse to: [proyecto-root]/eclipse-workspace
→ OK
```

### 2. Configuración de Encoding
```
Window → Preferences → General → Workspace
→ Text file encoding: UTF-8
→ Apply and Close
```

### 3. Configuración de Maven
```
Window → Preferences → Maven
→ Download Artifact Sources: ✓
→ Download Artifact JavaDoc: ✓
→ Apply and Close
```

### 4. Server Configuration
```
Servers tab → New → Server
→ Apache Tomcat v10.0
→ Server name: Turismo.uy Tomcat
→ Next → Add servidor-web → Finish
```

## 🔄 Workflow de Desarrollo

### 1. Desarrollo de Web Services
```
1. Modificar código en servidor-central
2. Run main.PublicadorWS
3. Verificar WSDL en http://localhost:9128/wsUsuario?wsdl
4. Regenerar cliente si es necesario
```

### 2. Desarrollo Web Frontend
```  
1. Modificar JSP/Servlets en servidor-web
2. Maven → Update Project
3. Clean/Republish en Tomcat
4. Refresh browser en http://localhost:8080/Laboratorio2PaP/
```

### 3. Testing Integrado
```
1. Asegurar servidor-central corriendo (puerto 9128)
2. Desplegar servidor-web en Tomcat (puerto 8080)
3. Ejecutar test-distributed-architecture.sh
4. Verificar funcionalidad end-to-end
```

## 📚 Estructura de Proyectos en Eclipse

### Servidor Central
```
servidor-central/
├── src/main/java/
│   ├── controllers/        # Controladores de negocio
│   ├── datatypes/         # DTOs para Web Services  
│   ├── entities/          # Entidades JPA
│   ├── exceptions/        # Excepciones personalizadas
│   ├── main/             # PublicadorWS
│   └── webservices/      # Implementaciones SOAP
├── src/main/resources/
│   └── META-INF/
│       └── persistence.xml
└── pom.xml
```

### Servidor Web
```
servidor-web/
├── src/main/java/
│   ├── servlets/         # Servlets HTTP
│   ├── clients/          # Clientes Web Services
│   └── utils/           # Utilidades
├── src/main/webapp/
│   ├── WEB-INF/
│   │   ├── web.xml
│   │   └── *.jsp        # Páginas JSP responsive
│   └── static/          # CSS/JS/Imágenes
└── pom.xml
```

## ✅ Checklist de Instalación

- [ ] JDK 11+ instalado y configurado
- [ ] Maven 3.8+ funcionando
- [ ] Eclipse con Jakarta EE tools
- [ ] MySQL server corriendo
- [ ] Base de datos creada
- [ ] Usuario de BD configurado
- [ ] Proyectos importados en Eclipse
- [ ] Tomcat configurado en Eclipse
- [ ] Dependencias Maven resueltas
- [ ] Servidor central compilado
- [ ] Web Services publicados (puerto 9128)
- [ ] Aplicación web desplegada (puerto 8080)
- [ ] Tests de arquitectura pasando
- [ ] Login funcional end-to-end

---

**¡Tu entorno está listo para desarrollar Turismo.uy!** 🚀

Para soporte adicional, revisar logs en:
- Eclipse Console (para Web Services)
- Tomcat Server Log (para aplicación web)
- MySQL Error Log (para problemas de BD)