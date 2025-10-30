<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
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
                List<?> activities = null;
                String errorMessage = null;
                Exception jspError = null;
                try {
                    activities = (List<?>) request.getAttribute("activities");
                    errorMessage = (String) request.getAttribute("error");
                } catch (Exception ex) {
                    jspError = ex;
                }
                if (jspError != null) {
            %>
                <div class="alert alert-error">
                    <strong>Error JSP:</strong> <%= jspError.getClass().getName() %> - <%= jspError.getMessage() %>
                </div>
            <%
                } else if (errorMessage != null) {
            %>
                <div class="alert alert-error">
                    Error: <%= errorMessage %>
                </div>
            <%
                } else if (activities != null && !activities.isEmpty()) {
                    for (Object obj : activities) {
                        java.util.Map<String, Object> act = (java.util.Map<String, Object>) obj;
                        String nombre = act.get("nombre") != null ? act.get("nombre").toString() : "(sin nombre)";
                        String descripcion = act.get("descripcion") != null ? act.get("descripcion").toString() : "";
                        String ciudad = act.get("ciudad") != null ? act.get("ciudad").toString() : "";
                        String estado = act.get("estado") != null ? act.get("estado").toString() : "";
                        String proveedor = act.get("proveedor") != null ? act.get("proveedor").toString() : "";
                        int duracion = act.get("duracion") != null ? ((Number)act.get("duracion")).intValue() : 0;
                        float costo = act.get("costo") != null ? ((Number)act.get("costo")).floatValue() : 0;
            %>
                        <div class="card">
                            <div class="card-image">
                                <div style="width:100%;height:150px;background:#eee;display:flex;align-items:center;justify-content:center;color:#666;">
                                    🏞️ Imagen no disponible
                                </div>
                            </div>
                            <div class="card-content">
                                <div class="card-title"><%= nombre %></div>
                                <div class="card-description">
                                    <%= descripcion %><br><br>
                                    <strong>Duración:</strong> <%= duracion %> horas<br>
                                    <strong>Costo:</strong> $<%= costo %> UYU<br>
                                    <strong>Ciudad:</strong> <%= ciudad %><br>
                                    <strong>Estado:</strong> <%= estado %><br>
                                    <strong>Proveedor:</strong> <%= proveedor %>
                                </div>
                                <div class="card-actions">
                                    <a href="${pageContext.request.contextPath}/activity-detail?name=<%= java.net.URLEncoder.encode((String)act.get("nombre"), "UTF-8") %>" class="btn-small">Ver Detalles</a>
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
