<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registro de Proveedor</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body class="bg-light">

<div class="container my-4 my-md-5">
    <div class="row justify-content-center">
        <div class="col-12 col-lg-10 col-xl-8">
            
            <!-- Hero Section -->
            <div class="text-center mb-4">
                <div class="display-1 mb-3">🏢</div>
                <h1 class="display-5 fw-bold">Regístrate como Proveedor</h1>
                <p class="text-muted">Ofrece experiencias turísticas únicas</p>
            </div>

            <!-- Form Card -->
            <div class="card shadow-sm">
                <div class="card-body p-4">
                    <h2 class="card-title mb-3">Registro de Proveedor</h2>
                    <p class="text-muted mb-4">Ingresa tus datos para comenzar a publicar actividades</p>

                    <jsp:include page="/WEB-INF/jsp/includes/messages.jsp"/>

                    <form method="post" action="${pageContext.request.contextPath}/register-provider" enctype="multipart/form-data">
                        
                        <!-- Información de Cuenta -->
                        <div class="card mb-3">
                            <div class="card-header bg-primary text-white">
                                <i class="bi bi-person-badge"></i> Información de Cuenta
                            </div>
                            <div class="card-body">
                                <div class="row g-3">
                                    <div class="col-md-6">
                                        <label for="nickname" class="form-label">
                                            Nickname <span class="text-danger">*</span>
                                        </label>
                                        <input type="text" class="form-control" id="nickname" name="nickname" required>
                                    </div>
                                    <div class="col-md-6">
                                        <label for="email" class="form-label">
                                            Email <span class="text-danger">*</span>
                                        </label>
                                        <input type="email" class="form-control" id="email" name="email" required>
                                    </div>
                                    <div class="col-md-6">
                                        <label for="password" class="form-label">
                                            Contraseña <span class="text-danger">*</span>
                                        </label>
                                        <div class="input-group">
                                            <input type="password" class="form-control" id="password" name="password" required>
                                            <button class="btn btn-outline-secondary" type="button" onclick="togglePassword('password')">
                                                <i class="bi bi-eye" id="password-icon"></i>
                                            </button>
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <label for="confirmPassword" class="form-label">
                                            Confirmar Contraseña <span class="text-danger">*</span>
                                        </label>
                                        <div class="input-group">
                                            <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" required>
                                            <button class="btn btn-outline-secondary" type="button" onclick="togglePassword('confirmPassword')">
                                                <i class="bi bi-eye" id="confirmPassword-icon"></i>
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Información Personal -->
                        <div class="card mb-3">
                            <div class="card-header bg-success text-white">
                                <i class="bi bi-person"></i> Información Personal
                            </div>
                            <div class="card-body">
                                <div class="row g-3">
                                    <div class="col-md-6">
                                        <label for="firstName" class="form-label">
                                            Nombre <span class="text-danger">*</span>
                                        </label>
                                        <input type="text" class="form-control" id="firstName" name="firstName" required>
                                    </div>
                                    <div class="col-md-6">
                                        <label for="lastName" class="form-label">
                                            Apellido <span class="text-danger">*</span>
                                        </label>
                                        <input type="text" class="form-control" id="lastName" name="lastName" required>
                                    </div>
                                    <div class="col-12">
                                        <label for="birthDate" class="form-label">
                                            Fecha de Nacimiento <span class="text-danger">*</span>
                                        </label>
                                        <input type="date" class="form-control" id="birthDate" name="birthDate" required 
                                               max="${java.time.LocalDate.now()}" autocomplete="bday">
                                        <div class="form-text">Debes ser mayor de edad para registrarte</div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Información del Negocio -->
                        <div class="card mb-3">
                            <div class="card-header bg-info text-white">
                                <i class="bi bi-building"></i> Información del Negocio
                            </div>
                            <div class="card-body">
                                <div class="row g-3">
                                    <div class="col-12">
                                        <label for="description" class="form-label">
                                            Descripción <span class="text-danger">*</span>
                                        </label>
                                        <textarea class="form-control" id="description" name="description" rows="4" required></textarea>
                                        <div class="form-text">Describe los servicios que ofreces</div>
                                    </div>
                                    <div class="col-12">
                                        <label for="website" class="form-label">Sitio Web (Opcional)</label>
                                        <input type="url" class="form-control" id="website" name="website" placeholder="https://www.ejemplo.com">
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Imagen de Perfil -->
                        <div class="card mb-4">
                            <div class="card-header bg-warning">
                                <i class="bi bi-camera"></i> Imagen de Perfil
                            </div>
                            <div class="card-body">
                                <label for="profileImage" class="form-label">Imagen de Perfil (Opcional)</label>
                                <input type="file" class="form-control" id="profileImage" name="profileImage" accept="image/*">
                                <div class="form-text">Selecciona una imagen para tu perfil</div>
                            </div>
                        </div>

                        <!-- Botones -->
                        <div class="d-grid gap-2">
                            <button type="submit" class="btn btn-primary btn-lg">
                                <i class="bi bi-check-circle"></i> Registrarse como Proveedor
                            </button>
                            <a href="${pageContext.request.contextPath}/" class="btn btn-outline-secondary">
                                <i class="bi bi-arrow-left"></i> Volver
                            </a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/js/app.js"></script>
<script>
function togglePassword(inputId) {
    const input = document.getElementById(inputId);
    const icon = document.getElementById(inputId + '-icon');
    
    if (input.type === 'password') {
        input.type = 'text';
        icon.className = 'bi bi-eye-slash';
    } else {
        input.type = 'password';
        icon.className = 'bi bi-eye';
    }
}
</script>
</body>
</html>
