<%-- 
    Document   : loginPage
    Created on : Dec 6, 2021, 8:31:16 PM
    Author     : Karoline Malko
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Welcome!</h1>
        <p>Login with your credential information:</p>
        <form method="post">
            <label>Email:</label>
            <input type=\"text\" id="\visitEmail" name="visitEmail" placeholder="example@kth.se"></input><br><br>
            <label>Password:</label>
            <input type="password" id="visitPassword" name="visitPassword" placeholder="123e5Np"></input><br><br>
            <button type=\"submit\" >login</button>
        </form>
    </body>
</html>
