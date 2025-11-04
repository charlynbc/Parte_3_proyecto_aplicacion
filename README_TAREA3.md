# Tarea 3 (2025) – Interoperabilidad vía Web Services + Responsive Web Design

Este documento guía, paso a paso y de forma tranquila, lo que vamos a ir haciendo, qué se elimina, qué se agrega y dónde queda lo nuevo para la 3ra iteración del proyecto.

## 1) Idea general y funcionamiento

- Objetivo: distribuir los componentes según la arquitectura del curso y lograr que el Servidor Web consuma la lógica de negocio del Servidor Central exclusivamente vía Web Services (sin usar jars locales de lógica). Además, adaptar la interfaz a un diseño Responsive (RWD) para uso en dispositivos móviles.
- Funcionamiento deseado:
  - El Servidor Central expone la lógica (casos de uso) mediante servicios web.
  - El Servidor Web invoca esos servicios para atender al Visitante y al Proveedor/Turista.
  - La UI del sitio Web se adapta a móviles (grid, navbar, tarjetas, formularios), usando Bootstrap o el sistema responsive existente.

Actores:
- Visitante: navega y consulta.
- Proveedor/Turista: se autentica y accede a funcionalidades adicionales.
- Administrador: sólo en estación de trabajo (no inicia sesión en el sitio web).

Casos de uso a cubrir en esta iteración (mínimos):
- Inicio de Sesión (Visitante → Proveedor/Turista).
- Cierre de Sesión (Proveedor/Turista).
- Consulta de Actividad Turística (incluye salidas e imagen) y detalle de Salida Turística.

## 2) Arquitectura objetivo (alto nivel)

- Componente Servidor Central
  - Expone API (Web Services) para casos de uso. Opción práctica en Tomcat: REST JSON con JAX‑RS (Jersey). Alternativa si el curso exige SOAP: JAX‑WS (Metro/Apache CXF).
  - Accede a DB y concentra la lógica de negocio.
- Componente Servidor Web
  - Es un front-end server-side (Servlets/JSP) que invoca al Servidor Central vía HTTP (REST o SOAP), sin enlazar jars de lógica.
  - Renderiza las vistas responsive para móvil y desktop.

Elección técnica: usaremos SOAP (JAX‑WS) como pediste. Implementaremos el Servidor Central con Metro JAX‑WS (Jakarta) en Tomcat 10. Si más adelante se requiere REST, lo dejaremos como alternativa documentada.

## 3) Plan por fases (qué haremos, qué se borra y dónde queda lo nuevo)

Fase 0 – Rama y documentación
- Crear rama de trabajo: `Parte_3`. (Hecho)
- Este documento: `README_TAREA3.md`. (Hecho)
- Enlazar desde `README.md`. (Hecho)

Fase 1 – Definición de contratos (WSDL/operaciones SOAP)
- Diseñar puertos/operaciones para:
  - AuthService: `ping()`, `login(identifier, password)`
  - ActividadesService: `listarActividades()`, `obtenerActividad(id)`, `obtenerSalida(id)`
- Definir DTOs (UserDTO, ActividadDTO, SalidaDTO) con JAXB.
- Entregable: servicios publicados y WSDL accesibles bajo `/central-ws/services/*`.

Fase 2 – Servidor Central (SOAP JAX‑WS)
- Nuevo módulo `Laboratorio3Pap-main/central-ws` (WAR) con Metro JAX‑WS (Jakarta) en Tomcat 10.
- Endpoints: `AuthService` y `ActividadesService` (con mocks iniciales, luego conectan a la DB Central).
- Entregable: WAR `central-ws.war` desplegado; WSDL disponibles.

Fase 3 – Servidor Web (cliente SOAP)
- Sustituir el uso del jar de lógica por consumo de servicios SOAP del Central.
- Generar stubs (wsimport) en módulo `client-ws/` y usarlos desde el Web.
- Actualizar Servlets para invocar a los stubs: `LoginServlet`, `ActivitiesServlet`, etc.
- Entregable: sitio web funcionando contra el Central vía SOAP, sin acoplar jar de lógica.

