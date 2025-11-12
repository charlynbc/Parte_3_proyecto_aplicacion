<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <jsp:include page="/WEB-INF/jsp/includes/head.jsp"/>
    <title>Turismo.uy - Inicio de Sesión</title>
</head>
<body>
<div class="main-content container">
    <div class="page-header">
        <div aria-hidden="true" style="font-size:3rem;">🏖️</div>
        <h1>Turismo.uy</h1>
        <p>Descubre las mejores experiencias turísticas de Uruguay</p>
    </div>
    <div class="card">
        <div class="card-content">
            <h2>Iniciar Sesión</h2>
            <p style="color:#666;">Accede a tu cuenta para explorar actividades</p>

            <jsp:include page="/WEB-INF/jsp/includes/messages.jsp"/>

            <form method="post" action="${pageContext.request.contextPath}/login" novalidate autocomplete="on">
                <div class="form-group">
                    <label for="username">Nickname o Email <span class="required" aria-hidden="true">*</span></label>
                    <input type="text" id="username" name="username"
                           required aria-required="true" aria-describedby="username-hint"
                           placeholder="Ingresa tu nickname o email" autocomplete="username"
                           value="<%= request.getAttribute("username") != null ? request.getAttribute("username") : "" %>"
                           minlength="4" maxlength="50" autofocus>
                    <div id="username-hint" class="form-hint">Ingresa tu nombre de usuario o correo electrónico registrado</div>
                </div>

                <div class="form-group">
                    <label for="password">Contraseña <span class="required" aria-hidden="true">*</span></label>
                    <div class="password-input">
                        <input type="password" id="password" name="password"
                               required aria-required="true" aria-describedby="password-hint"
                               placeholder="Ingresa tu contraseña" autocomplete="current-password">
                        <button type="button" class="toggle-password" aria-label="Mostrar contraseña" title="Mostrar contraseña" onclick="togglePassword('password')">👁️</button>
                    </div>
                    <div id="password-hint" class="form-hint">Ingresa tu contraseña registrada</div>
                </div>

                <div class="form-actions">
                    <button type="submit" class="btn">🔒 Iniciar Sesión</button>
                </div>
            </form>

            <div class="register-links">
                <p>¿No tienes cuenta? Regístrate como:</p>
                <div class="register-options">
                    <a href="${pageContext.request.contextPath}/register-tourist" class="register-btn">🧳 Turista</a>
                    <a href="${pageContext.request.contextPath}/register-provider" class="register-btn">🏢 Proveedor</a>
                </div>
                <div style="margin-top:1rem;">
                    <a href="${pageContext.request.contextPath}/" class="back-link">← Volver al inicio</a>
                </div>
            </div>
        </div>
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

// Validación simple (sin Bootstrap)
(function(){
    var form = document.querySelector('form');
    form.addEventListener('submit', function(e){
        if(!form.checkValidity()){
            e.preventDefault(); e.stopPropagation();
        }
    });
})();
</script>
</body>
</html>
