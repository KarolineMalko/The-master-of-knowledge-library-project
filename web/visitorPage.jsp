<%-- 
    Document   : visitorPage
    Created on : Dec 21, 2021, 9:09:27 PM
    Author     : Karoline Malko
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
   
     if(session.getAttribute("visitorID") == null) {
        String str = "<!DOCTYPE html>\n " + 
                        "<html>\n" + 
                            "<head> \n" +
                                "<meta name=\"viewport\" http-equiv=\"Content-Type\" content=\"width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no\">\n" + 
                                "<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css\">\n" +
                                "<title>VisitorOptionsPage</title> \n" + 
                            "</head>\n" + 
                            "<body> \n" +
                               "<h1>You have to login first as visitor to have access for this page, go back to main page to login as Visitor</h1>\n" +
                                    "<button onclick=\"location.href=\'http://localhost:8080/TheMasterOfKnowledgeLibrary/Controller\'\" type=\"button\" class=\"btn btn-default\">Main Page</button>\n" + 
                            "</body>\n" +
                        "</html>\n";
        out.print(str);
        return;
    }else if (session.getAttribute("visitorID") == "") {
        String str = "<!DOCTYPE html>\n " + 
                        "<html>\n" + 
                            "<head> \n" +
                                "<meta name=\"viewport\" http-equiv=\"Content-Type\" content=\"width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no\">\n" + 
                                "<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css\">\n" +
                                "<title>Administrator Page</title> \n" + 
                            "</head>\n" + 
                            "<body> \n" +
                               "<h1>You have entered incorrect email or password. Double check the entered info, and try again! </h1>\n" +
                                    "<button onclick=\"location.href=\'http://localhost:8080/TheMasterOfKnowledgeLibrary/Controller\'\" type=\"button\" class=\"btn btn-default\">Main Page</button>\n" + 
                            "</body>\n" +
                        "</html>\n";
        out.print(str);
        return;
    }
%>

<!DOCTYPE html>
<html>
    <head>
        <meta name="viewport" http-equiv="Content-Type" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
        <title>VisitorOptionsPage</title>
        <body>
            <h1>Welcome! </h1>
            <h2>Book:</h2>
            </form>
            <form class="form-horizontal" method="post">
                <div class="form-group">
                    <div>
                        <button type="submit" class="btn btn-default col-sm-1" name="rentBook">Rent a book</button>
                    </div>
                    <div>
                        <button type="submit" class="btn btn-default col-sm-1" name="returnBook">Return a book</button>
                    </div>
                </div>
                <h2>Lecture:</h2>
                <div>
                    <div>
                        <button type="submit" class="btn btn-default col-sm-1" name="displayLectures">Book a lecture</button>
                    </div>
                </div>
            </form>
    </body>
</html>

