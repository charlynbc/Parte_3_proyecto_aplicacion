<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="uy.edu.pa.central.client.ActividadDTO" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Mis Actividades</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
    
    <jsp:include page="/WEB-INF/jsp/includes/navbar.jsp"/>
    
    <div class="main-content">
        <div class="page-header">
            <h1>📊 Mis Actividades</h1>
            <p>Gestiona las actividades que ofreces</p>
            <div class="mt-3">
                <a href="${pageContext.request.contextPath}/create-activity" class="btn" style="display:inline-block;width:auto;padding:12px 24px;">
                    ➕ Crear Nueva Actividad
                </a>
            </div>
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

        <div class="card-grid">
            <%
                List<ActividadDTO> activities = (List<ActividadDTO>) request.getAttribute("activities");
                
                if (activities != null && !activities.isEmpty()) {
                    for (ActividadDTO act : activities) {
                        String imagen = act.getImagen();
                        boolean tieneImagen = imagen != null && !imagen.trim().isEmpty();
            %>
                        <div class="card">
                            <div class="card-image">
                                <% if (tieneImagen) { %>
                                    <img src="<%= imagen %>" alt="<%= act.getId() %>" style="width:100%;height:200px;object-fit:cover;">
                                <% } else { %>
                                    <div style="width:100%;height:150px;background:#eee;display:flex;align-items:center;justify-content:center;color:#666;">
                                        🏞️ Sin imagen
                                    </div>
                                <% } %>
                            </div>
                            <div class="card-content">
                                <div class="card-title"><%= act.getId() != null ? act.getId() : "Actividad" %></div>
                                <div class="card-description">
                                    <%= act.getDescripcion() != null ? act.getDescripcion() : "" %><br><br>
                                    <strong>Costo:</strong> $<%= act.getCosto() %> UYU<br>
                                    <strong>Lugar:</strong> <%= act.getLugar() != null ? act.getLugar() : "-" %>
                                </div>
                                <div class="card-actions" style="display:flex;gap:0.5rem;">
                                    <a href="${pageContext.request.contextPath}/activity-detail?name=<%= java.net.URLEncoder.encode(act.getId() != null ? act.getId() : "", "UTF-8") %>" class="btn-small" style="flex:1;">Ver Detalles</a>
                                    <a href="${pageContext.request.contextPath}/edit-activity?name=<%= java.net.URLEncoder.encode(act.getId() != null ? act.getId() : "", "UTF-8") %>" class="btn-small" style="flex:1;background:#ff9800;">✏️ Editar</a>
                                </div>
                            </div>
                        </div>
            <%
                    }
                } else {
            %>
                    <div class="empty-state">
                        <h3>No has creado actividades aún</h3>
                        <p>Crea tu primera actividad para comenzar a recibir turistas</p>
                        <a href="${pageContext.request.contextPath}/create-activity" class="btn">Crear Primera Actividad</a>
                    </div>
            <%
                }
            %>
        </div>
    </div>
    
    <script src="${pageContext.request.contextPath}/js/app.js"></script>
</body>
</html>
