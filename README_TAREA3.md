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

Nota sobre la elección técnica: por simplicidad de despliegue en Tomcat, propondremos REST JSON (JAX‑RS). Si se requiere estrictamente SOAP, dejaremos el plan de alternativa con Metro/CXF.

## 3) Plan por fases (qué haremos, qué se borra y dónde queda lo nuevo)

Fase 0 – Rama y documentación
- Crear rama de trabajo: `Parte_3`. (Hecho)
- Este documento: `README_TAREA3.md`. (Hecho)
- Enlazar desde `README.md`. (Hecho)

Fase 1 – Definición de contratos (API del Servidor Central)
- Diseñar endpoints/operaciones para:
  - Autenticación: POST `/api/auth/login` → {nickname/email, password} → {perfil + tipoUsuario}
  - Logout: POST `/api/auth/logout`
  - Listado/consulta de actividades: GET `/api/actividades`, GET `/api/actividades/{id}` (incluye salidas + imagen)
  - Detalle de salida: GET `/api/salidas/{id}`
- Especificar formatos JSON (request/response) y códigos de error.
- Entregable: documento de contratos en `docs/api-contratos.md` (por crear).

Fase 2 – Servidor Central (API REST)
- Nuevo módulo o carpeta `central/` (o reutilizar proyecto actual separando responsabilidades) con:
  - Recurso JAX‑RS `AuthResource` (login/logout).
  - Recurso `ActividadesResource` y `SalidasResource`.
  - DTOs serializables para requests/responses (evitar exponer entidades JPA en crudo).
- Seguridad básica (sesión/token ligero) para identificar al usuario entre llamadas si hace falta.
- Entregable: WAR del central (o contexto separado) corriendo en Tomcat.

Fase 3 – Servidor Web (cliente de la API)
- Reemplazar usos del jar de lógica por invocaciones HTTP al Central.
- Actualizar Servlets:
  - `LoginServlet` → invoca `/api/auth/login` y setea sesión local con el resultado.
  - `ActivitiesServlet`, `ActivityDetailServlet` → invocan `/api/actividades` y `/api/actividades/{id}`.
  - `InscripcionServlet`/`MyRegistrationsServlet` → adaptar si el caso de uso está incluido en la iteración.
- Añadir un cliente HTTP reutilizable (por ejemplo, `utils/ApiClient.java`) con manejo de baseURL, timeouts, JSON (Jackson), y errores.
- Entregable: sitio web funcionando contra la API, sin acoplar jar de lógica.

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

## 4) Diseño de contratos (borrador rápido)

- POST `/api/auth/login`
  - Request: `{ "identifier": "<nick|mail>", "password": "<pwd>" }`
  - Response 200: `{ "nickname": "turi1", "nombre":"Ana", "tipoUsuario":"turista" }`
  - 401: `{ "error": "Credenciales inválidas" }`

- GET `/api/actividades`
  - Response 200: `[{ "id":"Kayak Colonia", "lugar":"Colonia", "costo":1500, "imagen": "https://..." }, ...]`

- GET `/api/actividades/{id}`
  - Response 200: `{ "id":"Kayak Colonia", "descripcion":"...", "salidas":[{"id":"Salida-Kayak-1", "fecha":"2025-11-21", "hora":"10:00"}, ...] }`
  - 404: `{ "error": "Actividad no encontrada" }`

- GET `/api/salidas/{id}`
  - Response 200: `{ "id":"Salida-Kayak-1", "lugar":"Puerto de Yates", "fecha":"2025-11-21", "hora":"10:00" }`

(Estos contratos se precisarán en `docs/api-contratos.md`.)

## 5) Qué ya hicimos en esta rama (Parte_3)

- Sección “Mis Salidas” (Turista), con filtro de acceso por rol y vista responsive con tarjetas.
- Unificación de estilos de tarjetas (removidos estilos inline; uso de `cards.css`).
- Estabilización de despliegue en Tomcat 10.1 y conexión a DB Railway.
- Documentación inicial (este archivo) y enlace desde `README.md`.

## 6) Próximos pasos inmediatos

1. Acordar REST (JAX‑RS) vs SOAP (JAX‑WS). Por simplicidad, propondremos REST.
2. Crear esqueleto de Servidor Central con endpoints de login y actividades.
3. Implementar `ApiClient` en el Servidor Web y adaptar `LoginServlet` y `ActivitiesServlet` a la API.
4. Incorporar Bootstrap 5 (si aplicamos framework RWD) o validar que el CSS actual cumple RWD.
5. Pruebas de extremo a extremo (login → actividades → detalle) exclusivamente vía servicios web.

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
