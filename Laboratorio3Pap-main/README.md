# Laboratorio 3 PaP – SOAP (Servidor Central + Contratos)

Aquí se guardará TODO lo nuevo de la iteración con Web Services (SOAP).

## Módulos

- `central-ws/` (WAR): Servidor Central SOAP con Metro JAX‑WS (Jakarta) en Tomcat 10.
  - Endpoints iniciales: `AuthService` y `ActividadesService`.
  - Mock de datos por ahora (luego se conecta a DB del Central).

Próximos (a crear):
- `client-ws/` (lib): Stubs del cliente generados desde WSDL para consumir desde el Servidor Web.
- `docs/` (notas, WSDLs, contratos definitivos).

## Cómo compilar y desplegar `central-ws` (puerto 8081)

Requisitos:
- Java 11
- Maven 3.6+

Pasos (usa una instancia Tomcat separada en 8081):
```bash
# Arrancar Tomcat central y desplegar
./deploy-central.sh

# Ver logs del central
./scripts/tomcat-central.sh logs
```

Endpoints (una vez desplegado en 8081):
- WSDL Auth: `http://localhost:8081/central-ws/services/AuthService?wsdl`
- WSDL Actividades: `http://localhost:8081/central-ws/services/ActividadesService?wsdl`

Pruebas rápidas:
- `AuthService.ping()` → "pong".
- `AuthService.login(identifier, password)` → UserDTO simulado.
- `ActividadesService.listarActividades()` → lista simulada con una actividad y salida.

## Notas
- Todo el código nuevo de SOAP vive aquí para mantener separada la iteración 3.
- La integración del Servidor Web existente consumirá estos servicios (sin jar de lógica).
- Si el curso exige SOAP (JAX‑WS), nos quedamos en SOAP. Si nos permiten REST, dejamos constancia pero continuamos con SOAP como pediste.

---
Actualizado: 2025‑11‑04
