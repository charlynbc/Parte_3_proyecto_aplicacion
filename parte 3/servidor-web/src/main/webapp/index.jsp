<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Turismo.uy - Bienvenido</title>
    <link rel="stylesheet" href="css/styles.css">
</head>
<body>
    
<div class="welcome-container">
    <jsp:include page="/WEB-INF/jsp/includes/navbar.jsp" />
    
    <div class="welcome-hero">
        <div style="font-size: 5em; margin-bottom: 20px;">🏖️</div>
        <h1>Bienvenido a Turismo.uy</h1>
        <p>Tu plataforma para descubrir las mejores experiencias turísticas de Uruguay</p>
    </div>
    
    <div class="info-section">
        <h2>¿Qué puedes hacer en Turismo.uy?</h2>
        <div class="features-list">
            <div class="feature-item">
                <div class="feature-icon">🗺️</div>
                <div class="feature-content">
                    <h3>Explorar Actividades</h3>
                    <p>Descubre una amplia variedad de experiencias turísticas en todo Uruguay</p>
                </div>
            </div>
            
            <div class="feature-item">
                <div class="feature-icon">📅</div>
                <div class="feature-content">
                    <h3>Reservar Salidas</h3>
                    <p>Selecciona fechas y horarios que se adapten a tu itinerario</p>
                </div>
            </div>
            
            <div class="feature-item">
                <div class="feature-icon">⭐</div>
                <div class="feature-content">
                    <h3>Gestionar Reservas</h3>
                    <p>Administra tus inscripciones y mantén todo organizado</p>
                </div>
            </div>
            
            <div class="feature-item">
                <div class="feature-icon">💼</div>
                <div class="feature-content">
                    <h3>Para Proveedores</h3>
                    <p>Crea y gestiona tus actividades turísticas profesionalmente</p>
                </div>
            </div>
        </div>
    </div>
    
    <div class="info-section text-center">
        <h2>¿Listo para comenzar?</h2>
        <p style="font-size: 1.2em; color: #666; margin-bottom: 25px;">
            Únete a nuestra comunidad y vive las mejores experiencias en Uruguay
        </p>
        <div style="display: flex; gap: 15px; justify-content: center; flex-wrap: wrap;">
            <a href="login" class="btn btn-primary">Iniciar Sesión</a>
            <a href="register-tourist" class="btn btn-secondary">Crear Cuenta</a>
        </div>
    </div>
    
    <footer>
        <p>&copy; 2025 Turismo.uy - Todos los derechos reservados</p>
        <p>
            <a href="#">Términos y Condiciones</a> | 
            <a href="#">Política de Privacidad</a> | 
            <a href="#">Contacto</a>
        </p>
    </footer>
</div>
</body>
</html>
