<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="logica.DataUsuario" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Perfil de Usuario</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
    <jsp:include page="/WEB-INF/jsp/includes/navbar.jsp"/>

<div class="main-content">
    <div class="registration-container">
        <div class="registration-hero">
            <div class="hero-icon">👤</div>
            <h1>Perfil de Usuario</h1>
            <p>Información del usuario seleccionado</p>
        </div>

        <div class="registration-form-section">
            <%
                DataUsuario user = (DataUsuario) request.getAttribute("user");
                if (user == null) {
            %>
                <p>Usuario no encontrado</p>
            <%
                } else {
            %>
                <h2><%= user.getNombre() != null ? user.getNombre() : "" %> <%= user.getApellido() != null ? user.getApellido() : "" %></h2>
                <p>Email: <%= user.getEmail() != null ? user.getEmail() : "No especificado" %></p>

                <%
                if (user instanceof logica.DataTurista) {
                    logica.DataTurista turista = (logica.DataTurista) user;
                %>
                    <p><strong>Turista</strong></p>
                    <p>Nacionalidad: <%= turista.getNacionalidad() != null ? turista.getNacionalidad() : "No especificada" %></p>
                <%
                } else if (user instanceof logica.DataProveedor) {
                    logica.DataProveedor proveedor = (logica.DataProveedor) user;
                %>
                    <p><strong>Proveedor</strong></p>
                    <p>Sitio web: 
                        <%
                        if (proveedor.getSitioWeb() != null && !proveedor.getSitioWeb().isEmpty()) {
                        %>
                            <a href="<%= proveedor.getSitioWeb() %>" target="_blank"><%= proveedor.getSitioWeb() %></a>
                        <%
                        } else {
                        %>
                            <span>No especificado</span>
                        <%
                        }
                        %>
                    </p>
                    <p>Descripción: <%= proveedor.getDescripcion() != null ? proveedor.getDescripcion() : "No especificada" %></p>
                <%
                } else {
                %>
                    <p><strong>Tipo de usuario desconocido</strong></p>
                <%
                }
                %>

                <div class="mt-3">
                    <a href="${pageContext.request.contextPath}/edit-profile" class="btn">Editar Perfil</a>
                </div>
            <%
                }
            %>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/js/app.js"></script>
</body>
</html>
