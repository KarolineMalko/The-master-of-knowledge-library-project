<%-- 
    Document   : loginPage
    Created on : Dec 6, 2021, 8:31:16 PM
    Author     : Karoline Malko
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta name="viewport" http-equiv="Content-Type" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
        <title>Administrator Login Page</title>
    </head>
    <body>
        <h1>  Welcome!</h1>
        <h3>  Login with your administrator credential information: </h3>
        
        
        
        <form class="form-horizontal" method="post">
            <div class="form-group">
                <label for="adminEmail" class="col-sm-2 control-label" >Email</label>
                    <div class="col-sm-2">
                        <input type=\"text\" class="form-control" id="adminEmail" name="adminEmail" placeholder="example@kth.se"></input>
                    </div>
            </div>
            <div class="form-group">
                <label for="adminPassword" class="col-sm-2 control-label" >Password</label>
                    <div class="col-sm-2">
                        <input type="password" class="form-control" id="adminPassword" name="adminPassword" placeholder="123e5Np"></input>
                    </div>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <button type="submit" class="btn btn-default">Sign in</button>
                </div>
            </div>
        </form>
        
        
        
    </body>
</html>