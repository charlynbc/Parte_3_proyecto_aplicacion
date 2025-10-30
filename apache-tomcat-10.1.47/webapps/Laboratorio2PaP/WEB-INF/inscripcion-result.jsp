<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Inscripción - Resultado</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/buttons.css">
    
</head>
<body>
    <jsp:include page="/WEB-INF/jsp/includes/navbar.jsp" />
    <div class="result">
        <h2>Inscripción</h2>
        <c:if test="${not empty success}">
            <div class="success-message">${success}</div>
        </c:if>
        <p>Se registró la inscripción correctamente.</p>
        <div style="margin-top:20px;">
            <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-primary">Ir al Dashboard</a>
            <a href="${pageContext.request.contextPath}/" class="btn btn-secondary">Volver al inicio</a>
        </div>
    </div>
</body>
</html>