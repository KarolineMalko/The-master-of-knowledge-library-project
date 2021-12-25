<%-- 
    Document   : visitorPage
    Created on : Dec 21, 2021, 9:09:27 PM
    Author     : Karoline Malko
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    if(session.getAttribute("userID") == null) {
        return;
    }
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <body>
        <h1>Welcome! </h1>
        <%
            out.print("<h2>Book: " + session.getAttribute("userID") + "</h2>\n");
        %>
        <h2>Book:</h2>
        <form method="post">
            <button type=\"submit\" name="rentBook">Rent a book</button>
            <button type=\"submit\" name="returnBook">return a book</button>
        </form>
        <h2>Lecture:</h2>
        <form method="post">
            <button type=\"submit\" name="bookLecture">Book a lecture</button>
        </form>
    </body>
</html>

