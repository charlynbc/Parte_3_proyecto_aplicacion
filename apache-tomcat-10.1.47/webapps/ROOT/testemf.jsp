<%
    out.println("Testing createEntityManagerFactory(\"emf\") via reflection...<br/>");
    try {
        // Load jakarta.persistence.Persistence reflectively to avoid compile-time dependency
        Class<?> persClass = Class.forName("jakarta.persistence.Persistence");
        java.lang.reflect.Method createMethod = persClass.getMethod("createEntityManagerFactory", String.class);
        Object emf = createMethod.invoke(null, "emf");
        if (emf != null) {
            out.println("EMF instance created: " + emf.getClass().getName() + "<br/>");
            try {
                java.lang.reflect.Method isOpenMethod = emf.getClass().getMethod("isOpen");
                Object isOpen = isOpenMethod.invoke(emf);
                out.println("EMF.isOpen() = " + isOpen + "<br/>");
                java.lang.reflect.Method closeMethod = emf.getClass().getMethod("close");
                closeMethod.invoke(emf);
                out.println("EMF closed.<br/>");
            } catch (NoSuchMethodException ns) {
                out.println("EMF does not expose expected methods.<br/>");
            }
        } else {
            out.println("createEntityManagerFactory returned null.<br/>");
        }
    } catch (Throwable t) {
        out.println("Exception creating EMF: " + t.toString() + "<br/>");
    out.println("<pre>");
    t.printStackTrace(new java.io.PrintWriter(out));
    out.println("</pre>");
    }
%>
