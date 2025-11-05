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
    
    <div class="main-content container">
        <div class="page-header" style="text-align:center;">
            <div style="font-size:2.5rem;">👋</div>
            <h1>Bienvenido, ${sessionScope.username}</h1>
            <p>¿Qué deseas hacer hoy?</p>
        </div>
        <div class="navigation-grid">
            <%
                String userType = (String) session.getAttribute("userType");
                if ("Turista".equals(userType)) {
            %>
            <a href="${pageContext.request.contextPath}/activities" class="nav-card">🎯 Actividades Turísticas</a>
            <%
                }
                if ("Proveedor".equals(userType)) {
            %>
            <a href="${pageContext.request.contextPath}/create-departure" class="nav-card">🚌 Nueva Salida Turística</a>
            <a href="${pageContext.request.contextPath}/create-activity" class="nav-card">📊 Nueva Actividad</a>
            <%
                }
            %>
            <a href="${pageContext.request.contextPath}/user-profile" class="nav-card">👤 Mi Perfil</a>
            <a href="${pageContext.request.contextPath}/logout" class="nav-card">🚪 Cerrar Sesión</a>
        </div>
    </div>
    
    <script src="${pageContext.request.contextPath}/js/app.js"></script>
</body>
</html>
