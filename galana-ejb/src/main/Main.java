// package main;

// import java.sql.Date;

// import invoice.SaleInvoice;
// import order.Order;
// import order.OrderItem;
// import product.Product;
// import stock.Stock;

// public class Main {

//     public static void main(String[] args) {
//         try {
//             Date dateSession = Date.valueOf("2024-10-19");
//             Order order = new Order(1, "CL0001", dateSession);
//             order.insert(null);

//             Product coca = new Product().getById(21, Product.class, null);
//             Product doritos = new Product().getById(22, Product.class, null);
//             Product eauVive = new Product().getById(24, Product.class, null);

//             OrderItem item1 = new OrderItem(1, order, coca, 2); 
//             OrderItem item2 = new OrderItem(1, order, doritos, 1); 
//             OrderItem item3 = new OrderItem(1, order, eauVive, 5); 

//             item1.insert(null);
//             item2.insert(null);
//             item3.insert(null);

//             SaleInvoice invoice = new SaleInvoice(1, order, order.getSumAmount(null));
//             invoice.insert(null);

//             Stock stock1 = new Stock(1, item1.getProduct(), dateSession, 0, item1.getQuantity());
//             Stock stock2 = new Stock(1, item2.getProduct(), dateSession, 0, item2.getQuantity());
//             Stock stock3 = new Stock(1, item3.getProduct(), dateSession, 0, item3.getQuantity());
            
//             stock1.insert(null);
//             stock2.insert(null);
//             stock3.insert(null);

//             System.out.println("Mety");
//         } 
        
//         catch (Exception e) {
//             e.printStackTrace();
//         }
//     }   
// }