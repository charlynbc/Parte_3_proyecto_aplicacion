<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
    <div class="main-content container">
        <div class="card">
            <div class="card-content">
                <h1 style="margin-bottom: 0.5rem;">Inscripción</h1>
                <c:if test="${not empty success}">
                    <div class="alert" role="alert">${success}</div>
                </c:if>
                <p>Se registró la inscripción correctamente.</p>
                <div class="form-actions" style="display:flex; gap:0.75rem;">
                    <a href="${pageContext.request.contextPath}/dashboard" class="btn">Ir al Dashboard</a>
                    <a href="${pageContext.request.contextPath}/" class="register-btn">Volver al inicio</a>
                </div>
            </div>
        </div>
    </div>
</body>
</html>