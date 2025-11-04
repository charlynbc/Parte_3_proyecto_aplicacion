<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
        String username = (String) session.getAttribute("username");
        boolean isLoggedIn = username != null && !username.trim().isEmpty();
        String tipoUsuario = (String) session.getAttribute("tipoUsuario");
%>

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">

<nav class="navbar">
    <div class="nav-container">
        <a class="nav-brand" href="${pageContext.request.contextPath}/<%= isLoggedIn ? "dashboard" : "" %>">🏖️ Turismo.uy</a>
        <ul class="nav-menu">
            <li><a href="${pageContext.request.contextPath}/activities">Actividades</a></li>
            <li><a href="${pageContext.request.contextPath}/users">Usuarios</a></li>
            <% if(isLoggedIn && "turista".equalsIgnoreCase(tipoUsuario)) { %>
                <li><a href="${pageContext.request.contextPath}/mis-salidas">Mis Salidas</a></li>
            <% } %>
        </ul>
        <div class="nav-user">
            <% if(isLoggedIn) { %>
                <a href="${pageContext.request.contextPath}/user-profile" class="nav-username">${username}</a>
                <form action="${pageContext.request.contextPath}/logout" method="get" style="margin:0;">
                    <button type="submit" class="logout-btn">Cerrar Sesión</button>
                </form>
            <% } else { %>
                <a href="${pageContext.request.contextPath}/login" class="register-btn">Iniciar Sesión</a>
                <a href="${pageContext.request.contextPath}/register-tourist" class="register-btn">Turista</a>
                <a href="${pageContext.request.contextPath}/register-provider" class="register-btn">Proveedor</a>
            <% } %>
        </div>
    </div>
</nav>