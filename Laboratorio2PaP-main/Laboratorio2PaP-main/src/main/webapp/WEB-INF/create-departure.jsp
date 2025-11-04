<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Crear Nueva Salida</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>

<jsp:include page="/WEB-INF/jsp/includes/navbar.jsp"/>

<div class="main-content">
    <div class="registration-container">
        <div class="registration-hero">
            <div class="hero-icon">🚌</div>
            <h1>Nueva Salida</h1>
            <p>Programa una nueva salida para tu actividad.</p>
        </div>
        <div class="registration-form-section">
            <div class="form-header">
                <h2>Crear Nueva Salida</h2>
                <p>Completa los campos para registrar la salida.</p>
            </div>

            <jsp:include page="/WEB-INF/jsp/includes/messages.jsp"/>

            <form method="post" action="${pageContext.request.contextPath}/CreateDepartureServlet" enctype="multipart/form-data" class="registration-form">
                <fieldset>
                    <legend>Información de la Salida</legend>
                    
                    <div class="form-group">
                        <label for="nombreSalida">Nombre de la Salida <span class="required">*</span></label>
                        <input type="text" id="nombreSalida" name="nombreSalida" required placeholder="Ej: City Tour Punta del Este">
                    </div>

                    <%
                        logica.DataActividad[] actividadesArr = (logica.DataActividad[]) request.getAttribute("actividades");
                        if (actividadesArr == null || actividadesArr.length == 0) {
                    %>
                        <div class="alert alert-error">
                            No tienes actividades registradas. Debes crear una actividad antes de poder crear una salida.
                        </div>
                    <%
                        } else {
                    %>
                        <div class="form-group">
                            <label for="activityId">Actividad <span class="required">*</span></label>
                            <select id="activityId" name="activityId" required>
                                <%= request.getAttribute("actividadesHtml") != null ? request.getAttribute("actividadesHtml") : "" %>
                            </select>
                        </div>
                    <% } %>
                </fieldset>

                <fieldset>
                    <legend>Fecha y Hora</legend>
                    
                    <div class="form-row">
                        <div class="form-group">
                            <label for="departureDate">Fecha <span class="required">*</span></label>
                            <input type="date" id="departureDate" name="departureDate" required>
                        </div>
                        <div class="form-group">
                            <label for="departureTime">Hora <span class="required">*</span></label>
                            <input type="time" id="departureTime" name="departureTime" required>
                        </div>
                    </div>
                </fieldset>

                <fieldset>
                    <legend>Detalles del Encuentro</legend>
                    
                    <div class="form-row">
                        <div class="form-group">
                            <label for="meetingPlace">Lugar <span class="required">*</span></label>
                            <input type="text" id="meetingPlace" name="meetingPlace" required placeholder="Ej: Playa Brava, entrada principal">
                        </div>
                        <div class="form-group">
                            <label for="maxTourists">Cupo <span class="required">*</span></label>
                            <input type="number" id="maxTourists" name="maxTourists" required min="1" max="50">
                            <div class="form-hint">Máximo de turistas permitidos</div>
                        </div>
                    </div>
                </fieldset>

                <fieldset>
                    <legend>Imagen de la Salida</legend>
                    
                    <div class="form-group">
                        <label for="departureImage">Imagen de la Salida (Opcional)</label>
                        <div class="file-input">
                            <input type="file" id="departureImage" name="departureImage" accept="image/*">
                            <label for="departureImage" class="file-input-label">
                                📷 Seleccionar imagen de la salida
                            </label>
                            <span class="file-name"></span>
                        </div>
                    </div>
                </fieldset>

                <div class="form-actions">
                    <button type="submit" class="btn">Crear Salida</button>
                    
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
document.getElementById('departureImage').addEventListener('change', function(e) {
    const file = e.target.files[0];
    const fileNameSpan = this.parentElement.querySelector('.file-name');
    if (file) {
        fileNameSpan.textContent = file.name;
    }
});
</script>
</body>
</html>
