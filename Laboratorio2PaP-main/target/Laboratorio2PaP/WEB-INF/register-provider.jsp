<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Registro de Proveedor</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>

<div class="registration-container">
    <div class="registration-hero">
        <div class="hero-icon">🏢</div>
        <h1>Regístrate como Proveedor</h1>
        <p>Ofrece experiencias turísticas únicas</p>
    </div>

    <div class="registration-form-section">
        <div class="form-header">
            <h2>Registro de Proveedor</h2>
            <p>Ingresa tus datos para comenzar a publicar actividades</p>
        </div>

        <jsp:include page="/WEB-INF/jsp/includes/messages.jsp"/>

        <form method="post" action="${pageContext.request.contextPath}/register-provider" enctype="multipart/form-data" class="registration-form">
            <fieldset>
                <legend>Información de Cuenta</legend>
                
                <div class="form-row">
                    <div class="form-group">
                        <label for="nickname">Nickname <span class="required">*</span></label>
                        <input type="text" id="nickname" name="nickname" required>
                    </div>
                    <div class="form-group">
                        <label for="email">Email <span class="required">*</span></label>
                        <input type="email" id="email" name="email" required>
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label for="password">Contraseña <span class="required">*</span></label>
                        <div class="password-input">
                            <input type="password" id="password" name="password" required>
                            <button type="button" class="toggle-password" onclick="togglePassword('password')">
                                <span>👁️</span>
                            </button>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="confirmPassword">Confirmar Contraseña <span class="required">*</span></label>
                        <div class="password-input">
                            <input type="password" id="confirmPassword" name="confirmPassword" required>
                            <button type="button" class="toggle-password" onclick="togglePassword('confirmPassword')">
                                <span>👁️</span>
                            </button>
                        </div>
                    </div>
                </div>
            </fieldset>

            <fieldset>
                <legend>Información Personal</legend>
                
                <div class="form-row">
                    <div class="form-group">
                        <label for="firstName">Nombre <span class="required">*</span></label>
                        <input type="text" id="firstName" name="firstName" required>
                    </div>
                    <div class="form-group">
                        <label for="lastName">Apellido <span class="required">*</span></label>
                        <input type="text" id="lastName" name="lastName" required>
                    </div>
                </div>
                
                <div class="form-group">
                    <label for="birthDate">Fecha de Nacimiento <span class="required">*</span></label>
                    <input type="date" id="birthDate" name="birthDate" required 
                           max="${java.time.LocalDate.now()}" autocomplete="bday">
                    <div class="form-hint">Debes ser mayor de edad para registrarte</div>
                </div>
            </fieldset>

            <fieldset>
                <legend>Información del Negocio</legend>
                
                <div class="form-group">
                    <label for="description">Descripción <span class="required">*</span></label>
                    <textarea id="description" name="description" required></textarea>
                    <div class="form-hint">Describe los servicios que ofreces</div>
                </div>

                <div class="form-group">
                    <label for="website">Sitio Web (Opcional)</label>
                    <input type="url" id="website" name="website" placeholder="https://www.ejemplo.com">
                </div>
            </fieldset>

            <fieldset>
                <legend>Imagen de Perfil</legend>
                
                <div class="form-group">
                    <label for="profileImage">Imagen de Perfil (Opcional)</label>
                    <div class="file-input">
                        <input type="file" id="profileImage" name="profileImage" accept="image/*">
                        <label for="profileImage" class="file-input-label">
                            📷 Seleccionar imagen de perfil
                        </label>
                        <span class="file-name"></span>
                    </div>
                </div>
            </fieldset>

            <div class="form-actions">
                <button type="submit" class="btn">Registrarse como Proveedor</button>
                
                <div class="login-footer">
                    <a href="${pageContext.request.contextPath}/" class="back-link">Volver</a>
                </div>
            </div>
        </form>
    </div>
</div>

<script src="${pageContext.request.contextPath}/js/app.js"></script>
<script>
function togglePassword(inputId) {
    const input = document.getElementById(inputId);
    const button = input.nextElementSibling;
    
    if (input.type === 'password') {
        input.type = 'text';
        button.querySelector('span').textContent = '🔒';
    } else {
        input.type = 'password';
        button.querySelector('span').textContent = '👁️';
    }
}

document.getElementById('profileImage').addEventListener('change', function(e) {
    const file = e.target.files[0];
    const fileNameSpan = this.parentElement.querySelector('.file-name');
    if (file) {
        fileNameSpan.textContent = file.name;
    }
});
</script>
</body>
</html>
