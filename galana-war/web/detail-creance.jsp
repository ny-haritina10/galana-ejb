<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="prelevement.Prelevement" %>
<%@ page import="encaissement.Encaissement" %>
<%@ page import="creance.DetailCreance" %>
<%@ page import="client.Client" %>
<%@ page import="java.util.*" %>

<%
  Encaissement encaissement = (Encaissement) request.getAttribute("encaissement");  
  List<Client> clients = (List<Client>) request.getAttribute("clients");
  List<DetailCreance> concernedCreances = (List<DetailCreance>) request.getAttribute("concerned-creances");

  double payedAmount = encaissement.getMontantEncaisse();
  double amountToPay = encaissement.getPrelevement().getPrelevementDifference();
  double amountConcerned = DetailCreance.getSumAmountConcernedDetailCreances(concernedCreances);
  double unpayedAmount =  (amountToPay - payedAmount);
%>


<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Document</title>
  <style>
    body 
    {
        font-family: Arial, sans-serif;
        line-height: 1.6;
        margin: 0;
        padding: 20px;
        background-color: #f4f4f4;
    }
    .container {
        max-width: 800px;
        margin: auto;
        background: white;
        padding: 20px;
        border-radius: 5px;
        box-shadow: 0 0 10px rgba(0,0,0,0.1);
    }
    h1 {
        color: #333;
    }
    .result {
        background-color: #e7f3fe;
        border-left: 6px solid #2196F3;
        margin-bottom: 15px;
        padding: 10px;
    }
    .error {
        background-color: #ffebee;
        border-left: 6px solid #f44336;
        margin-bottom: 15px;
        padding: 10px;
    }
    .back-link {
        display: inline-block;
        margin-top: 20px;
        color: #2196F3;
        text-decoration: none;
    }
    .back-link:hover {
        text-decoration: underline;
    }
    h1, h2, h3, h4, h5, h6 {
        font-weight: 600; /* Semi-bold for headers */
    }

    input[type="submit"],
    button {
        font-weight: 500; /* Medium weight for buttons */
    }

    .container {
        max-width: 600px;
        margin: auto;
    }

    form {
        background-color: #fff;
        padding: 25px;
        border-radius: 8px;
        box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        margin: 30px auto;
        width: 100%;
        max-width: 400px; /* Fixed width for the form */
        box-sizing: border-box;
    }

    form h1,
    form h2,
    form h3,
    form h4,
    form h5,
    form h6 {
        text-align: center;
        margin-bottom: 20px;
    }

    /* Rest of the CSS remains the same */

    /* Common styles for most input types */
    input[type="text"],
    input[type="number"],
    input[type="date"],
    input[type="password"],
    input[type="email"],
    input[type="tel"],
    input[type="url"],
    textarea,
    select {
        width: 100%;
        padding: 12px;
        margin-bottom: 20px;
        border: 1px solid #ddd;
        border-radius: 6px;
        font-size: 16px;
        transition: border-color 0.3s ease;
        box-sizing: border-box;
    }

    /* Focus styles for inputs */
    input[type="text"]:focus,
    input[type="number"]:focus,
    input[type="date"]:focus,
    input[type="password"]:focus,
    input[type="email"]:focus,
    input[type="tel"]:focus,
    input[type="url"]:focus,
    textarea:focus,
    select:focus {
        outline: none;
        border-color: #4a90e2;
        box-shadow: 0 0 0 2px rgba(74, 144, 226, 0.2);
    }

    /* Style for select dropdown */
    select {
        appearance: none;
        -webkit-appearance: none;
        -moz-appearance: none;
        background-image: url("data:image/svg+xml;charset=US-ASCII,%3Csvg%20xmlns%3D%22http%3A%2F%2Fwww.w3.org%2F2000%2Fsvg%22%20width%3D%22292.4%22%20height%3D%22292.4%22%3E%3Cpath%20fill%3D%22%23007CB2%22%20d%3D%22M287%2069.4a17.6%2017.6%200%200%200-13-5.4H18.4c-5%200-9.3%201.8-12.9%205.4A17.6%2017.6%200%200%200%200%2082.2c0%205%201.8%209.3%205.4%2012.9l128%20127.9c3.6%203.6%207.8%205.4%2012.8%205.4s9.2-1.8%2012.8-5.4L287%2095c3.5-3.5%205.4-7.8%205.4-12.8%200-5-1.9-9.2-5.5-12.8z%22%2F%3E%3C%2Fsvg%3E");
        background-repeat: no-repeat;
        background-position: right 12px top 50%;
        background-size: 12px auto;
        padding-right: 30px;
    }

    input[type="submit"],
    button {
        width: 100%;
        padding: 12px;
        background-color: #4a90e2;
        color: #fff;
        border: none;
        border-radius: 6px;
        cursor: pointer;
        font-size: 16px;
        font-weight: bold;
        transition: background-color 0.3s ease;
    }

    input[type="submit"]:hover,
    button:hover {
        background-color: #3a7bc8;
    }

    label {
        display: block;
        margin-bottom: 8px;
        font-weight: bold;
        color: #333;
    }
  </style>
  <link rel="stylesheet" href="./assets/css/table.css">
</head>
<body>

  <div class="container">
    <h1>Detail creance</h1>

    <div class="result">
      <h1>Detail of the concerned creance</h1>
      <table border="2">
        <thead>
            <tr>
                <th>ID</th>
                <th>ID Client</th>
                <th>Date Échéance</th>
                <th>Montant</th>
                <th>Date Prélèvement</th>
            </tr>
        </thead>
        <tbody>
            <%
                for (DetailCreance detail : concernedCreances) {
            %>
                <tr>
                    <td><%= detail.getId() %></td>
                    <td><%= detail.getIdClient() %></td>
                    <td><%= detail.getDateEcheance() %></td>
                    <td><%= detail.getAmount() %></td>
                    <td><%= detail.getPrelevement().getDatePrelevement() %></td>
                </tr>
            <%
                }
            %>
        </tbody>
        </table>  
        
        <p>Amount to pay : <b><%= amountToPay %></b></p>
        <p>Amount payed : <b><%= payedAmount %></b></p>
        <p>Unpayed amount : <b><%= unpayedAmount %></b></p>
        <p>Concerned detail creances amount: <b><%= amountConcerned %></b></p>
    </div>
    
    <% if (unpayedAmount == 0 || (amountConcerned == unpayedAmount && unpayedAmount != 0) ) { %>
        <div class="result">
            <h4>All of the amount has been payed or has detailled on this prelevement, Thank you !</h4>
        </div>

    <% } else { %>

        <% if (request.getAttribute("error") != null) { %>
            <div class="error">
              <p><%= request.getAttribute("error") %></p>
            </div>
        <% } %> 
        
        <form action="CreanceDetailController" method="post">
            <label for="date-echeance">Date echeance payement</label>
            <input type="date" name="date-echeance" required>
            <br><br>

            <select name="client" required>
            <% for(Client client : clients) { %>
                <option value="<%= client.getId() %>"><%= client.getNom() %></option>
            <% } %>
            </select>
            <br><br>

            <label for="amount">Amount</label>
            <input type="number" name="amount" id="amount" required step="0.01" min="0">
            <br>
            <br><br>

            <input type="submit" value="Submit">
        </form>
    <% } %>
  </div>

  <br><br>
  <center>
    <a href="index.jsp">BACK</a>
  </center>
</body>
</html>