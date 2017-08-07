<%-- 
    Document   : forum
    Created on : 08-Jul-2017, 00:05:30
    Author     : tszin
--%>

<%@page import="uk.tryzub.entity.Topic"%>
<%@page import="java.util.List"%>
<%@page import="uk.tryzub.entity.Dating"%>
<%@page import="uk.tryzub.controllers.TopicHelper"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html> 
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Форум на Тризубі</h1>
        <% TopicHelper topicHelper = new TopicHelper();
            List<Topic> arr = topicHelper.getTopics();
        %>


        <table border="1" cellpadding="1">
            <thead>
                <tr>
                    <th>Номер</th>
                    <th>Тема</th>
                    <th>Автор</th>
                    <th>Відповіді</th>
                    <th>Перегляди</th>
                    <th>Останній коментарій</th>
                </tr>
            </thead>

            <tbody>
                <% for (int i = 0; i < arr.size(); i++) {%>
                <tr>                    
                    <td><%= arr.get(i).getTopicid()%></td>
                    <td><a href="postforumu.jsp?topicid=<%= arr.get(i).getTopicid()%>"> <%= arr.get(i).getName()%></a></td>
                    <td><%= arr.get(i).getUser().getUsername()%></td>
                    <td><%= arr.get(i).getAnswers()%></td>
                    <td><%= arr.get(i).getViews()%></td>
                    <td><%= topicHelper.getLastPostFromTopic(arr.get(i)).getText()%></td>
                </tr> 

                <%}%>
            </tbody>
        </table>




    </body>
</html>
