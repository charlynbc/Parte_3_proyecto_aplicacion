# Guía de Pruebas - Interfaz Móvil Responsive

## 🧪 Cómo Probar la Interfaz Móvil

### Opción 1: Chrome DevTools (Recomendado)

1. **Abrir la aplicación en Chrome:**
   ```
   http://localhost:8080/Laboratorio2PaP/
   ```

2. **Activar DevTools:**
   - Presiona `F12` o `Ctrl+Shift+I` (Linux/Windows)
   - Presiona `Cmd+Option+I` (Mac)

3. **Activar modo dispositivo:**
   - Presiona `Ctrl+Shift+M` o clic en el ícono 📱 en DevTools
   - Alternativamente: Menu → More tools → Device toolbar

4. **Probar diferentes dispositivos:**
   - **iPhone SE (320x568)** - Móvil pequeño
   - **iPhone 12 Pro (390x844)** - Móvil estándar
   - **Samsung Galaxy S20 (360x800)** - Android
   - **iPad (768x1024)** - Tablet
   - **Responsive** - Ajusta manualmente el ancho

5. **Probar orientaciones:**
   - Clic en el ícono de rotación para cambiar portrait/landscape
   - Observar cómo se adapta el layout

### Opción 2: Firefox Responsive Design Mode

1. **Abrir la aplicación:**
   ```
   http://localhost:8080/Laboratorio2PaP/
   ```

2. **Activar RDM:**
   - Presiona `Ctrl+Shift+M`
   - O Menu → More Tools → Responsive Design Mode

3. **Seleccionar dispositivos preset o personalizar**

### Opción 3: Dispositivo Real (Opcional)

Si tienes acceso al puerto del Codespace:

1. **Obtener URL pública del Codespace:**
   - GitHub Codespaces debe exponer el puerto 8080
   - URL formato: `https://<codespace-name>-8080.app.github.dev/Laboratorio2PaP/`

2. **Acceder desde tu móvil/tablet**
   - Asegúrate de estar conectado a internet
   - Abre la URL en Safari (iOS) o Chrome (Android)

## ✅ Checklist de Pruebas

### 📱 Pantalla de Login (320px - Mobile)
- [ ] Formulario ocupa todo el ancho
- [ ] Inputs tienen altura mínima 44px (fácil de tocar)
- [ ] Botón "Iniciar Sesión" es full-width
- [ ] Logo y título se reducen apropiadamente
- [ ] No hay zoom automático al tocar inputs
- [ ] Mensajes de error son legibles
- [ ] Links "Registrar" visibles y accesibles

**Prueba:**
```
Usuario: pabloFer (proveedor) o carmelo123 (turista)
Password: password123
```

### 📱 Dashboard (390px - iPhone)
- [ ] Cards de acciones apiladas verticalmente
- [ ] Iconos y texto legibles sin zoom
- [ ] Botones tienen espacio suficiente entre ellos
- [ ] Navbar colapsa y muestra menú hamburguesa
- [ ] Nombre de usuario visible en header
- [ ] Footer se adapta correctamente

**Prueba:**
- Iniciar sesión
- Observar adaptación del dashboard
- Probar navegación del menú colapsable

### 📱 Lista de Actividades (480px)
- [ ] Cards de actividad en columna única
- [ ] Imágenes responsive (width: 100%)
- [ ] Texto descriptivo legible
- [ ] Botón "Ver Detalles" accesible
- [ ] Scroll suave en listas largas
- [ ] Filtros se apilan verticalmente

**Prueba:**
- Ir a "Consultar Actividades"
- Scroll por la lista
- Verificar que todas las cards sean visibles

### 📱 Detalle de Actividad (768px - Tablet)
- [ ] Layout de 2 columnas en landscape
- [ ] Layout de 1 columna en portrait
- [ ] Galería de imágenes adaptativa
- [ ] Descripción completa legible
- [ ] Datos de salidas en tabla responsive
- [ ] Botón inscripción destacado

**Prueba:**
- Seleccionar una actividad (ej: "Rafting en el Río Negro")
- Rotar dispositivo (portrait/landscape)
- Verificar adaptación del layout

### 📱 Formularios (Crear/Editar)
- [ ] Labels encima de inputs
- [ ] Inputs full-width en móvil
- [ ] Selectores fáciles de tocar
- [ ] Textarea con altura adecuada
- [ ] Botones de acción diferenciados
- [ ] Validación visible claramente

