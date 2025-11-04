<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - Turismo.uy</title>
    
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css" rel="stylesheet">
    
    <style>
        .navbar-brand {
            font-weight: bold;
            color: #667eea !important;
        }
        
        .hero-welcome {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 3rem 0;
            margin-bottom: 2rem;
            border-radius: 15px;
        }
        
        .dashboard-card {
            border: none;
            box-shadow: 0 5px 15px rgba(0,0,0,0.1);
            border-radius: 15px;
            transition: transform 0.3s ease, box-shadow 0.3s ease;
        }
        
        .dashboard-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 10px 25px rgba(0,0,0,0.15);
        }
        
        .action-btn {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border: none;
            padding: 15px;
            border-radius: 10px;
            color: white;
            text-decoration: none;
            display: block;
            text-align: center;
            transition: all 0.3s ease;
        }
        
        .action-btn:hover {
            color: white;
            transform: translateY(-2px);
        }
        
        .user-info {
            background: white;
            border-radius: 15px;
            padding: 1.5rem;
            box-shadow: 0 5px 15px rgba(0,0,0,0.1);
        }
        
        @media (max-width: 768px) {
            .hero-welcome {
                padding: 2rem 0;
            }
            
            .hero-welcome h1 {
                font-size: 1.8rem;
            }
        }
    </style>
</head>
<body class="bg-light">
    
    <!-- Responsive Navbar -->
    <nav class="navbar navbar-expand-lg navbar-light bg-white shadow-sm">
        <div class="container">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/dashboard">
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
                        <a class="nav-link" href="${pageContext.request.contextPath}/profile">
                            <i class="bi bi-person me-1"></i>Mi Perfil
                        </a>
                    </li>
                </ul>
                
                <ul class="navbar-nav">
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown">
                            <i class="bi bi-person-circle me-1"></i>${sessionScope.username}
                        </a>
                        <ul class="dropdown-menu">
                            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/profile">
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
        <!-- Welcome Section -->
        <div class="hero-welcome text-center">
            <div class="container">
                <div class="display-1 mb-3">👋</div>
                <h1 class="display-5 fw-bold">Bienvenido, ${sessionScope.username}</h1>
                <p class="lead">Tu panel de control turístico</p>
            </div>
        </div>

        <div class="row">
            <!-- User Info Panel -->
            <div class="col-lg-4 mb-4">
                <div class="user-info">
                    <h5 class="mb-3">
                        <i class="bi bi-info-circle me-2"></i>Información de Usuario
                    </h5>
                    <p class="mb-2"><strong>Usuario:</strong> ${sessionScope.username}</p>
                    <p class="mb-2"><strong>Email:</strong> ${sessionScope.email}</p>
                    <p class="mb-0"><strong>Tipo:</strong> 
                        <span class="badge bg-primary">${sessionScope.userType}</span>
                    </p>
                </div>
            </div>
            
            <!-- Action Cards -->
            <div class="col-lg-8">
                <div class="row">
                    <%
                        String userType = (String) session.getAttribute("userType");
                        if ("Turista".equals(userType)) {
                    %>
                        <div class="col-md-6 mb-4">
                            <div class="card dashboard-card h-100">
                                <div class="card-body text-center">
                                    <div class="display-4 mb-3">🎯</div>
                                    <h5 class="card-title">Actividades Turísticas</h5>
                                    <p class="card-text">Explora y reserva actividades increíbles</p>
                                    <a href="${pageContext.request.contextPath}/activities" class="action-btn">
                                        Ver Actividades
                                    </a>
                                </div>
                            </div>
                        </div>
                        
                        <div class="col-md-6 mb-4">
                            <div class="card dashboard-card h-100">
                                <div class="card-body text-center">
                                    <div class="display-4 mb-3">📋</div>
                                    <h5 class="card-title">Mis Inscripciones</h5>
                                    <p class="card-text">Revisa tus reservas y actividades</p>
                                    <a href="${pageContext.request.contextPath}/inscriptions" class="action-btn">
                                        Ver Inscripciones
                                    </a>
                                </div>
                            </div>
                        </div>
                    <%
                        } else if ("Proveedor".equals(userType)) {
                    %>
                        <div class="col-md-6 mb-4">
                            <div class="card dashboard-card h-100">
                                <div class="card-body text-center">
                                    <div class="display-4 mb-3">🚌</div>
                                    <h5 class="card-title">Nueva Salida</h5>
                                    <p class="card-text">Crea una nueva salida turística</p>
                                    <a href="${pageContext.request.contextPath}/create-departure" class="action-btn">
                                        Crear Salida
                                    </a>
                                </div>
                            </div>
                        </div>

                        <div class="col-md-6 mb-4">
                            <div class="card dashboard-card h-100">
                                <div class="card-body text-center">
                                    <div class="display-4 mb-3">📊</div>
                                    <h5 class="card-title">Nueva Actividad</h5>
                                    <p class="card-text">Registra una nueva actividad turística</p>
                                    <a href="${pageContext.request.contextPath}/create-activity" class="action-btn">
                                        Crear Actividad
                                    </a>
                                </div>
                            </div>
                        </div>
                        
                        <div class="col-md-6 mb-4">
                            <div class="card dashboard-card h-100">
                                <div class="card-body text-center">
                                    <div class="display-4 mb-3">📈</div>
                                    <h5 class="card-title">Mis Actividades</h5>
                                    <p class="card-text">Gestiona tus actividades creadas</p>
                                    <a href="${pageContext.request.contextPath}/my-activities" class="action-btn">
                                        Ver Actividades
                                    </a>
                                </div>
                            </div>
                        </div>
                    <%
                        }
                    %>
                    
                    <!-- Common actions for all users -->
                    <div class="col-md-6 mb-4">
                        <div class="card dashboard-card h-100">
                            <div class="card-body text-center">
                                <div class="display-4 mb-3">👤</div>
                                <h5 class="card-title">Mi Perfil</h5>
                                <p class="card-text">Actualiza tu información personal</p>
                                <a href="${pageContext.request.contextPath}/profile" class="action-btn">
                                    Editar Perfil
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        
        <!-- Quick Stats for Mobile -->
        <div class="row d-md-none mt-4">
            <div class="col-12">
                <div class="card dashboard-card">
                    <div class="card-body">
                        <h6 class="card-title">Acciones Rápidas</h6>
                        <div class="d-grid gap-2">
                            <a href="${pageContext.request.contextPath}/activities" class="btn btn-outline-primary">
                                <i class="bi bi-compass me-2"></i>Ver Actividades
                            </a>
                            <a href="${pageContext.request.contextPath}/profile" class="btn btn-outline-secondary">
                                <i class="bi bi-person-gear me-2"></i>Editar Perfil
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>