<%-- 
    Document   : ukrbiz
    Created on : 27-Jul-2017, 22:36:09
    Author     : tszin
--%>

<%@page import="java.util.Arrays"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="pragma" content="no-cache">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Укрбіз</title>
        <link href="resources/css/dating.css" rel="stylesheet" type="text/css">

    </head>
    <body>

        <div id="menu">
            <a href="index.xhtml">
                <h3>Головна</h3>
            </a>

            <h3>Українські товари та послуги </h3>
            <h4>Ресторани та кафе</h4>
            <h4>Магазини</h4>
            <h4>Перукарні та салони краси</h4>
            <h4>Індивідуальні майстри: </h4>
            <h5> - сектор краси та здоров'я (зачіски, манікюр, макіяж, масаж і т.п.)</h5>
            <h5> - сектор харчування (виготовлення тортів, смаколиків і т.п.)</h5>
            <h5> - сектор ремонту та будівельних послуг</h5>
            <h4>Перевізники</h4>
            <hr></hr>



            <h3>Громадські організації </h3>
            <h4>Посольство України у Великій Британії</h4>
            <h4>Церкви</h4>
            <h4>Школи та класи з українською мовою</h4>
            <h4>Благодійні організації</h4>








        </div>

        <div id="content">
            <% String []arr=  session.getValueNames();%>
            
            <%= Arrays.toString(arr) %>
          

        </div>

        <div id="cap">

        </div>





    </body>
</html>
