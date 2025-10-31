<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%-- Debug info --%>
<!-- Session: <%= session != null %> -->
<!-- Error: <%= request.getAttribute("error") != null %> -->

<% if (request.getAttribute("error") != null) { %>
    <div class="error-message" style="display: block;">
        <%= request.getAttribute("error") %>
    </div>
<% } %>