<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="webservices.SalidaDTO" %>
<%
    if (session == null || session.getAttribute("usuario") == null) {
        response.sendRedirect("../login.jsp");
        return;
    }
    SalidaDTO salida = (SalidaDTO) request.getAttribute("salida");
    if (salida == null) {
        response.sendRedirect("../my-activities.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Editar Salida - Turismo UY</title>
    <link rel="stylesheet" href="../css/style.css">
    <style>
        .edit-container {
            max-width: 800px;
            margin: 2rem auto;
            padding: 2rem;
            background: white;
            border-radius: 12px;
            box-shadow: 0 4px 6px rgba(0,0,0,0.1);
        }
        .form-group {
            margin-bottom: 1.5rem;
        }
        .form-group label {
            display: block;
            margin-bottom: 0.5rem;
            font-weight: 600;
            color: #333;
        }
        .form-group input, .form-group textarea, .form-group select {
            width: 100%;
            padding: 0.75rem;
            border: 2px solid #e0e0e0;
            border-radius: 8px;
            font-size: 1rem;
            transition: border-color 0.3s;
        }
        .form-group input:focus, .form-group textarea:focus, .form-group select:focus {
            outline: none;
            border-color: #667eea;
        }
        .form-group input:disabled {
            background: #f5f5f5;
            cursor: not-allowed;
        }
        .button-group {
            display: flex;
            gap: 1rem;
            margin-top: 2rem;
        }
        .btn-primary, .btn-secondary {
            flex: 1;
            padding: 0.75rem 1.5rem;
            border: none;
            border-radius: 8px;
            font-size: 1rem;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s;
        }
        .btn-primary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }
        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
        }
        .btn-secondary {
            background: #e0e0e0;
            color: #333;
        }
        .btn-secondary:hover {
            background: #d0d0d0;
        }
        .current-image {
            max-width: 300px;
            max-height: 200px;
            border-radius: 8px;
            margin-top: 0.5rem;
        }
        .required {
            color: red;
        }
    </style>
</head>
<body>
    <jsp:include page="../components/header.jsp" />
    
    <div class="edit-container">
        <h1>✏️ Editar Salida</h1>
        <p style="color: #666; margin-bottom: 2rem;">Modificar los detalles de <strong><%= salida.getId() %></strong></p>
        
        <form action="../edit-departure" method="post" enctype="multipart/form-data">
            <div class="form-group">
                <label for="nombre">Nombre de la Salida <span class="required">*</span></label>
                <input type="text" id="nombre" name="nombre" value="<%= salida.getId() %>" 
                       disabled title="El nombre no puede modificarse">
                <input type="hidden" name="nombre" value="<%= salida.getId() %>">
                <small style="color: #666;">El nombre de la salida no puede modificarse</small>
            </div>
            
            <div class="form-group">
                <label for="actividad">Actividad</label>
                <input type="text" value="<%= salida.getActividadNombre() != null ? salida.getActividadNombre() : "N/A" %>" 
                       disabled>
                <small style="color: #666;">La actividad de la salida no puede modificarse</small>
            </div>
            
            <div class="form-group">
                <label for="fecha">Fecha <span class="required">*</span></label>
                <%
                    String fechaInput = "";
                    if (salida.getFecha() != null && !salida.getFecha().isEmpty()) {
                        // Convertir de dd/MM/yyyy a yyyy-MM-dd
                        String[] partes = salida.getFecha().split("/");
                        if (partes.length == 3) {
                            fechaInput = partes[2] + "-" + partes[1] + "-" + partes[0];
                        }
                    }
                %>
                <input type="date" id="fecha" name="fecha" required 
                       value="<%= fechaInput %>">
            </div>
            
            <div class="form-group">
                <label for="hora">Hora <span class="required">*</span></label>
                <input type="time" id="hora" name="hora" required 
                       value="<%= salida.getHora() != null ? salida.getHora() : "" %>">
            </div>
            
            <div class="form-group">
                <label for="lugar">Lugar <span class="required">*</span></label>
                <input type="text" id="lugar" name="lugar" required 
                       value="<%= salida.getLugar() != null ? salida.getLugar() : "" %>"
                       placeholder="Ej: Plaza Independencia, Montevideo">
            </div>
            
            <div class="form-group">
                <label for="cantMax">Cantidad Máxima de Turistas <span class="required">*</span></label>
                <input type="number" id="cantMax" name="cantMax" required min="1"
                       value="<%= salida.getTuristasMax() > 0 ? salida.getTuristasMax() : 10 %>"
                       placeholder="Ej: 20">
            </div>
            
            <div class="form-group">
                <label for="imagen">Imagen (opcional)</label>
                <% if (salida.getImagen() != null && !salida.getImagen().isEmpty()) { %>
                    <p style="color: #666; margin-bottom: 0.5rem;">Imagen actual:</p>
                    <img src="<%= salida.getImagen() %>" alt="Imagen actual" class="current-image">
                <% } else { %>
                    <p style="color: #666;">Esta salida no tiene imagen actualmente</p>
                <% } %>
                <input type="file" id="imagen" name="imagen" accept="image/*">
                <small style="color: #666;">Subir una nueva imagen reemplazará la actual</small>
            </div>
            
            <div class="button-group">
                <button type="button" class="btn-secondary" onclick="window.location.href='../my-activities.jsp'">
                    Cancelar
                </button>
                <button type="submit" class="btn-primary">
                    💾 Guardar Cambios
                </button>
            </div>
        </form>
    </div>
</body>
</html>
