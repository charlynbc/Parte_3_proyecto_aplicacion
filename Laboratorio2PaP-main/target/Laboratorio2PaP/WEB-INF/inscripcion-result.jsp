<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Inscripción - Resultado</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/buttons.css">
    <style>
        .result-container {
            max-width: 600px;
            margin: 2rem auto;
            padding: 2rem;
            background: #fff;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        .success-message {
            background-color: #d4edda;
            border: 1px solid #c3e6cb;
            color: #155724;
            padding: 1rem;
            border-radius: 4px;
            margin-bottom: 1rem;
        }
        .error-message {
            background-color: #f8d7da;
            border: 1px solid #f5c6cb;
            color: #721c24;
            padding: 1rem;
            border-radius: 4px;
            margin-bottom: 1rem;
        }
        .inscription-details {
            background-color: #f8f9fa;
            padding: 1rem;
            border-radius: 4px;
            margin: 1rem 0;
        }
        .button-group {
            margin-top: 2rem;
            text-align: center;
        }
        .button-group .btn {
            margin: 0 0.5rem;
        }
    </style>
</head>
<body>
    <jsp:include page="/WEB-INF/jsp/includes/navbar.jsp" />
    
    <div class="result-container">
        <h2>Resultado de Inscripción</h2>
        
        <!-- Mensaje de éxito -->
        <c:if test="${not empty success}">
            <div class="success-message">
                <strong>¡Éxito!</strong> ${success}
            </div>
        </c:if>
        
        <!-- Mensaje de error -->
        <c:if test="${not empty error}">
            <div class="error-message">
                <strong>Error:</strong> ${error}
            </div>
        </c:if>
        
        <!-- Detalles de la inscripción si está disponible -->
        <c:if test="${not empty inscripcion}">
            <div class="inscription-details">
                <h3>Detalles de la Inscripción</h3>
                <p><strong>ID de Inscripción:</strong> ${inscripcion.id}</p>
                <p><strong>Usuario:</strong> ${inscripcion.usuario.nombre} ${inscripcion.usuario.apellido}</p>
                <p><strong>Actividad:</strong> ${inscripcion.actividad.nombre}</p>
                <p><strong>Fecha de Inscripción:</strong> 
                    <fmt:formatDate value="${inscripcion.fechaInscripcion}" pattern="dd/MM/yyyy HH:mm"/>
                </p>
                <c:if test="${not empty inscripcion.estado}">
                    <p><strong>Estado:</strong> ${inscripcion.estado}</p>
                </c:if>
            </div>
        </c:if>
        
        <!-- Mensaje por defecto si no hay inscripción específica -->
        <c:if test="${empty inscripcion and empty error}">
            <p>Se registró la inscripción correctamente.</p>
        </c:if>
        
        <!-- Información adicional -->
        <c:if test="${not empty mensaje}">
            <div class="info-message">
                <p>${mensaje}</p>
            </div>
        </c:if>
        
        <!-- Botones de navegación -->
        <div class="button-group">
            <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-primary">
                Ir al Dashboard
            </a>
            <a href="${pageContext.request.contextPath}/inscripciones" class="btn btn-secondary">
                Ver Mis Inscripciones
            </a>
            <a href="${pageContext.request.contextPath}/actividades" class="btn btn-outline">
                Ver Actividades
            </a>
            <a href="${pageContext.request.contextPath}/" class="btn btn-secondary">
                Volver al Inicio
            </a>
        </div>
        
        <!-- Script para auto-redirect en caso de éxito -->
        <c:if test="${not empty success and param.autoRedirect eq 'true'}">
            <script>
                setTimeout(function() {
                    window.location.href = '${pageContext.request.contextPath}/dashboard';
                }, 3000);
            </script>
            <p><small>Serás redirigido automáticamente en 3 segundos...</small></p>
        </c:if>
    </div>
    
    <!-- Scripts adicionales -->
    <script>
        // Función para mostrar/ocultar detalles adicionales
        function toggleDetails() {
            var details = document.querySelector('.inscription-details');
            if (details) {
                details.style.display = details.style.display === 'none' ? 'block' : 'none';
            }
        }
        
        // Validar que los enlaces funcionen correctamente
        document.addEventListener('DOMContentLoaded', function() {
            var buttons = document.querySelectorAll('.btn');
            buttons.forEach(function(button) {
                button.addEventListener('click', function(e) {
                    if (this.href && this.href.includes('undefined')) {
                        e.preventDefault();
                        console.error('Invalid URL detected:', this.href);
                        alert('Error en la navegación. Por favor, contacta al administrador.');
                    }
                });
            });
        });
    </script>
</body>
</html>