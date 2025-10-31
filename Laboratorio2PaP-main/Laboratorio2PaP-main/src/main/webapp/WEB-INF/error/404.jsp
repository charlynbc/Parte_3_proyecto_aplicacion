<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Página no encontrada - Turismo.uy</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
    <jsp:include page="/WEB-INF/jsp/includes/navbar.jsp"/>
    
    <div class="container">
        <div class="hero-section">
            <div class="hero-icon">🔍</div>
            <h1>404</h1>
            <p>Lo sentimos, la página que buscas no existe</p>
        </div>
        
        <div class="login-section">
            <div class="login-header">
                <h2>¿Te has perdido?</h2>
                <p>No te preocupes, te ayudamos a volver</p>
            </div>

            <div style="text-align:center;">
                <a href="${pageContext.request.contextPath}/" class="btn">Volver al inicio</a>
            </div>
        </div>
    </div>
</body>
</html>