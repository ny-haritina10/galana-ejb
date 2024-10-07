<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="vente.Vente" %>

<html>
<head>
    <title>Ventes List</title>
    <link rel="stylesheet" href="./assets/css/table.css">
</head>
<body>

<h2>List of Ventes</h2>

<table border="2">
    <thead>
        <tr>
            <th>Product</th>
            <th>Pompe</th>
            <th>Date Vente</th>
            <th>Somme Ventes</th>
        </tr>
    </thead>
    <tbody>
        <%
            Vente[] ventes = (Vente[]) request.getAttribute("ventes");

            if (ventes != null && ventes.length > 0) {
                for (Vente vente : ventes) {
        %>
                    <tr>
                        <td><%= vente.getProduct().getName() %></td>
                        <td><%= vente.getPompe().getName() %></td>
                        <td><%= vente.getDateVente() %></td>
                        <td>$<%= vente.getSommeVentes() %></td>
                    </tr>
        <%
                }
            } else {
        %>
                <tr>
                    <td colspan="4">No ventes available.</td>
                </tr>
        <%
            }
        %>
    </tbody>
</table>

<br><br>
<center>
    <a href="index.jsp">BACK</a>
</center>

</body>
</html>