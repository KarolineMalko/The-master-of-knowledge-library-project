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
        <h1>Add a new lecture:</h1>
        <form method="post">
        <label>Enter the lecture name:</label>
        <input type=\"text\" id="\lecture" name="lectureName" placeholder="lecture name"></input><br><br>
        <label>Enter the date of the lecture:</label>
        <input type=\"text\" id="\lectureDate" name="lectureDate" placeholder="example 22-10-2021"></input><br><br>
        <label>Enter room id:</label>
        <input type="text\" id="roomId" name="roomId" placeholder="example 9"></input><br><br>
        <label>Enter lecturer person number:</label>
        <input type="text\" id="lecturerPerNum" name="lecturerPerNum" placeholder="19720705-XXXX"></input><br><br>
        <button type=\"submit\ name ="addNewLecture" >Add lecture</button>
    </form>
    </body>
</html>
