<%@ page import="java.sql.Connection" %>
<%
    try {
        javax.naming.InitialContext ic = new javax.naming.InitialContext();
        Object obj = ic.lookup("java:comp/env/jdbc/railway");
        out.println("Lookup returned: " + (obj == null ? "null" : obj.getClass().getName()));
        if (obj instanceof javax.sql.DataSource) {
            javax.sql.DataSource ds = (javax.sql.DataSource) obj;
            try (Connection c = ds.getConnection()) {
                out.println("<br/>Got connection: OK");
            } catch (Exception e) {
                out.println("<br/>Connection error: " + e.getMessage());
            }
        }
    } catch (Exception e) {
        out.println("Lookup exception: " + e.toString());
    }
%>
