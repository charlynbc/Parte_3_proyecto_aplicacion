<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Turismo.uy - Inicio de Sesión</title>
    
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css" rel="stylesheet">
    
    <!-- Custom CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    
    <style>
        .hero-section {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 3rem 0;
            margin-bottom: 2rem;
        }
        
        .hero-icon {
            font-size: 4rem;
            margin-bottom: 1rem;
        }
        
        .login-container {
            max-width: 400px;
            margin: 0 auto;
        }
        
        .card {
            border: none;
            box-shadow: 0 10px 30px rgba(0,0,0,0.1);
            border-radius: 15px;
        }
        
        .btn-primary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border: none;
            padding: 12px;
            border-radius: 10px;
        }
        
        .form-control:focus {
            border-color: #667eea;
            box-shadow: 0 0 0 0.2rem rgba(102, 126, 234, 0.25);
        }
        
        @media (max-width: 576px) {
            .hero-section {
                padding: 2rem 0;
            }
            
            .hero-icon {
                font-size: 3rem;
            }
            
            .container {
                padding: 0 15px;
            }
        }
    </style>
</head>
<body class="bg-light">
    <!-- Hero Section -->
    <div class="hero-section text-center">
        <div class="container">
            <div class="hero-icon">🏖️</div>
            <h1 class="display-4 fw-bold">Turismo.uy</h1>
            <p class="lead">Descubre las mejores experiencias turísticas de Uruguay</p>
        </div>
    </div>

    <!-- Login Section -->
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-12 col-sm-8 col-md-6 col-lg-4">
                <div class="card">
                    <div class="card-body p-4">
                        <div class="text-center mb-4">
                            <h2 class="card-title mb-2">Iniciar Sesión</h2>
                            <p class="text-muted">Accede a tu cuenta para explorar actividades</p>
                        </div>

                        <jsp:include page="/WEB-INF/jsp/includes/messages.jsp"/>

                        <form method="post" 
                              action="${pageContext.request.contextPath}/login" 
                              novalidate 
                              autocomplete="on">
                            
                            <div class="mb-3">
                                <label for="username" class="form-label">
                                    <i class="bi bi-person-fill me-2"></i>Usuario o Email
                                </label>
                                <input type="text" 
                                       class="form-control" 
                                       id="username" 
                                       name="username" 
                                       value="${param.username != null ? param.username : ''}"
                                       placeholder="Ingresa tu nickname o email"
                                       required
                                       autocomplete="username">
                            </div>

                            <div class="mb-4">
                                <label for="password" class="form-label">
                                    <i class="bi bi-lock-fill me-2"></i>Contraseña
                                </label>
                                <input type="password" 
                                       class="form-control" 
                                       id="password" 
                                       name="password" 
                                       placeholder="Ingresa tu contraseña"
                                       required
                                       autocomplete="current-password">
                            </div>

                            <div class="d-grid mb-3">
                                <button type="submit" class="btn btn-primary btn-lg">
                                    <i class="bi bi-box-arrow-in-right me-2"></i>Iniciar Sesión
                                </button>
                            </div>
                        </form>

                        <div class="text-center">
                            <p class="mb-0">¿No tienes cuenta?</p>
                            <a href="${pageContext.request.contextPath}/register" class="btn btn-outline-secondary btn-sm mt-2">
                                <i class="bi bi-person-plus me-2"></i>Registrarse
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
                <label for="username">Nickname o Email <span class="required" aria-hidden="true">*</span></label>
          <input type="text" 
              id="username" 
              name="username" 
                       required 
                       aria-required="true"
                       aria-describedby="username-hint"
                       placeholder="Ingresa tu nickname o email"
              autocomplete="username"
              value="<%= request.getAttribute("username") != null ? request.getAttribute("username") : "" %>"
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
