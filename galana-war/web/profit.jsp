<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.util.Map" %>


<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Profit Calculation Results</title>
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
        <h1>Profit Calculation Results</h1>
        
        <%
        String error = (String) request.getAttribute("error");
        if (error != null && !error.isEmpty()) {
        %>
            <div class="error">
                <p><%= error %></p>
            </div>
        <%
        } else {
            Double profit = (Double) request.getAttribute("profit");
            String period = (String) request.getAttribute("period");
            Map<String, Double> revenueDetails = (Map<String, Double>) request.getAttribute("revenueDetails");
            Map<String, Double> expenseDetails = (Map<String, Double>) request.getAttribute("expenseDetails");
            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);
        %>
            <div class="result">
                <h2>Calculated Profit</h2>
                <p>Period: <%= period %></p>
                <p>Profit: <%= currencyFormatter.format(profit) %></p>
            </div>

            <!-- <h3>Revenue Details (Compte 7)</h3>
            <table>
                <tr>
                    <th>Account</th>
                    <th>Amount</th>
                </tr>
                <% for (Map.Entry<String, Double> entry : revenueDetails.entrySet()) { %>
                    <tr>
                        <td><%= entry.getKey() %></td>
                        <td><%= currencyFormatter.format(entry.getValue()) %></td>
                    </tr>
                <% } %>
            </table>

            <h3>Expense Details (Compte 6)</h3>
            <table>
                <tr>
                    <th>Account</th>
                    <th>Amount</th>
                </tr>
                <% for (Map.Entry<String, Double> entry : expenseDetails.entrySet()) { %>
                    <tr>
                        <td><%= entry.getKey() %></td>
                        <td><%= currencyFormatter.format(entry.getValue()) %></td>
                    </tr>
                <% } %>
            </table> -->
        <%
        }
        %>
        
        <a href="profit-form.jsp" class="back-link">Back to Calculation Form</a>
    </div>
</body>
</html>