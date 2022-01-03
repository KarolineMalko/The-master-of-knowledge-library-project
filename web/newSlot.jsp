<%-- 
    Document   : newSlot
    Created on : Dec 22, 2021, 9:12:56 PM
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
%>

<!DOCTYPE html>
<html>
    <head>
        <meta name="viewport" http-equiv="Content-Type" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
        <title>New lecture Page</title>
    </head>
    <body>
        <h1>Add a new lecture:</h1>
        
        <form class="form-horizontal" method="post">
            <div class="form-group">
                <label for="lecture" class="col-sm-2 control-label" >Enter the lecture name:</label>
                    <div class="col-sm-2">
                        <input type=\"text\" class="form-control" id="lecture" name="lectureName" placeholder="lecture name"></input>
                    </div>
            </div>
            <div class="form-group">
                <label for="lectureDate" class="col-sm-2 control-label" >Enter the date of the lecture:</label>
                    <div class="col-sm-2">
                        <input type=\"text\" class="form-control" id="lectureDate" name="lectureDate" placeholder="2022-01-03 13:15"></input>
                    </div>
            </div>
            <div class="form-group">
                <label for="roomId" class="col-sm-2 control-label" >Enter room id:</label>
                    <div class="col-sm-2">
                        <input type=\"text\" class="form-control" id="roomId" name="roomId" placeholder="9"></input>
                    </div>
            </div>
            <div class="form-group">
                <label for="lecturerPerNum" class="col-sm-2 control-label">Enter lecturer personal Number:</label>
                    <div class="col-sm-2">
                        <input type=\"text\" class="form-control" id="lecturerPerNum" name="lecturerPerNum" placeholder="19720705-XXXX"></input>
                    </div>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <button type="submit" class="btn btn-default" name ="addNewLecture">Add lecture</button>
                </div>
            </div>
        </form>
        
    </body>
</html>
