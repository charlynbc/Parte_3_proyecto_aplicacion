<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="logica.DataActividad" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Actividades Turísticas</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
    
    <jsp:include page="/WEB-INF/jsp/includes/navbar.jsp"/>
    
    <div class="main-content">
        <div class="page-header">
            <h1>Actividades Turísticas</h1>
            <p>Explora todas las experiencias disponibles</p>
            <%
                String userType = (String) session.getAttribute("userType");
                if ("Proveedor".equals(userType)) {
            %>
                <div class="mt-3">
                    <a href="${pageContext.request.contextPath}/create-activity" class="btn" style="display:inline-block;width:auto;padding:12px 24px;">
                        + Crear Actividad
                    </a>
                </div>
            <%
                }
            %>
        </div>

        <div class="card-grid">
            <%
                DataActividad[] activities = (DataActividad[]) request.getAttribute("activities");
                String errorMessage = (String) request.getAttribute("error");
                
                if (errorMessage != null) {
            %>
                <div class="alert alert-error">
                    Error: <%= errorMessage %>
                </div>
            <%
                } else if (activities != null && activities.length > 0) {
                    for (DataActividad act : activities) {
            %>
                        <div class="card">
                            <div class="card-image">
                                <div style="width:100%;height:150px;background:#eee;display:flex;align-items:center;justify-content:center;color:#666;">
                                    🏞️ Imagen no disponible
                                </div>
                            </div>
                            <div class="card-content">
                                <div class="card-title"><%= act.getNombre() %></div>
                                <div class="card-description">
                                    <%= act.getDescripcion() %><br><br>
                                    <strong>Duración:</strong> <%= act.getDuracion() %> horas<br>
                                    <strong>Costo:</strong> $<%= act.getCosto() %> UYU<br>
                                    <strong>Ciudad:</strong> <%= act.getCiudad() %><br>
                                    <strong>Estado:</strong> <%= act.getEstado() %><br>
                                    <strong>Proveedor:</strong> <%= act.getProveedor() %>
                                </div>
                                <div class="card-actions">
                                    <a href="${pageContext.request.contextPath}/activity-detail?name=<%= java.net.URLEncoder.encode(act.getNombre(), "UTF-8") %>" class="btn-small">Ver Detalles</a>
                                </div>
                            </div>
                        </div>
            <%
                    }
                } else {
            %>
                    <div class="empty-state">
                        <h3>No hay actividades disponibles</h3>
                        <p>No se encontraron actividades para mostrar.</p>
                    </div>
            <%
                }
            %>
        </div>
    </div>
    <script src="${pageContext.request.contextPath}/js/app.js"></script>
</body>
</html>
