<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Iniciar Sesión - Turismo.uy</title>
    
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css" rel="stylesheet">
    
    <style>
        body {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
        }
        
        .login-card {
            background: rgba(255, 255, 255, 0.95);
            backdrop-filter: blur(10px);
            border: none;
            border-radius: 20px;
            box-shadow: 0 20px 40px rgba(0,0,0,0.1);
        }
        
        .login-header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border-radius: 20px 20px 0 0;
            padding: 2rem;
            text-align: center;
        }
        
        .login-logo {
            font-size: 4rem;
            margin-bottom: 1rem;
        }
        
        .form-floating {
            margin-bottom: 1rem;
        }
        
        .form-floating > .form-control {
            border: 2px solid #e9ecef;
            border-radius: 10px;
            transition: all 0.3s ease;
        }
        
        .form-floating > .form-control:focus {
            border-color: #667eea;
            box-shadow: 0 0 0 0.2rem rgba(102, 126, 234, 0.25);
        }
        
        .login-btn {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border: none;
            border-radius: 10px;
            padding: 0.75rem 2rem;
            font-weight: 600;
            transition: transform 0.3s ease;
        }
        
        .login-btn:hover {
            transform: translateY(-2px);
            background: linear-gradient(135deg, #764ba2 0%, #667eea 100%);
        }
        
        .alert {
            border: none;
            border-radius: 10px;
        }
        
        .register-links {
            background: #f8f9fa;
            border-radius: 0 0 20px 20px;
            padding: 1.5rem;
        }
        
        @media (max-width: 576px) {
            .login-header {
                padding: 1.5rem;
            }
            
            .login-logo {
                font-size: 3rem;
            }
            
            .card-body {
                padding: 2rem 1.5rem;
            }
            
            .register-links {
                padding: 1rem;
            }
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-lg-5 col-md-7 col-sm-9">
                <div class="card login-card">
                    <div class="login-header">
                        <div class="login-logo">🌎</div>
                        <h2 class="mb-1">Turismo.uy</h2>
                        <p class="mb-0 opacity-75">Descubre lo mejor de Uruguay</p>
                    </div>
                    
                    <div class="card-body p-4">
                        <!-- Alert Messages -->
                        <% 
                            String error = (String) request.getAttribute("error");
                            String success = (String) request.getAttribute("success");
                            if (error != null) {
                        %>
                        <div class="alert alert-danger d-flex align-items-center" role="alert">
                            <i class="bi bi-exclamation-triangle-fill me-2"></i>
                            <%= error %>
                        </div>
                        <% } %>
                        
                        <% if (success != null) { %>
                        <div class="alert alert-success d-flex align-items-center" role="alert">
                            <i class="bi bi-check-circle-fill me-2"></i>
                            <%= success %>
                        </div>
                        <% } %>
                        
                        <!-- Login Form -->
                        <form method="post" action="${pageContext.request.contextPath}/login" novalidate>
                            <div class="form-floating">
                                <input type="text" 
                                       class="form-control" 
                                       id="nickname" 
                                       name="nickname" 
                                       placeholder="Nickname o Email"
                                       value="${param.nickname}"
                                       required
                                       autocomplete="username">
                                <label for="nickname">
                                    <i class="bi bi-person me-2"></i>Nickname o Email
                                </label>
                            </div>
                            
                            <div class="form-floating">
                                <input type="password" 
                                       class="form-control" 
                                       id="password" 
                                       name="password" 
                                       placeholder="Contraseña"
                                       required
                                       autocomplete="current-password">
                                <label for="password">
                                    <i class="bi bi-lock me-2"></i>Contraseña
                                </label>
                            </div>
                            
                            <div class="form-check mb-3">
                                <input class="form-check-input" 
                                       type="checkbox" 
                                       id="remember" 
                                       name="remember">
                                <label class="form-check-label" for="remember">
                                    Recordar sesión
                                </label>
                            </div>
                            
                            <div class="d-grid">
                                <button type="submit" class="btn btn-primary login-btn">
                                    <i class="bi bi-box-arrow-in-right me-2"></i>Iniciar Sesión
                                </button>
                            </div>
                        </form>
                    </div>
                    
                    <!-- Registration Links -->
                    <div class="register-links text-center">
                        <p class="mb-3 text-muted">¿No tienes una cuenta?</p>
                        <div class="row g-2">
                            <div class="col-6">
                                <a href="${pageContext.request.contextPath}/register-tourist" 
                                   class="btn btn-outline-primary w-100">
                                    <i class="bi bi-person-plus me-1"></i>
                                    <span class="d-none d-sm-inline">Registro </span>Turista
                                </a>
                            </div>
                            <div class="col-6">
                                <a href="${pageContext.request.contextPath}/register-provider" 
                                   class="btn btn-outline-secondary w-100">
                                    <i class="bi bi-briefcase me-1"></i>
                                    <span class="d-none d-sm-inline">Registro </span>Proveedor
                                </a>
                            </div>
                        </div>
                        
                        <!-- Mobile Demo Access -->
                        <div class="d-md-none mt-3">
                            <small class="text-muted">
                                <i class="bi bi-info-circle me-1"></i>
                                Demo: usuario/password o proveedor/password
                            </small>
                        </div>
                    </div>
                </div>
                
                <!-- Footer for larger screens -->
                <div class="text-center mt-4 d-none d-md-block">
                    <p class="text-white-50">
                        <i class="bi bi-shield-check me-1"></i>
                        Conexión segura | Tarea 3 - Programación de Aplicaciones
                    </p>
                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    
    <!-- Form Enhancement -->
    <script>
        // Auto-focus en el primer input
        document.addEventListener('DOMContentLoaded', function() {
            const nicknameInput = document.getElementById('nickname');
            if (nicknameInput && !nicknameInput.value) {
                nicknameInput.focus();
            }
        });
        
        // Bootstrap form validation
        (function() {
            'use strict';
            window.addEventListener('load', function() {
                var forms = document.getElementsByTagName('form');
                var validation = Array.prototype.filter.call(forms, function(form) {
                    form.addEventListener('submit', function(event) {
                        if (form.checkValidity() === false) {
                            event.preventDefault();
                            event.stopPropagation();
                        }
                        form.classList.add('was-validated');
                    }, false);
                });
            }, false);
        })();
        
        // Mejorar UX en móviles
        if (window.innerWidth <= 576) {
            // Ocultar alertas después de 5 segundos en móviles
            setTimeout(function() {
                const alerts = document.querySelectorAll('.alert');
                alerts.forEach(function(alert) {
                    alert.style.transition = 'opacity 0.5s';
                    alert.style.opacity = '0';
                    setTimeout(() => alert.remove(), 500);
                });
            }, 5000);
        }
    </script>
</body>
</html>