<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="logica.DataUsuario" %>
<%@ page import="logica.DataTurista" %>
<%@ page import="logica.DataProveedor" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Usuarios - Turismo.uy</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
    
    <jsp:include page="/WEB-INF/jsp/includes/navbar.jsp" />

    <div class="main-content">
        <div class="page-header">
            <h1>Usuarios Registrados</h1>
            <p>Explora los perfiles de turistas y proveedores</p>
        </div>

        <%
            DataUsuario[] users = (DataUsuario[]) request.getAttribute("users");
            String error = (String) request.getAttribute("error");
            
            if (error != null) {
        %>
            <div class="alert alert-error">
                <%= error %>
            </div>
        <%
            }
            
            if (users != null && users.length > 0) {
        %>
            <div class="card-grid">
        <%
                for (DataUsuario user : users) {
                    String userType = "";
                    String additionalInfo = "";
                    
                    if (user instanceof DataTurista) {
                        userType = "Turista";
                        DataTurista turista = (DataTurista) user;
                        additionalInfo = "Nacionalidad: " + (turista.getNacionalidad() != null ? turista.getNacionalidad() : "No especificada");
                    } else if (user instanceof DataProveedor) {
                        userType = "Proveedor";
                        DataProveedor proveedor = (DataProveedor) user;
                        additionalInfo = "Proveedor de servicios turísticos";
                    }
        %>
                <div class="card">
                    <div class="card-image">
                        <div style="display: flex; align-items: center; justify-content: center; height: 200px; font-size: 4rem; background-color: #f8f9fa;">👤</div>
                    </div>
                    <div class="card-content">
                        <div class="card-title"><%= user.getNombre() + " " + user.getApellido() %></div>
                        <div class="card-description">
                            <strong><%= userType %></strong><br>
                            <%= additionalInfo %><br>
                            Nickname: <%= user.getNickname() %>
                        </div>
                        <div class="card-actions">
                            <a href="<%= request.getContextPath() %>/user-profile?nickname=<%= user.getNickname() %>" class="btn-small">Ver Perfil</a>
                        </div>
                    </div>
                </div>
        <%
                }
        %>
            </div>
        <%
            } else {
        %>
            <div class="empty-state">
                <p>No hay usuarios registrados todavía.</p>
            </div>
        <%
            }
        %>
    </div>

    <script src="${pageContext.request.contextPath}/js/app.js"></script>
</body>
</html>
