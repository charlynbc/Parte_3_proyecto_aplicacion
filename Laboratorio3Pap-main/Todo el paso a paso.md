# Todo el paso a paso

Este documento explica, según la letra, qué se hizo, cómo funciona, dónde está cada cosa, qué tecnologías se usaron, qué se cambió y el paso a paso completo para reproducirlo.

## Qué pide la letra (resumen)

- Java 17 sí o sí.
- Dos Tomcat distintos:
  - Servidor Web (8080) con las servlets/JSP.
  - Servidor Central (8081) que expone servicios SOAP (JAX‑WS/Metro).
- El Servidor Web debe consumir SOLO los servicios SOAP del Central. La persistencia “real” vive en el Central.
- UI sobria (sin Bootstrap “pesado”), CSS unificada propia.

## Arquitectura final

- Servidor Central (Tomcat 8081): WAR `central-ws` con servicios SOAP.
  - Servicios:
    - AuthService: login, ping.
    - ActividadesService: InscribirTurista(...) y ListarInscripcionesPorTurista(turista).
  - Persistencia real a MySQL con JPA (EclipseLink) vía DataSource JNDI `jdbc/railway`.
- Cliente SOAP (library): JAR `client-ws` con los stubs generados por wsimport.
- Servidor Web (Tomcat 8080): la app web usa los stubs del cliente para invocar al Central.
  - Ejemplo visible: “Mis Salidas” del Turista consume ListarInscripcionesPorTurista y las muestra en un JSP.

## Dónde está cada cosa (ubicación en el repo)

- Carpeta de esta iteración (letra SOAP): `Laboratorio3Pap-main/`
  - Servidor Central (WAR): `Laboratorio3Pap-main/central-ws/`
    - Servicios: `src/main/java/uy/edu/pa/central/ws/`
      - `AuthService.java`, `AuthServiceImpl.java`
      - `ActividadesService.java`, `ActividadesServiceImpl.java`
      - DTOs: `ws/dtos/`
    - Persistencia (JPA): `src/main/java/uy/edu/pa/central/persistence/JpaUtil.java`
    - Config Metro/Tomcat: `src/main/webapp/WEB-INF/sun-jaxws.xml`, `web.xml`
    - POM: `central-ws/pom.xml`
  - Cliente SOAP (JAR): `Laboratorio3Pap-main/client-ws/`
    - Stubs generados: `target/generated-sources/wsimport/uy/edu/pa/central/client/`
    - Clase de conveniencia: `src/main/java/uy/edu/pa/central/client/CentralSoapClient.java`
    - POM (wsimport): `client-ws/pom.xml`
- Servidor Web (8080): `Laboratorio2PaP-main/`
  - Servlet Mis Salidas: `src/main/java/servlets/MyRegistrationsServlet.java` (usa stubs SOAP)
  - Vista Mis Salidas: `src/main/webapp/WEB-INF/my-registrations.jsp` (renderiza InscripcionDTO)
  - Includes/CSS: `src/main/webapp/WEB-INF/jsp/includes/navbar.jsp`, `src/main/webapp/css/`

## Tecnologías usadas

- Java 17
- Tomcat 10.1.x
- Jakarta Servlet/JSP
- SOAP con Metro JAX‑WS 4.0.x
- JAXB 4.0.x (DTOs)
- JPA EclipseLink 4.0.2
- MySQL Connector/J 8.4.x
- Maven 3.9.x

## Qué se cambió (puntos clave)

1) Java 17 en todo (POMs y scripts de despliegue ajustados para forzar JDK 17).
2) Se creó el Servidor Central `central-ws` con servicios SOAP reales (Metro), WSDLs publicados y configuración en `WEB-INF`.
3) Se agregó persistencia real en el Central: EclipseLink + JNDI `jdbc/railway`. El script de deploy inyecta/ajusta `persistence.xml` para apuntar al DataSource.
4) Se generaron stubs del cliente en `client-ws` con wsimport apuntando a `http://localhost:8081/central-ws/services`.
5) En el Servidor Web, “Mis Salidas” se cambió para consumir el nuevo método SOAP `ListarInscripcionesPorTurista`, y el JSP fue adaptado a `InscripcionDTO`.
6) CSS unificado propio (sin Bootstrap pesado) para respetar la letra.

## Paso a paso – cómo se hizo

1. Configurar Java 17
   - Ajuste de `maven-compiler-plugin` (source/target 17) en los POMs.
   - Scripts de deploy exportan `JAVA_HOME` a JDK 17 antes de compilar/arrancar Tomcat.
2. Crear Servidor Central `central-ws`
   - POM con dependencias: `jaxws-rt` (Metro), `jakarta.xml.ws-api`, JAXB, JPA/EclipseLink y MySQL runtime.
   - `sun-jaxws.xml` y `web.xml` para publicar los endpoints en `/services/*`.
3. Implementar servicios SOAP
   - `AuthService`: `login`, `ping`.
   - `ActividadesService`: `InscribirTurista(...)` (persiste Inscripcion) y `ListarInscripcionesPorTurista(turista)` (devuelve lista de `InscripcionDTO`).
   - DTO `InscripcionDTO` con salida, actividad, fecha, hora, lugar, cantidad, costo, fechaInscripcion, imagen.
4. Persistencia en el Central
   - `JpaUtil` con EMF configurado a JNDI `jdbc/railway`.
   - `ActividadesServiceImpl` usa JPA para buscar Turista/Salida, validar duplicados y persistir Inscripcion.
5. Generar stubs del cliente (`client-ws`)
   - `jaxws-maven-plugin` con `wsdl.baseUrl` → genera paquete `uy.edu.pa.central.client`.
6. Integración en el Web (8080)
   - `MyRegistrationsServlet`: invoca `ActividadesService.listarInscripcionesPorTurista(username)` y setea `inscripcionesDto`.
   - `my-registrations.jsp`: itera `List<InscripcionDTO>` y muestra las tarjetas.
7. Despliegue
   - Central (8081): `./deploy-central.sh` (compila WAR, ajusta persistence, arranca Tomcat central y verifica WSDLs).
   - Web (8080): `./deploy-tomcat10.sh` (compila y despliega la app web en Tomcat 10). 
8. Verificación rápida
   - Login como Turista y navegar a `/mis-salidas`.
   - Comprobar que aparecen las inscripciones del usuario.

## Cómo se usa (rápido)

- Desplegar Central (8081): `./deploy-central.sh`.
- Generar stubs cliente: `cd Laboratorio3Pap-main/client-ws && mvn -DskipTests clean install`.
- Desplegar Web (8080): `./deploy-tomcat10.sh`.
- Entrar a `http://localhost:8080/Laboratorio2PaP`, loguearse como Turista y abrir “Mis Salidas”.

## Diferencias con Laboratorio2PaP

- Antes (monolítico/Lab2): la web accedía a JPA directamente (mismo WAR). 
- Ahora (letra SOAP/Lab3): la web no toca la base; consume servicios SOAP del Central, y el Central es quien persiste y expone datos.
- Ventaja: separación clara de responsabilidades, coherente con el enunciado.

---
Actualizado: 2025‑11‑05
