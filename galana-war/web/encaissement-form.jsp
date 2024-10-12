<%@ page import="prelevement.Prelevement" %>
<%@ page import="client.Client" %>


<%
    Prelevement[] prelevements = (Prelevement[]) request.getAttribute("prelevements");
    List<Client> clients = (List<Client>) request.getAttribute("clients");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Encaissement Form</title>
    <link rel="stylesheet" href="./assets/css/form.css">
</head>

<body>
    <form action="EncaissementController" method="post">
        <h1>Encaissement Form</h1>
        
        <label for="prelevement">Selectionnez le prelevement :</label>
        <select name="id_prelevement" id="prelevement" required>
            <option value="">Selectionnez un prelevement</option>
            <% 
                for (Prelevement prelevement : prelevements) {
            %>
                <% if (prelevement.getId() % 2 == 0) { %>
                    <option value="<%= prelevement.getId() %>">
                        <%= " Montant preleve: $" + prelevement.getPrelevementDifference() + " - Date: " + prelevement.getDatePrelevement() %>
                    </option>
                <% } %>
            <% } %>
        </select>
        <br><br>

        <select name="client">
            <% for(Client client : clients) { %>
              <option value="<%= client.getId() %>"><%= client.getNom() %></option>
            <% } %>
        </select>
        <br><br>

        <label for="montant_encaisse">Montant a encaisser :</label>
        <input type="number" name="montant_encaisse" id="montant_encaisse" required step="0.01">
        <br><br>

        <label for="date_encaissement">Date d'encaissement:</label>
        <input type="date" name="date_encaissement" id="date_encaissement" required>
        <br><br>

        <label for="date_encaissement">Date echeance:</label>
        <input type="date" name="date_echeance" id="date_echeance" required>
        <br><br>

        <button type="submit">Soumettre</button>
    </form>
    <br><br>
    <center>
        <a href="index.jsp">BACK</a>
    </center>
</body>
</html>