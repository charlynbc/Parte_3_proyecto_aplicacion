<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="uy.edu.pa.central.client.SalidaDTO" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mis Salidas - Turismo UY</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
    <jsp:include page="/WEB-INF/jsp/includes/navbar.jsp"/>
    
    <div class="main-content">
        <div class="page-header">
            <h1>🗓️ Mis Salidas</h1>
            <p>Todas las salidas de tus actividades</p>
        </div>

        <%
            String errorMessage = (String) request.getAttribute("error");
            if (errorMessage != null) {
        %>
            <div class="alert alert-error">
                <%= errorMessage %>
            </div>
        <%
            }
        %>

        <%
            List<SalidaDTO> salidas = (List<SalidaDTO>) request.getAttribute("salidas");
            
            if (salidas != null && !salidas.isEmpty()) {
        %>
            <div class="table-container">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>Salida</th>
                            <th>Actividad</th>
                            <th>Fecha</th>
                            <th>Hora</th>
                            <th>Lugar</th>
                            <th>Cupo Máx.</th>
                            <th>Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            for (SalidaDTO sal : salidas) {
                        %>
                            <tr>
                                <td><strong><%= sal.getId() != null ? sal.getId() : "-" %></strong></td>
                                <td><%= sal.getActividadNombre() != null ? sal.getActividadNombre() : "-" %></td>
                                <td><%= sal.getFecha() != null ? sal.getFecha() : "-" %></td>
                                <td><%= sal.getHora() != null ? sal.getHora() : "-" %></td>
                                <td><%= sal.getLugar() != null ? sal.getLugar() : "-" %></td>
                                <td><%= sal.getTuristasMax() %></td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/edit-departure?id=<%= java.net.URLEncoder.encode(sal.getId() != null ? sal.getId() : "", "UTF-8") %>" 
                                       class="btn-small" style="background:#ff9800;">✏️ Editar</a>
                                </td>
                            </tr>
                        <%
                            }
                        %>
                    </tbody>
                </table>
            </div>
        <%
            } else {
        %>
            <div class="empty-state">
                <h3>No tienes salidas creadas</h3>
                <p>Crea salidas para tus actividades para comenzar</p>
                <a href="${pageContext.request.contextPath}/my-activities" class="btn">Ver Mis Actividades</a>
            </div>
        <%
            }
        %>
    </div>
    
    <script src="${pageContext.request.contextPath}/js/app.js"></script>
</body>
</html>
