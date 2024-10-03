<%@ page import="prelevement.Prelevement" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Encaissement Form</title>
</head>

<body>
    <h1>Encaissement Form</h1>
    <form action="EncaissementController" method="post">
        <label for="prelevement">Selectionnez le prelevement :</label>
        <select name="id_prelevement" id="prelevement" required>
            <option value="">Selectionnez un prelevement</option>
            <% 
                Prelevement[] prelevements = (Prelevement[]) request.getAttribute("prelevements");
                for (Prelevement prelevement : prelevements) {
            %>
                <option value="<%= prelevement.getId() %>">
                    <%= "ID: " + prelevement.getId() + " - Montant: $" + prelevement.getAmount() + " - Date: " + prelevement.getDatePrelevement() %>
                </option>
            <% } %>
        </select>
        <br><br>

        <label for="montant_encaisse">Montant a encaisser :</label>
        <input type="number" name="montant_encaisse" id="montant_encaisse" required step="0.01">
        <br><br>

        <label for="date_encaissement">Date d'encaissement :</label>
        <input type="date" name="date_encaissement" id="date_encaissement" required>
        <br><br>

        <button type="submit">Soumettre</button>
    </form>
</body>
</html>