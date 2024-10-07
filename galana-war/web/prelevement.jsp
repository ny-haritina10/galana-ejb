<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="pompe.Pompe" %>
<%@ page import="pompiste.Pompiste" %>
<%@ page import="product.Product" %>

<html>
<head>
    <title>Prelevement Form</title>
    <link rel="stylesheet" href="./assets/css/form.css">
</head>
<body>
    <form action="PrelevementController" method="POST">
        <h2>Prelevement Form</h2>
        
        <!-- Select for Pompes -->
        <label for="pompe">Pompe:</label>
        <select name="pompe" id="pompe">
            <%
                Pompe[] pompes = (Pompe[]) request.getAttribute("pompes");
                for (int i = 0; i < pompes.length; i++) {
                    Pompe pompe = pompes[i];
            %>
                <option value="<%= pompe.getId() %>"><%= pompe.getName() %></option>
            <%
                }
            %>
        </select>
        <br><br>

        <!-- Select for Pompistes -->
        <label for="pompiste">Pompiste:</label>
        <select name="pompiste" id="pompiste">
            <%
                Pompiste[] pompistes = (Pompiste[]) request.getAttribute("pompistes");
                for (int i = 0; i < pompistes.length; i++) {
                    Pompiste pompiste = pompistes[i];
            %>
                <option value="<%= pompiste.getId() %>"><%= pompiste.getName() %></option>
            <%
                }
            %>
        </select>
        <br><br>

        <!-- Select for product -->
        <label for="product">Product: </label>
        <select name="product" id="product">
            <%
                Product[] products = (Product[]) request.getAttribute("products");
                for (int i = 0; i < products.length; i++) {
                    Product product = products[i];
            %>
                <option value="<%= product.getId() %>"><%= product.getName() %></option>
            <%
                }
            %>
        </select>
        <br><br>

        <!-- Input for date -->
        <label for="date_prelevement">Date Prelevement:</label>
        <input type="date" name="date_prelevement" id="date_prelevement" required>
        <br><br>

        <!-- Input for amount -->
        <label for="amount">Quantity taken: (L)</label>
        <input type="number" name="amount" id="amount" step="0.01" required>
        <br><br>

        <!-- Submit button -->
        <button type="submit">Submit</button>
    </form>

    <br><br>
    <center>
        <a href="index.jsp">BACK</a>
    </center>
</body>
</html>