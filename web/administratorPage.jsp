<%-- 
    Document   : administratorPage
    Created on : Dec 21, 2021, 9:09:44 PM
    Author     : Karoline Malko
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    if(session.getAttribute("adminID") == "") {
        return;
    }
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
       <h1>Welcome!</h1>
       <%
            out.print("<h2>Book: " + session.getAttribute("adminID") + "</h2>\n");
        %>
        <h2>choose what to do:</h2>
        <form method="post">
            <button type=\"submit\" name="listLectures">Se listed lectures</button>
            <button type=\"submit\" name="newSlot">Create a new slot</button>
        </form>
    </body>
</html>
