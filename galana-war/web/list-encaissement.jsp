<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="prelevement.Prelevement" %>
<%@ page import="encaissement.Encaissement" %>

<%
    Encaissement[] encaissements = (Encaissement[]) request.getAttribute("encaissements");  
%>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Document</title>
  <link rel="stylesheet" href="./assets/css/table.css">
</head>
<body>
  <h1>Encaissement List</h1>

  <table border="2">
    <thead>
      <tr>
        <th>Encaissement ID</th>
        <th>Payed Amount</th>
        <th>Unpayed Amount</th>
        <th>Amount to pay</th>
        <th>Date Encaissement</th>
        <th>Date Prelevement</th>
        <th>Action</th>
      </tr>
    </thead>

    <tbody>
      <% if (encaissements.length != 0 || encaissements != null) { %>
        <% for (Encaissement encaissement : encaissements) { %>
          
          <%
            double payedAmount = encaissement.getMontantEncaisse();
            double amountToPay = encaissement.getPrelevement().getPrelevementDifference();
            double unpayedAmount =  amountToPay - payedAmount;
          %>

          <tr>
            <td><%= encaissement.getId() %></td>
            <td><%= payedAmount %></td>
            <td><%= unpayedAmount %></td>
            <td><%= amountToPay %></td>
            <td><%= encaissement.getDateEncaissement() %></td>
            <td><%= encaissement.getPrelevement().getDatePrelevement() %></td>
            <td><a href="CreanceDetailController?id_encaissement=<%= encaissement.getId() %>">Add creance details</a></td>
          </tr>

        <% } %>

      <% } else { %>
          <tr>
            <td>No encaissements avalaible! </td>
          </tr>
      <% }%>
    </tbody>
  </table>
</body>
</html>