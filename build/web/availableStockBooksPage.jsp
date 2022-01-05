<%-- 
    Document   : rentalBookPage
    Created on : Dec 22, 2021, 7:31:36 PM
    Author     : Karoline Malko
--%>

<%@page import="java.util.ArrayList"%>
<%@page import="beans.StockBook"%>
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
                                    "<button onclick=\"location.href=\'http://localhost:8080/TheMasterOfKnowledgeLibrary/Controller\'\" type=\"button\" class=\"btn btn-default\" >Main Page</button>\n" + 
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
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Books available in stock:</h1>
        <h4>choose the book that you want to rent: </h4>
        
        <%
            ArrayList<StockBook> stockBooks = (ArrayList<StockBook>)session.getAttribute("stockBooks");
            int stockBooksNumber = stockBooks.size();
            StringBuilder content = new StringBuilder();
            content.append("<form method=\"post\">");
            content.append("<div class=\"container\">");
            content.append("<table class=\"table table-striped\">");
            content.append("<thead>\n" +
                                "<tr>\n" +
                                    "<th>ISBN</th>\n" +
                                    "<th>Title</th>\n" +
                                    "<th>Author</th>\n" +
                                    "<th>Language</th>\n" +
                                    "<th>Publish Date</th>\n" +
                                    "<th>Available Copies</th>\n" +
                                "</tr>\n" +
                            "</thead>\n"
                        + "<tbody>");
            for(int i = 0; i < stockBooksNumber; i++) {
                content.append("<tr>\n");
                content.append("<td>\n");
                content.append(stockBooks.get(i).getIsbn());
                content.append("</td>\n" );
                content.append("<td>\n");
                content.append(stockBooks.get(i).getTitle());
                content.append("</td>\n" );
                content.append("<td>\n");
                content.append(stockBooks.get(i).getAuthor());
                content.append("</td>\n" );
                content.append("<td>\n");
                content.append(stockBooks.get(i).getLanguage());
                content.append("</td>\n" );
                content.append("<td>\n");
                content.append(stockBooks.get(i).getPublishDate());
                content.append("</td>\n" );
                content.append("<td>\n");
                content.append(stockBooks.get(i).getAvailableCopies());
                content.append("</td>\n" );
                content.append("<td>\n");
                if(stockBooks.get(i).getAvailableCopies() <= 0) {
                    content.append("<button type=\"button\" disabled class=\"btn btn-default\" name =\"rentBookAction\" value =\""+ stockBooks.get(i).getIsbn() + "\">Rent</button>");
                }else {
                    content.append("<button type=\"submit\" class=\"btn btn-default\" name =\"rentBookAction\" value =\""+ stockBooks.get(i).getIsbn() + "\">Rent</button>");
                }
                content.append("</div>"); 
                content.append("</td>\n" );
                content.append("</tr>\n");
                
            }
            content.append("</tbody>\n");
            content.append("</table>\n");
            content.append("</div>");
            content.append("</form>");
            
            String bookInfo = content.toString();
            out.println(String.format("%s", bookInfo));
        %>
    </body>
</html>
