# Laboratorio 3 PaP – SOAP (Servidor Central + Cliente)

Aquí va TODO lo requerido por la letra para la iteración con Web Services (SOAP): servidor central y stubs de cliente.

## Módulos

- `central-ws/` (WAR): Servidor Central SOAP (Metro JAX‑WS / Jakarta) para Tomcat 10.1 (puerto 8081).
  - Servicios expuestos:
    - `AuthService`: `login`, `ping`.
    - `ActividadesService`: `InscribirTurista(actividad, salida, turista, cantidad, fecha, costo)` y `ListarInscripcionesPorTurista(turista)`.
  - Persistencia real (JPA/EclipseLink + MySQL vía JNDI `jdbc/railway`).

- `client-ws/` (JAR): Stubs generados por wsimport para consumo desde el Servidor Web (en 8080). Paquete: `uy.edu.pa.central.client`.

No se agrega nada extra fuera de estos módulos.

## Requisitos

- Java 17 (mandatorio por la letra)
- Maven 3.9+
- Tomcat 10.1 (central en 8081)

## Build y deploy del Central (8081)

1) Compilar y empaquetar el WAR del central:
```bash
cd Laboratorio3Pap-main/central-ws
mvn -q -DskipTests package
```

2) Desplegar en Tomcat 10.1 (instancia central en 8081). Opciones:
- Usar el script del repo (recomendado):
```bash
./deploy-central.sh
```
- O copiar manualmente el WAR `target/central-ws.war` al webapps del Tomcat central y reiniciar.

3) Verificar WSDLs:
- `http://localhost:8081/central-ws/services/AuthService?wsdl`
- `http://localhost:8081/central-ws/services/ActividadesService?wsdl`

## Build de los Stubs de Cliente

Genera e instala el JAR de stubs en el repositorio local para que el Servidor Web lo use como dependencia:
```bash
cd Laboratorio3Pap-main/client-ws
mvn -q -DskipTests clean install
```
Los WSDLs se toman de `${wsdl.baseUrl}` (por defecto `http://localhost:8081/central-ws/services`).

## Contrato (DTOs principales)

- `UserDTO` (AuthService)
- `InscripcionDTO` (ActividadesService): salidaNombre, actividadNombre, fechaSalida, horaSalida, lugar, cantidad, costo, fechaInscripcion, imagen.

## Notas de configuración

- El WAR del central incluye Metro (`jaxws-rt`) y configuración en `WEB-INF/sun-jaxws.xml` y `WEB-INF/web.xml`.
- La persistencia JPA usa JNDI `jdbc/railway`. Durante el deploy, el script ajusta `persistence.xml` para el entorno.
- El módulo `client-ws` genera stubs en el paquete `uy.edu.pa.central.client` y se usa desde el Servidor Web (8080) para cumplir que las servlets consumen SOLO SOAP.

---
Actualizado: 2025‑11‑05
