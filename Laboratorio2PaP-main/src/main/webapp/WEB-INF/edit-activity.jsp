<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="uy.edu.pa.central.client.ActividadDTO" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Editar Actividad</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
    
<jsp:include page="/WEB-INF/jsp/includes/navbar.jsp"/>

<div class="main-content">
    <div class="registration-container">
        <div class="registration-hero">
            <div class="hero-icon">✏️</div>
            <h1>Editar Actividad</h1>
            <p>Actualiza la información de tu actividad.</p>
        </div>
        <div class="registration-form-section">
            <div class="form-header">
                <h2>Modificar Actividad</h2>
                <p>Cambia los campos que desees actualizar.</p>
            </div>

            <jsp:include page="/WEB-INF/jsp/includes/messages.jsp"/>

            <%
                ActividadDTO actividad = (ActividadDTO) request.getAttribute("actividad");
                if (actividad != null) {
            %>
            <form method="post" action="${pageContext.request.contextPath}/edit-activity" class="registration-form" enctype="multipart/form-data">
                <input type="hidden" name="nombre" value="<%= actividad.getId() %>">
                
                <fieldset>
                    <legend>Información Básica</legend>
                    
                    <div class="form-group">
                        <label for="nombre">Nombre de la Actividad</label>
                        <input type="text" id="nombre" value="<%= actividad.getId() %>" disabled
                            style="background:#f5f5f5;cursor:not-allowed;">
                        <div class="form-hint">El nombre no se puede modificar</div>
                    </div>
                    
                    <div class="form-group">
                        <label for="descripcion">Descripción <span class="required">*</span></label>
                        <textarea id="descripcion" name="descripcion" required
                                  placeholder="Describe la actividad"><%= actividad.getDescripcion() != null ? actividad.getDescripcion() : "" %></textarea>
                    </div>
                </fieldset>

                <fieldset>
                    <legend>Detalles de la Actividad</legend>
                    
                    <div class="form-row">
                        <div class="form-group">
                            <label for="duracion">Duración (horas) <span class="required">*</span></label>
                            <input type="number" id="duracion" name="duracion" required min="1"
                                   value="<%= actividad.getDuracion() > 0 ? actividad.getDuracion() : 4 %>"
                                   placeholder="Ej: 3">
                        </div>
                        
                        <div class="form-group">
                            <label for="costo">Costo (UYU) <span class="required">*</span></label>
                            <input type="number" id="costo" name="costo" required min="0" step="0.01"
                                   value="<%= actividad.getCosto() %>"
                                   placeholder="Ej: 1500.00">
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label for="ciudad">Ciudad <span class="required">*</span></label>
                        <input type="text" id="ciudad" name="ciudad" required
                               value="<%= actividad.getLugar() != null ? actividad.getLugar() : "" %>"
                               placeholder="Ej: Montevideo">
                    </div>
                </fieldset>

                <fieldset>
                    <legend>Imagen de la Actividad</legend>
                    
                    <% if (actividad.getImagen() != null && !actividad.getImagen().trim().isEmpty()) { %>
                    <div class="form-group">
                        <label>Imagen Actual</label>
                        <div style="margin:10px 0;">
                            <img src="<%= actividad.getImagen() %>" alt="Imagen actual" 
                                 style="max-width:300px;max-height:200px;border-radius:8px;border:1px solid #ddd;">
                        </div>
                    </div>
                    <% } %>
                    
                    <div class="form-group">
                        <label for="activityImage">Nueva Imagen (Opcional)</label>
                        <input type="file" id="activityImage" name="activityImage" accept="image/*">
                        <div class="form-hint">Selecciona una nueva imagen solo si deseas cambiarla</div>
                    </div>
                </fieldset>

                <div class="form-actions">
                    <button type="submit" class="btn">💾 Guardar Cambios</button>
                    <a href="${pageContext.request.contextPath}/my-activities" class="btn-secondary">Cancelar</a>
                </div>
            </form>
            <% } else { %>
                <p>No se encontró la actividad.</p>
            <% } %>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/js/app.js"></script>
</body>
</html>
