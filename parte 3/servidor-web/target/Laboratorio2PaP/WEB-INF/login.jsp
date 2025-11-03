<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Turismo.uy - Inicio de Sesión</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<div class="container">
    <div class="hero-section" role="banner">
        <div class="hero-icon" aria-hidden="true">🏖️</div>
        <h1>Turismo.uy</h1>
        <p>Descubre las mejores experiencias turísticas de Uruguay</p>
    </div>
    
    <div class="login-section" role="main">
        <div class="login-header">
            <h2>Iniciar Sesión</h2>
            <p>Accede a tu cuenta para explorar actividades</p>
        </div>

        <jsp:include page="/WEB-INF/jsp/includes/messages.jsp"/>

        <form method="post" 
              action="${pageContext.request.contextPath}/login" 
              class="login-form" 
              novalidate 
              autocomplete="on">
            <div class="form-group">
                <label for="username">Nickname o Email <span class="required" aria-hidden="true">*</span></label>
                <input type="text" 
                       id="username" 
                       name="username" 
                       required 
                       aria-required="true"
                       aria-describedby="username-hint"
                       placeholder="Ingresa tu nickname o email"
                       autocomplete="username"
                       minlength="4"
                       maxlength="50"
                       autofocus>
                <div id="username-hint" class="form-hint">Ingresa tu nombre de usuario o correo electrónico registrado</div>
            </div>

            <div class="form-group">
                <label for="password">Contraseña <span class="required" aria-hidden="true">*</span></label>
                <div class="password-input">
                    <input type="password" 
                           id="password" 
                           name="password" 
                           required 
                           aria-required="true"
                           aria-describedby="password-hint"
                           placeholder="Ingresa tu contraseña"
                           autocomplete="current-password">
                    <button type="button" 
                            class="toggle-password" 
                            aria-label="Mostrar contraseña" 
                            title="Mostrar contraseña"
                            tabindex="0"
                            onclick="togglePassword('password')">
                        <span aria-hidden="true">👁️</span>
                    </button>
                </div>
                <div id="password-hint" class="form-hint">Ingresa tu contraseña registrada</div>
            </div>

            <div class="form-actions">
                <button type="submit" class="btn btn-primary">
                    <span class="btn-icon" aria-hidden="true">🔒</span>
                    Iniciar Sesión
                </button>
            </div>
        </form>

        <div class="register-links" role="navigation">
            <p>¿No tienes cuenta? Regístrate como:</p>
            <div class="register-options">
                <a href="${pageContext.request.contextPath}/register-tourist" class="register-btn" role="button">
                    <span class="btn-icon">🧳</span>
                    Turista
                </a>
                <a href="${pageContext.request.contextPath}/register-provider" class="register-btn" role="button">
                    <span class="btn-icon">🏢</span>
                    Proveedor
                </a>
            </div>
            <div style="margin-top: 15px;">
                <a href="${pageContext.request.contextPath}/" class="help-link">← Volver al inicio</a>
            </div>
        </div>

        <footer class="login-footer">
            <p>¿Necesitas ayuda? <a href="#" class="help-link">Contacta con soporte</a></p>
        </footer>
    </div>
</div>

<script src="${pageContext.request.contextPath}/js/app.js"></script>
<script>
function togglePassword(inputId) {
    const input = document.getElementById(inputId);
    const button = input.nextElementSibling;
    
    if (input.type === 'password') {
        input.type = 'text';
        button.textContent = '🔒';
        button.setAttribute('aria-label', 'Ocultar contraseña');
        button.setAttribute('title', 'Ocultar contraseña');
    } else {
        input.type = 'password';
        button.textContent = '👁️';
        button.setAttribute('aria-label', 'Mostrar contraseña');
        button.setAttribute('title', 'Mostrar contraseña');
    }
}

// Form validation
document.querySelector('form').addEventListener('submit', function(e) {
    const username = document.getElementById('username').value.trim();
    const password = document.getElementById('password').value;
    let isValid = true;
    
    // Clear previous error states
    document.querySelectorAll('.error-field').forEach(el => el.classList.remove('error-field'));
    
    if (!username) {
        document.getElementById('username').parentElement.classList.add('error-field');
        isValid = false;
    }
    
    if (!password) {
        document.getElementById('password').parentElement.classList.add('error-field');
        isValid = false;
    }
    
    if (!isValid) {
        e.preventDefault();
        showMessage('error', 'Por favor completa todos los campos requeridos');
    }
});
</script>
</body>
</html>
