<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registro de Proveedor</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <style>
        .registration-container {
            max-width: 900px;
            margin: 40px auto;
            padding: 0 20px;
        }
        
        .registration-hero {
            text-align: center;
            margin-bottom: 40px;
        }
        
        .hero-icon {
            font-size: 80px;
            margin-bottom: 20px;
        }
        
        .registration-hero h1 {
            font-size: 2.5em;
            margin-bottom: 10px;
            color: #2c3e50;
        }
        
        .registration-hero p {
            font-size: 1.2em;
            color: #7f8c8d;
        }
        
        .registration-form-section {
            background: white;
            border-radius: 12px;
            box-shadow: 0 4px 6px rgba(0,0,0,0.1);
            padding: 40px;
        }
        
        .form-header {
            margin-bottom: 30px;
        }
        
        .form-header h2 {
            color: #2c3e50;
            margin-bottom: 10px;
        }
        
        .form-header p {
            color: #7f8c8d;
            margin: 0;
        }
        
        .form-section {
            margin-bottom: 30px;
            border: 1px solid #e0e0e0;
            border-radius: 8px;
            overflow: hidden;
        }
        
        .form-section-header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 15px 20px;
            font-weight: 600;
            font-size: 1.1em;
        }
        
        .form-section-header.business {
            background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
        }
        
        .form-section-header.personal {
            background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
        }
        
        .form-section-header.image {
            background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
        }
        
        .form-section-body {
            padding: 25px;
        }
        
        .form-group {
            margin-bottom: 20px;
        }
        
        .form-row {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 20px;
            margin-bottom: 20px;
        }
        
        .form-label {
            display: block;
            margin-bottom: 8px;
            color: #2c3e50;
            font-weight: 500;
        }
        
        .required {
            color: #e74c3c;
        }
        
        .form-control {
            width: 100%;
            padding: 12px;
            border: 2px solid #e0e0e0;
            border-radius: 6px;
            font-size: 1em;
            transition: all 0.3s ease;
            box-sizing: border-box;
        }
        
        .form-control:focus {
            outline: none;
            border-color: #667eea;
            box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
        }
        
        textarea.form-control {
            resize: vertical;
            min-height: 100px;
        }
        
        .form-hint {
            font-size: 0.9em;
            color: #7f8c8d;
            margin-top: 5px;
        }
        
        .password-toggle-group {
            position: relative;
        }
        
        .password-toggle-group input {
            padding-right: 50px;
        }
        
        .password-toggle-btn {
            position: absolute;
            right: 10px;
            top: 50%;
            transform: translateY(-50%);
            background: none;
            border: none;
            color: #7f8c8d;
            cursor: pointer;
            font-size: 1.2em;
            padding: 5px 10px;
        }
        
        .password-toggle-btn:hover {
            color: #667eea;
        }
        
        .btn {
            padding: 14px 28px;
            border: none;
            border-radius: 6px;
            font-size: 1.1em;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
            text-decoration: none;
            display: inline-block;
            text-align: center;
        }
        
        .btn-primary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            width: 100%;
            margin-bottom: 15px;
        }
        
        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
        }
        
        .btn-secondary {
            background: #ecf0f1;
            color: #2c3e50;
            width: 100%;
        }
        
        .btn-secondary:hover {
            background: #d5dbdb;
        }
        
        .file-input-wrapper {
            position: relative;
        }
        
        .file-input-wrapper input[type="file"] {
            position: absolute;
            opacity: 0;
            width: 100%;
            height: 100%;
            cursor: pointer;
        }
        
        .file-input-label {
            display: block;
            padding: 12px;
            border: 2px dashed #e0e0e0;
            border-radius: 6px;
            text-align: center;
            cursor: pointer;
            transition: all 0.3s ease;
        }
        
        .file-input-label:hover {
            border-color: #667eea;
            background: #f8f9fa;
        }
        
        @media (max-width: 768px) {
            .registration-container {
                padding: 0 15px;
            }
            
            .registration-form-section {
                padding: 25px 20px;
            }
            
            .form-row {
                grid-template-columns: 1fr;
            }
            
            .registration-hero h1 {
                font-size: 2em;
            }
        }
    </style>
</head>
<body>

