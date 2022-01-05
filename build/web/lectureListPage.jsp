<%-- 
    Document   : lectureListPage
    Created on : Dec 22, 2021, 9:12:40 PM
    Author     : Karoline Malko
--%>

<%@page import="beans.OngoingLectureInfo"%>
<%@page import="java.util.ArrayList"%>
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
        <title>Lecture List Page</title>
    </head>
    <body>
        <h1>List of the lectures:</h1>
        <%  StringBuilder content = new StringBuilder();
            ArrayList<OngoingLectureInfo> lectures = (ArrayList<OngoingLectureInfo>)session.getAttribute("lectures");
            content.append("<div class=\"container\">");
            content.append("<table class=\"table table-striped\">");
            content.append("<thead>\n" +
                                "<tr>\n" +
                                  "<th>Subject</th>\n" +
                                  "<th>Date</th>\n" +
                                  "<th>Enrolled Visitors</th>\n" +
                                "</tr>\n" +
                            "</thead>\n"
                        + "<tbody>");
            for(int i=0; i<lectures.size(); i++){
                content.append("<tr>\n");
                content.append("<td>" +lectures.get(i).getLectureSubject() + "</td>\n" 
                                + "<td>" +lectures.get(i).getLectureDate() +"</td>\n" 
                                + "<td>" + lectures.get(i).getEnrolledVisitorsNum() + "</td>\n");
                content.append("</tr>\n");
            }
            content.append("</tbody>\n");
            content.append("</table>\n");
            content.append("</div>");
            String lecturesList = content.toString();
            System.out.println(lecturesList);
            out.println(String.format("<p>%s</p>", lecturesList));
            %>
    </body>
</html>
