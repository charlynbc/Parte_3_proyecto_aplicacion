<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <jsp:include page="/WEB-INF/jsp/includes/head.jsp"/>
    <title>Dashboard - Turismo.uy</title>
</head>
<body style="display:block; background: #f5f7fa;">
    
    <jsp:include page="/WEB-INF/jsp/includes/navbar.jsp"/>
    
    <div class="main-content container">
        <div class="page-header" style="text-align:center;">
            <%
                String userImage = (String) session.getAttribute("userImage");
                boolean hasImage = userImage != null && !userImage.trim().isEmpty();
            %>
            <% if (hasImage) { %>
                <img src="<%= userImage %>" alt="Foto de perfil" 
                     style="width:100px;height:100px;border-radius:50%;object-fit:cover;border:3px solid #667eea;margin-bottom:1rem;">
            <% } else { %>
                <div style="font-size:2.5rem;">👋</div>
            <% } %>
            <h1>Bienvenido, ${sessionScope.username}</h1>
            <%
                String userType = (String) session.getAttribute("userType");
                String userTypeDisplay = "";
                String userTypeIcon = "";
                if ("Turista".equals(userType)) {
                    userTypeDisplay = "Turista";
                    userTypeIcon = "🧳";
                } else if ("Proveedor".equals(userType)) {
                    userTypeDisplay = "Proveedor de Servicios";
                    userTypeIcon = "🏢";
                }
            %>
            <p style="margin-top:0.5rem;"><%= userTypeIcon %> <strong><%= userTypeDisplay %></strong></p>
            <p>¿Qué deseas hacer hoy?</p>
        </div>
        <%
            String dashboardError = (String) request.getAttribute("error");
            if (dashboardError != null && !dashboardError.isEmpty()) {
        %>
        <div style="margin:1rem auto; max-width:600px; padding:0.75rem 1rem; background:#ffebee; border:1px solid #f44336; color:#c62828; border-radius:6px;">
            <%= dashboardError %>
        </div>
        <%
            }
            String dashboardSuccess = (String) request.getAttribute("success");
            if (dashboardSuccess != null && !dashboardSuccess.isEmpty()) {
        %>
        <div style="margin:1rem auto; max-width:600px; padding:0.75rem 1rem; background:#e8f5e9; border:1px solid #4caf50; color:#2e7d32; border-radius:6px;">
            <%= dashboardSuccess %>
        </div>
        <%
            }
        %>
        <div class="navigation-grid">
            <%
                if ("Turista".equals(userType)) {
            %>
            <a href="${pageContext.request.contextPath}/activities" class="nav-card">🎯 Actividades Turísticas</a>
            <a href="${pageContext.request.contextPath}/my-registrations" class="nav-card">📋 Mis Inscripciones</a>
            <%
                }
                if ("Proveedor".equals(userType)) {
            %>
            <a href="${pageContext.request.contextPath}/my-activities" class="nav-card">📊 Mis Actividades</a>
            <a href="${pageContext.request.contextPath}/create-activity" class="nav-card">➕ Nueva Actividad</a>
            <a href="${pageContext.request.contextPath}/create-departure" class="nav-card">� Nueva Salida</a>
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
