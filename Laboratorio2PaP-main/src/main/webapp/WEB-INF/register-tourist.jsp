<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registro de Turista - Turismo.uy</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/registration.css">
</head>
<body>

<div class="registration-container">
    <!-- Lado izquierdo: hero -->
    <div class="registration-hero" role="banner">
        <div class="hero-icon" aria-hidden="true">🧳</div>
        <h1>Regístrate como Turista</h1>
        <p>Únete para descubrir increíbles experiencias turísticas</p>
    </div>

    <!-- Lado derecho: formulario -->
    <div class="registration-form-section" role="main">
        <div class="form-header">
            <h2>Registro de Turista</h2>
            <p>Completa tus datos para crear una cuenta</p>
        </div>

        <!-- Mensajes de error/éxito -->
        <jsp:include page="/WEB-INF/jsp/includes/messages.jsp"/>

        <!-- Formulario de registro -->
        <form method="post" 
              action="${pageContext.request.contextPath}/register-tourist" 
              enctype="multipart/form-data"
              class="registration-form"
              novalidate>
            
            <!-- Información de cuenta -->
            <fieldset>
                <legend>Información de Cuenta</legend>
                
                <div class="form-row">
                    <div class="form-group">
                        <label for="nickname">Nickname <span class="required">*</span></label>
                        <input type="text" 
                               id="nickname" 
                               name="nickname" 
                               required 
                               pattern="[A-Za-z0-9_]{4,20}"
                               placeholder="Tu nombre de usuario único"
                               autocomplete="username"
                               autofocus>
                        <div class="form-hint">4-20 caracteres, solo letras, números y guiones bajos</div>
                    </div>
                    
                    <div class="form-group">
                        <label for="email">Email <span class="required">*</span></label>
                        <input type="email" 
                               id="email" 
                               name="email" 
                               required
                               placeholder="tu@email.com"
                               autocomplete="email">
                        <div class="form-hint">Dirección de correo electrónico válida</div>
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label for="password">Contraseña <span class="required">*</span></label>
                        <div class="password-input">
                            <input type="password" 
                                   id="password" 
                                   name="password" 
                                   required
                                   placeholder="Tu contraseña"
                                   autocomplete="new-password"
                                   minlength="8">
                            <button type="button" 
                                    class="toggle-password" 
                                    onclick="togglePassword('password')">
                                <span>👁️</span>
                            </button>
                        </div>
                        <div class="form-hint">Mínimo 8 caracteres</div>
                    </div>
                    
                    <div class="form-group">
                        <label for="confirmPassword">Confirmar Contraseña <span class="required">*</span></label>
                        <div class="password-input">
                            <input type="password" 
                                   id="confirmPassword" 
                                   name="confirmPassword" 
                                   required
                                   placeholder="Repite tu contraseña"
                                   autocomplete="new-password"
                                   minlength="8">
                            <button type="button" 
                                    class="toggle-password" 
                                    onclick="togglePassword('confirmPassword')">
                                <span>👁️</span>
                            </button>
                        </div>
                        <div class="form-hint">Debe coincidir con la contraseña</div>
                    </div>
                </div>
            </fieldset>

            <!-- Información personal -->
            <fieldset>
                <legend>Información Personal</legend>
                
                <div class="form-row">
                    <div class="form-group">
                        <label for="firstName">Nombre <span class="required">*</span></label>
                        <input type="text" 
                               id="firstName" 
                               name="firstName" 
                               required
                               placeholder="Tu nombre"
                               autocomplete="given-name">
                        <div class="form-hint">Tu nombre de pila</div>
                    </div>
                    
                    <div class="form-group">
                        <label for="lastName">Apellido <span class="required">*</span></label>
                        <input type="text" 
                               id="lastName" 
                               name="lastName" 
                               required
                               placeholder="Tu apellido"
                               autocomplete="family-name">
                        <div class="form-hint">Tu apellido</div>
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label for="birthDate">Fecha de Nacimiento <span class="required">*</span></label>
                        <input type="date" 
                               id="birthDate" 
                               name="birthDate" 
                               required
                               autocomplete="bday">
                        <div class="form-hint">Debes ser mayor de edad</div>
                    </div>
                    
                    <div class="form-group">
                        <label for="nationality">Nacionalidad <span class="required">*</span></label>
                        <input type="text" 
                               id="nationality" 
                               name="nationality" 
                               required
                               placeholder="Tu nacionalidad"
                               autocomplete="country-name">
                        <div class="form-hint">País de origen</div>
                    </div>
                </div>
            </fieldset>

            <!-- Imagen de perfil -->
            <fieldset>
                <legend>Imagen de Perfil</legend>
                
                <div class="form-group">
                    <label for="profileImage">Foto de Perfil (Opcional)</label>
                    <div class="file-input">
                        <input type="file" 
                               id="profileImage" 
                               name="profileImage"
                               accept="image/*">
                        <label for="profileImage" class="file-input-label">
                            📷 Seleccionar imagen
                        </label>
                        <span class="file-name"></span>
                    </div>
                    <div class="form-hint">JPG, PNG. Máximo 5MB</div>
                </div>
            </fieldset>

            <!-- Acciones del formulario -->
            <div class="form-actions">
                <button type="submit" class="btn btn-primary">
                    <span class="btn-icon">✨</span>
                    Completar Registro
                </button>
                
                <div class="login-footer">
                    <a href="${pageContext.request.contextPath}/" class="back-link">
                        Volver
                    </a>
                </div>
            </div>
        </form>
    </div>
</div>

<script src="${pageContext.request.contextPath}/js/app.js"></script>
<script>
// Form validation
document.querySelector('.registration-form').addEventListener('submit', function(e) {
    let isValid = true;
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirmPassword').value;
    
    document.querySelectorAll('.error-field').forEach(el => el.classList.remove('error-field'));
    
    this.querySelectorAll('input[required]').forEach(input => {
        if (!input.value.trim()) {
            input.closest('.form-group').classList.add('error-field');
            isValid = false;
        }
    });
    
    if (password !== confirmPassword) {
        document.getElementById('confirmPassword').closest('.form-group').classList.add('error-field');
        showMessage('error', 'Las contraseñas no coinciden');
        isValid = false;
    }
    
    const email = document.getElementById('email');
    if (email.value && !email.value.match(/^[^\s@]+@[^\s@]+\.[^\s@]+$/)) {
        email.closest('.form-group').classList.add('error-field');
        showMessage('error', 'Email inválido');
        isValid = false;
    }
    
    if (!isValid) {
        e.preventDefault();
    } else {
        const submitBtn = this.querySelector('button[type="submit"]');
        submitBtn.classList.add('loading');
        submitBtn.disabled = true;
    }
});

// File preview
document.getElementById('profileImage').addEventListener('change', function(e) {
    const file = e.target.files[0];
    const fileNameSpan = this.parentElement.querySelector('.file-name');
    if (file) {
        fileNameSpan.textContent = file.name;
        this.nextElementSibling.textContent = '✅ ' + file.name;
    }
});
</script>
</body>
</html>
