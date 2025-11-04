<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String username = (String) session.getAttribute("username");
    boolean isLoggedIn = username != null && !username.trim().isEmpty();
%>

<nav class="navbar">
    <div class="nav-container">
        <a href="${pageContext.request.contextPath}/<%= isLoggedIn ? "dashboard" : "" %>" class="nav-brand">
            🏖️️ Turismo.uy
        </a>
        
        <ul class="nav-menu">
            <li><a href="${pageContext.request.contextPath}/activities">Actividades</a></li>
            <li><a href="${pageContext.request.contextPath}/users">Usuarios</a></li>
            <% if(isLoggedIn && "turista".equalsIgnoreCase((String)session.getAttribute("tipoUsuario"))) { %>
                <li><a href="${pageContext.request.contextPath}/mis-salidas">Mis Salidas</a></li>
            <% } %>
        </ul>
        
        <div class="nav-user">
            <% if(isLoggedIn) { %>
                <a href="${pageContext.request.contextPath}/user-profile">${username}</a>
                <form action="${pageContext.request.contextPath}/logout" method="get" style="display: inline;">
                    <button type="submit" class="logout-btn">Cerrar Sesión</button>
                </form>
            <% } else { %>
                <a href="${pageContext.request.contextPath}/login" class="login-btn">Iniciar Sesión</a>
                <a href="${pageContext.request.contextPath}/register-tourist" class="register-btn">Registrarse como Turista</a>
                <a href="${pageContext.request.contextPath}/register-provider" class="register-btn">Registrarse como Proveedor</a>
            <% } %>
        </div>
    </div>
</nav>