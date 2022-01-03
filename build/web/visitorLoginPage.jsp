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
        <title>VisitorLoginPage</title>
    </head>
    <body>
        <h3>Login with your credential information:</h3>
                
        <form class="form-horizontal" method="post">
            <div class="form-group">
                <label for="visitEmail" class="col-sm-2 control-label" >Email</label>
                    <div class="col-sm-2">
                        <input type=\"text\" class="form-control" id="visitEmail" name="visitEmail" placeholder="example@kth.se"></input>
                    </div>
            </div>
            <div class="form-group">
                <label for="visitPassword" class="col-sm-2 control-label" >Password</label>
                    <div class="col-sm-2">
                        <input type="password" class="form-control" id="visitPassword" name="visitPassword" placeholder="123e5Np"></input>
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
