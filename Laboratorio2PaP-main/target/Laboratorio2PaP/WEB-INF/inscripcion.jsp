<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Inscripción a Salida - Turismo.uy</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/navbar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/inscripcion.css">
    
</head>
<body>
    <jsp:include page="/WEB-INF/jsp/includes/navbar.jsp" />
    
    <div class="inscription-container">
        <div class="inscription-card">
            <div class="inscription-header">
                <h2>🎟️ Inscripción a Salida Turística</h2>
                <p>Complete el formulario para registrarse en una salida</p>
            </div>

            <c:if test="${not empty error}">
                <div class="error-message">${error}</div>
            </c:if>
            <c:if test="${not empty success}">
                <div class="success-message">${success}</div>
            </c:if>

            <form method="post" action="inscripcion" id="inscripcionForm">
                <div class="form-group">
                    <label for="actividad">Actividad Turística</label>
                    <select id="actividad" name="actividad" onchange="onActividadChange(this.value)" required>
                        <option value="">-- Seleccione una actividad --</option>
                        <%= request.getAttribute("actividadesHtml") != null ? request.getAttribute("actividadesHtml") : "" %>
                    </select>
                    <div class="help-text">Elija la actividad turística de su interés</div>
                </div>

                <div class="form-group">
                    <label for="salida">Salida Disponible</label>
                    <select id="salida" name="salida" required>
                        <%= request.getAttribute("salidasHtml") != null ? request.getAttribute("salidasHtml") : "<option value=\"\">-- Seleccione una salida --</option>" %>
                    </select>
                    <div class="help-text">Seleccione la fecha y horario de la salida</div>
                </div>

                <div class="form-group">
                    <label for="cantidad">Cantidad de Turistas</label>
                    <input type="number" id="cantidad" name="cantidad" value="1" min="1" max="50" required>
                    <div class="help-text">Indique el número de personas que se inscribirán</div>
                </div>

                <div class="form-actions">
                    <button type="submit" class="btn btn-primary">
                        ✓ Confirmar Inscripción
                    </button>
                    <a href="${pageContext.request.contextPath}/" class="btn btn-secondary">
                        ← Cancelar
                    </a>
                </div>
            </form>
        </div>
    </div>

    <script>
        // Cuando cambia la actividad recargamos la página enviando la actividad por GET
        function onActividadChange(actividad) {
            var url = new URL(window.location.href);
            if (!actividad) {
                url.searchParams.delete('actividad');
                url.searchParams.delete('salida');
            } else {
                url.searchParams.set('actividad', actividad);
                url.searchParams.delete('salida'); // Limpiar salida al cambiar actividad
            }
            window.location.href = url.toString();
        }
        
        // Pre-seleccionar actividad y salida si vienen en los atributos del request
        (function() {
            var actividadParam = '<%= request.getAttribute("actividadSeleccionada") != null ? request.getAttribute("actividadSeleccionada") : "" %>';
            var salidaParam = '<%= request.getAttribute("salidaSeleccionada") != null ? request.getAttribute("salidaSeleccionada") : "" %>';
            
            // Pre-seleccionar actividad
            if (actividadParam) {
                var selActividad = document.getElementById('actividad');
                for (var i = 0; i < selActividad.options.length; i++) {
                    if (selActividad.options[i].value === actividadParam) {
                        selActividad.selectedIndex = i;
                        break;
                    }
                }
            }
            
            // Pre-seleccionar salida
            if (salidaParam) {
                var selSalida = document.getElementById('salida');
                for (var j = 0; j < selSalida.options.length; j++) {
                    if (selSalida.options[j].value === salidaParam) {
                        selSalida.selectedIndex = j;
                        break;
                    }
                }
            }
        })();

        // Form validation feedback
        document.getElementById('inscripcionForm').addEventListener('submit', function(e) {
            const actividad = document.getElementById('actividad').value;
            const salida = document.getElementById('salida').value;
            
            if (!actividad || !salida) {
                e.preventDefault();
                alert('Por favor, complete todos los campos obligatorios.');
            }
        });
    </script>
</body>
</html>
