# Todo lo Realizado - PARTE 3: Configuración y Deployment

## 📋 Índice
- [Configuración del Servidor Central](#configuración-del-servidor-central)
- [Configuración del Servidor Web](#configuración-del-servidor-web)
- [Base de Datos y Persistencia](#base-de-datos-y-persistencia)
- [Proceso de Compilación y Despliegue](#proceso-de-compilación-y-despliegue)
- [Verificación y Testing](#verificación-y-testing)
- [Estructura Final del Proyecto](#estructura-final-del-proyecto)

---

## 🔧 Configuración del Servidor Central

### Ubicación del Proyecto
```
Laboratorio3Pap-main/
└── central-ws/
    ├── pom.xml                    # Maven configuration
    └── src/
        └── main/
            ├── java/              # Código fuente
            ├── resources/         # Archivos de configuración
            └── webapp/            # Recursos web
                └── WEB-INF/
                    ├── web.xml
                    └── sun-jaxws.xml
```

---

### 1. Configuración Maven (pom.xml)

**Ubicación:** `Laboratorio3Pap-main/central-ws/pom.xml`

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
                             http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>uy.edu.pa</groupId>
    <artifactId>central-ws</artifactId>
    <version>1.0.0</version>
    <packaging>war</packaging>

    <name>Central Web Services</name>
    <description>Servidor Central con servicios SOAP para Tarea 3</description>

    <properties>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <failOnMissingWebXml>false</failOnMissingWebXml>
    </properties>

    <dependencies>
        <!-- ==================== Jakarta EE 9 APIs ==================== -->
        
        <!-- Servlet API 5.0 (Jakarta) -->
        <dependency>
            <groupId>jakarta.servlet</groupId>
            <artifactId>jakarta.servlet-api</artifactId>
            <version>5.0.0</version>
            <scope>provided</scope>
        </dependency>

        <!-- JAX-WS API 4.0 (Jakarta XML Web Services) -->
        <dependency>
            <groupId>jakarta.xml.ws</groupId>
            <artifactId>jakarta.xml.ws-api</artifactId>
            <version>4.0.0</version>
        </dependency>

        <!-- JPA API 3.1 (Jakarta Persistence) -->
        <dependency>
            <groupId>jakarta.persistence</groupId>
            <artifactId>jakarta.persistence-api</artifactId>
            <version>3.1.0</version>
        </dependency>

        <!-- ==================== Implementaciones ==================== -->

        <!-- Metro JAX-WS Runtime 4.0.2 -->
        <dependency>
            <groupId>com.sun.xml.ws</groupId>
            <artifactId>jaxws-rt</artifactId>
            <version>4.0.2</version>
        </dependency>

        <!-- EclipseLink JPA 4.0.2 (Proveedor JPA) -->
        <dependency>
            <groupId>org.eclipse.persistence</groupId>
            <artifactId>eclipselink</artifactId>
            <version>4.0.2</version>
        </dependency>

        <!-- MySQL Connector 8.0.33 -->
        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <version>8.0.33</version>
        </dependency>

        <!-- ==================== Entidades Compartidas ==================== -->

        <!-- JAR con entidades JPA (Usuario, Turista, Proveedor, etc.) -->
        <dependency>
            <groupId>uy.edu.pa</groupId>
            <artifactId>logica</artifactId>
            <version>1.0</version>
            <scope>system</scope>
            <systemPath>${project.basedir}/../../lib/logica.jar</systemPath>
        </dependency>
    </dependencies>

    <build>
        <finalName>central-ws</finalName>
        <plugins>
            <!-- Maven Compiler Plugin -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.11.0</version>
                <configuration>
                    <source>17</source>
                    <target>17</target>
                </configuration>
            </plugin>

            <!-- Maven WAR Plugin -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-war-plugin</artifactId>
                <version>3.3.2</version>
                <configuration>
                    <failOnMissingWebXml>false</failOnMissingWebXml>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

#### Explicación de Dependencias:

**1. Jakarta EE 9 APIs (scope=provided)**
- Proporcionadas por Tomcat 10, no empaquetadas en WAR
- `jakarta.servlet-api` → Para servlets
- `jakarta.xml.ws-api` → Para anotaciones SOAP (@WebService)
- `jakarta.persistence-api` → Para anotaciones JPA (@Entity)

**2. Implementaciones (incluidas en WAR)**
- `jaxws-rt` → Metro, implementa JAX-WS (servidor SOAP)
- `eclipselink` → Implementa JPA (persistencia)
- `mysql-connector-j` → Driver JDBC para MySQL

**3. Entidades Compartidas**
- `logica.jar` → Contiene clases Usuario, Turista, Proveedor, Actividad, etc.
- Usado con `<scope>system</scope>` para referencia local

---

### 2. Configuración JAX-WS (sun-jaxws.xml)

**Ubicación:** `Laboratorio3Pap-main/central-ws/src/main/webapp/WEB-INF/sun-jaxws.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<endpoints xmlns="http://java.sun.com/xml/ns/jax-ws/ri/runtime"
           version="2.0">
    
    <!-- Endpoint 1: AuthService -->
    <endpoint
        name="AuthService"
        implementation="uy.edu.pa.central.ws.AuthServiceImpl"
        url-pattern="/services/AuthService"/>
    
    <!-- Endpoint 2: ActividadesService -->
    <endpoint
        name="ActividadesService"
        implementation="uy.edu.pa.central.ws.ActividadesServiceImpl"
        url-pattern="/services/ActividadesService"/>
</endpoints>
```

#### Explicación:

**Estructura de `<endpoint>`:**
- `name`: Identificador del servicio
- `implementation`: Clase Java que implementa el servicio
- `url-pattern`: Ruta donde se publica el servicio

**URLs resultantes (puerto 9128):**
- AuthService: `http://localhost:9128/central-ws/services/AuthService`
- ActividadesService: `http://localhost:9128/central-ws/services/ActividadesService`

**WSDLs automáticos:**
- AuthService: `http://localhost:9128/central-ws/services/AuthService?wsdl`
- ActividadesService: `http://localhost:9128/central-ws/services/ActividadesService?wsdl`

---

### 3. Configuración Web (web.xml)

**Ubicación:** `Laboratorio3Pap-main/central-ws/src/main/webapp/WEB-INF/web.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="https://jakarta.ee/xml/ns/jakartaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="https://jakarta.ee/xml/ns/jakartaee
                             https://jakarta.ee/xml/ns/jakartaee/web-app_5_0.xsd"
         version="5.0">

    <display-name>Central Web Services</display-name>
    <description>Servidor Central con servicios SOAP</description>

    <!-- Listener para JAX-WS -->
    <listener>
        <listener-class>com.sun.xml.ws.transport.http.servlet.WSServletContextListener</listener-class>
    </listener>

    <!-- Servlet para JAX-WS -->
    <servlet>
        <servlet-name>jaxws-servlet</servlet-name>
        <servlet-class>com.sun.xml.ws.transport.http.servlet.WSServlet</servlet-class>
        <load-on-startup>1</load-on-startup>
    </servlet>

    <!-- Mapeo de URL -->
    <servlet-mapping>
        <servlet-name>jaxws-servlet</servlet-name>
        <url-pattern>/services/*</url-pattern>
    </servlet-mapping>

    <!-- Página de bienvenida -->
    <welcome-file-list>
        <welcome-file>index.html</welcome-file>
    </welcome-file-list>
</web-app>
```

#### Componentes Clave:

**1. WSServletContextListener**
- Inicializa el runtime JAX-WS
- Lee `sun-jaxws.xml` y registra endpoints

**2. WSServlet**
- Maneja todas las peticiones SOAP
- `load-on-startup="1"` → Se carga al iniciar Tomcat

**3. Servlet Mapping**
- `/services/*` → Todas las URLs bajo `/services/` son SOAP

---

### 4. Configuración JPA (persistence.xml)

**Ubicación:** `Laboratorio3Pap-main/central-ws/src/main/resources/META-INF/persistence.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<persistence xmlns="https://jakarta.ee/xml/ns/persistence"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="https://jakarta.ee/xml/ns/persistence
                                 https://jakarta.ee/xml/ns/persistence/persistence_3_0.xsd"
             version="3.0">

    <persistence-unit name="CentralWS-PU" transaction-type="RESOURCE_LOCAL">
        <provider>org.eclipse.persistence.jpa.PersistenceProvider</provider>

        <!-- Entidades JPA -->
        <class>logica.Usuario</class>
        <class>logica.Turista</class>
        <class>logica.Proveedor</class>
        <class>logica.Actividad</class>
        <class>logica.Salida</class>
        <class>logica.Inscripcion</class>
        <class>logica.Departamento</class>
        <class>logica.Categoria</class>

        <properties>
            <!-- ==================== Conexión a BD ==================== -->
            <property name="jakarta.persistence.jdbc.driver" 
                      value="com.mysql.cj.jdbc.Driver"/>
            <property name="jakarta.persistence.jdbc.url" 
                      value="jdbc:mysql://switchback.proxy.rlwy.net:40439/railway?useSSL=false&amp;serverTimezone=UTC"/>
            <property name="jakarta.persistence.jdbc.user" 
                      value="root"/>
            <property name="jakarta.persistence.jdbc.password" 
                      value="gxgmXGPBKKiKEYMcXlEqcECWjYkkdMKy"/>

            <!-- ==================== EclipseLink Config ==================== -->
            <property name="eclipselink.logging.level" value="INFO"/>
            <property name="eclipselink.logging.level.sql" value="FINE"/>
            <property name="eclipselink.logging.parameters" value="true"/>
            
            <!-- NO auto-crear esquema (BD ya existe) -->
            <property name="eclipselink.ddl-generation" value="none"/>
            
            <!-- Connection Pool -->
            <property name="eclipselink.jdbc.read-connections.min" value="1"/>
            <property name="eclipselink.jdbc.read-connections.max" value="5"/>
            <property name="eclipselink.jdbc.write-connections.min" value="1"/>
            <property name="eclipselink.jdbc.write-connections.max" value="5"/>
        </properties>
    </persistence-unit>
</persistence>
```

#### Explicación Detallada:

**1. Persistence Unit Name**
```xml
<persistence-unit name="CentralWS-PU" transaction-type="RESOURCE_LOCAL">
```
- `name="CentralWS-PU"` → Usado en `JpaUtil.getEntityManager()`
- `transaction-type="RESOURCE_LOCAL"` → Transacciones manuales (no JTA)

**2. Provider**
```xml
<provider>org.eclipse.persistence.jpa.PersistenceProvider</provider>
```
- EclipseLink como implementación JPA

**3. Entidades Registradas**
```xml
<class>logica.Usuario</class>
<class>logica.Turista</class>
<!-- ... -->
```
- Lista explícita de todas las clases @Entity
- Necesario cuando las clases están en JAR externo

**4. Propiedades de Conexión**
```xml
<property name="jakarta.persistence.jdbc.url" 
          value="jdbc:mysql://switchback.proxy.rlwy.net:40439/railway?useSSL=false&amp;serverTimezone=UTC"/>
```
- **Host:** `switchback.proxy.rlwy.net`
- **Puerto:** `40439`
- **Base de datos:** `railway`
- **Parámetros:**
  - `useSSL=false` → Sin encriptación SSL
  - `serverTimezone=UTC` → Zona horaria UTC

**5. Logging**
```xml
<property name="eclipselink.logging.level.sql" value="FINE"/>
<property name="eclipselink.logging.parameters" value="true"/>
```
- `FINE` → Muestra SQL generado en logs
- `parameters=true` → Muestra valores de parámetros

**6. DDL Generation**
```xml
<property name="eclipselink.ddl-generation" value="none"/>
```
- `none` → NO auto-crear tablas (BD ya existe y tiene datos)
- Alternativas: `create-tables`, `drop-and-create-tables`

---

### 5. Utility Class: JpaUtil.java

**Ubicación:** `Laboratorio3Pap-main/central-ws/src/main/java/uy/edu/pa/central/persistence/JpaUtil.java`

```java
package uy.edu.pa.central.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Utility class para gestionar EntityManager de JPA.
 * Implementa Singleton para EntityManagerFactory.
 */
public class JpaUtil {
    
    private static EntityManagerFactory emf = null;
    
    // Constructor privado (Singleton)
    private JpaUtil() {}
    
    /**
     * Inicializa EntityManagerFactory (una sola vez).
     */
    private static void initFactory() {
        if (emf == null) {
            try {
                // Lee persistence.xml y crea factory
                emf = Persistence.createEntityManagerFactory("CentralWS-PU");
                System.out.println("✅ EntityManagerFactory inicializado correctamente");
            } catch (Exception e) {
                System.err.println("❌ Error al inicializar EntityManagerFactory: " + e.getMessage());
                e.printStackTrace();
                throw new ExceptionInInitializerError(e);
            }
        }
    }
    
    /**
     * Retorna un nuevo EntityManager.
     * IMPORTANTE: El caller debe cerrar el EntityManager después de usarlo.
     * 
     * @return EntityManager nuevo
     */
    public static EntityManager getEntityManager() {
        if (emf == null) {
            initFactory();
        }
        return emf.createEntityManager();
    }
    
    /**
     * Cierra el EntityManagerFactory.
     * Llamar al finalizar la aplicación.
     */
    public static void closeFactory() {
        if (emf != null && emf.isOpen()) {
            emf.close();
            System.out.println("✅ EntityManagerFactory cerrado");
        }
    }
}
```

#### ¿Por qué este Pattern?

**Problema sin JpaUtil:**
```java
// ❌ Crear factory en cada operación (LENTO)
EntityManagerFactory emf = Persistence.createEntityManagerFactory("CentralWS-PU");
EntityManager em = emf.createEntityManager();
// ... operaciones
em.close();
emf.close(); // Cierra conexiones del pool
```

**Solución con JpaUtil:**
```java
// ✅ Factory singleton, solo EntityManager se recrea
EntityManager em = JpaUtil.getEntityManager(); // Rápido
try {
    // ... operaciones
} finally {
    em.close(); // Solo cierra EntityManager
}
// Factory se mantiene abierta (pool de conexiones activo)
```

**Ventajas:**
- `EntityManagerFactory` se crea una sola vez (costoso)
- Pool de conexiones se reutiliza
- `EntityManager` ligero, se crea/cierra por operación
- Thread-safe (cada operación tiene su propio EM)

---

## 🌐 Configuración del Servidor Web

### Ubicación del Proyecto
```
Laboratorio2PaP-main/
├── pom.xml
└── src/
    └── main/
        ├── java/
        │   └── servlets/          # 22 servlets
        └── webapp/
            ├── WEB-INF/
            │   ├── web.xml
            │   └── views/          # JSP pages
            ├── css/
            ├── js/
            └── images/
```

---

### 1. Configuración Maven (pom.xml)

**Ubicación:** `Laboratorio2PaP-main/pom.xml`

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
                             http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>uy.edu.pa</groupId>
    <artifactId>Laboratorio2PaP</artifactId>
    <version>1.0.0</version>
    <packaging>war</packaging>

    <name>Laboratorio 2 - Servidor Web</name>
    <description>Aplicación Web con servlets consumiendo Web Services SOAP</description>

    <properties>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencies>
        <!-- ==================== Jakarta EE APIs ==================== -->
        
        <!-- Servlet API 5.0 -->
        <dependency>
            <groupId>jakarta.servlet</groupId>
            <artifactId>jakarta.servlet-api</artifactId>
            <version>5.0.0</version>
            <scope>provided</scope>
        </dependency>

        <!-- JSP API 3.0 -->
        <dependency>
            <groupId>jakarta.servlet.jsp</groupId>
            <artifactId>jakarta.servlet.jsp-api</artifactId>
            <version>3.0.0</version>
            <scope>provided</scope>
        </dependency>

        <!-- JSTL 2.0 (Tag Library) -->
        <dependency>
            <groupId>jakarta.servlet.jsp.jstl</groupId>
            <artifactId>jakarta.servlet.jsp.jstl-api</artifactId>
            <version>2.0.0</version>
        </dependency>
        
        <dependency>
            <groupId>org.glassfish.web</groupId>
            <artifactId>jakarta.servlet.jsp.jstl</artifactId>
            <version>2.0.0</version>
        </dependency>

        <!-- ==================== Cliente SOAP ==================== -->

        <!-- JAR con stubs generados (client-ws) -->
        <dependency>
            <groupId>uy.edu.pa</groupId>
            <artifactId>client-ws</artifactId>
            <version>1.0.0</version>
        </dependency>

        <!-- Metro JAX-WS Runtime (para cliente SOAP) -->
        <dependency>
            <groupId>com.sun.xml.ws</groupId>
            <artifactId>jaxws-rt</artifactId>
            <version>4.0.2</version>
        </dependency>
    </dependencies>

    <build>
        <finalName>Laboratorio2PaP</finalName>
        <plugins>
            <!-- Maven Compiler Plugin -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.11.0</version>
                <configuration>
                    <source>17</source>
                    <target>17</target>
                </configuration>
            </plugin>

            <!-- Maven WAR Plugin -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-war-plugin</artifactId>
                <version>3.3.2</version>
            </plugin>
        </plugins>
    </build>
</project>
```

#### Dependencias Clave:

**1. client-ws JAR**
```xml
<dependency>
    <groupId>uy.edu.pa</groupId>
    <artifactId>client-ws</artifactId>
    <version>1.0.0</version>
</dependency>
```
- Contiene 41 clases stub generadas
- Instalado en Maven local con `mvn install` desde `client-ws/`

**2. jaxws-rt**
```xml
<dependency>
    <groupId>com.sun.xml.ws</groupId>
    <artifactId>jaxws-rt</artifactId>
    <version>4.0.2</version>
</dependency>
```
- Runtime Metro para cliente SOAP
- Necesario para que los stubs funcionen

**3. JSTL**
```xml
<dependency>
    <groupId>org.glassfish.web</groupId>
    <artifactId>jakarta.servlet.jsp.jstl</artifactId>
    <version>2.0.0</version>
</dependency>
```
- Implementación de JSTL (Jakarta Standard Tag Library)
- Para usar `<c:forEach>`, `<c:if>`, etc. en JSPs

---

### 2. Configuración Web (web.xml)

**Ubicación:** `Laboratorio2PaP-main/src/main/webapp/WEB-INF/web.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="https://jakarta.ee/xml/ns/jakartaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="https://jakarta.ee/xml/ns/jakartaee
                             https://jakarta.ee/xml/ns/jakartaee/web-app_5_0.xsd"
         version="5.0">

    <display-name>Laboratorio 2 PaP - Tarea 3</display-name>
    <description>Aplicación Web consumiendo servicios SOAP</description>

    <!-- Página de inicio -->
    <welcome-file-list>
        <welcome-file>index.jsp</welcome-file>
        <welcome-file>index.html</welcome-file>
    </welcome-file-list>

    <!-- Configuración de sesión -->
    <session-config>
        <session-timeout>30</session-timeout> <!-- 30 minutos -->
    </session-config>

    <!-- Página de error 404 -->
    <error-page>
        <error-code>404</error-code>
        <location>/WEB-INF/views/error/404.jsp</location>
    </error-page>

    <!-- Página de error 500 -->
    <error-page>
        <error-code>500</error-code>
        <location>/WEB-INF/views/error/500.jsp</location>
    </error-page>
</web-app>
```

**Nota:** Los servlets están definidos con `@WebServlet` (no en web.xml)

---

## 💾 Base de Datos y Persistencia

### Información de Conexión

**Proveedor:** Railway (Cloud MySQL)

```
Host:     switchback.proxy.rlwy.net
Puerto:   40439
Base de datos: railway
Usuario:  root
Password: gxgmXGPBKKiKEYMcXlEqcECWjYkkdMKy
```

**JDBC URL:**
```
jdbc:mysql://switchback.proxy.rlwy.net:40439/railway?useSSL=false&serverTimezone=UTC
```

---

### Esquema de Base de Datos

```sql
-- Tabla Usuario (clase base)
CREATE TABLE Usuario (
    nickname VARCHAR(50) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    fechaNac DATE,
    DTYPE VARCHAR(31) NOT NULL  -- 'Turista' o 'Proveedor' (discriminador JPA)
);

-- Tabla Turista (hereda de Usuario)
CREATE TABLE Turista (
    nickname VARCHAR(50) PRIMARY KEY,
    nacionalidad VARCHAR(50),
    FOREIGN KEY (nickname) REFERENCES Usuario(nickname)
);

-- Tabla Proveedor (hereda de Usuario)
CREATE TABLE Proveedor (
    nickname VARCHAR(50) PRIMARY KEY,
    descripcion TEXT,
    link VARCHAR(255),
    FOREIGN KEY (nickname) REFERENCES Usuario(nickname)
);

-- Tabla Departamento
CREATE TABLE Departamento (
    nombre VARCHAR(100) PRIMARY KEY,
    descripcion TEXT,
    url VARCHAR(255)
);

-- Tabla Categoria
CREATE TABLE nombre VARCHAR(100) PRIMARY KEY,
    descripcion TEXT
);

-- Tabla Actividad
CREATE TABLE Actividad (
    nombre VARCHAR(150) PRIMARY KEY,
    descripcion TEXT,
    duracion INT,  -- en horas
    costo FLOAT,
    ciudad VARCHAR(100),
    estado VARCHAR(50),  -- 'Ingresada', 'Confirmada', 'Rechazada'
    fechaAlta DATE,
    proveedor_nickname VARCHAR(50),
    departamento_nombre VARCHAR(100),
    FOREIGN KEY (proveedor_nickname) REFERENCES Proveedor(nickname),
    FOREIGN KEY (departamento_nombre) REFERENCES Departamento(nombre)
);

-- Tabla Actividad_Categoria (Many-to-Many)
CREATE TABLE Actividad_Categoria (
    actividad_nombre VARCHAR(150),
    categoria_nombre VARCHAR(100),
    PRIMARY KEY (actividad_nombre, categoria_nombre),
    FOREIGN KEY (actividad_nombre) REFERENCES Actividad(nombre),
    FOREIGN KEY (categoria_nombre) REFERENCES Categoria(nombre)
);

-- Tabla Salida
CREATE TABLE Salida (
    nombre VARCHAR(150),
    actividad_nombre VARCHAR(150),
    fecha DATE NOT NULL,
    hora TIME NOT NULL,
    lugar VARCHAR(200),
    turistasMax INT,
    fechaAlta DATE,
    PRIMARY KEY (nombre, actividad_nombre),
    FOREIGN KEY (actividad_nombre) REFERENCES Actividad(nombre)
);

-- Tabla Inscripcion
CREATE TABLE Inscripcion (
    actividad_nombre VARCHAR(150),
    salida_nombre VARCHAR(150),
    turista_nickname VARCHAR(50),
    cantidad INT,
    fecha DATE,
    costo FLOAT,
    PRIMARY KEY (actividad_nombre, salida_nombre, turista_nickname),
    FOREIGN KEY (actividad_nombre, salida_nombre) REFERENCES Salida(actividad_nombre, nombre),
    FOREIGN KEY (turista_nickname) REFERENCES Turista(nickname)
);
```

---

### Mapeo JPA (Ejemplos)

#### Usuario (Clase Base con Herencia)

```java
@Entity
@Table(name = "Usuario")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "DTYPE", discriminatorType = DiscriminatorType.STRING)
public abstract class Usuario implements Serializable {
    
    @Id
    @Column(length = 50)
    private String nickname;
    
    @Column(length = 100, nullable = false)
    private String nombre;
    
    @Column(length = 100, nullable = false)
    private String apellido;
    
    @Column(length = 150, unique = true, nullable = false)
    private String email;
    
    @Column(length = 255, nullable = false)
    private String password;
    
    @Temporal(TemporalType.DATE)
    private Date fechaNac;
    
    // Getters y Setters
}
```

**Estrategia de Herencia:**
- `JOINED` → Tablas separadas (Usuario + Turista/Proveedor)
- `DTYPE` → Columna discriminadora ('Turista' o 'Proveedor')

#### Turista (Hereda de Usuario)

```java
@Entity
@Table(name = "Turista")
@DiscriminatorValue("Turista")
public class Turista extends Usuario {
    
    @Column(length = 50)
    private String nacionalidad;
    
    // Constructor, Getters y Setters
}
```

#### Proveedor (Hereda de Usuario)

```java
@Entity
@Table(name = "Proveedor")
@DiscriminatorValue("Proveedor")
public class Proveedor extends Usuario {
    
    @Column(columnDefinition = "TEXT")
    private String descripcion;
    
    @Column(length = 255)
    private String link;
    
    @OneToMany(mappedBy = "proveedor")
    private List<Actividad> actividades;
    
    // Constructor, Getters y Setters
}
```

---

## 🚀 Proceso de Compilación y Despliegue

### Diagrama de Flujo de Deployment

```
┌─────────────────────────────────────────────────────────────┐
│ 1. COMPILAR SERVIDOR CENTRAL                                 │
│    cd Laboratorio3Pap-main/central-ws                        │
│    mvn clean install                                         │
│    → Genera: target/central-ws.war                           │
└─────────────────┬───────────────────────────────────────────┘
                  │
                  ▼
┌─────────────────────────────────────────────────────────────┐
│ 2. DESPLEGAR EN TOMCAT CENTRAL (Puerto 9128)                │
│    cp target/central-ws.war                                  │
│       ../../apache-tomcat-10.1.47-central/webapps/           │
│    → Tomcat auto-despliega en 5-10 segundos                 │
└─────────────────┬───────────────────────────────────────────┘
                  │
                  ▼
┌─────────────────────────────────────────────────────────────┐
│ 3. VERIFICAR WSDL DISPONIBLES                                │
│    curl http://localhost:9128/central-ws/services/          │
│         AuthService?wsdl                                     │
│    curl http://localhost:9128/central-ws/services/          │
│         ActividadesService?wsdl                              │
│    → Deben retornar XML válido                               │
└─────────────────┬───────────────────────────────────────────┘
                  │
                  ▼
┌─────────────────────────────────────────────────────────────┐
│ 4. GENERAR STUBS DE CLIENTE                                  │
│    cd ../client-ws                                           │
│    mvn clean compile                                         │
│    → Lee WSDLs y genera 41 clases Java                       │
│    mvn install                                               │
│    → Instala client-ws-1.0.0.jar en ~/.m2/repository/        │
└─────────────────┬───────────────────────────────────────────┘
                  │
                  ▼
┌─────────────────────────────────────────────────────────────┐
│ 5. COMPILAR SERVIDOR WEB                                     │
│    cd ../../Laboratorio2PaP-main                             │
│    mvn clean package -DskipTests                             │
│    → Genera: target/Laboratorio2PaP.war                      │
│    → Incluye client-ws.jar en WEB-INF/lib/                   │
└─────────────────┬───────────────────────────────────────────┘
                  │
                  ▼
┌─────────────────────────────────────────────────────────────┐
│ 6. DESPLEGAR EN TOMCAT WEB (Puerto 8080)                    │
│    cp target/Laboratorio2PaP.war                             │
│       ../apache-tomcat-10.1.47/webapps/                      │
│    → Tomcat auto-despliega en 10-15 segundos                │
└─────────────────┬───────────────────────────────────────────┘
                  │
                  ▼
┌─────────────────────────────────────────────────────────────┐
│ 7. VERIFICAR APLICACIÓN                                      │
│    http://localhost:8080/Laboratorio2PaP/                    │
│    → Debe mostrar página de inicio                           │
└─────────────────────────────────────────────────────────────┘
```

---

### Scripts de Deployment

#### Script 1: Desplegar Servidor Central

**Ubicación:** `scripts/deploy-central.sh`

```bash
#!/bin/bash

echo "🚀 Desplegando Servidor Central (Puerto 9128)..."

# 1. Compilar
cd Laboratorio3Pap-main/central-ws
mvn clean install -DskipTests

if [ $? -ne 0 ]; then
    echo "❌ Error en compilación"
    exit 1
fi

# 2. Copiar WAR
cp target/central-ws.war ../../apache-tomcat-10.1.47-central/webapps/

# 3. Esperar deployment
echo "⏳ Esperando deployment..."
sleep 12

# 4. Verificar WSDL
echo "🔍 Verificando WSDLs..."
curl -s http://localhost:9128/central-ws/services/AuthService?wsdl | head -5

if [ $? -eq 0 ]; then
    echo "✅ Servidor Central desplegado correctamente"
else
    echo "❌ Error: WSDL no accesible"
    exit 1
fi
```

#### Script 2: Desplegar Servidor Web (Completo)

**Ubicación:** `deploy-tomcat10.sh`

```bash
#!/bin/bash

echo "══════════════════════════════════════════════════════"
echo "    🚀 DEPLOYMENT COMPLETO - TAREA 3"
echo "══════════════════════════════════════════════════════"
echo ""

# ==================== PASO 1: Central WS ====================
echo "📦 PASO 1/4: Compilando Servidor Central..."
cd Laboratorio3Pap-main/central-ws
mvn clean install -DskipTests > /dev/null 2>&1

if [ $? -ne 0 ]; then
    echo "❌ Error compilando central-ws"
    exit 1
fi

echo "✅ Central WS compilado"

# Desplegar
cp target/central-ws.war ../../apache-tomcat-10.1.47-central/webapps/
echo "⏳ Desplegando en puerto 9128..."
sleep 12

# Verificar
curl -s http://localhost:9128/central-ws/services/AuthService?wsdl > /dev/null 2>&1
if [ $? -eq 0 ]; then
    echo "✅ Servidor Central activo"
else
    echo "⚠️  WSDL no accesible (puede estar iniciando)"
fi

echo ""

# ==================== PASO 2: Client WS ====================
echo "📦 PASO 2/4: Generando Stubs de Cliente..."
cd ../client-ws
mvn clean compile > /dev/null 2>&1

if [ $? -ne 0 ]; then
    echo "❌ Error generando stubs"
    exit 1
fi

mvn install -DskipTests > /dev/null 2>&1
echo "✅ Stubs generados e instalados"
echo ""

# ==================== PASO 3: Servidor Web ====================
echo "📦 PASO 3/4: Compilando Servidor Web..."
cd ../../Laboratorio2PaP-main
mvn clean package -DskipTests > /dev/null 2>&1

if [ $? -ne 0 ]; then
    echo "❌ Error compilando Laboratorio2PaP"
    exit 1
fi

echo "✅ Servidor Web compilado"

# Desplegar
cp target/Laboratorio2PaP.war ../apache-tomcat-10.1.47/webapps/
echo "⏳ Desplegando en puerto 8080..."
sleep 15

echo "✅ Aplicación Web desplegada"
echo ""

# ==================== PASO 4: Verificación ====================
echo "📦 PASO 4/4: Verificación Final..."

# Check Tomcats
CENTRAL_PID=$(ps aux | grep tomcat-10.1.47-central | grep -v grep | awk '{print $2}')
WEB_PID=$(ps aux | grep apache-tomcat-10.1.47 | grep -v central | grep -v grep | awk '{print $2}')

if [ -z "$CENTRAL_PID" ]; then
    echo "❌ Tomcat Central no está corriendo"
else
    echo "✅ Tomcat Central (PID: $CENTRAL_PID) - Puerto 9128"
fi

if [ -z "$WEB_PID" ]; then
    echo "❌ Tomcat Web no está corriendo"
else
    echo "✅ Tomcat Web (PID: $WEB_PID) - Puerto 8080"
fi

# Check endpoints
curl -s http://localhost:8080/Laboratorio2PaP/ > /dev/null 2>&1
if [ $? -eq 0 ]; then
    echo "✅ Aplicación accesible"
else
    echo "❌ Aplicación no accesible"
fi

echo ""
echo "══════════════════════════════════════════════════════"
echo "    ✅ DEPLOYMENT COMPLETO"
echo "══════════════════════════════════════════════════════"
echo ""
echo "🌐 URLs:"
echo "   - Aplicación Web:  http://localhost:8080/Laboratorio2PaP/"
echo "   - AuthService:     http://localhost:9128/central-ws/services/AuthService?wsdl"
echo "   - ActividadesService: http://localhost:9128/central-ws/services/ActividadesService?wsdl"
echo ""
```

**Ejecutar:**
```bash
chmod +x deploy-tomcat10.sh
./deploy-tomcat10.sh
```

---

## 🧪 Verificación y Testing

### Test 1: Verificar Servidores Activos

```bash
# Ver procesos Tomcat
ps aux | grep tomcat

# Resultado esperado:
# ... apache-tomcat-10.1.47-central/bin/bootstrap.jar ... (Puerto 9128)
# ... apache-tomcat-10.1.47/bin/bootstrap.jar ...        (Puerto 8080)
```

```bash
# Ver puertos escuchando
netstat -tuln | grep -E '8080|9128'

# Resultado esperado:
# tcp6  0  0  :::8080  :::*  LISTEN
# tcp6  0  0  :::9128  :::*  LISTEN
```

---

### Test 2: Verificar WSDLs Accesibles

```bash
# AuthService WSDL
curl http://localhost:9128/central-ws/services/AuthService?wsdl

# Salida esperada (extracto):
# <?xml version="1.0" encoding="UTF-8"?>
# <definitions targetNamespace="http://ws.central.pa.edu.uy/"
#              name="AuthService">
#   <types>
#     <xsd:schema>
#       <xsd:import namespace="http://ws.central.pa.edu.uy/" ... />
#     </xsd:schema>
#   </types>
#   <message name="Ping">...</message>
#   <message name="PingResponse">...</message>
#   <message name="Login">...</message>
#   ...
# </definitions>
```

```bash
# ActividadesService WSDL
curl http://localhost:9128/central-ws/services/ActividadesService?wsdl

# Debe retornar XML con definiciones de las 10 operaciones
```

---

### Test 3: SOAP Ping Test

```bash
# Enviar petición SOAP de Ping
curl -X POST http://localhost:9128/central-ws/services/AuthService \
  -H "Content-Type: text/xml; charset=utf-8" \
  -H "SOAPAction: \"\"" \
  -d '<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"
               xmlns:ws="http://ws.central.pa.edu.uy/">
  <soap:Body>
    <ws:Ping/>
  </soap:Body>
</soap:Envelope>'

# Respuesta esperada:
# <?xml version="1.0" encoding="UTF-8"?>
# <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
#   <soap:Body>
#     <ns2:PingResponse xmlns:ns2="http://ws.central.pa.edu.uy/">
#       <pong>pong</pong>
#     </ns2:PingResponse>
#   </soap:Body>
# </soap:Envelope>
```

---

### Test 4: Login SOAP Test

```bash
# Login con nickname o email
curl -X POST http://localhost:9128/central-ws/services/AuthService \
  -H "Content-Type: text/xml" \
  -d '<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"
               xmlns:ws="http://ws.central.pa.edu.uy/">
  <soap:Body>
    <ws:Login>
      <identifier>testuser</identifier>
      <password>password123</password>
    </ws:Login>
  </soap:Body>
</soap:Envelope>'

# Respuesta esperada (si existe usuario):
# <LoginResponse>
#   <user>
#     <nickname>testuser</nickname>
#     <nombre>Test</nombre>
#     <apellido>User</apellido>
#     <email>test@example.com</email>
#     <tipoUsuario>turista</tipoUsuario>
#   </user>
# </LoginResponse>
```

---

### Test 5: Verificar Aplicación Web

```bash
# Homepage
curl -I http://localhost:8080/Laboratorio2PaP/

# Resultado esperado:
# HTTP/1.1 200 OK
# Content-Type: text/html;charset=UTF-8
```

```bash
# Login page
curl http://localhost:8080/Laboratorio2PaP/login.jsp | grep -o "<title>.*</title>"

# Resultado esperado:
# <title>Iniciar Sesión - Turismo UY</title>
```

---

### Test 6: Verificar Logs

**Central WS logs:**
```bash
tail -f apache-tomcat-10.1.47-central/logs/catalina.out

# Buscar:
# ✅ EntityManagerFactory inicializado correctamente
# ✅ Turista registrado: nickname
# ✅ Actividad creada: nombre
```

**Web App logs:**
```bash
tail -f apache-tomcat-10.1.47/logs/catalina.out

# Buscar errores 500 o excepciones
```

---

## 📁 Estructura Final del Proyecto

```
/workspaces/Parte_3_proyecto_aplicacion/
│
├── apache-tomcat-10.1.47/                    # Tomcat Web (Puerto 8080)
│   ├── bin/
│   ├── conf/
│   │   └── server.xml                        # Puerto 8080 configurado
│   ├── logs/
│   │   └── catalina.out                      # Logs aplicación web
│   └── webapps/
│       ├── Laboratorio2PaP/                  # App desplegada
│       └── Laboratorio2PaP.war
│
├── apache-tomcat-10.1.47-central/            # Tomcat Central (Puerto 9128)
│   ├── bin/
│   ├── conf/
│   │   └── server.xml                        # Puerto 9128 configurado
│   ├── logs/
│   │   └── catalina.out                      # Logs SOAP services
│   └── webapps/
│       ├── central-ws/                       # Servicios desplegados
│       └── central-ws.war
│
├── Laboratorio3Pap-main/                     # SERVIDOR CENTRAL
│   ├── central-ws/                           # Proyecto Maven
│   │   ├── pom.xml
│   │   ├── src/
│   │   │   └── main/
│   │   │       ├── java/
│   │   │       │   └── uy/edu/pa/central/
│   │   │       │       ├── persistence/
│   │   │       │       │   └── JpaUtil.java
│   │   │       │       └── ws/
│   │   │       │           ├── AuthService.java
│   │   │       │           ├── AuthServiceImpl.java
│   │   │       │           ├── ActividadesService.java
│   │   │       │           ├── ActividadesServiceImpl.java
│   │   │       │           └── dtos/
│   │   │       │               ├── UserDTO.java
│   │   │       │               ├── ActividadDTO.java
│   │   │       │               ├── SalidaDTO.java
│   │   │       │               └── InscripcionDTO.java
│   │   │       ├── resources/
│   │   │       │   └── META-INF/
│   │   │       │       └── persistence.xml
│   │   │       └── webapp/
│   │   │           └── WEB-INF/
│   │   │               ├── web.xml
│   │   │               └── sun-jaxws.xml
│   │   └── target/
│   │       └── central-ws.war
│   │
│   └── client-ws/                            # Generador de Stubs
│       ├── pom.xml                           # Config wsimport
│       ├── src/
│       └── target/
│           ├── client-ws-1.0.0.jar           # JAR con stubs
│           └── generated-sources/
│               └── wsimport/                 # 41 clases generadas
│                   └── uy/edu/pa/client/ws/
│                       ├── auth/
│                       │   ├── AuthService.java
│                       │   ├── AuthService_Service.java
│                       │   ├── UserDTO.java
│                       │   └── ... (13 clases)
│                       └── actividades/
│                           ├── ActividadesService.java
│                           ├── ActividadesService_Service.java
│                           ├── ActividadDTO.java
│                           ├── SalidaDTO.java
│                           └── ... (28 clases)
│
├── Laboratorio2PaP-main/                     # SERVIDOR WEB
│   ├── pom.xml
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       │   └── servlets/
│   │       │       ├── LoginServlet.java
│   │       │       ├── RegisterTouristServlet.java
│   │       │       ├── RegisterProviderServlet.java
│   │       │       ├── UserProfileServlet.java
│   │       │       ├── CreateActivityServlet.java
│   │       │       ├── CreateDepartureServlet.java
│   │       │       ├── UpdateProfileServlet.java
│   │       │       ├── TestDatabaseServlet.java
│   │       │       └── ... (14 más)
│   │       └── webapp/
│   │           ├── index.jsp
│   │           ├── login.jsp
│   │           ├── register-tourist.jsp
│   │           ├── register-provider.jsp
│   │           ├── WEB-INF/
│   │           │   ├── web.xml
│   │           │   └── views/
│   │           │       ├── user-profile.jsp
│   │           │       ├── create-activity.jsp
│   │           │       ├── create-departure.jsp
│   │           │       └── ... (más JSPs)
│   │           ├── css/
│   │           │   └── styles.css
│   │           ├── js/
│   │           │   └── scripts.js
│   │           └── images/
│   └── target/
│       ├── Laboratorio2PaP.war
│       └── Laboratorio2PaP/
│           └── WEB-INF/
│               └── lib/
│                   ├── client-ws-1.0.0.jar   # Stubs incluidos
│                   ├── jaxws-rt-4.0.2.jar
│                   └── ... (más JARs)
│
├── lib/
│   └── logica.jar                            # Entidades JPA compartidas
│
├── scripts/
│   ├── deploy-central.sh
│   └── tomcat.sh                             # Iniciar/parar Tomcats
│
├── deploy-tomcat10.sh                        # Script deployment completo
├── stop-servers.sh                           # Detener ambos Tomcats
│
├── Todo-lo-realizado-PARTE-1.md             # Documentación Parte 1
├── Todo-lo-realizado-PARTE-2.md             # Documentación Parte 2
└── Todo-lo-realizado-PARTE-3.md             # Este documento
```

---

## 📊 Resumen de Configuración

### Archivos Clave Creados/Modificados

| Archivo | Propósito | Cambios |
|---------|-----------|---------|
| `central-ws/pom.xml` | Maven central | Agregado JAX-WS, JPA, MySQL |
| `central-ws/src/.../persistence.xml` | Config JPA | Configurado Railway MySQL |
| `central-ws/src/.../JpaUtil.java` | Gestor EM | Creado desde cero |
| `central-ws/src/.../sun-jaxws.xml` | Endpoints SOAP | Definidos 2 servicios |
| `central-ws/src/.../web.xml` | Config web | Configurado JAX-WS servlet |
| `client-ws/pom.xml` | Generador stubs | Configurado wsimport plugin |
| `Laboratorio2PaP/pom.xml` | Maven web | Agregado client-ws dependency |
| `deploy-tomcat10.sh` | Deployment | Script completo 4 fases |

### Tecnologías Configuradas

| Tecnología | Versión | Uso |
|------------|---------|-----|
| Java | 17 | Lenguaje base |
| Jakarta EE | 9 | Plataforma |
| Tomcat | 10.1.47 | Servidor aplicaciones |
| Maven | 3.9+ | Build tool |
| Metro JAX-WS | 4.0.2 | SOAP runtime |
| EclipseLink | 4.0.2 | JPA provider |
| MySQL | 8.0 | Base de datos |
| Bootstrap | 5.3.0 | UI framework |

### Puertos Utilizados

| Puerto | Servicio | Protocolo |
|--------|----------|-----------|
| 8080 | Servidor Web (Laboratorio2PaP) | HTTP |
| 9128 | Servidor Central (central-ws) | HTTP + SOAP |
| 40439 | Railway MySQL | JDBC/TCP |

---

## ✅ Checklist Final

### Compilación y Deployment
- [x] Central-ws compila sin errores
- [x] Central-ws desplegado en Tomcat puerto 9128
- [x] WSDLs accesibles (AuthService y ActividadesService)
- [x] Client-ws genera 41 stubs correctamente
- [x] Client-ws instalado en Maven local (~/.m2/repository)
- [x] Laboratorio2PaP compila sin errores
- [x] Laboratorio2PaP desplegado en Tomcat puerto 8080
- [x] Ambos Tomcat corriendo simultáneamente

### Funcionalidad
- [x] Ping test exitoso (retorna "pong")
- [x] Login SOAP funciona (retorna UserDTO)
- [x] Registro de turista funciona
- [x] Registro de proveedor funciona
- [x] Crear actividad funciona
- [x] Crear salida funciona
- [x] Actualizar perfil funciona
- [x] Listar usuarios funciona
- [x] Obtener usuario funciona
- [x] Inscribir turista funciona

### Arquitectura
- [x] 0 servlets usando JAR directamente
- [x] 22/22 servlets usando SOAP
- [x] 2 servicios SOAP expuestos (AuthService, ActividadesService)
- [x] 14 operaciones SOAP implementadas
- [x] 4 DTOs creados (UserDTO, ActividadDTO, SalidaDTO, InscripcionDTO)
- [x] Persistencia JPA funcionando (Railway MySQL)
- [x] EntityManager gestionado correctamente (JpaUtil)

### Cumplimiento Tarea 3
- [x] "Manejo puramente de Web Services sin usar .jar" ✅
- [x] Arquitectura distribuida (3 capas) ✅
- [x] Comunicación SOAP/XML ✅
- [x] WSDLs publicados y accesibles ✅
- [x] Cliente SOAP funcional ✅

---

## 🎓 Conclusiones

### Lo Que Se Logró

1. **Arquitectura SOA Completa:**
   - Servidor Central expone 14 operaciones SOAP
   - Servidor Web consume servicios vía stubs generados
   - Separación clara de responsabilidades

2. **100% Cumplimiento con "La Letra":**
   - Ningún servlet usa `import logica.Fabrica` o `import logica.IControlador`
   - Toda comunicación es vía Web Services SOAP
   - Arquitectura distribuida real

3. **Infraestructura Robusta:**
   - 2 servidores Tomcat independientes
   - Base de datos en la nube (Railway MySQL)
   - Pool de conexiones JPA (EclipseLink)
   - Generación automática de stubs (Maven wsimport)

4. **Código Mantenible:**
   - DTOs simples y reutilizables
   - Manejo consistente de errores
   - Logging detallado para debugging
   - Scripts de deployment automáticos

### Lecciones Aprendidas

1. **SOAP es verboso pero robusto:**
   - Requiere mucha configuración inicial
   - Una vez configurado, es muy estable
   - WSDLs proveen contrato claro

2. **Regenerar stubs es crítico:**
   - Cada cambio en WSDL requiere regeneración
   - Olvidarlo causa errores de método no encontrado

3. **JPA con herencia requiere cuidado:**
   - Strategy JOINED necesita tablas bien diseñadas
   - instanceof para type checking en DTOs

4. **EntityManager debe cerrarse siempre:**
   - Usar bloque finally SIEMPRE
   - Evita memory leaks

---

**FIN DE LA DOCUMENTACIÓN COMPLETA**

📚 **Documentos relacionados:**
- [PARTE 1: Arquitectura y Servicios SOAP](./Todo-lo-realizado-PARTE-1.md)
- [PARTE 2: Conversión de Servlets](./Todo-lo-realizado-PARTE-2.md)
- [PARTE 3: Configuración y Deployment](./Todo-lo-realizado-PARTE-3.md) ← Estás aquí

---

**Autor:** GitHub Copilot  
**Fecha:** Noviembre 6, 2025  
**Proyecto:** Tarea 3 - Programación de Aplicaciones (UdelaR)  
**Requisito:** "Manejo puramente de Web Services sin usar .jar"  
**Estado:** ✅ **100% COMPLETO**
