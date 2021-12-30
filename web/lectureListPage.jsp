<%-- 
    Document   : lectureListPage
    Created on : Dec 22, 2021, 9:12:40 PM
    Author     : Karoline Malko
--%>

<%@page import="beans.OngoingLectureInfo"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>List of the lectures:</h1>
        <%  StringBuilder content = new StringBuilder();
            ArrayList<OngoingLectureInfo> lectures = (ArrayList<OngoingLectureInfo>)session.getAttribute("lectures");
            for(int i=0; i<lectures.size(); i++){
                content.append("<form method=\"post\" >\n");
                content.append("<p>" + "Name of the Lecture: " +lectures.get(i).getLectureSubject() + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" 
                                + "Date of the lecture: " +lectures.get(i).getLectureDate() + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"  
                                + "Number of students: " + lectures.get(i).getEnrolledVisitorsNum() + "</p>\n");
                content.append("</form>\n");
            }
            String lecturesList = content.toString();
            System.out.println(lecturesList);
            out.println(String.format("<p>%s</p>", lecturesList));
            %>
    </body>
</html>
