<%-- 
    Document   : administratorPage
    Created on : Dec 21, 2021, 9:09:44 PM
    Author     : Karoline Malko
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    if(session.getAttribute("adminID") == null) {
        String str = "<!DOCTYPE html>\n " + 
                        "<html>\n" + 
                            "<head> \n" +
                                "<meta name=\"viewport\" http-equiv=\"Content-Type\" content=\"width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no\">\n" + 
                                "<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css\">\n" +
                                "<title>Administrator Page</title> \n" + 
                            "</head>\n" + 
                            "<body> \n" +
                               "<h1>You have to login first as Admintrator to have access for this page, go back to main page to login as Administrator</h1>\n" +
                                    "<button onclick=\"location.href=\'http://localhost:8080/TheMasterOfKnowledgeLibrary/Controller\'\" type=\"button\" class=\"btn btn-default\">Main Page</button>\n" + 
                            "</body>\n" +
                        "</html>\n";
        out.print(str);
        return;
    }
    else if (session.getAttribute("adminID") == "") {
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
        <title>Administrator Page</title>
    </head>
    <body>
       <h1>Welcome!</h1>
        <h2>choose what to do:</h2>
        <form method="post">
          <div class="col-sm-2">  <button type=\"submit\" class="form-control" name="listLectures">Show the ongoing lectures</button> </div>
          <div class="col-sm-2">  <button type=\"submit\" class="form-control" name="showWeekLectures">Show week's incoming lectures</button> </div>
           <div class="col-sm-2"> <button type=\"submit\" class="form-control" name="newSlot">Create a new slot</button></div>
        </form>
    </body>
</html>
