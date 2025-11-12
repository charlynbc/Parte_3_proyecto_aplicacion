# Cómo Demostrar los Web Services al Profesor

## 🎯 Qué Mostrar

El profesor querrá ver que:
1. ✅ El servidor Central expone servicios SOAP (puerto 9128)
2. ✅ El servidor Web consume esos servicios (puerto 8080)
3. ✅ La comunicación SOAP funciona correctamente

---

## 📋 Demostración Paso a Paso

### 1. Mostrar los WSDLs del Servidor Central

En una terminal, ejecutar:

```bash
# Servicio de Autenticación
curl http://localhost:9128/central-ws/services/AuthService?wsdl

# Servicio de Actividades
curl http://localhost:9128/central-ws/services/ActividadesService?wsdl

# Servicio de Usuarios
curl http://localhost:9128/central-ws/services/UsuariosService?wsdl

# Servicio de Paquetes
curl http://localhost:9128/central-ws/services/PaquetesService?wsdl
```

**Resultado esperado:** Cada comando debe devolver XML con `<definitions>`, `<service>`, `<binding>`, `<portType>`, etc.

---

### 2. Mostrar los Procesos Tomcat Activos

```bash
ps aux | grep tomcat
```

**Resultado esperado:** Dos procesos Java con:
- `-Dcatalina.base=/workspaces/Parte_3_proyecto_aplicacion/apache-tomcat-10.1.47-central` (puerto 9128)
- `-Dcatalina.base=/workspaces/Parte_3_proyecto_aplicacion/apache-tomcat-10.1.47` (puerto 8080)

---

### 3. Mostrar los Logs del Servidor Central

```bash
tail -n 50 /workspaces/Parte_3_proyecto_aplicacion/apache-tomcat-10.1.47-central/logs/catalina.out
```

**Buscar líneas como:**
```
INFO: Deploying web application archive [central-ws.war]
INFO: Deployment of web application archive [central-ws.war] has finished
```

---

### 4. Demostrar la Comunicación SOAP en Acción

#### a) Acceder al dashboard desde el navegador:
```
https://[tu-codespace]-8080.app.github.dev/Laboratorio2PaP/dashboard
```

#### b) En terminal, monitorear las peticiones SOAP:
```bash
# Seguir los logs del servidor Central en tiempo real
tail -f /workspaces/Parte_3_proyecto_aplicacion/apache-tomcat-10.1.47-central/logs/catalina.out
```

#### c) Realizar una acción en el navegador:
- Login
- Buscar actividades
- Ver paquetes

**Efecto:** Los logs mostrarán peticiones SOAP entrantes al servidor Central

---

### 5. Mostrar el Código de los Web Services

#### Servicios expuestos en Central:
```bash
# Estructura del proyecto Central
ls -la /workspaces/Parte_3_proyecto_aplicacion/Laboratorio3Pap-main/central-ws/src/main/java/webservices/
```

**Archivos clave:**
- `AuthService.java` - Autenticación
- `ActividadesService.java` - Gestión de actividades
- `UsuariosService.java` - Gestión de usuarios
- `PaquetesService.java` - Gestión de paquetes

#### Cliente consumiendo los servicios:
```bash
# Stubs generados desde los WSDLs
ls -la /workspaces/Parte_3_proyecto_aplicacion/Laboratorio3Pap-main/client-ws/src/main/java/client/
```

---

### 6. Probar un Servicio SOAP con SoapUI (Opcional)

Si el profesor quiere ver una prueba más técnica, usar **cURL con XML**:

```bash
curl -X POST http://localhost:9128/central-ws/services/AuthService \
  -H "Content-Type: text/xml;charset=UTF-8" \
  -H "SOAPAction: http://webservices/login" \
  -d '<?xml version="1.0" encoding="UTF-8"?>
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:web="http://webservices/">
   <soapenv:Header/>
   <soapenv:Body>
      <web:login>
         <email>turista@example.com</email>
         <password>pass123</password>
      </web:login>
   </soapenv:Body>
</soapenv:Envelope>'
```

**Resultado esperado:** Respuesta SOAP XML con el resultado del login

---

## 🔍 Script de Verificación Rápida

He creado un script que verifica todo:

```bash
./verificar-proyecto.sh
```

