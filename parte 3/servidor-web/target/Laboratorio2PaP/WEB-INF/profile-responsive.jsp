<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="datatypes.DataUsuario" %>
<%@ page import="datatypes.DataTurista" %>
<%@ page import="datatypes.DataProveedor" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mi Perfil - Turismo.uy</title>
    
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css" rel="stylesheet">
    
    <style>
        .profile-card {
            border: none;
            box-shadow: 0 10px 30px rgba(0,0,0,0.1);
            border-radius: 20px;
            overflow: hidden;
        }
        
        .profile-header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 2rem;
            text-align: center;
        }
        
        .profile-avatar {
            width: 100px;
            height: 100px;
            background: rgba(255,255,255,0.2);
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 3rem;
            margin: 0 auto 1rem;
            border: 4px solid rgba(255,255,255,0.3);
        }
        
        .info-card {
            background: white;
            border-radius: 15px;
            padding: 1.5rem;
            box-shadow: 0 5px 15px rgba(0,0,0,0.05);
            margin-bottom: 1.5rem;
        }
        
        .info-label {
            color: #6c757d;
            font-size: 0.9rem;
            margin-bottom: 0.25rem;
        }
        
        .info-value {
            font-weight: 500;
            color: #495057;
        }
        
        .edit-btn {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border: none;
            border-radius: 10px;
            padding: 0.75rem 1.5rem;
            color: white;
            transition: transform 0.3s ease;
        }
        
        .edit-btn:hover {
            transform: translateY(-2px);
            color: white;
        }
        
        @media (max-width: 768px) {
            .profile-header {
                padding: 1.5rem;
            }
            
            .profile-avatar {
                width: 80px;
                height: 80px;
                font-size: 2.5rem;
            }
            
            .info-card {
                padding: 1rem;
            }
        }
    </style>