**Prueba (si eres Proveedor):**
- Ir a "Crear Actividad"
- Intentar rellenar formulario
- Verificar usabilidad en móvil

### 📱 Perfil de Usuario
- [ ] Información apilada verticalmente
- [ ] Avatar/imagen de perfil responsive
- [ ] Botón "Editar Perfil" accesible
- [ ] Campos de datos legibles
- [ ] Mis Inscripciones en cards apiladas

**Prueba:**
- Ir a "Mi Perfil"
- Verificar adaptación de información personal
- Probar botón "Editar Perfil"

## 🎨 Aspectos Visuales a Verificar

### Tipografía
- [ ] Títulos h1: ~1.75rem en móvil (vs 2.5rem desktop)
- [ ] Texto body: 14px en móvil (vs 16px desktop)
- [ ] Line-height adecuado para lectura
- [ ] Contraste suficiente (WCAG AA)

### Espaciado
- [ ] Padding interno: 1rem (vs 2rem desktop)
- [ ] Margin entre elementos: 0.5-1rem
- [ ] Gap en grids: 1rem (vs 1.5rem desktop)

### Touch Targets
- [ ] Botones: min-height 44px
- [ ] Links: min-height 44px
- [ ] Inputs: height ~44-48px
- [ ] Selectores: height ~44-48px

### Performance
- [ ] Transiciones suaves sin lag
- [ ] Scroll fluido
- [ ] Imágenes cargan rápidamente
- [ ] No hay reflows excesivos

## 🐛 Posibles Problemas y Soluciones

### Problema: Zoom automático al tocar input (iOS)
**Causa:** Font-size menor a 16px  
**Solución:** ✅ Ya implementado en `mobile.css`
```css
input, select, textarea {
    font-size: 16px !important;
}
```

### Problema: Botones muy pequeños para tocar
**Causa:** Height insuficiente  
**Solución:** ✅ Ya implementado (min-height: 44px)

### Problema: Layout no se adapta
**Causa:** Falta meta viewport  
**Solución:** ✅ Ya agregado en `head.jsp`
```html
<meta name="viewport" content="width=device-width, initial-scale=1.0">
```

### Problema: CSS mobile no carga
**Causa:** Cache del navegador  
**Solución:**
- Hard refresh: `Ctrl+Shift+R` (Chrome) o `Cmd+Shift+R` (Mac)
- Abrir DevTools → Network → Disable cache

### Problema: Navbar no colapsa
**Causa:** JavaScript del toggle no implementado  
**Nota:** El CSS está listo, solo falta agregar JS para el botón hamburguesa

## 📊 Resultados Esperados

### ✅ Breakpoint 320px (iPhone SE)
- Layout minimal funcional
- Single column
- Texto legible sin zoom
- Botones accesibles

### ✅ Breakpoint 480px (Mobile Standard)
- Espaciado mejorado
- Formularios optimizados
- Cards más amplias

### ✅ Breakpoint 768px (Tablet)
- Posibilidad de 2 columnas en landscape
- Navbar expandida
- Mayor aprovechamiento del espacio

### ✅ Breakpoint 1024px+ (Desktop)
- Layout completo multi-columna
- Sidebars visibles
- Experiencia completa

## 📸 Screenshots Recomendados

Capturar pantallas de:
1. Login en 320px (portrait)
2. Dashboard en 390px (portrait)
3. Lista actividades en 480px (portrait)
4. Detalle actividad en 768px (landscape)
5. Formulario crear actividad en 360px

## 🔗 URLs de Prueba

```bash
# Aplicación Web
http://localhost:8080/Laboratorio2PaP/

# Páginas específicas
http://localhost:8080/Laboratorio2PaP/login
http://localhost:8080/Laboratorio2PaP/activities
http://localhost:8080/Laboratorio2PaP/dashboard

# Web Services (Central)
http://localhost:9128/central-ws/services/TurismoService?wsdl
```

## 🎯 Criterios de Éxito

La implementación móvil es **exitosa** si:

1. ✅ **Funcional**: Todos los casos de uso funcionan en móvil
2. ✅ **Usable**: Touch targets ≥44px, texto legible sin zoom
3. ✅ **Adaptable**: Layout se ajusta en todos los breakpoints
4. ✅ **Performance**: No lag ni reflows excesivos
5. ✅ **Accesible**: Cumple WCAG 2.1 AA para móviles
6. ✅ **Compatible**: Funciona en Chrome, Safari, Firefox Mobile

---

**Última actualización:** 2025-11-12  
**Versión:** 1.0.0
