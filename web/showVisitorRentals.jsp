<%-- 
    Document   : showVisitorRentals
    Created on : Dec 28, 2021, 5:34:05 PM
    Author     : garom
--%>

<%@page import="java.util.ArrayList"%>
<%@page import="beans.VisitorCurrentRental"%>
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
        <title>Visitor's Rentals</title>
    </head>
    <body>
        <h1>Here are your current Rentals</h1>
        <h4>Choose the book you want to return:</h2>
        <%
            ArrayList<VisitorCurrentRental> rentals = (ArrayList<VisitorCurrentRental>)session.getAttribute("visitorRentals");
            int rentalsNumber = rentals.size();
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
                                    "<th>Rental Date</th>\n" +
                                    "<th>Return Date</th>\n" +
                                "</tr>\n" +
                            "</thead>\n"
                        + "<tbody>");
            for(int i = 0; i < rentalsNumber; i++) {
                content.append("<tr>\n");
                content.append("<td>\n");
                content.append(rentals.get(i).getIsbn());
                content.append("</td>\n" );
                content.append("<td>\n");
                content.append(rentals.get(i).getTitle());
                content.append("</td>\n" );
                content.append("<td>\n");
                content.append(rentals.get(i).getAuthor());
                content.append("</td>\n" );
                content.append("<td>\n");
                content.append(rentals.get(i).getLanguage());
                content.append("</td>\n" );
                content.append("<td>\n");
                content.append(rentals.get(i).getRental_date());
                content.append("</td>\n" );
                content.append("<td>\n");
                content.append(rentals.get(i).getReturn_date());
                content.append("</td>\n" );
                content.append("<td>\n");
                content.append("<button type=\"submit\"  class=\"btn btn-default\" name=\"returnBookAction\" value =\""+ rentals.get(i).getIsbn() + "\">Return</button>\n");
                content.append("</div>\n"); 
                content.append("</td>\n" );
                content.append("</tr>\n");
                
            }
            content.append("</tbody>\n");
            content.append("</table>\n");
            content.append("</div>\n");
            content.append("</form>\n");
            
            String bookInfo = content.toString();
            out.println(String.format("%s", bookInfo));
        %>
    </body>
</html>
