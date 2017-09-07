<%-- 
    Document   : Postforumu
    Created on : 24-Jul-2017, 17:59:02
    Author     : tszin
--%>

<%@page import="uk.tryzub.entity.Post"%>
<%@page import="java.util.List"%>
<%@page import="uk.tryzub.controllers.PostHelper"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
         <link href="resources/css/postforumu.css" rel="stylesheet" type="text/css">
    </head>
    <body>
        <% String numberTopic = request.getParameter("topicid");
            PostHelper postHelper = new PostHelper();
            List<Post> arr = postHelper.getPosts(numberTopic);
            int numberPost = 1;
        %>
        
        
        
        <h1> Тема: <%= arr.get(0).getTopic().getName()%></h1>




        <table border="1" cellpadding="1">
            <thead>
                <tr>
                    <th>№</th>
                    <th>Тема</th>
                    <th>Текст</th>
                    <th>Дата</th>
                    <th>Автор</th>                    
                </tr>
            </thead>

            <tbody>
                <% for (int i = 0; i < arr.size(); i++) {%>
                <tr>                    
                    <td><%= numberPost++%></td>
                    <td> <%= arr.get(i).getTopic().getName()%></td>
                    <td><%= arr.get(i).getText()%></td>
                    <td><%= arr.get(i).getDate()%></td>
                    <td><%= arr.get(i).getUser().getUsername()%></td>                    
                </tr> 

                <%}%>
            </tbody>
        </table>

    </body>
</html>
