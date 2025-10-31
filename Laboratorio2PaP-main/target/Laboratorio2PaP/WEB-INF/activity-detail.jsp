<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Detalle de Actividad</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <style>
        .activity-header {
            background: white;
            border-radius: 12px;
            padding: 2rem;
            margin-bottom: 2rem;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        
        .activity-image {
            width: 100%;
            height: 300px;
            border-radius: 8px;
            overflow: hidden;
            margin-bottom: 1.5rem;
        }
        
        .info-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 1.5rem;
        }
        
        .info-item {
            text-align: center;
        }
        
        .info-label {
            font-size: 0.9rem;
            color: #666;
            margin-bottom: 0.5rem;
        }
        
        .info-value {
            font-size: 1.2rem;
            font-weight: 600;
            color: #333;
        }
        
        .departure-list {
            background: white;
            border-radius: 12px;
            padding: 2rem;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        
        .departure-list h2 {
            margin-bottom: 1.5rem;
            color: #2c3e50;
        }
        
        .departure-item {
            border: 2px solid #e1e5e9;
            border-radius: 8px;
            padding: 1.5rem;
            margin-bottom: 1rem;
            transition: all 0.3s;
        }
        
        .departure-item:hover {
            border-color: #4facfe;
            box-shadow: 0 4px 8px rgba(79, 172, 254, 0.2);
        }
        
        .departure-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 0.75rem;
        }
        
        .departure-name {
            font-size: 1.2rem;
            font-weight: 600;
            color: #333;
        }
        
        .departure-date {
            color: #4facfe;
            font-weight: 500;
        }
        
        .departure-details {
            color: #666;
            margin-bottom: 1rem;
        }
    </style>
</head>
<body>
    <jsp:include page="/WEB-INF/jsp/includes/navbar.jsp"/>

    <div class="main-content">
        <div class="page-header">
            <h1>${activity.nombre}</h1>
            <p>${activity.descripcion}</p>
        </div>
        
        <div class="activity-header">
            <div class="activity-image">
                <div style="width:100%;height:100%;background:#eee;display:flex;align-items:center;justify-content:center;color:#666;font-size:3rem;">
                    🏞️
                </div>
            </div>
            <div class="info-grid">
                <div class="info-item">
                    <div class="info-label">Duración</div>
                    <div class="info-value">${activity.duracion} horas</div>
                </div>
                <div class="info-item">
                    <div class="info-label">Costo</div>
                    <div class="info-value">$${activity.costo} UYU</div>
                </div>
                <div class="info-item">
                    <div class="info-label">Ciudad</div>
                    <div class="info-value">${activity.ciudad}</div>
                </div>
                <div class="info-item">
                    <div class="info-label">Proveedor</div>
                    <div class="info-value">${activity.proveedor}</div>
                </div>
            </div>
        </div>
        
        <div class="departure-list">
            <h2>Salidas Disponibles</h2>
            <c:if test="${sessionScope.isActivityOwner}">
                <div class="mb-3">
                    <a href="${pageContext.request.contextPath}/create-departure?activityId=${activity.id}" class="btn" style="display:inline-block;width:auto;padding:12px 24px;">
                        + Crear Nueva Salida
                    </a>
                </div>
            </c:if>
            <c:choose>
                <c:when test="${not empty departures}">
                    <c:forEach var="dep" items="${departures}">
                        <div class="departure-item">
                            <div class="departure-header">
                                <div class="departure-name">${dep.nombre}</div>
                                <div class="departure-date">${dep.fecha} ${dep.hora}</div>
                            </div>
                            <div class="departure-details">
                                Lugar: ${dep.lugar} |
                                Cupos: ${dep.turistasmax}
                            </div>
                            <div>
                                <c:if test="${sessionScope.userType eq 'Turista'}">
                                    <a href="${pageContext.request.contextPath}/inscripcion?id=${dep.nombre}" class="btn-small" style="background:#27ae60;">Inscribirse</a>
                                </c:if>
                            </div>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <div class="empty-state">
                        <p>No hay salidas disponibles para esta actividad.</p>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
    <script src="${pageContext.request.contextPath}/js/app.js"></script>
</body>
</html>