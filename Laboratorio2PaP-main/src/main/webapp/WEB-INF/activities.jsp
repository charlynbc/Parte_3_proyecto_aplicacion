<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="uy.edu.pa.central.client.ActividadDTO" %>
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
                List<ActividadDTO> activities = (List<ActividadDTO>) request.getAttribute("activities");
                String errorMessage = (String) request.getAttribute("error");
                
                if (errorMessage != null) {
            %>
                <div class="alert alert-error">
                    Error: <%= errorMessage %>
                </div>
            <%
                } else if (activities != null && !activities.isEmpty()) {
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
                                        🏞️ Imagen no disponible
                                    </div>
                                <% } %>
                            </div>
                            <div class="card-content">
                                <div class="card-title"><%= act.getId() != null ? act.getId() : "Actividad" %></div>
                                <div class="card-description">
                                    <%= act.getDescripcion() != null ? act.getDescripcion() : "" %><br><br>
                                    <strong>Costo:</strong> $<%= act.getCosto() %> UYU<br>
                                    <strong>Lugar:</strong> <%= act.getLugar() != null ? act.getLugar() : "-" %>
                                    
                                    <%
                                        // Mostrar salidas asociadas con cupos
                                        if (act.getSalidas() != null && !act.getSalidas().isEmpty()) {
                                    %>
                                        <div style="margin-top: 15px; padding-top: 15px; border-top: 1px solid #e0e0e0;">
                                            <strong>Salidas Disponibles:</strong>
                                            <div style="margin-top: 10px;">
                                                <% 
                                                    String currentUserType = (String) session.getAttribute("userType");
                                                    boolean esTurista = "Turista".equals(currentUserType);
                                                    
                                                    for (uy.edu.pa.central.client.SalidaDTO salida : act.getSalidas()) { 
                                                %>
                                                    <div style="background: #f8f9fa; padding: 10px; margin-bottom: 8px; border-radius: 4px;">
                                                        <div style="font-weight: 500;"><%= salida.getId() %></div>
                                                        <div style="font-size: 0.9em; color: #666;">
                                                            📅 <%= salida.getFecha() %> 
                                                            <% if (salida.getHora() != null) { %>
                                                                - ⏰ <%= salida.getHora() %>
                                                            <% } %>
                                                        </div>
                                                        <div style="font-size: 0.9em; color: #666;">
                                                            📍 <%= salida.getLugar() != null ? salida.getLugar() : "" %>
                                                        </div>
                                                        <div style="margin-top: 5px;">
                                                            <% 
                                                                String colorCupo = salida.getTuristasMax() > 0 ? "#28a745" : "#dc3545";
                                                                String textoCupo = salida.getTuristasMax() > 0 ? "✓ " + salida.getTuristasMax() + " cupos disponibles" : "✗ Sin cupos";
                                                            %>
                                                            <span style="font-weight: 600; color: <%= colorCupo %>;">
                                                                <%= textoCupo %>
                                                            </span>
                                                            <% if (esTurista && salida.getTuristasMax() > 0) { %>
                                                                <a href="${pageContext.request.contextPath}/inscripcion?actividad=<%= java.net.URLEncoder.encode(act.getId(), "UTF-8") %>&salida=<%= java.net.URLEncoder.encode(salida.getId(), "UTF-8") %>" 
                                                                   class="btn-small" style="margin-left: 10px; font-size: 0.85em; padding: 4px 10px;">
                                                                    Inscribirse
                                                                </a>
                                                            <% } %>
                                                        </div>
                                                    </div>
                                                <% } %>
                                            </div>
                                        </div>
                                    <% } %>
                                </div>
                                <div class="card-actions">
                                    <a href="${pageContext.request.contextPath}/activity-detail?name=<%= java.net.URLEncoder.encode(act.getId() != null ? act.getId() : "", "UTF-8") %>" class="btn-small">Ver Detalles</a>
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