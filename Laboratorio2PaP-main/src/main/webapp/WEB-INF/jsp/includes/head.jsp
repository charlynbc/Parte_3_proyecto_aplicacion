<%-- 
    Head Include - Meta tags y CSS comunes
    Incluye configuración para dispositivos móviles
--%>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=5.0, user-scalable=yes">
<meta name="description" content="Turismo.uy - Descubre las mejores experiencias turísticas de Uruguay">
<meta name="keywords" content="turismo, uruguay, actividades, salidas turísticas">
<meta name="author" content="Turismo.uy">

<!-- iOS Meta Tags -->
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="default">
<meta name="apple-mobile-web-app-title" content="Turismo.uy">

<!-- Android Meta Tags -->
<meta name="mobile-web-app-capable" content="yes">
<meta name="theme-color" content="#007bff">

<!-- Favicon -->
<link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/favicon.png">

<!-- CSS Principal -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">

<!-- Prevenir zoom en inputs iOS -->
<style>
    @supports (-webkit-touch-callout: none) {
        input, select, textarea {
            font-size: 16px !important;
        }
    }
</style>
