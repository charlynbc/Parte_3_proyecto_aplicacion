package uy.edu.pa.central.config;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Filtro que reescribe las URLs en los WSDLs para usar la URL pública de Codespaces
 */
@WebFilter(urlPatterns = "/services/*")
public class WsdlPublicUrlFilter implements Filter {
    
    private String publicBaseUrl = null;
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Detectar Codespaces
        String codespaceName = System.getenv("CODESPACE_NAME");
        String domain = System.getenv("GITHUB_CODESPACES_PORT_FORWARDING_DOMAIN");
        
        if (codespaceName != null && domain != null) {
            publicBaseUrl = "https://" + codespaceName + "-9128." + domain + "/central-ws";
            System.out.println("🔄 WsdlPublicUrlFilter inicializado con URL: " + publicBaseUrl);
        } else {
            // También intentar desde propiedades del sistema
            codespaceName = System.getProperty("CODESPACE_NAME");
            domain = System.getProperty("GITHUB_CODESPACES_PORT_FORWARDING_DOMAIN");
            
            if (codespaceName != null && domain != null) {
                publicBaseUrl = "https://" + codespaceName + "-9128." + domain + "/central-ws";
                System.out.println("🔄 WsdlPublicUrlFilter inicializado con URL (props): " + publicBaseUrl);
            }
        }
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        // Solo procesar peticiones WSDL
        String queryString = httpRequest.getQueryString();
        boolean isWsdlRequest = queryString != null && 
            (queryString.contains("wsdl") || queryString.contains("xsd"));
        
        // Detectar si la petición viene desde localhost/red interna
        String remoteAddr = httpRequest.getRemoteAddr();
        boolean isLocalRequest = remoteAddr != null && 
            (remoteAddr.equals("127.0.0.1") || 
             remoteAddr.equals("0:0:0:0:0:0:0:1") || 
             remoteAddr.equals("::1") ||
             remoteAddr.startsWith("10.") ||
             remoteAddr.startsWith("172.16.") ||
             remoteAddr.startsWith("192.168."));
        
        if (isWsdlRequest && publicBaseUrl != null && !isLocalRequest) {
            // Solo reescribir para peticiones externas
            // Usar un wrapper para capturar la respuesta
            ResponseCapturingWrapper wrapper = new ResponseCapturingWrapper(httpResponse);
            
            chain.doFilter(request, wrapper);
            
            // Reescribir las URLs en el contenido
            String content = wrapper.getCapturedContent();
            if (content != null && !content.isEmpty()) {
                // Reemplazar localhost por la URL pública
                content = content.replaceAll(
                    "http://localhost:9128/central-ws",
                    publicBaseUrl
                );
                
                // También reemplazar posibles referencias a localhost sin puerto
                content = content.replaceAll(
                    "http://localhost/central-ws",
                    publicBaseUrl
                );
                
                // Escribir el contenido modificado
                httpResponse.setContentLength(content.getBytes(StandardCharsets.UTF_8).length);
                httpResponse.getWriter().write(content);
            }
        } else {
            // Pasar sin modificar (peticiones locales o no-WSDL)
            chain.doFilter(request, response);
        }
    }
    
    @Override
    public void destroy() {
        // Cleanup si es necesario
    }
    
    /**
     * Wrapper para capturar la respuesta
     */
    private static class ResponseCapturingWrapper extends HttpServletResponseWrapper {
        private final StringWriter contentWriter = new StringWriter();
        private PrintWriter writer;
        
        public ResponseCapturingWrapper(HttpServletResponse response) {
            super(response);
        }
        
        @Override
        public PrintWriter getWriter() throws IOException {
            if (writer == null) {
                writer = new PrintWriter(contentWriter);
            }
            return writer;
        }
        
        @Override
        public ServletOutputStream getOutputStream() throws IOException {
            return new ServletOutputStream() {
                @Override
                public void write(int b) throws IOException {
                    contentWriter.write(b);
                }
                
                @Override
                public boolean isReady() {
                    return true;
                }
                
                @Override
                public void setWriteListener(WriteListener writeListener) {
                    // No-op
                }
            };
        }
        
        public String getCapturedContent() {
            if (writer != null) {
                writer.flush();
            }
            return contentWriter.toString();
        }
    }
}
