<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Error del Servidor - Turismo.uy</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
    <jsp:include page="/WEB-INF/jsp/includes/navbar.jsp"/>
    
    <div class="container">
        <div class="hero-section">
            <div class="hero-icon">⚠️</div>
            <h1>500</h1>
            <p>Ha ocurrido un error en el servidor</p>
        </div>
        
        <div class="login-section">
            <div class="login-header">
                <h2>Lo sentimos</h2>
                <p>Estamos trabajando para solucionar el problema</p>
            </div>

            <div style="text-align:center;">
                <a href="${pageContext.request.contextPath}/" class="btn">Volver al inicio</a>
            </div>
        </div>
    </div>
</body>
</html>