<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="uy.edu.pa.central.client.UserDTO" %>
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
                UserDTO user = (UserDTO) request.getAttribute("user");
                if (user == null) {
            %>
                <p>Usuario no encontrado</p>
            <%
                } else {
                    String imagenPerfil = user.getImagen();
                    boolean tieneImagen = imagenPerfil != null && !imagenPerfil.trim().isEmpty();
            %>
                <div style="text-align:center;margin-bottom:2rem;">
                    <% if (tieneImagen) { %>
                        <img src="<%= imagenPerfil %>" alt="Foto de perfil" 
                             style="width:150px;height:150px;border-radius:50%;object-fit:cover;border:4px solid #667eea;">
                    <% } else { %>
                        <div style="width:150px;height:150px;border-radius:50%;background:#e0e0e0;display:inline-flex;align-items:center;justify-content:center;font-size:4rem;">
                            👤
                        </div>
                    <% } %>
                </div>
                
                <h2><%= user.getNombre() != null ? user.getNombre() : "" %> <%= user.getApellido() != null ? user.getApellido() : "" %></h2>
                <p>Email: <%= user.getEmail() != null ? user.getEmail() : "No especificado" %></p>
                <p>Nickname: <%= user.getNickname() != null ? user.getNickname() : "No especificado" %></p>

                <%
                String tipoUsuario = user.getTipoUsuario();
                if ("turista".equalsIgnoreCase(tipoUsuario)) {
                %>
                    <p><strong>Turista</strong></p>
                    <p>Nacionalidad: <%= user.getNacionalidad() != null ? user.getNacionalidad() : "No especificada" %></p>
                    <p>Fecha de Nacimiento: <%= user.getFechaNacimiento() != null ? user.getFechaNacimiento() : "No especificada" %></p>
                <%
                } else if ("proveedor".equalsIgnoreCase(tipoUsuario)) {
                %>
                    <p><strong>Proveedor</strong></p>
                    <p>Descripción: <%= user.getDescripcionProveedor() != null ? user.getDescripcionProveedor() : "No especificada" %></p>
                    <p>Link: <%= user.getLinkProveedor() != null ? user.getLinkProveedor() : "No especificado" %></p>
                <%
                } else {
                %>
                    <p><strong>Usuario</strong></p>
                    <p>Tipo: <%= tipoUsuario != null ? tipoUsuario : "No especificado" %></p>
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
