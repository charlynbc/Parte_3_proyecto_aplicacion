# Laboratorio 1 - Programación de Aplicaciones

# README - Capa de Presentación (Swing)

## Qué se hizo
- Se desarrolló la capa de presentación (package `presentacion`) con ventanas Swing.
- Cada pantalla (Principal, CrearUsuario, ConsultarUsuario, ListaUsuarios) funciona de manera aislada y sin depender de la capa de lógica.
- Se dejaron bloques de código comentados con `TODO` indicando dónde va la integración con la lógica.
- Se usaron "stubs" (datos de prueba) para que las pantallas puedan abrirse y validarse visualmente aunque todavía no haya lógica implementada.

## Por qué se hizo así
- Para **separar responsabilidades**: la presentación solo maneja la interfaz y validaciones básicas, mientras que la lógica manejará persistencia y reglas de negocio.
- Para que el proyecto **compile y corra desde ya**, sin esperar a tener implementada la lógica.
- Para que los compañeros puedan **seguir trabajando en paralelo**: la lógica puede evolucionar y luego simplemente conectarse en los puntos marcados.
- Para que el equipo tenga un **contrato claro**: dónde enchufar controladores, manejadores o DTOs sin revisar toda la GUI.

## Qué falta en cada clase
- **Principal**: inyectar controladores/manejadores reales al abrir los InternalFrames.
- **CrearUsuario**: reemplazar stub por alta real de usuario en la lógica.
- **ConsultarUsuario**: traer usuario real por CI o nickname y mostrarlo.
- **ListaUsuarios**: poblar lista con usuarios reales y mostrar detalle.
- **AltaActividad**: registrar la actividad real en la lógica.

## Estado actual
La interfaz es navegable y compila sin errores.  
No persiste ni consulta datos: solo muestra stubs y validaciones básicas.  
Queda lista para que la lógica se integre en los puntos comentados.

