<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
              action="${pageContext.request.contextPath}/register" 
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
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registro - Sistema de Inscripciones</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 20px;
        }
        
        .register-container {
            background: white;
            padding: 2rem;
            border-radius: 15px;
            box-shadow: 0 15px 35px rgba(0, 0, 0, 0.1);
            width: 100%;
            max-width: 500px;
        }
        
        .register-header {
            text-align: center;
            margin-bottom: 2rem;
        }
        
        .register-header h1 {
            color: #333;
            font-size: 2rem;
            margin-bottom: 0.5rem;
        }
        
        .register-header p {
            color: #666;
            font-size: 1rem;
        }
        
        .form-group {
            margin-bottom: 1.5rem;
        }
        
        .form-group label {
            display: block;
            margin-bottom: 0.5rem;
            color: #333;
            font-weight: 500;
        }
        
        .form-group input,
        .form-group select,
        .form-group textarea {
            width: 100%;
            padding: 0.75rem;
            border: 2px solid #e1e5e9;
            border-radius: 8px;
            font-size: 1rem;
            transition: border-color 0.3s ease;
        }
        
        .form-group input:focus,
        .form-group select:focus,
        .form-group textarea:focus {
            outline: none;
            border-color: #667eea;
        }
        
        .form-group textarea {
            resize: vertical;
            min-height: 80px;
        }
        
        .tipo-usuario-group {
            display: flex;
            gap: 1rem;
            margin-bottom: 1.5rem;
        }
        
        .tipo-usuario-option {
            flex: 1;
            position: relative;
        }
        
        .tipo-usuario-option input[type="radio"] {
            position: absolute;
            opacity: 0;
        }
        
        .tipo-usuario-option label {
            display: block;
            padding: 1rem;
            border: 2px solid #e1e5e9;
            border-radius: 8px;
            text-align: center;
            cursor: pointer;
            transition: all 0.3s ease;
        }
        
        .tipo-usuario-option input[type="radio"]:checked + label {
            border-color: #667eea;
            background-color: #f8f9ff;
            color: #667eea;
        }
        
        .campos-especificos {
            display: none;
        }
        
        .campos-especificos.active {
            display: block;
        }
        
        .btn {
            width: 100%;
            padding: 1rem;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
            border-radius: 8px;
            font-size: 1.1rem;
            font-weight: 600;
            cursor: pointer;
            transition: transform 0.2s ease;
        }
        
        .btn:hover {
            transform: translateY(-2px);
        }
        
        .error-message {
            background-color: #f8d7da;
            color: #721c24;
            padding: 1rem;
            border-radius: 8px;
            margin-bottom: 1.5rem;
            border: 1px solid #f5c6cb;
        }
        
        .success-message {
            background-color: #d4edda;
            color: #155724;
            padding: 1rem;
            border-radius: 8px;
            margin-bottom: 1.5rem;
            border: 1px solid #c3e6cb;
        }
        
        .login-link {
            text-align: center;
            margin-top: 1.5rem;
            padding-top: 1.5rem;
            border-top: 1px solid #e1e5e9;
        }
        
        .login-link a {
            color: #667eea;
            text-decoration: none;
            font-weight: 500;
        }
        
        .login-link a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
    <div class="register-container">
        <div class="register-header">
            <h1>🎯 Registro</h1>
            <p>Crea tu cuenta en el sistema de inscripciones</p>
        </div>
        
        <c:if test="${not empty error}">
            <div class="error-message">
                <strong>Error:</strong> ${error}
            </div>
        </c:if>
        
        <c:if test="${not empty success}">
            <div class="success-message">
                <strong>Éxito:</strong> ${success}
            </div>
        </c:if>
        
        <form method="post" action="${pageContext.request.contextPath}/register">
            
            <!-- Tipo de Usuario -->
            <div class="form-group">
                <label>Tipo de Usuario</label>
                <div class="tipo-usuario-group">
                    <div class="tipo-usuario-option">
                        <input type="radio" id="turista" name="tipoUsuario" value="turista" 
                               ${tipoUsuario eq 'turista' ? 'checked' : ''} required>
                        <label for="turista">🧑 Turista</label>
                    </div>
                    <div class="tipo-usuario-option">
                        <input type="radio" id="proveedor" name="tipoUsuario" value="proveedor" 
                               ${tipoUsuario eq 'proveedor' ? 'checked' : ''} required>
                        <label for="proveedor">🏢 Proveedor</label>
                    </div>
                </div>
            </div>
            
            <!-- Campos Básicos -->
            <div class="form-group">
                <label for="nickname">Nickname *</label>
                <input type="text" id="nickname" name="nickname" value="${nickname}" required>
            </div>
            
            <div class="form-group">
                <label for="email">Email *</label>
                <input type="email" id="email" name="email" value="${email}" required>
            </div>
            
            <div class="form-group">
                <label for="nombre">Nombre *</label>
                <input type="text" id="nombre" name="nombre" value="${nombre}" required>
            </div>
            
            <div class="form-group">
                <label for="apellido">Apellido *</label>
                <input type="text" id="apellido" name="apellido" value="${apellido}" required>
            </div>
            
            <div class="form-group">
                <label for="fechaNacimiento">Fecha de Nacimiento *</label>
                <input type="date" id="fechaNacimiento" name="fechaNacimiento" value="${fechaNacimiento}" required>
            </div>
            
            <!-- Campos específicos para Turista -->
            <div id="campos-turista" class="campos-especificos ${tipoUsuario eq 'turista' ? 'active' : ''}">
                <div class="form-group">
                    <label for="nacionalidad">Nacionalidad *</label>
                    <input type="text" id="nacionalidad" name="nacionalidad" value="${nacionalidad}">
                </div>
            </div>
            
            <!-- Campos específicos para Proveedor -->
            <div id="campos-proveedor" class="campos-especificos ${tipoUsuario eq 'proveedor' ? 'active' : ''}">
                <div class="form-group">
                    <label for="descripcion">Descripción *</label>
                    <textarea id="descripcion" name="descripcion" placeholder="Describe tu empresa o servicios">${descripcion}</textarea>
                </div>
                
                <div class="form-group">
                    <label for="link">Sitio Web</label>
                    <input type="url" id="link" name="link" value="${link}" placeholder="https://tu-sitio-web.com">
                </div>
            </div>
            
            <button type="submit" class="btn">
                🚀 Crear Cuenta
            </button>
        </form>
        
        <div class="login-link">
            <p>¿Ya tienes cuenta? <a href="${pageContext.request.contextPath}/login">Inicia sesión aquí</a></p>
        </div>
    </div>
    
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            const tipoUsuarioRadios = document.querySelectorAll('input[name="tipoUsuario"]');
            const camposTurista = document.getElementById('campos-turista');
            const camposProveedor = document.getElementById('campos-proveedor');
            
            function toggleCamposEspecificos() {
                const tipoSeleccionado = document.querySelector('input[name="tipoUsuario"]:checked').value;
                
                if (tipoSeleccionado === 'turista') {
                    camposTurista.classList.add('active');
                    camposProveedor.classList.remove('active');
                    document.getElementById('nacionalidad').setAttribute('required', '');
                    document.getElementById('descripcion').removeAttribute('required');
                } else if (tipoSeleccionado === 'proveedor') {
                    camposProveedor.classList.add('active');
                    camposTurista.classList.remove('active');
                    document.getElementById('descripcion').setAttribute('required', '');
                    document.getElementById('nacionalidad').removeAttribute('required');
                }
            }
            
            tipoUsuarioRadios.forEach(radio => {
                radio.addEventListener('change', toggleCamposEspecificos);
            });
            
            // Ejecutar al cargar la página
            if (document.querySelector('input[name="tipoUsuario"]:checked')) {
                toggleCamposEspecificos();
            }
        });
    </script>
</body>
</html>
