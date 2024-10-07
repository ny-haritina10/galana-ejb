<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="anomalie.Anomalie" %>
<%@ page import="pompe.Pompe" %>

<html>
<head>
    <title>Anomalies List</title>
    <link rel="stylesheet" href="./assets/css/table.css">
    <style>
        .anomaly-row {
            background-color: #FFA500; 
        }

        .anomaly-row:hover {
            background-color: #FF8C00; 
        }

        tr:nth-child(even) {
            background-color: #f2f2f2;
        }

        .anomaly-row:nth-child(even) {
            background-color: #FFB732;
        }

        .anomaly-row:nth-child(even):hover {
            background-color: #FFA500; 
        }
    </style>
</head>

<body>

<h2>List of Anomalies</h2>

<%
    Anomalie[] anomalies = (Anomalie[]) request.getAttribute("anomalies");
    if (anomalies != null && anomalies.length > 0) {
%>

    <table>
        <thead>
            <tr>
                <th>ID Pompe</th>
                <th>Date Vente</th>
                <th>Prelevement (L)</th>
                <th>Prelevement (cm)</th>
                <th>Mesurement (cm)</th>
                <th>Anomalie (L)</th>
            </tr>
        </thead>
        <tbody>
        <%
            for (Anomalie anomalie : anomalies) {
                boolean hasAnomaly = anomalie.getAnomalieInL() != 0;
        %>
            <tr class="<%= hasAnomaly ? "anomaly-row" : "" %>">
                <td><%= anomalie.getPompe().getName() %></td>
                <td><%= anomalie.getDateVente() %></td>
                <td><%= anomalie.getPrelevInL() %></td>
                <td><%= anomalie.getPrelevementInCm() %></td>
                <td><%= anomalie.getMesureInL() %></td>
                <td><%= anomalie.getAnomalieInL() %></td>
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

<% } else { %>
    <p>No anomalies found.</p>
<% } %>

</body>
</html>