</head>
<body class="bg-light">
    
    <!-- Responsive Navbar -->
    <nav class="navbar navbar-expand-lg navbar-light bg-white shadow-sm">
        <div class="container">
            <a class="navbar-brand fw-bold text-primary" href="${pageContext.request.contextPath}/dashboard">
                <i class="bi bi-geo-alt-fill me-2"></i>Turismo.uy
            </a>
            
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav me-auto">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/activities">
                            <i class="bi bi-compass me-1"></i>Actividades
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/dashboard">
                            <i class="bi bi-house me-1"></i>Dashboard
                        </a>
                    </li>
                </ul>
                
                <ul class="navbar-nav">
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle active" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown">
                            <i class="bi bi-person-circle me-1"></i>${sessionScope.username}
                        </a>
                        <ul class="dropdown-menu">
                            <li><a class="dropdown-item active" href="${pageContext.request.contextPath}/profile">
                                <i class="bi bi-person-gear me-2"></i>Mi Perfil
                            </a></li>
                            <li><hr class="dropdown-divider"></li>
                            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/logout">
                                <i class="bi bi-box-arrow-right me-2"></i>Cerrar Sesión
                            </a></li>
                        </ul>
                    </li>
                </ul>
            </div>
        </div>
    </nav>
    
    <!-- Main Content -->
    <div class="container mt-4">
        <%
            DataUsuario usuario = (DataUsuario) request.getAttribute("usuario");
            if (usuario != null) {
        %>
        
        <!-- Profile Header -->
        <div class="row justify-content-center">
            <div class="col-lg-8">
                <div class="card profile-card">
                    <div class="profile-header">
                        <div class="profile-avatar">
                            <% if (usuario instanceof DataTurista) { %>
                                👤
                            <% } else if (usuario instanceof DataProveedor) { %>
                                🏢
                            <% } else { %>
                                👤
                            <% } %>
                        </div>
                        <h2 class="mb-1"><%= usuario.getNombre() %> <%= usuario.getApellido() %></h2>
                        <p class="mb-0 opacity-75">
                            <% if (usuario instanceof DataTurista) { %>
                                <i class="bi bi-person me-2"></i>Turista
                            <% } else if (usuario instanceof DataProveedor) { %>
                                <i class="bi bi-briefcase me-2"></i>Proveedor de Servicios
                            <% } %>
                        </p>
                    </div>
                    
                    <div class="card-body p-4">
                        <!-- Basic Information -->
                        <div class="info-card">
                            <h5 class="mb-3">
                                <i class="bi bi-info-circle text-primary me-2"></i>Información Personal
                            </h5>
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <div class="info-label">Email</div>
                                    <div class="info-value">
                                        <i class="bi bi-envelope me-2"></i><%= usuario.getEmail() %>
                                    </div>
                                </div>
                                <div class="col-md-6 mb-3">
                                    <div class="info-label">Nickname</div>
                                    <div class="info-value">
                                        <i class="bi bi-at me-2"></i><%= usuario.getNickname() %>
                                    </div>
                                </div>
                                <div class="col-md-6 mb-3">
                                    <div class="info-label">Fecha de Nacimiento</div>
                                    <div class="info-value">
                                        <i class="bi bi-calendar me-2"></i><%= usuario.getFechaNac() %>
                                    </div>
                                </div>
                                <div class="col-md-6 mb-3">
                                    <div class="info-label">Fecha de Registro</div>
                                    <div class="info-value">
                                        <i class="bi bi-calendar-plus me-2"></i><%= usuario.getFechaReg() %>
                                    </div>
                                </div>
                            </div>
                        </div>
                        
                        <!-- Specific User Type Information -->
                        <% if (usuario instanceof DataTurista) { 
                            DataTurista turista = (DataTurista) usuario;
                        %>
                        <div class="info-card">
                            <h5 class="mb-3">
                                <i class="bi bi-person-badge text-success me-2"></i>Información de Turista
                            </h5>
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <div class="info-label">Nacionalidad</div>
                                    <div class="info-value">
                                        <i class="bi bi-flag me-2"></i><%= turista.getNacionalidad() != null ? turista.getNacionalidad() : "No especificada" %>
                                    </div>
                                </div>
                                <div class="col-md-6 mb-3">
                                    <div class="info-label">Tipo de Usuario</div>
                                    <div class="info-value">
                                        <span class="badge bg-success">
                                            <i class="bi bi-person-check me-1"></i>Turista
                                        </span>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <% } else if (usuario instanceof DataProveedor) { 
                            DataProveedor proveedor = (DataProveedor) usuario;
                        %>
                        <div class="info-card">
                            <h5 class="mb-3">
                                <i class="bi bi-building text-primary me-2"></i>Información de Proveedor
                            </h5>
                            <div class="row">
                                <div class="col-12 mb-3">
                                    <div class="info-label">Descripción</div>
                                    <div class="info-value">
                                        <i class="bi bi-file-text me-2"></i>
                                        <%= proveedor.getDescripcion() != null ? proveedor.getDescripcion() : "No especificada" %>
                                    </div>
                                </div>
                                <div class="col-md-6 mb-3">
                                    <div class="info-label">Sitio Web</div>
                                    <div class="info-value">
                                        <% if (proveedor.getLink() != null && !proveedor.getLink().isEmpty()) { %>
                                            <a href="<%= proveedor.getLink() %>" target="_blank" class="text-decoration-none">
                                                <i class="bi bi-link-45deg me-2"></i><%= proveedor.getLink() %>
                                            </a>
                                        <% } else { %>
                                            <i class="bi bi-link-45deg me-2"></i>No especificado
                                        <% } %>
                                    </div>
                                </div>
                                <div class="col-md-6 mb-3">
                                    <div class="info-label">Tipo de Usuario</div>
                                    <div class="info-value">
                                        <span class="badge bg-primary">
                                            <i class="bi bi-briefcase me-1"></i>Proveedor
                                        </span>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <% } %>
                        
                        <!-- Action Buttons -->
                        <div class="text-center">
                            <button type="button" class="btn edit-btn me-2" data-bs-toggle="modal" data-bs-target="#editProfileModal">
                                <i class="bi bi-pencil me-2"></i>Editar Perfil
                            </button>
                            <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-outline-secondary">
                                <i class="bi bi-arrow-left me-2"></i>Volver al Dashboard
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        
        <% } else { %>
        <div class="row justify-content-center">
            <div class="col-lg-6">
                <div class="text-center py-5">
                    <div class="display-1 text-muted mb-3">❌</div>
                    <h3 class="text-muted">Error al cargar perfil</h3>
                    <p class="text-muted">No se pudo cargar la información del usuario</p>
                    <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-primary">
                        <i class="bi bi-arrow-left me-2"></i>Volver al Dashboard
                    </a>
                </div>
            </div>
        </div>
        <% } %>
        
        <!-- Mobile Quick Actions -->
        <div class="d-md-none mt-4">
            <div class="row">
                <div class="col-6">
                    <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-outline-secondary w-100">
                        <i class="bi bi-house me-1"></i>Dashboard
                    </a>
                </div>
                <div class="col-6">
                    <a href="${pageContext.request.contextPath}/activities" class="btn btn-outline-primary w-100">
                        <i class="bi bi-compass me-1"></i>Actividades
                    </a>
                </div>
            </div>
        </div>
    </div>

    <!-- Edit Profile Modal -->
    <div class="modal fade" id="editProfileModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">
                        <i class="bi bi-pencil me-2"></i>Editar Perfil
                    </h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <p class="text-muted">
                        <i class="bi bi-info-circle me-2"></i>
                        Funcionalidad de edición de perfil en desarrollo.
                    </p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cerrar</button>
                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>