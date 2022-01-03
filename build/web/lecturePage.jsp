<%-- 
    Document   : lecturesPage
    Created on : Dec 22, 2021, 7:45:11 PM
    Author     : Karoline Malko
--%>

<%@page import="beans.IncomingLecture"%>
<%@page import="java.util.ArrayList"%>
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
    }
%>

<!DOCTYPE html>
<html>
    <head>
        <meta name="viewport" http-equiv="Content-Type" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Available lectures:</h1>
         <%
            ArrayList<IncomingLecture> lectures = (ArrayList<IncomingLecture>)session.getAttribute("weekIncomingLecture");
            int lecturesNumber = lectures.size();
            StringBuilder content = new StringBuilder();
            content.append("<form method=\"post\">");
            content.append("<div class=\"container\">");
            content.append("<table class=\"table table-striped\">");
            content.append("<thead>\n" +
                                "<tr>\n" +
                                    "<th>Date</th>\n" +
                                    "<th>Subject</th>\n" +
                                    "<th>Lecturer FirstName</th>\n" +
                                    "<th>Lecturer LastName</th>\n" +
                                    "<th>Room Id</th>\n" +
                                "</tr>\n" +
                            "</thead>\n"
                        + "<tbody>");
            for(int i = 0; i < lecturesNumber; i++) {
                content.append("<tr>\n");
                content.append("<td>\n");
                content.append(lectures.get(i).getDate());
                content.append("</td>\n" );
                content.append("<td>\n");
                content.append(lectures.get(i).getSubject());
                content.append("</td>\n" );
                content.append("<td>\n");
                content.append(lectures.get(i).getLecturerFirstName());
                content.append("</td>\n" );
                content.append("<td>\n");
                content.append(lectures.get(i).getLecturerLastName());
                content.append("</td>\n" );
                content.append("<td>\n");
                content.append(lectures.get(i).getRoomId());
                content.append("</td>\n" );
                content.append("<td>\n");
                content.append("<div class=\"col-sm-offset-2 col-sm-10\">"); 
                content.append("<button type=\"submit\" class=\"btn btn-default\" name =\"bookLecture\" value =\""+ lectures.get(i).getId() + "\">book a seat</button>");
                content.append("</div>"); 
                content.append("</td>\n" );
                content.append("</tr>\n");
            }
            content.append("</tbody>\n");
            content.append("</table>\n");
            content.append("</div>");
            content.append("</form>");
            
            String lecutresInfo = content.toString();
            out.println(String.format("<p>%s</p>", lecutresInfo));
        %>
    </body>
</html>
