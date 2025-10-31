<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Crear Nueva Actividad</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
    
<jsp:include page="/WEB-INF/jsp/includes/navbar.jsp"/>

<div class="main-content">
    <div class="registration-container">
        <div class="registration-hero">
            <div class="hero-icon">🎯</div>
            <h1>Nueva Actividad</h1>
            <p>Comparte tu experiencia turística con otros.</p>
        </div>
        <div class="registration-form-section">
            <div class="form-header">
                <h2>Crear Nueva Actividad</h2>
                <p>Completa los campos para publicar una actividad.</p>
            </div>

            <jsp:include page="/WEB-INF/jsp/includes/messages.jsp"/>

            <form method="post" action="${pageContext.request.contextPath}/create-activity" class="registration-form">
                <fieldset>
                    <legend>Información Básica</legend>
                    
                    <div class="form-group">
                        <label for="nombre">Nombre de la Actividad <span class="required">*</span></label>
                        <input type="text" id="nombre" name="nombre" required
                            placeholder="Ej: Surf en Punta del Este">
                    </div>
                    
                    <div class="form-group">
                        <label for="descripcion">Descripción <span class="required">*</span></label>
                        <textarea id="descripcion" name="descripcion" required
                                  placeholder="Describe la actividad, qué incluye, requisitos, etc."></textarea>
                        <div class="form-hint">Describe detalladamente qué incluye la actividad</div>
                    </div>
                </fieldset>

                <fieldset>
                    <legend>Detalles de la Actividad</legend>
                    
                    <div class="form-row">
                        <div class="form-group">
                            <label for="duracion">Duración (horas) <span class="required">*</span></label>
                            <input type="number" id="duracion" name="duracion" required min="0.5" step="0.5">
                            <div class="form-hint">Duración estimada en horas</div>
                        </div>
                        <div class="form-group">
                            <label for="costo">Costo (UYU) <span class="required">*</span></label>
                            <input type="number" id="costo" name="costo" required min="0" step="50">
                            <div class="form-hint">Precio por persona</div>
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label for="ciudad">Ciudad <span class="required">*</span></label>
                        <select id="ciudad" name="ciudad" required>
                            <option value="">Selecciona una ciudad</option>
                            <option value="Montevideo">Montevideo</option>
                            <option value="Punta del Este">Punta del Este</option>
                            <option value="Colonia del Sacramento">Colonia del Sacramento</option>
                            <option value="Salto">Salto</option>
                            <option value="Paysandú">Paysandú</option>
                            <option value="Maldonado">Maldonado</option>
                            <option value="Rivera">Rivera</option>
                            <option value="Tacuarembó">Tacuarembó</option>
                        </select>
                    </div>
                </fieldset>

                <fieldset>
                    <legend>Imagen de la Actividad</legend>
                    
                    <div class="form-group">
                        <label for="activityImage">Imagen de la Actividad (Opcional)</label>
                        <div class="file-input">
                            <input type="file" id="activityImage" name="activityImage" accept="image/*">
                            <label for="activityImage" class="file-input-label">
                                📷 Seleccionar imagen de la actividad
                            </label>
                            <span class="file-name"></span>
                        </div>
                        <div class="form-hint">JPG, PNG. Máximo 5MB</div>
                    </div>
                </fieldset>

                <div class="form-actions">
                    <button type="submit" class="btn">Crear Actividad</button>
                    
                    <div class="login-footer">
                        <a href="${pageContext.request.contextPath}/activities" class="back-link">Volver a actividades</a>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/js/app.js"></script>
<script>
document.getElementById('activityImage').addEventListener('change', function(e) {
    const file = e.target.files[0];
    const fileNameSpan = this.parentElement.querySelector('.file-name');
    if (file) {
        fileNameSpan.textContent = file.name;
    }
});
</script>
</body>
</html>
