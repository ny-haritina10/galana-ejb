<%@ page import="java.util.List" %>
<%@ page import="mg.cnaps.compta.ComptaSousEcriture" %> 
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<!DOCTYPE html>
<html>
<head>
    <title>Compta Sous Écritures</title>
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
                    <th>Libelle Piece</th>
                    <th>ID Mère</th>
                    <th>Référence Engagement</th>
                    <th>Compte Aux</th>
                    <th>Lettrage</th>
                    <th>Journal</th>
                    <th>Exercice</th>
                    <th>Folio</th>
                    <th>Daty</th>
                    <th>Analytique</th>
                    <th>Source</th>
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
                        <td><%= ecriture.getLibellePiece() %></td>
                        <td><%= ecriture.getIdMere() %></td>
                        <td><%= ecriture.getReference_engagement() %></td>
                        <td><%= ecriture.getCompte_aux() %></td>
                        <td><%= ecriture.getLettrage() %></td>
                        <td><%= ecriture.getJournal() %></td>
                        <td><%= ecriture.getExercice() %></td>
                        <td><%= ecriture.getFolio() %></td>
                        <td><%= ecriture.getDaty() %></td>
                        <td><%= ecriture.getAnalytique() %></td>
                        <td><%= ecriture.getSource() %></td>
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