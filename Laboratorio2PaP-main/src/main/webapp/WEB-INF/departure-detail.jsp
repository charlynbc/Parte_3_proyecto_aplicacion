<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Detalle de Salida</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
    <jsp:include page="/WEB-INF/jsp/includes/navbar.jsp"/>

    <div class="main-content container">
        <div class="page-header">
            <h1>Salida: ${departure.nombre}</h1>
            <p>Información detallada de la salida seleccionada</p>
        </div>
        <div class="card">
            <div class="card-content">
                <div class="info-grid" style="display:grid; grid-template-columns: repeat(auto-fit, minmax(220px, 1fr)); gap: 1rem;">
                    <div class="info-item" style="border:2px solid #e1e5e9; border-radius:8px; padding:1rem;">
                        <div class="info-label">Fecha</div>
                        <div class="info-value">${departure.fecha}</div>
                    </div>
                    <div class="info-item" style="border:2px solid #e1e5e9; border-radius:8px; padding:1rem;">
                        <div class="info-label">Hora</div>
                        <div class="info-value">${departure.hora}</div>
                    </div>
                    <div class="info-item" style="border:2px solid #e1e5e9; border-radius:8px; padding:1rem; grid-column: 1 / -1;">
                        <div class="info-label">Lugar</div>
                        <div class="info-value">${departure.lugar}</div>
                    </div>
                </div>
                <div class="form-actions" style="display:flex; gap:0.75rem; margin-top:1rem;">
                    <a href="${pageContext.request.contextPath}/activities" class="register-btn">Volver</a>
                    <c:if test="${sessionScope.userType eq 'Turista'}">
                        <a href="${pageContext.request.contextPath}/inscripcion?salida=${departure.nombre}" class="btn">Inscribirse</a>
                    </c:if>
                </div>
            </div>
        </div>
    </div>

    <script src="${pageContext.request.contextPath}/js/app.js"></script>
</body>
</html>
