<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registro de Turista - Turismo.uy</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<div class="main-content container">
    <div class="page-header">
        <div style="font-size:3rem;">🧳</div>
        <h1>Regístrate como Turista</h1>
        <p style="color:#666;">Únete para descubrir increíbles experiencias turísticas</p>
    </div>

    <div class="card">
        <div class="card-content">
            <h2>Registro de Turista</h2>
            <p style="color:#666;">Completa tus datos para crear una cuenta</p>

            <jsp:include page="/WEB-INF/jsp/includes/messages.jsp"/>

            <form method="post" action="${pageContext.request.contextPath}/register-tourist" enctype="multipart/form-data" id="registerTouristForm" novalidate>
                <fieldset>
                    <legend class="fs-6">Información de Cuenta</legend>
                    <div class="form-row">
                        <div class="form-group">
                            <label for="nickname">Nickname <span class="required">*</span></label>
                            <input type="text" id="nickname" name="nickname" required pattern="[A-Za-z0-9_]{4,20}" placeholder="Tu usuario" autocomplete="username" autofocus>
                            <div class="form-hint">4-20 caracteres, solo letras, números y guión bajo</div>
                        </div>
                        <div class="form-group">
                            <label for="email">Email <span class="required">*</span></label>
                            <input type="email" id="email" name="email" required placeholder="tu@email.com" autocomplete="email">
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label for="password">Contraseña <span class="required">*</span></label>
                            <div class="password-input">
                                <input type="password" id="password" name="password" required minlength="8" placeholder="Tu contraseña" autocomplete="new-password">
                                <button type="button" class="toggle-password" onclick="togglePassword('password')">👁️</button>
                            </div>
                            <div class="form-hint">Mínimo 8 caracteres</div>
                        </div>
                        <div class="form-group">
                            <label for="confirmPassword">Confirmar Contraseña <span class="required">*</span></label>
                            <div class="password-input">
                                <input type="password" id="confirmPassword" name="confirmPassword" required minlength="8" placeholder="Repite tu contraseña" autocomplete="new-password">
                                <button type="button" class="toggle-password" onclick="togglePassword('confirmPassword')">👁️</button>
                            </div>
                        </div>
                    </div>
                </fieldset>

                <fieldset>
                    <legend class="fs-6">Información Personal</legend>
                    <div class="form-row">
                        <div class="form-group">
                            <label for="firstName">Nombre <span class="required">*</span></label>
                            <input type="text" id="firstName" name="firstName" required placeholder="Tu nombre" autocomplete="given-name">
                        </div>
                        <div class="form-group">
                            <label for="lastName">Apellido <span class="required">*</span></label>
                            <input type="text" id="lastName" name="lastName" required placeholder="Tu apellido" autocomplete="family-name">
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="form-group">
                            <label for="birthDate">Fecha de Nacimiento <span class="required">*</span></label>
                            <input type="date" id="birthDate" name="birthDate" required autocomplete="bday">
                        </div>
                        <div class="form-group">
                            <label for="nationality">Nacionalidad <span class="required">*</span></label>
                            <input type="text" id="nationality" name="nationality" required placeholder="Tu nacionalidad" autocomplete="country-name">
                        </div>
                    </div>
                </fieldset>

                <fieldset>
                    <legend class="fs-6">Imagen de Perfil</legend>
                    <div class="form-group">
                        <label for="profileImage">Foto de Perfil (Opcional)</label>
                        <input type="file" id="profileImage" name="profileImage" accept="image/*">
                        <div class="form-hint">JPG, PNG. Máximo 5MB</div>
                    </div>
                </fieldset>

                <div class="form-actions">
                    <button type="submit" class="btn">✨ Completar Registro</button>
                    <a href="${pageContext.request.contextPath}/" class="register-btn">Volver</a>
                </div>
            </form>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/js/app.js"></script>
<script>
// Validación simple + chequeo de contraseñas
(function(){
    const form = document.getElementById('registerTouristForm');
    form.addEventListener('submit', function(e){
        const pwd = document.getElementById('password').value;
        const cpwd = document.getElementById('confirmPassword').value;
        if (pwd !== cpwd) {
            e.preventDefault(); e.stopPropagation();
            document.getElementById('confirmPassword').setCustomValidity('No coincide');
        } else {
            document.getElementById('confirmPassword').setCustomValidity('');
        }
        if (!form.checkValidity()) {
            e.preventDefault(); e.stopPropagation();
        }
    });
})();

function togglePassword(inputId) {
    const input = document.getElementById(inputId);
    if (input.type === 'password') input.type = 'text'; else input.type = 'password';
}
</script>
</body>
</html>