Fase 4 – Responsive Web Design (RWD)
- Incluir Bootstrap 5 (o mantener CSS modular existente) y asegurar:
  - Navbar responsive (collapse en móvil), grid para tarjetas, formularios adaptables.
  - Páginas clave: login, actividades, detalle de actividad/salida, “Mis salidas”.
- Verificación con breakpoints comunes (≤480, ≤768, ≤1024, ≥1280).
- Entregable: capturas y checklist de responsividad.

Fase 5 – Pruebas y despliegue
- Pruebas locales: flujos de login/logout, búsqueda y detalle de actividades/salidas.
- Validación de errores: credenciales inválidas, actividad inexistente, timeouts al central.
- Despliegue: script `deploy-tomcat10.sh` actualizado para ambos componentes o dos scripts (`deploy-central.sh`, `deploy-web.sh`).

Fase 6 – Limpieza y notas de migración
- “Qué se borra”: dejar de depender de jars de lógica en el Servidor Web; ajustar `pom.xml` y eliminar referencias a `Laboratorio1.jar` del despliegue del web.
- “Dónde está lo nuevo”:
  - Código del Central: `central/` (o `LaboratorioCentral/`), con sus endpoints REST.
  - Cliente HTTP del Web: `src/main/java/utils/ApiClient.java` y DTOs.
  - Vistas y CSS responsive: `src/main/webapp/` (Bootstrap/estilos).

## 4) Contratos SOAP (borrador rápido)

- AuthService
  - `string ping()` → "pong"
  - `User login(string identifier, string password)` → Devuelve `UserDTO` o Fault

- ActividadesService
  - `Actividad[] listarActividades()`
  - `Actividad obtenerActividad(string id)`
  - `Salida obtenerSalida(string id)`

DTOs (JAXB): `UserDTO`, `ActividadDTO` (incluye `List<SalidaDTO>`), `SalidaDTO`.
Los WSDL se publicarán bajo `http://localhost:8080/central-ws/services/*.wsdl`.

## 5) Qué ya hicimos en esta rama (Parte_3)

- Sección “Mis Salidas” (Turista), con filtro de acceso por rol y vista responsive con tarjetas.
- Unificación de estilos de tarjetas (removidos estilos inline; uso de `cards.css`).
- Estabilización de despliegue en Tomcat 10.1 y conexión a DB Railway.
- Documentación inicial (este archivo) y enlace desde `README.md`.

## 6) Próximos pasos inmediatos

1. SOAP confirmado: usar JAX‑WS (Metro) en Tomcat 10.
2. Esqueleto del Servidor Central creado en `Laboratorio3Pap-main/central-ws` (Hecho, con mocks).
3. Generar stubs del cliente (`client-ws`) y adaptar servlets del Web para consumir SOAP.
4. Incorporar/validar RWD (Bootstrap 5 o CSS existente) en vistas clave.
5. Pruebas E2E vía SOAP.

## 7) Cómo ejecutar (estado actual)

- Web (estado actual):
  - Compilar y desplegar con `./deploy-tomcat10.sh`.
  - Abrir `http://localhost:8080/Laboratorio2PaP`.
- Central: (por crear en Fase 2). Se documentará la URL base y puerto al implementarlo.

## 8) Referencias
- [Bootstrap 5](https://getbootstrap.com)
- [RWD: grid y layouts](https://getbootstrap.com/docs/5.3/layout/grid/)
- [JAX‑RS (Jersey)](https://eclipse-ee4j.github.io/jersey/)
- [JAX‑WS (Metro)](https://eclipse-ee4j.github.io/metro-jax-ws/)
- [A survey of web service technologies](http://eprints.biblio.unitn.it/586/1/mike.pdf)

---
Última actualización: 2025‑11‑04
