<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registro de Proveedor - Turismo.uy</title>
    
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css" rel="stylesheet">
    
    <style>
        body {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            padding: 2rem 0;
        }
        
        .register-card {
            background: rgba(255, 255, 255, 0.95);
            backdrop-filter: blur(10px);
            border: none;
            border-radius: 20px;
            box-shadow: 0 20px 40px rgba(0,0,0,0.1);
        }
        
        .register-header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border-radius: 20px 20px 0 0;
            padding: 2rem;
            text-align: center;
        }
        
        .form-floating {
            margin-bottom: 1rem;
        }
        
        .form-floating > .form-control,
        .form-floating > .form-select {
            border: 2px solid #e9ecef;
            border-radius: 10px;
            transition: all 0.3s ease;
        }
        
        .form-floating > .form-control:focus,
        .form-floating > .form-select:focus {
            border-color: #667eea;
            box-shadow: 0 0 0 0.2rem rgba(102, 126, 234, 0.25);
        }
        
        .register-btn {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border: none;
            border-radius: 10px;
            padding: 0.75rem 2rem;
            font-weight: 600;
            transition: transform 0.3s ease;
        }
        
        .register-btn:hover {
            transform: translateY(-2px);
            background: linear-gradient(135deg, #764ba2 0%, #667eea 100%);
        }
        
        .alert {
            border: none;
            border-radius: 10px;
        }
        
        .provider-icon {
            font-size: 3rem;
            margin-bottom: 1rem;
        }
        
        @media (max-width: 576px) {
            .register-header {
                padding: 1.5rem;
            }
            
            .card-body {
                padding: 2rem 1.5rem;
            }
            
            body {
                padding: 1rem 0;
            }
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-lg-8 col-md-10">
                <div class="card register-card">
                    <div class="register-header">
                        <div class="provider-icon">🏢</div>
                        <h2 class="mb-1">
                            <i class="bi bi-briefcase-fill me-2"></i>Registro de Proveedor
                        </h2>
                        <p class="mb-0 opacity-75">Ofrece tus servicios turísticos en Uruguay</p>
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
                        
                        <!-- Registration Form -->
                        <form method="post" action="${pageContext.request.contextPath}/register-provider" novalidate>
                            <!-- Basic Information Section -->
                            <div class="mb-4">
                                <h5 class="text-primary mb-3">
                                    <i class="bi bi-person me-2"></i>Información Personal
                                </h5>
                                
                                <div class="row">
                                    <div class="col-md-6">
                                        <div class="form-floating">
                                            <input type="text" 
                                                   class="form-control" 
                                                   id="nickname" 
                                                   name="nickname" 
                                                   placeholder="Nickname"
                                                   value="${param.nickname}"
                                                   required>
                                            <label for="nickname">
                                                <i class="bi bi-at me-2"></i>Nickname
                                            </label>
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="form-floating">
                                            <input type="email" 
                                                   class="form-control" 
                                                   id="email" 
                                                   name="email" 
                                                   placeholder="Email"
                                                   value="${param.email}"
                                                   required>
                                            <label for="email">
                                                <i class="bi bi-envelope me-2"></i>Email
                                            </label>
                                        </div>
                                    </div>
                                </div>
                                
                                <div class="row">
                                    <div class="col-md-6">
                                        <div class="form-floating">
                                            <input type="text" 
                                                   class="form-control" 
                                                   id="nombre" 
                                                   name="nombre" 
                                                   placeholder="Nombre"
                                                   value="${param.nombre}"
                                                   required>
                                            <label for="nombre">
                                                <i class="bi bi-person me-2"></i>Nombre
                                            </label>
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="form-floating">
                                            <input type="text" 
                                                   class="form-control" 
                                                   id="apellido" 
                                                   name="apellido" 
                                                   placeholder="Apellido"
                                                   value="${param.apellido}"
                                                   required>
                                            <label for="apellido">
                                                <i class="bi bi-person me-2"></i>Apellido
                                            </label>
                                        </div>
                                    </div>
                                </div>
                                
                                <div class="form-floating">
                                    <input type="date" 
                                           class="form-control" 
                                           id="fechaNac" 
                                           name="fechaNac" 
                                           placeholder="Fecha de Nacimiento"
                                           value="${param.fechaNac}"
                                           required>
                                    <label for="fechaNac">
                                        <i class="bi bi-calendar me-2"></i>Fecha de Nacimiento
                                    </label>
                                </div>
                            </div>
                            
                            <!-- Security Section -->
                            <div class="mb-4">
                                <h5 class="text-primary mb-3">
                                    <i class="bi bi-shield-lock me-2"></i>Seguridad
                                </h5>
                                
                                <div class="row">
                                    <div class="col-md-6">
                                        <div class="form-floating">
                                            <input type="password" 
                                                   class="form-control" 
                                                   id="password" 
                                                   name="password" 
                                                   placeholder="Contraseña"
                                                   required
                                                   minlength="6">
                                            <label for="password">
                                                <i class="bi bi-lock me-2"></i>Contraseña
                                            </label>
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="form-floating">
                                            <input type="password" 
                                                   class="form-control" 
                                                   id="confirmPassword" 
                                                   name="confirmPassword" 
                                                   placeholder="Confirmar Contraseña"
                                                   required
                                                   minlength="6">
                                            <label for="confirmPassword">
                                                <i class="bi bi-lock-fill me-2"></i>Confirmar Contraseña
                                            </label>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            
                            <!-- Business Information Section -->
                            <div class="mb-4">
                                <h5 class="text-primary mb-3">
                                    <i class="bi bi-building me-2"></i>Información del Negocio
                                </h5>
                                
                                <div class="form-floating">
                                    <textarea class="form-control" 
                                              id="descripcion" 
                                              name="descripcion" 
                                              placeholder="Descripción del negocio"
                                              style="height: 100px"
                                              required>${param.descripcion}</textarea>
                                    <label for="descripcion">
                                        <i class="bi bi-file-text me-2"></i>Descripción del Negocio
                                    </label>
                                </div>
                                
                                <div class="form-floating">
                                    <input type="url" 
                                           class="form-control" 
                                           id="link" 
                                           name="link" 
                                           placeholder="Sitio Web"
                                           value="${param.link}">
                                    <label for="link">
                                        <i class="bi bi-link-45deg me-2"></i>Sitio Web (Opcional)
                                    </label>
                                </div>
                            </div>
                            
                            <div class="d-grid gap-2 mt-4">
                                <button type="submit" class="btn btn-primary register-btn">
                                    <i class="bi bi-briefcase-fill me-2"></i>Registrarse como Proveedor
                                </button>
                            </div>
                        </form>
                        
                        <!-- Links -->
                        <div class="text-center mt-4">
                            <p class="text-muted mb-2">¿Ya tienes una cuenta?</p>
                            <a href="${pageContext.request.contextPath}/login" class="text-decoration-none me-3">
                                <i class="bi bi-box-arrow-in-right me-1"></i>Iniciar Sesión
                            </a>
                            <span class="text-muted">|</span>
                            <a href="${pageContext.request.contextPath}/register-tourist" class="text-decoration-none ms-3">
                                <i class="bi bi-person me-1"></i>Registro de Turista
                            </a>
                        </div>
                        
                        <!-- Mobile Back Button -->
                        <div class="d-md-none text-center mt-3">
                            <a href="${pageContext.request.contextPath}/" class="btn btn-outline-secondary">
                                <i class="bi bi-arrow-left me-1"></i>Volver al Inicio
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    
    <!-- Form Validation -->
    <script>
        // Password confirmation validation
        document.getElementById('confirmPassword').addEventListener('input', function() {
            const password = document.getElementById('password').value;
            const confirmPassword = this.value;
            
            if (password !== confirmPassword) {
                this.setCustomValidity('Las contraseñas no coinciden');
            } else {
                this.setCustomValidity('');
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
        
        // Auto-resize textarea
        document.getElementById('descripcion').addEventListener('input', function() {
            this.style.height = 'auto';
            this.style.height = (this.scrollHeight) + 'px';
        });
    </script>
</body>
</html>