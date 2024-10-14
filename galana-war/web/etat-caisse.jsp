<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Etat de Caisse</title>
  <link rel="stylesheet" href="./assets/css/form.css">
  <link rel="stylesheet" href="./assets/css/table.css">
</head>
<body>

  <% if (request.getAttribute("exercice") != null) { %>
    <div class="container">
      <div class="result">
        <table>
          <thead>
            <tr>
              <th>Exercice</th>
              <th>Date Min</th>
              <th>Date Max</th>
              <th>Amount</th>
            </tr>
          </thead>
  
          <tbody>
            <tr>
              <td><%= request.getAttribute("exercice") %></td>
              <td><%= request.getAttribute("dateMin") %></td>
              <td><%= request.getAttribute("dateMax") %></td>
              <td>$<%= request.getAttribute("amount") %></td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  <% } %>
  
  <% if (request.getAttribute("error") != null) { %>
    <div class="error">
      <p><%= request.getAttribute("error") %></p>
    </div>
  <% } %>
  
  <form action="EtatCaisseController" method="post">
    <h2>Etat de caisse</h2>

    <label for="dateMin">Date Min</label>
    <input type="date" name="date-min">
    <br><br>

    <label for="dateMax">Date Max</label>
    <input type="date" name="date-max">
    <br><br>

    <input type="submit" value="Submit">
  </form>

</body>
</html>