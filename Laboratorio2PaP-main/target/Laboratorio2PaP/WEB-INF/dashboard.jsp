<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Dashboard - Turismo.uy</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body style="display:block; background: #f5f7fa;">
    
    <jsp:include page="/WEB-INF/jsp/includes/navbar.jsp"/>
    
    <div class="container mt-4" style="min-height: auto;">
        <div class="hero-section">
            <div class="hero-icon">👋</div>
            <h1>Bienvenido, ${sessionScope.username}</h1>
        </div>

        <div class="login-section" style="background: #fff;">
            <div class="login-header">
                <h2>Panel de Control</h2>
                <p>¿Qué deseas hacer hoy?</p>
            </div>

            <div class="register-links" style="border-top: none; display: grid; grid-template-columns: repeat(auto-fit, minmax(250px, 1fr)); gap: 20px;">
                
                <%
                    String userType = (String) session.getAttribute("userType");
                    if ("Turista".equals(userType)) {
                %>
                    <a href="${pageContext.request.contextPath}/activities" class="register-btn" style="width: 100%; text-align: center;">
                        🎯 Actividades Turísticas
                    </a>
                    
                <%
                    }
                %>
                
                <%
                    if ("Proveedor".equals(userType)) {
                %>
                    <a href="${pageContext.request.contextPath}/create-departure" class="register-btn" style="width: 100%; text-align: center;">
                        🚌 Nueva Salida Turística
                    </a>

                    <a href="${pageContext.request.contextPath}/create-activity" class="register-btn" style="width: 100%; text-align: center;">
                        📊 Nueva Actividad
                    </a>
                <%
                    }
                %>
                
                <a href="${pageContext.request.contextPath}/user-profile" class="register-btn" style="width: 100%; text-align: center;">
                    👤 Mi Perfil
                </a>
                
                <a href="${pageContext.request.contextPath}/logout" class="register-btn" style="width: 100%; text-align: center; border-color: #dc3545; color: #dc3545;">
                    🚪 Cerrar Sesión
                </a>
            </div>
        </div>
    </div>
    
    <script src="${pageContext.request.contextPath}/js/app.js"></script>
</body>
</html>
