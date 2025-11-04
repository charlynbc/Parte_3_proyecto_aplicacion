package uy.edu.pa.central.client;

import javax.xml.namespace.QName;
import jakarta.xml.ws.BindingProvider;
import java.net.URL;

/**
 * Cliente de conveniencia para configurar endpoints y exponer métodos simples.
 * Requiere stubs generados por wsimport bajo el mismo package.
 */
public class CentralSoapClient {
    private final String baseUrl; // e.g., http://localhost:8080/central-ws/services

    public CentralSoapClient(String baseUrl) {
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length()-1) : baseUrl;
    }

    public AuthService getAuthService() throws java.lang.Exception {
        URL wsdl = new URL(baseUrl + "/AuthService?wsdl");
        QName serviceName = new QName("http://ws.central.pa.edu.uy/", "AuthService");
        AuthService_Service svc = new AuthService_Service(wsdl, serviceName);
        AuthService port = svc.getAuthServicePort();
        ((BindingProvider) port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, baseUrl + "/AuthService");
        return port;
    }

    public ActividadesService getActividadesService() throws java.lang.Exception {
        URL wsdl = new URL(baseUrl + "/ActividadesService?wsdl");
        QName serviceName = new QName("http://ws.central.pa.edu.uy/", "ActividadesService");
        ActividadesService_Service svc = new ActividadesService_Service(wsdl, serviceName);
        ActividadesService port = svc.getActividadesServicePort();
        ((BindingProvider) port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, baseUrl + "/ActividadesService");
        return port;
    }
}
