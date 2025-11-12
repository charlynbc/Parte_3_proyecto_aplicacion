<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*" %>
<%@ page import="uy.edu.pa.central.client.*" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Mis Salidas Registradas - Turismo.uy</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600;700&display=swap" rel="stylesheet">
</head>
<body>
    <jsp:include page="/WEB-INF/jsp/includes/navbar.jsp" />

    <div class="main-content">
        <div class="page-header">
            <h1>Mis Salidas Registradas</h1>
            <p>Aquí verás las salidas en las que estás inscripto</p>
        </div>

        <%
            String error = (String) request.getAttribute("error");
            if (error != null) {
        %>
            <div class="alert alert-error"><%= error %></div>
        <%
            }

            List<InscripcionDTO> inscripcionesDto = (List<InscripcionDTO>) request.getAttribute("inscripcionesDto");
            if (inscripcionesDto != null && !inscripcionesDto.isEmpty()) {
        %>
            <div class="card-grid">
                <%
                    for (InscripcionDTO ins : inscripcionesDto) {
                        String img = (ins.getImagen() != null && !ins.getImagen().trim().isEmpty())
                                ? ins.getImagen() : "https://images.unsplash.com/photo-1482192505345-5655af888cc4?q=80&w=1200&auto=format&fit=crop";
                %>
                    <div class="card">
                        <div class="card-image">
                            <img src="<%= img %>" alt="Imagen de salida"/>
                        </div>
                        <div class="card-content">
                            <div class="card-title"><%= ins.getSalidaNombre() %></div>
                            <div class="card-description">
                                <div><strong>Actividad:</strong> <%= ins.getActividadNombre() %></div>
                                <div><strong>Fecha:</strong> <%= ins.getFechaSalida() %>
                                    <strong style="margin-left:12px;">Hora:</strong> <%= ins.getHoraSalida() != null ? ins.getHoraSalida() : "" %></div>
                                <div><strong>Lugar:</strong> <%= ins.getLugar() %></div>
                                <div><strong>Cantidad Inscripta:</strong> <%= ins.getCantidad() %></div>
                                <div><strong>Inscripto el:</strong> <%= ins.getFechaInscripcion() != null ? ins.getFechaInscripcion() : "" %></div>
                            </div>
                        </div>
                    </div>
                <%
                    }
                %>
            </div>
        <%
            } else if (error == null) {
        %>
            <div class="empty-state">
                <p>¡Aún no te inscribiste a ninguna salida!</p>
                <a href="<%= request.getContextPath() %>/activities" class="btn">Explorar actividades</a>
            </div>
        <%
            }
        %>
    </div>

    <script src="${pageContext.request.contextPath}/js/app.js"></script>
    </body>
    </html>
