<%-- 
    Document   : showVisitorRentals
    Created on : Dec 28, 2021, 5:34:05 PM
    Author     : garom
--%>

<%@page import="java.util.ArrayList"%>
<%@page import="beans.VisitorCurrentRental"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Here are your current Rentals</h1>
        <h2>Choose the book you want to return:</h2>
        <%
            ArrayList<VisitorCurrentRental> rentals = (ArrayList<VisitorCurrentRental>)session.getAttribute("visitorRentals");
            int rentalsNumber = rentals.size();
            StringBuilder content = new StringBuilder();
            content.append("<form method=\"post\">");
            content.append("<p> ISBN &emsp;&emsp; Title &emsp;&emsp; Author &emsp;&emsp; Language &emsp;&emsp; Rental Date &emsp;&emsp; Return Date");
            for(int i = 0; i < rentalsNumber; i++) {
                content.append("<p>");
                content.append(rentals.get(i).getIsbn());
                content.append("&emsp;&emsp;");
                content.append(rentals.get(i).getTitle());
                content.append("&emsp;&emsp;");
                content.append(rentals.get(i).getAuthor());
                content.append("&emsp;&emsp;");
                content.append(rentals.get(i).getLanguage());
                content.append("&emsp;&emsp;");
                content.append(rentals.get(i).getRental_date());
                content.append("&emsp;&emsp;");
                content.append(rentals.get(i).getReturn_date());
                content.append("&emsp;&emsp;");
                content.append("<button type=\"submit\" name =\"returnBookAction\" value =\""+ rentals.get(i).getIsbn() + "\">Return</button>");
                content.append("</p>");
                
            }
            content.append("</form>");
            
            String bookInfo = content.toString();
            out.println(String.format("<p>%s</p>", bookInfo));
        %>
    </body>
</html>
