<%-- 
    Document   : lecturesPage
    Created on : Dec 22, 2021, 7:45:11 PM
    Author     : Karoline Malko
--%>

<%@page import="beans.IncomingLecture"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Available lectures:</h1>
         <%
            ArrayList<IncomingLecture> lectures = (ArrayList<IncomingLecture>)session.getAttribute("weekIncomingLecture");
            int lecturesNumber = lectures.size();
            StringBuilder content = new StringBuilder();
            content.append("<form method=\"post\">");
            content.append("<p> Date &emsp;&emsp; Subject &emsp;&emsp; Lecturer first name &emsp;&emsp; Lecturer last name &emsp;&emsp; Room id");
            for(int i = 0; i < lecturesNumber; i++) {
                content.append("<p>");
                content.append(lectures.get(i).getDate());
                content.append("&emsp;&emsp;");
                content.append(lectures.get(i).getSubject());
                content.append("&emsp;&emsp;");
                content.append(lectures.get(i).getLecturerFirstName());
                content.append("&emsp;&emsp;");
                content.append(lectures.get(i).getLecturerLastName());
                content.append("&emsp;&emsp;");
                content.append(lectures.get(i).getRoomId());
                content.append("&emsp;&emsp;");
                content.append("<button type=\"submit\" name =\"bookLecture\" value =\""+ lectures.get(i).getId() + "\">book a seat</button>");
                content.append("</p>");
                
            }
            content.append("</form>");
            
            String lecutresInfo = content.toString();
            out.println(String.format("<p>%s</p>", lecutresInfo));
        %>
    </body>
</html>
