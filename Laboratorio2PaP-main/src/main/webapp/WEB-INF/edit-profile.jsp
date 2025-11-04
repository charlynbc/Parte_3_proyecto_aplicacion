<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Modificar Perfil</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
    <jsp:include page="/WEB-INF/jsp/includes/navbar.jsp"/>

<div class="main-content">
    <div class="registration-container">
        <div class="registration-hero">
            <div class="hero-icon">✏️</div>
            <h1>Editar Perfil</h1>
            <p>Actualiza tu información personal.</p>
        </div>

        <div class="registration-form-section">
            <div class="form-header">
                <h2>Modificar Perfil</h2>
                <p>Modifica tus datos y tu foto de perfil.</p>
            </div>

            <jsp:include page="/WEB-INF/jsp/includes/messages.jsp"/>

            <form method="post" action="${pageContext.request.contextPath}/edit-profile" enctype="multipart/form-data" class="registration-form">
                <div class="form-row">
                    <div class="form-group">
                        <label for="firstName">Nombre <span class="required">*</span></label>
                        <input type="text" id="firstName" name="firstName" required
                               value="<%= session.getAttribute("firstName") != null ? session.getAttribute("firstName") : "" %>">
                    </div>
                    <div class="form-group">
                        <label for="lastName">Apellido <span class="required">*</span></label>
                        <input type="text" id="lastName" name="lastName" required
                               value="<%= session.getAttribute("lastName") != null ? session.getAttribute("lastName") : "" %>">
                    </div>
                </div>

                <div class="form-group">
                    <label for="birthDate">Fecha de Nacimiento <span class="required">*</span></label>
                    <input type="date" id="birthDate" name="birthDate" required
                           value="<%= session.getAttribute("birthDate") != null ? session.getAttribute("birthDate") : "" %>">
                </div>

                <%
                    String userType = (String) session.getAttribute("userType");
                    if ("Turista".equals(userType)) {
                %>
                    <div class="form-group">
                        <label for="nationality">Nacionalidad <span class="required">*</span></label>
                        <input type="text" id="nationality" name="nationality"
                               value="<%= session.getAttribute("nacionalidad") != null ? session.getAttribute("nacionalidad") : "" %>">
                    </div>
                <%
                    } else if ("Proveedor".equals(userType)) {
                %>
                    <div class="form-group">
                        <label for="description">Descripción <span class="required">*</span></label>
                        <textarea id="description" name="description" required><%= session.getAttribute("descripcion") != null ? session.getAttribute("descripcion") : "" %></textarea>
                    </div>
                    <div class="form-group">
                        <label for="website">Sitio Web (Opcional)</label>
                        <input type="url" id="website" name="website"
                               value="<%= session.getAttribute("sitioWeb") != null ? session.getAttribute("sitioWeb") : "" %>">
                    </div>
                <%
                    }
                %>

                <div class="form-group">
                    <label for="profileImage">Nueva Imagen de Perfil (Opcional)</label>
                    <div class="file-input">
                        <input type="file" id="profileImage" name="profileImage" accept="image/*">
                        <label for="profileImage" class="file-input-label">
                            📷 Cambiar imagen de perfil
                        </label>
                    </div>
                </div>

                <div class="form-actions">
                    <button type="submit" class="btn">Guardar Cambios</button>
                </div>
                
                <div class="login-footer">
                    <a href="${pageContext.request.contextPath}/user-profile" class="back-link">← Volver al perfil</a>
                </div>
            </form>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/js/app.js"></script>
</body>
</html>
