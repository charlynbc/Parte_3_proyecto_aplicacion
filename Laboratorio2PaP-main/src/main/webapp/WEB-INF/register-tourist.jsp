<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registro de Turista - Turismo.uy</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/registration.css">
</head>
<body>
<div class="container my-4">
    <div class="row justify-content-center">
        <div class="col-12 col-lg-10">
            <div class="row g-4 align-items-start">
                <div class="col-12 col-md-5 text-center text-md-start">
                    <div style="font-size:3rem;">🧳</div>
                    <h1 class="h4 mt-2">Regístrate como Turista</h1>
                    <p class="text-muted">Únete para descubrir increíbles experiencias turísticas</p>
                </div>
                <div class="col-12 col-md-7">
                    <div class="card shadow-sm">
                        <div class="card-body">
                            <h2 class="h5">Registro de Turista</h2>
                            <p class="text-muted">Completa tus datos para crear una cuenta</p>
                            <jsp:include page="/WEB-INF/jsp/includes/messages.jsp"/>
                            <form method="post" action="${pageContext.request.contextPath}/register-tourist" enctype="multipart/form-data" class="needs-validation" novalidate>
                                <fieldset class="mt-3">
                                    <legend class="fs-6">Información de Cuenta</legend>
                                    <div class="row g-3">
                                        <div class="col-12 col-md-6">
                                            <label for="nickname" class="form-label">Nickname <span class="text-danger">*</span></label>
                                            <input type="text" id="nickname" name="nickname" class="form-control" required pattern="[A-Za-z0-9_]{4,20}" placeholder="Tu usuario" autocomplete="username" autofocus>
                                            <div class="form-text">4-20 caracteres, solo letras, números y guión bajo</div>
                                            <div class="invalid-feedback">Ingrese un nickname válido.</div>
                                        </div>
                                        <div class="col-12 col-md-6">
                                            <label for="email" class="form-label">Email <span class="text-danger">*</span></label>
                                            <input type="email" id="email" name="email" class="form-control" required placeholder="tu@email.com" autocomplete="email">
                                            <div class="invalid-feedback">Ingrese un email válido.</div>
                                        </div>
                                    </div>
                                    <div class="row g-3 mt-1">
                                        <div class="col-12 col-md-6">
                                            <label for="password" class="form-label">Contraseña <span class="text-danger">*</span></label>
                                            <div class="input-group">
                                                <input type="password" id="password" name="password" class="form-control" required minlength="8" placeholder="Tu contraseña" autocomplete="new-password">
                                                <button type="button" class="btn btn-outline-secondary" onclick="togglePassword('password')">👁️</button>
                                            </div>
                                            <div class="form-text">Mínimo 8 caracteres</div>
                                        </div>
                                        <div class="col-12 col-md-6">
                                            <label for="confirmPassword" class="form-label">Confirmar Contraseña <span class="text-danger">*</span></label>
                                            <div class="input-group">
                                                <input type="password" id="confirmPassword" name="confirmPassword" class="form-control" required minlength="8" placeholder="Repite tu contraseña" autocomplete="new-password">
                                                <button type="button" class="btn btn-outline-secondary" onclick="togglePassword('confirmPassword')">👁️</button>
                                            </div>
                                            <div class="invalid-feedback">Las contraseñas deben coincidir.</div>
                                        </div>
                                    </div>
                                </fieldset>

                                <fieldset class="mt-3">
                                    <legend class="fs-6">Información Personal</legend>
                                    <div class="row g-3">
                                        <div class="col-12 col-md-6">
                                            <label for="firstName" class="form-label">Nombre <span class="text-danger">*</span></label>
                                            <input type="text" id="firstName" name="firstName" class="form-control" required placeholder="Tu nombre" autocomplete="given-name">
                                        </div>
                                        <div class="col-12 col-md-6">
                                            <label for="lastName" class="form-label">Apellido <span class="text-danger">*</span></label>
                                            <input type="text" id="lastName" name="lastName" class="form-control" required placeholder="Tu apellido" autocomplete="family-name">
                                        </div>
                                    </div>
                                    <div class="row g-3 mt-1">
                                        <div class="col-12 col-md-6">
                                            <label for="birthDate" class="form-label">Fecha de Nacimiento <span class="text-danger">*</span></label>
                                            <input type="date" id="birthDate" name="birthDate" class="form-control" required autocomplete="bday">
                                        </div>
                                        <div class="col-12 col-md-6">
                                            <label for="nationality" class="form-label">Nacionalidad <span class="text-danger">*</span></label>
                                            <input type="text" id="nationality" name="nationality" class="form-control" required placeholder="Tu nacionalidad" autocomplete="country-name">
                                        </div>
                                    </div>
                                </fieldset>

                                <fieldset class="mt-3">
                                    <legend class="fs-6">Imagen de Perfil</legend>
                                    <div class="mb-3">
                                        <label for="profileImage" class="form-label">Foto de Perfil (Opcional)</label>
                                        <input type="file" id="profileImage" name="profileImage" class="form-control" accept="image/*">
                                        <div class="form-text">JPG, PNG. Máximo 5MB</div>
                                    </div>
                                </fieldset>

                                <div class="d-flex gap-2 mt-3">
                                    <button type="submit" class="btn btn-primary">✨ Completar Registro</button>
                                    <a href="${pageContext.request.contextPath}/" class="btn btn-outline-secondary">Volver</a>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/js/app.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
<script>
// Validación Bootstrap + chequeo de contraseñas
(function(){
    const form = document.querySelector('form.needs-validation');
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
        form.classList.add('was-validated');
    });
})();
</script>
</body>
</html>
