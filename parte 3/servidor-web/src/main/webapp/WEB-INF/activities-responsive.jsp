<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="datatypes.DataActividad" %>
<%@ page import="datatypes.DataSalida" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Actividades - Turismo.uy</title>
    
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css" rel="stylesheet">
    
    <style>
        .activity-card {
            border: none;
            box-shadow: 0 5px 15px rgba(0,0,0,0.1);
            border-radius: 15px;
            transition: transform 0.3s ease;
            overflow: hidden;
        }
        
        .activity-card:hover {
            transform: translateY(-5px);
        }
        
        .activity-image {
            height: 200px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 3rem;
            color: white;
        }
        
        .price-badge {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 0.5rem 1rem;
            border-radius: 20px;
            font-weight: bold;
        }
        
        .search-section {
            background: white;
            border-radius: 15px;
            padding: 1.5rem;
            margin-bottom: 2rem;
            box-shadow: 0 5px 15px rgba(0,0,0,0.1);
        }
        
        @media (max-width: 768px) {
            .activity-image {
                height: 150px;
                font-size: 2rem;
            }
            
            .search-section {
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
                        <a class="nav-link active" href="${pageContext.request.contextPath}/activities">
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
        <!-- Header -->
        <div class="text-center mb-4">
            <h1 class="display-6 fw-bold text-primary mb-2">
                <i class="bi bi-compass me-3"></i>Actividades Turísticas
            </h1>
            <p class="lead text-muted">Descubre experiencias únicas en Uruguay</p>
        </div>
        
        <!-- Search Section -->
        <div class="search-section">
            <form method="get" action="${pageContext.request.contextPath}/activities">
                <div class="row g-3">
                    <div class="col-md-6">
                        <label for="search" class="form-label">
                            <i class="bi bi-search me-2"></i>Buscar actividad
                        </label>
                        <input type="text" 
                               class="form-control" 
                               id="search" 
                               name="search" 
                               placeholder="Nombre de la actividad..."
                               value="${param.search}">
                    </div>
                    <div class="col-md-4">
                        <label for="ciudad" class="form-label">
                            <i class="bi bi-geo me-2"></i>Ciudad
                        </label>
                        <select class="form-select" id="ciudad" name="ciudad">
                            <option value="">Todas las ciudades</option>
                            <option value="Montevideo" ${param.ciudad == 'Montevideo' ? 'selected' : ''}>Montevideo</option>
                            <option value="Punta del Este" ${param.ciudad == 'Punta del Este' ? 'selected' : ''}>Punta del Este</option>
                            <option value="Colonia" ${param.ciudad == 'Colonia' ? 'selected' : ''}>Colonia</option>
                            <option value="Salto" ${param.ciudad == 'Salto' ? 'selected' : ''}>Salto</option>
                        </select>
                    </div>
                    <div class="col-md-2">
                        <label class="form-label d-block">&nbsp;</label>
                        <button type="submit" class="btn btn-primary w-100">
                            <i class="bi bi-search me-1"></i>Buscar
                        </button>
                    </div>
                </div>
            </form>
        </div>
        
        <!-- Activities Grid -->
        <div class="row">
            <%
                DataActividad[] actividades = (DataActividad[]) request.getAttribute("actividades");
                if (actividades != null && actividades.length > 0) {
                    for (DataActividad actividad : actividades) {
            %>
            <div class="col-lg-4 col-md-6 mb-4">
                <div class="card activity-card h-100">
                    <div class="activity-image">
                        🏞️
                    </div>
                    <div class="card-body">
                        <h5 class="card-title fw-bold"><%= actividad.getNombre() %></h5>
                        <p class="card-text text-muted">
                            <i class="bi bi-geo-alt me-1"></i><%= actividad.getCiudad() %>
                        </p>
                        <p class="card-text">
                            <%= actividad.getDescripcion().length() > 100 ? 
                                actividad.getDescripcion().substring(0, 100) + "..." : 
                                actividad.getDescripcion() %>
                        </p>
                        <div class="d-flex justify-content-between align-items-center mb-3">
                            <small class="text-muted">
                                <i class="bi bi-clock me-1"></i><%= actividad.getDuracion() %> horas
                            </small>
                            <span class="price-badge">
                                $<%= actividad.getCosto() %>
                            </span>
                        </div>
                        <div class="d-grid">
                            <a href="${pageContext.request.contextPath}/activity-detail?nombre=<%= java.net.URLEncoder.encode(actividad.getNombre(), "UTF-8") %>" 
                               class="btn btn-outline-primary">
                                <i class="bi bi-eye me-2"></i>Ver Detalles
                            </a>
                        </div>
                    </div>
                </div>
            </div>
            <%
                    }
                } else {
            %>
            <div class="col-12">
                <div class="text-center py-5">
                    <div class="display-1 text-muted mb-3">🔍</div>
                    <h3 class="text-muted">No se encontraron actividades</h3>
                    <p class="text-muted">Intenta ajustar tus criterios de búsqueda</p>
                    <a href="${pageContext.request.contextPath}/activities" class="btn btn-primary">
                        <i class="bi bi-arrow-clockwise me-2"></i>Ver Todas las Actividades
                    </a>
                </div>
            </div>
            <%
                }
            %>
        </div>
        
        <!-- Mobile Quick Actions -->
        <div class="d-md-none mt-4">
            <div class="row">
                <div class="col-6">
                    <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-outline-secondary w-100">
                        <i class="bi bi-house me-1"></i>Dashboard
                    </a>
                </div>
                <div class="col-6">
                    <a href="${pageContext.request.contextPath}/profile" class="btn btn-outline-primary w-100">
                        <i class="bi bi-person me-1"></i>Mi Perfil
                    </a>
                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>