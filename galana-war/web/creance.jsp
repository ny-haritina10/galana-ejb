<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.util.Map" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Accounts Receivable Situation</title>
    <style>
        body {
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
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        th, td {
            border: 1px solid #ddd;
            padding: 8px;
            text-align: left;
        }
        th {
            background-color: #2196F3;
            color: white;
        }
        tr:nth-child(even) {
            background-color: #f2f2f2;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Accounts Receivable Situation</h1>

        <% if (request.getAttribute("exercice") != null) { %>
            <p>Exercise Year: <%= request.getAttribute("exercice") %></p>
        <% } else { %>
            <p>Showing all-time accounts receivable</p>
        <% } %>
        
        <%
        String error = (String) request.getAttribute("error");
        if (error != null && !error.isEmpty()) {
        %>
            <div class="error">
                <p><%= error %></p>
            </div>
        <%
        } else {
            Map<String, Double> creance = (Map<String, Double>) request.getAttribute("creance");
            Double totalCreance = (Double) request.getAttribute("total-creance");
            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);

            // Ensure totalCreance is not null
            if (totalCreance != null) {
        %>
            <div class="result">
                <h2>Total Accounts Receivable</h2>
                <p>Total: <%= currencyFormatter.format(totalCreance) %></p>
            </div>
        <%
            } else {
        %>
            <div class="error">
                <p>Total Creance is not available.</p>
            </div>
        <%
            }

            // Check if creance map is not null or empty
            if (creance != null && !creance.isEmpty()) {
        %>
            <h3>Accounts Receivable Details</h3>
            <table>
                <tr>
                    <th>Client Code</th>
                    <th>Balance</th>
                </tr>
                <% for (Map.Entry<String, Double> entry : creance.entrySet()) { 
                    Double balance = entry.getValue();
                    // Ensure the balance is not null before formatting
                    if (balance != null) {
                %>
                    <tr>
                        <td><%= entry.getKey() %></td>
                        <td><%= currencyFormatter.format(balance) %></td>
                    </tr>
                <% } else { %>
                    <tr>
                        <td><%= entry.getKey() %></td>
                        <td>N/A</td>
                    </tr>
                <% } } %>
            </table>
        <%
            } else {
        %>
            <div class="error">
                <p>No Accounts Receivable data available.</p>
            </div>
        <%
            }
        }
        %>
        
        <a href="creance-form.jsp" class="back-link">Back to Accounts Receivable Form</a>
        <br>
        <a href="index.jsp">BACK</a>
    </div>
</body>
</html>