<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="uy.edu.pa.central.client.UserDTO" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Usuarios - Turismo.uy</title>
    <!-- Bootstrap 5 -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
    
    <jsp:include page="/WEB-INF/jsp/includes/navbar.jsp" />

    <div class="container my-4">
        <div class="page-header mb-4">
            <h1 class="display-5 fw-bold">Usuarios Registrados</h1>
            <p class="text-muted">Explora los perfiles de turistas y proveedores</p>
        </div>

        <%
            List<UserDTO> users = (List<UserDTO>) request.getAttribute("users");
            String error = (String) request.getAttribute("error");
            
            if (error != null) {
        %>
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <i class="bi bi-exclamation-triangle-fill"></i> <%= error %>
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        <%
            }
            
            if (users != null && users.size() > 0) {
        %>
            <div class="row g-3">
        <%
                for (UserDTO user : users) {
                    String userType = user.getTipoUsuario() != null ? user.getTipoUsuario() : "usuario";
                    String iconClass = userType.equals("turista") ? "bi-person-circle" : "bi-building";
                    String badgeClass = userType.equals("turista") ? "bg-primary" : "bg-success";
        %>
                <div class="col-12 col-sm-6 col-md-4 col-lg-3">
                    <div class="card h-100 shadow-sm">
                        <div class="card-body text-center">
                            <i class="bi <%= iconClass %> text-primary" style="font-size: 4rem;"></i>
                            <h5 class="card-title mt-3 mb-1"><%= user.getNombre() %></h5>
                            <p class="text-muted small mb-2">@<%= user.getNickname() %></p>
                            <span class="badge <%= badgeClass %>"><%= userType.toUpperCase() %></span>
                            <p class="card-text small text-muted mt-2">
                                <i class="bi bi-envelope"></i> <%= user.getEmail() %>
                            </p>
                        </div>
                        <div class="card-footer bg-transparent">
                            <a href="<%= request.getContextPath() %>/user-profile?nickname=<%= user.getNickname() %>" 
                               class="btn btn-sm btn-outline-primary w-100">
                                <i class="bi bi-eye"></i> Ver Perfil
                            </a>
                        </div>
                    </div>
                </div>
        <%
                }
        %>
            </div>
        <%
            } else {
        %>
            <div class="alert alert-info text-center" role="alert">
                <i class="bi bi-info-circle fs-1 d-block mb-3"></i>
                <h4>No hay usuarios registrados todavía</h4>
                <p class="mb-0">Cuando se registren usuarios, aparecerán aquí.</p>
            </div>
        <%
            }
        %>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/app.js"></script>
</body>
</html>
