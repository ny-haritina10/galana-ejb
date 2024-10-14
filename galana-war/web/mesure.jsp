<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="pompe.Pompe" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Document</title>
  <link rel="stylesheet" href="./assets/css/form.css">
</head>
<body>

  <% if (request.getAttribute("error") != null) { %>
    <div class="error">
      <p><%= request.getAttribute("error") %></p>
    </div>
  <% } %>
  
  <form action="MesureController" method="post">
    <h1>Mesurement</h1>
    
    <!-- Select for Pompes -->
    <label for="pompe">Pompe:</label>
    <select name="pompe" id="pompe">
        <%
            Pompe[] pompes = (Pompe[]) request.getAttribute("pompes");
            for (int i = 0; i < pompes.length; i++) {
                Pompe pompe = pompes[i];
        %>
            <option value="<%= pompe.getId() %>"><%= pompe.getName() %></option>
        <%
            }
        %>
    </select>
    <br><br>

    <label for="date">Date: </label>
    <input type="date" name="date_mesure">
    <br><br>

    <label for="mesure">Mesure: (10cm)</label>
    <input type="text" name="mesure">
    <br><br>

    <label for="submit">Submit</label>
    <input type="submit" value="Submit">
  </form>

  <br><br>
  <center>
      <a href="index.jsp">BACK</a>
  </center>
</body>
</html>