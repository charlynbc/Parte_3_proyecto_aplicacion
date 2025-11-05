# Así se utiliza en Eclipse

Guía para importar, configurar y ejecutar los módulos del laboratorio SOAP en Eclipse (y usarlos con dos Tomcat: central en 8081 y web en 8080), respetando Java 17.

## Requisitos

- Eclipse IDE for Enterprise Java and Web Developers (versión 2023‑xx o 2024‑xx)
- JDK 17 instalado (y configurado en Eclipse)
- Apache Tomcat 10.1.x (dos instancias o una instancia con puertos distintos)
- Maven integrado en Eclipse (viene incluido)

## 1) Importar los proyectos Maven

- File > Import… > Maven > Existing Maven Projects
  - Importa estos módulos:
    - `Laboratorio3Pap-main/central-ws` (WAR del Servidor Central)
    - `Laboratorio3Pap-main/client-ws` (JAR de stubs)
    - (Opcional) `Laboratorio2PaP-main` (la app web que consume los stubs)
- Espera a que Eclipse haga el “Maven > Update Project” y descargue dependencias.

## 2) Configurar Java 17 en Eclipse

- Window > Preferences > Java > Installed JREs
  - Add… > Standard VM > selecciona ruta de tu JDK 17.
  - Marca ese JDK como “default”.
- En cada proyecto: Right click > Properties > Java Build Path > Libraries
  - Verifica que usa JRE System Library [JavaSE‑17].

## 3) Generar e instalar los stubs del cliente (client‑ws)

- En `client-ws`: Right click > Run As > Maven build…
  - Goals: `clean install -DskipTests`
  - Requiere que el Central (8081) esté arriba para que wsimport lea los WSDL.
- Alternativa: primero levantar el central con el script externo y luego `Run As > Maven install`.

## 4) Configurar Tomcat en Eclipse (dos servidores)

- Window > Show View > Servers (si no está visible)
- Click derecho en el área de Servers > New > Server > Apache > Tomcat v10.1 Server
  - Server name: `Tomcat 10.1 (8080)`
  - Tomcat installation directory: carpeta de tu Tomcat 10.1 (Eclipse la reutiliza)
  - Finish.
- Crea un segundo servidor para el Central:
  - New > Server > Apache > Tomcat v10.1 Server
  - Server name: `Tomcat 10.1 (8081)`
  - Finish y luego:
    - Doble click en el servidor > Ports > cambia HTTP/1.1 a 8081 (y shutdown a un puerto libre, p.ej. 8006+1).

Nota: Eclipse usa la misma instalación de Tomcat, pero separa la “Server configuration” por instancia (puertos distintos). Si prefieres, puedes usar tu instancia ya preparada fuera de Eclipse y no iniciar los servidores desde Eclipse.

## 5) Desplegar módulos a los servidores

- En `Servers`:
  - Click derecho sobre `Tomcat 10.1 (8081)` > Add and Remove… > agrega el proyecto `central-ws`.
  - Click derecho sobre `Tomcat 10.1 (8080)` > Add and Remove… > agrega la app web (`Laboratorio2PaP-main` si la importaste).
- Verifica el contexto:
  - Central: `/central-ws`
  - Web: `/Laboratorio2PaP`

## 6) Arrancar los servidores en Eclipse

- Inicia primero `Tomcat 10.1 (8081)` (central). Espera a que levante.
  - Verifica WSDLs: `http://localhost:8081/central-ws/services/AuthService?wsdl` y `.../ActividadesService?wsdl`.
- Inicia `Tomcat 10.1 (8080)` (web).

## 7) Probar la aplicación desde Eclipse

- Navega a `http://localhost:8080/Laboratorio2PaP`
- Login como Turista.
- Ir a “Mis Salidas” (`/mis-salidas`). Debe listar los registros devueltos por el Central vía SOAP.

## 8) Flujo y relación entre módulos (para entender el uso)

- Servidor Central (8081): expone servicios SOAP y persiste (JPA + MySQL JNDI).
- Cliente (client-ws): provee clases generadas (stubs) para consumir esos servicios desde el web.
- Servidor Web (8080): servlets llaman a los stubs; JSPs muestran lo recibido.
  - Ejemplo: `MyRegistrationsServlet` llama `ActividadesService.listarInscripcionesPorTurista(usuario)` y pasa la lista `InscripcionDTO` al JSP `my-registrations.jsp`.

## 9) Diferencias con el Laboratorio 2 (monolítico)

- Lab2 (antes): la app web accedía directamente a JPA y base de datos.
- Lab3 (letra): la app web NO accede a la base; sólo llama a SOAP del Central. El Central contiene la lógica y la persistencia.

## 10) Consejos en Eclipse

- Si el wsimport falla al compilar `client-ws`, asegúrate de que el central (8081) esté corriendo y los WSDL respondan 200.
- Si `Add and Remove…` no muestra tu WAR o proyecto web, haz Maven > Update Project y limpia (Project > Clean…) y vuelve a intentar.
- Los puertos deben estar libres: si 8081 está ocupado, cambia el puerto en la configuración del servidor en Eclipse.

---
Actualizado: 2025‑11‑05
