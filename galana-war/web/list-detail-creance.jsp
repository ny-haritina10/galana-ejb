<%@ page import="java.util.List" %>
<%@ page import="creance.DetailCreance" %> 
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <title>Detail Creances</title>
    <link rel="stylesheet" href="./assets/css/table.css">
</head>
<body>
<h1>Liste des Détails de Créances</h1>

<%
    DetailCreance[] detailCreances = null;

    try {
        detailCreances = (DetailCreance[]) request.getAttribute("detail-creances");
    } catch (Exception e) {
        out.println("<p>Error retrieving data: " + e.getMessage() + "</p>");
    }

    if (detailCreances != null) {
%>
    <div class="table-container">
        <table border="2">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>ID Client</th>
                    <th>ID Prélèvement</th>
                    <th>Date Échéance</th>
                    <th>Montant</th>
                    <th>Date Prélèvement</th>
                    <th>Pompe</th>
                    <th>Pompiste</th>
                </tr>
            </thead>
            <tbody>
                <%
                    for (DetailCreance detail : detailCreances) {
                %>
                    <tr>
                        <td><%= detail.getId() %></td>
                        <td><%= detail.getPrelevement().getId() %></td>
                        <td><%= detail.getIdClient() %></td>
                        <td><%= detail.getDateEcheance() %></td>
                        <td>$<%= detail.getAmount() %></td>
                        <td><%= detail.getPrelevement().getDatePrelevement() %></td>
                        <td><%= detail.getPrelevement().getPompe().getName() %></td>
                        <td><%= detail.getPrelevement().getPompiste().getName() %></td>
                    </tr>
                <%
                    }
                %>
            </tbody>
        </table>
    </div>
        
    <%
        } else {
            out.println("<p>Aucune créance trouvée.</p>");
        }
    %>
    <br><br>
    <center>
        <a href="index.jsp">RETOUR</a>
    </center>

</body>
</html>