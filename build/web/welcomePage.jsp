<%-- 
    Document   : WelcomePage
    Created on : Dec 21, 2021, 8:18:33 PM
    Author     : Karoline Malko
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta name="viewport" http-equiv="Content-Type" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
        <link rel="stylesheet" href="CSS/CSSFunction.css">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Welcome to The master of knowledge library!</h1>
        <h2>Login as:</h2>
        <form method="post">
           <div class="col-sm-1">  <button type=\"submit\" class="form-control" name="visitSub">Visitor</button></div>
           <div class="col-sm-1">  <button type=\"submit\" class="form-control" name="adminSub">Administrator</button></div>
        </form>
    </body>
</html>