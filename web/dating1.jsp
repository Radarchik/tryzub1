<%-- 
    Document   : dating1
    Created on : 03-Jul-2017, 11:42:20
    Author     : tszin
--%>

<%@page import="javax.servlet.http.HttpSession"%>
<%@page import="javax.jms.Session"%>
<%@page import="java.util.ArrayList"%>
<%@page import="uk.tryzub.entity.Dating"%>
<%@page import="java.util.List"%>
<%@page import="uk.tryzub.controllers.DatingHelper"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="pragma" content="no-cache">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Знайомства</title>
        <link href="resources/css/dating.css" rel="stylesheet" type="text/css">
                    
    </head>
    <body>
        
        <div id="menu">
            <a href="index.xhtml">
                <h3>Головна</h3>
            </a>

            <h4>Хлопець познайомиться з дівчиною</h4>
            <h4>Дівчина познайомиться з хлопцем</h4>
            <h4>Знайомства для дружби/по інтересам</h4>

            



        </div>

        <div id="content">
            <%!DatingHelper datingHelper = new DatingHelper();
                List<Dating> arr = datingHelper.getDatings();
            %>
            <%! int n = 0;%>
          
            
            <% for (int i = 0; i < arr.size(); i++) {%>

            <div class="messages">

                <div style="background-color: green; color: white; width: 95px; "><%= arr.get(i).getDate()%> </div>
                <div> <%= arr.get(i).getName()%> (<%=arr.get(i).getEmail()%> )</div>
                <div>Місто: <%out.print(arr.get(i).getCity());%> </div>
                </br>
                <div>
                    <%out.print(arr.get(i).getMessage());%> 
                </div>
                <HR></HR>

            </div>
            <% }%>
            <%=n++%>
            
               
        <%= session.getId()%>


            <form action = "ServletFormAddDating" method = "get">
                <table width="100%" border="0" class="xxx">
                    <tbody><tr><td class="xxx">Раздел</td>
                            <td><select name="razdel">
                                    <option value="1">Хлопець познайомиться з дівчиною</option><option value="2">Дівчина познайомиться з хлопцем</option><option value="3">Знайомства для дружби/по інтересам</option></select></td></tr>
                        <tr><td class="xxx">Ваше имя</td><td><input type="text" size="25" name="imja"></td></tr>
                        <tr><td class="xxx">Город</td><td><input type="text" size="25" name="city"></td></tr>
                        <tr><td class="xxx">E-mail</td><td><input type="text" size="25" name="email"></td></tr>
                        <tr><td class="xxx" valign="top">Информация о Вас</td>
                            <td width="70%">
                                <textarea rows="10" cols="60" name="info"></textarea>
                            </td></tr>
                        <tr>
                            <td></td>
                            <td>
                                <br><label for="no-phone">Моё объявление <b style="color:red">не содержит</b> номер телефона, адрес сайта и не носит коммерческий характер</label>
                                <input id="no-phone" name="noPhone" type="checkbox">
                            </td>
                        </tr>
                    </tbody> 
                </table>
                <br>
                <center>
                    <input type="submit" value="Отправить" >
                    <br>(нажимайте только 1 раз)
                </center>
            </form>


            <div id="add_dating">
                <input type="text" name="Имя" value="" size="10" />
            </div>
        
        
        
        </div>
        <div id="cap">

        </div>



        <script >
             $.get("dating1.jsp", function(data) {
    if(data !== null && data.length > 0 && data === 1) {
        // refresh this page
        document.location = document.location.href;
    }
 });
        </script>

    </body>
</html>
