<%@ page import="java.util.List" %>
<%@ page import="mg.cnaps.compta.ComptaSousEcriture" %> 
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<!DOCTYPE html>
<html>
<head>
    <title>Compta Sous Écritures</title>
    <link rel="stylesheet" href="./assets/css/table.css">
</head>
<body>
<h1>Liste des Compta Sous Écritures</h1>

<%
    List<ComptaSousEcriture> comptaSousEcritures = null;

    try {
        comptaSousEcritures = (List<ComptaSousEcriture>) request.getAttribute("ecriture");
    } catch (Exception e) {
        out.println("<p>Error retrieving data: " + e.getMessage() + "</p>");
    }

    if (comptaSousEcritures != null && !comptaSousEcritures.isEmpty()) {
%>
    <div class="table-container">
        <table border="2">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Compte</th>
                    <th>Débit</th>
                    <th>Crédit</th>
                    <th>Remarque</th>
                    <th>ID Mère</th>
                    <th>Journal</th>
                    <th>Exercice</th>
                    <th>Daty</th>
                </tr>
            </thead>
            <tbody>
                <%
                    for (ComptaSousEcriture ecriture : comptaSousEcritures) {
                %>
                    <tr>
                        <td><%= ecriture.getId() %></td>
                        <td><%= ecriture.getCompte() %></td>
                        <td><%= ecriture.getDebit() %></td>
                        <td><%= ecriture.getCredit() %></td>
                        <td><%= ecriture.getRemarque() %></td>
                        <td><%= ecriture.getIdMere() %></td>
                        <td><%= ecriture.getJournal() %></td>
                        <td><%= ecriture.getExercice() %></td>
                        <td><%= ecriture.getDaty() %></td>
                    </tr>
                <%
                    }
                %>
            </tbody>
        </table>
    </div>
        
<%
    } else {
        out.println("<p>Aucune écriture trouvée.</p>");
    }
%>

</body>
</html>