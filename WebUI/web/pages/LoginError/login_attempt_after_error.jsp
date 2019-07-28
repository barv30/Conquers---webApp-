<%--
    Document   : index
    Created on : Jan 24, 2012, 6:01:31 AM
    Author     : blecherl
    This is the login JSP for the online chat application
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <%@ page import="Server.Utils.SessionUtils" %>
    <%@ page import="Server.Constants.Constants" %>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Conquers</title>
<!--        Link the Bootstrap (from twitter) CSS framework in order to use its classes-->
        <link rel="stylesheet" href="/NinasRow/Common/bootstrap.min.css"/>
<!--        Link jQuery JavaScript library in order to use the $ (jQuery) method-->
<!--        <script src="script/jquery-2.0.3.min.js"></script>-->
<!--        and\or any other scripts you might need to operate the JSP file behind the scene once it arrives to the client-->
    </head>
    <body>
        <div class="container">
            <% String usernameFromSession = SessionUtils.getUsername(request);%>
            <% String usernameFromParameter = request.getParameter(Constants.USERNAME) != null ? request.getParameter(Constants.USERNAME) : "";%>
            <% if (usernameFromSession == null) {%>
            <h1>Play Conquers Online!</h1>
            <br/>
            <h2>Please enter your name:</h2>
            <form method="GET" action="login">
                <input type="text" name="username" class=""/>
                <input type="submit" value="Login"/>
                <br/>
            </form>
            <% Object errorMessage = request.getAttribute(Constants.USER_NAME_ERROR);%>
            <% if (errorMessage != null) {%>
            <span class="bg-danger" style="color:red;"><%=errorMessage%></span>
            <% } %>
            <% }%>
            <img src=Common/Images/armyLogin.png>
        </div>
    </body>
</html>