<%-- 
    Document   : rentalBookPage
    Created on : Dec 22, 2021, 7:31:36 PM
    Author     : Karoline Malko
--%>

<%@page import="java.util.ArrayList"%>
<%@page import="beans.StockBook"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Books available in stock:</h1>
        <h2>choose the book that you want to rent: </h2>
        
        <%
            ArrayList<StockBook> stockBooks = (ArrayList<StockBook>)session.getAttribute("stockBooks");
            int stockBooksNumber = stockBooks.size();
            StringBuilder content = new StringBuilder();
            content.append("<form method=\"post\">");
            content.append("<p> ISBN &emsp;&emsp; Title &emsp;&emsp; Author &emsp;&emsp; Language &emsp;&emsp; Publish Date &emsp;&emsp; Available Copies");
            for(int i = 0; i < stockBooksNumber; i++) {
                content.append("<p>");
                content.append(stockBooks.get(i).getIsbn());
                content.append("&emsp;&emsp;");
                content.append(stockBooks.get(i).getTitle());
                content.append("&emsp;&emsp;");
                content.append(stockBooks.get(i).getAuthor());
                content.append("&emsp;&emsp;");
                content.append(stockBooks.get(i).getLanguage());
                content.append("&emsp;&emsp;");
                content.append(stockBooks.get(i).getPublishDate());
                content.append("&emsp;&emsp;");
                content.append(stockBooks.get(i).getAvailableCopies());
                content.append("&emsp;&emsp;");
                content.append("<button type=\"submit\" name =\"rentBookAction\" value =\""+ stockBooks.get(i).getIsbn() + "\">Rent</button>");
                content.append("</p>");
                
            }
            content.append("</form>");
            
            String bookInfo = content.toString();
            out.println(String.format("<p>%s</p>", bookInfo));
        %>
    </body>
</html>