Este script muestra:
- ✅ Servidores activos
- ✅ WSDLs accesibles
- ✅ CSS responsive implementado
- ✅ Bootstrap eliminado
- ✅ Comunicación SOAP funcionando

---

## 📊 Arquitectura para Explicar

```
┌─────────────────────────────────────────────┐
│         NAVEGADOR (Browser)                 │
│  https://[codespace]-8080../dashboard       │
└──────────────────┬──────────────────────────┘
                   │ HTTP/HTTPS
                   ↓
┌─────────────────────────────────────────────┐
│    SERVIDOR WEB (Tomcat puerto 8080)        │
│    Laboratorio2PaP.war                      │
│    - JSPs (register-provider.jsp, etc.)     │
│    - CSS responsive (sin Bootstrap)         │
│    - Lógica de presentación                 │
└──────────────────┬──────────────────────────┘
                   │ SOAP/XML
                   │ (interno via localhost)
                   ↓
┌─────────────────────────────────────────────┐
│  SERVIDOR CENTRAL (Tomcat puerto 9128)      │
│  central-ws.war                             │
│  - AuthService (WSDL)                       │
│  - ActividadesService (WSDL)                │
│  - UsuariosService (WSDL)                   │
│  - PaquetesService (WSDL)                   │
│  - Lógica de negocio                        │
└──────────────────┬──────────────────────────┘
                   │ Acceso a datos
                   ↓
┌─────────────────────────────────────────────┐
│     Laboratorio1.jar (business logic)       │
│     - Clases de dominio                     │
│     - Lógica de negocio                     │
└─────────────────────────────────────────────┘
```

---

## 🎓 Puntos Clave para el Profesor

1. **Separación de capas:** Web (presentación) → Central (servicios) → Lab1 (negocio)
2. **Protocolo SOAP:** Comunicación estándar JAX-WS
3. **Sin Bootstrap:** CSS nativo responsive optimizado para Mac M1
4. **Puerto interno:** 9128 no es público, solo comunicación localhost SOAP
5. **Puerto público:** 8080 sirve la aplicación web completa

---

## ✅ Checklist de Demostración

- [ ] Mostrar ambos Tomcats corriendo (`ps aux | grep tomcat`)
- [ ] Acceder a los 4 WSDLs con `curl`
- [ ] Abrir la aplicación en el navegador (puerto 8080)
- [ ] Realizar login/registro y mostrar que funciona
- [ ] Mostrar logs del Central recibiendo peticiones SOAP
- [ ] Explicar arquitectura de 3 capas
- [ ] Mostrar código de un Web Service en `central-ws/`
- [ ] Mostrar código de consumo en `client-ws/`
- [ ] Demostrar CSS responsive redimensionando ventana
- [ ] Confirmar que no hay Bootstrap (`grep -r "bootstrap" src/`)

---

## 🚀 Comandos Rápidos para Copiar-Pegar

```bash
# Ver procesos Tomcat
ps aux | grep tomcat | grep -v grep

# Ver todos los WSDLs
echo "=== AuthService ===" && curl -s http://localhost:9128/central-ws/services/AuthService?wsdl | head -20
echo "=== ActividadesService ===" && curl -s http://localhost:9128/central-ws/services/ActividadesService?wsdl | head -20
echo "=== UsuariosService ===" && curl -s http://localhost:9128/central-ws/services/UsuariosService?wsdl | head -20
echo "=== PaquetesService ===" && curl -s http://localhost:9128/central-ws/services/PaquetesService?wsdl | head -20

# Monitorear logs en tiempo real
tail -f /workspaces/Parte_3_proyecto_aplicacion/apache-tomcat-10.1.47-central/logs/catalina.out

# Verificar que no hay Bootstrap
grep -r "bootstrap" /workspaces/Parte_3_proyecto_aplicacion/Laboratorio2PaP-main/src/ 2>/dev/null || echo "✅ Bootstrap eliminado correctamente"

# Ver tamaño de los WARs generados
ls -lh /workspaces/Parte_3_proyecto_aplicacion/Laboratorio3Pap-main/central-ws/target/*.war
ls -lh /workspaces/Parte_3_proyecto_aplicacion/Laboratorio2PaP-main/target/*.war
```