<jsp:include page="/WEB-INF/jsp/includes/navbar.jsp"/>

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

        <form method="post" action="${pageContext.request.contextPath}/register-provider" enctype="multipart/form-data">

            <!-- Información de Cuenta -->
            <div class="form-section">
                <div class="form-section-header">
                    🔐 Información de Cuenta
                </div>
                <div class="form-section-body">
                    <div class="form-row">
                        <div class="form-group">
                            <label for="nickname" class="form-label">
                                Nickname <span class="required">*</span>
                            </label>
                            <input type="text" class="form-control" id="nickname" name="nickname" required placeholder="Ej: turismo_uy">
                        </div>
                        <div class="form-group">
                            <label for="email" class="form-label">
                                Email <span class="required">*</span>
                            </label>
                            <input type="email" class="form-control" id="email" name="email" required placeholder="tu@email.com">
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="form-group">
                            <label for="password" class="form-label">
                                Contraseña <span class="required">*</span>
                            </label>
                            <div class="password-toggle-group">
                                <input type="password" class="form-control" id="password" name="password" required placeholder="••••••••">
                                <button class="password-toggle-btn" type="button" onclick="togglePassword('password')">
                                    👁️
                                </button>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="confirmPassword" class="form-label">
                                Confirmar Contraseña <span class="required">*</span>
                            </label>
                            <div class="password-toggle-group">
                                <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" required placeholder="••••••••">
                                <button class="password-toggle-btn" type="button" onclick="togglePassword('confirmPassword')">
                                    👁️
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Información Personal -->
            <div class="form-section">
                <div class="form-section-header personal">
                    👤 Información Personal
                </div>
                <div class="form-section-body">
                    <div class="form-row">
                        <div class="form-group">
                            <label for="firstName" class="form-label">
                                Nombre <span class="required">*</span>
                            </label>
                            <input type="text" class="form-control" id="firstName" name="firstName" required placeholder="Tu nombre">
                        </div>
                        <div class="form-group">
                            <label for="lastName" class="form-label">
                                Apellido <span class="required">*</span>
                            </label>
                            <input type="text" class="form-control" id="lastName" name="lastName" required placeholder="Tu apellido">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="birthDate" class="form-label">
                            Fecha de Nacimiento <span class="required">*</span>
                        </label>
                        <input type="date" class="form-control" id="birthDate" name="birthDate" required
                               max="${java.time.LocalDate.now()}" autocomplete="bday">
                        <div class="form-hint">Debes ser mayor de edad para registrarte</div>
                    </div>
                </div>
            </div>

            <!-- Información del Negocio -->
            <div class="form-section">
                <div class="form-section-header business">
                    🏪 Información del Negocio
                </div>
                <div class="form-section-body">
                    <div class="form-group">
                        <label for="description" class="form-label">
                            Descripción <span class="required">*</span>
                        </label>
                        <textarea class="form-control" id="description" name="description" required placeholder="Describe los servicios turísticos que ofreces..."></textarea>
                        <div class="form-hint">Cuenta a los turistas sobre tu empresa y experiencias</div>
                    </div>
                    <div class="form-group">
                        <label for="website" class="form-label">Sitio Web (Opcional)</label>
                        <input type="url" class="form-control" id="website" name="website" placeholder="https://www.tuempresa.com">
                    </div>
                </div>
            </div>

            <!-- Imagen de Perfil -->
            <div class="form-section">
                <div class="form-section-header image">
                    📷 Imagen de Perfil
                </div>
                <div class="form-section-body">
                    <div class="form-group">
                        <label for="profileImage" class="form-label">Imagen de Perfil (Opcional)</label>
                        <div class="file-input-wrapper">
                            <input type="file" class="form-control" id="profileImage" name="profileImage" accept="image/*">
                            <div class="file-input-label">
                                📤 Seleccionar imagen de perfil
                            </div>
                        </div>
                        <div class="form-hint">Formatos aceptados: JPG, PNG, GIF (máx. 5MB)</div>
                    </div>
                </div>
            </div>

            <!-- Botones -->
            <div style="margin-top: 30px;">
                <button type="submit" class="btn btn-primary">
                    ✓ Registrarse como Proveedor
                </button>
                <a href="${pageContext.request.contextPath}/" class="btn btn-secondary">
                    ← Volver al Inicio
                </a>
            </div>
        </form>
    </div>
</div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/js/app.js"></script>
<script>
function togglePassword(inputId) {
    const input = document.getElementById(inputId);
    const button = input.parentElement.querySelector('.password-toggle-btn');
    
    if (input.type === 'password') {
        input.type = 'text';
        button.textContent = '🙈';
    } else {
        input.type = 'password';
        button.textContent = '👁️';
    }
}

// Previsualización de imagen
document.getElementById('profileImage')?.addEventListener('change', function(e) {
    const file = e.target.files[0];
    const label = this.parentElement.querySelector('.file-input-label');
    
    if (file) {
        label.textContent = '✓ ' + file.name;
        label.style.borderColor = '#43e97b';
        label.style.color = '#43e97b';
    }
});

// Validación de contraseñas
const form = document.querySelector('form');
form?.addEventListener('submit', function(e) {
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirmPassword').value;
    
    if (password !== confirmPassword) {
        e.preventDefault();
        alert('Las contraseñas no coinciden. Por favor, verifica e intenta nuevamente.');
        document.getElementById('confirmPassword').focus();
    }
});
</script>
</body>
</html>
