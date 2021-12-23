<%-- 
    Document   : newSlot
    Created on : Dec 22, 2021, 9:12:56 PM
    Author     : Karoline Malko
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
        <body>
        <h1>Welcome!</h1>
        <p>Log in for taking the quiz:</p>
        <form method="post">
        <label>Enter the lecture name:</label>
        <input type=\"text\" id="\lecture" name="lecture" placeholder="lecture name"></input><br><br>
        <label>Enter the date of the lecture:</label>
        <input type=\"text\" id="\lectureDate" name="lectureDate" placeholder="22-10-2021"></input><br><br>
        <label>Enter lecturer name:</label>
        <input type="password" id="lecturerName" name="lecturerName" placeholder="John Andersson"></input><br><br>
        <label>Enter lecturer person number:</label>
        <input type="password" id="lecturerPerNum" name="lecturerPerNum" placeholder="19720705-XXXX"></input><br><br>
        <button type=\"submit\" >Add lecture</button>
    </form>
    </body>
</html>
