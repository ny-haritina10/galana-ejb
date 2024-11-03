// package controller;

// import java.io.IOException;
// import java.io.PrintWriter;
// import java.sql.Connection;
// import java.sql.Date;
// import java.sql.DriverManager;
// import java.util.List;
// import java.util.Map;

// import javax.servlet.ServletException;
// import javax.servlet.http.HttpServlet;
// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletResponse;

// import ejb.EcritureServiceRemote;
// import prevision.Prevision;
// import product.Product;
// import stock.Stock;
// import inventaire.Inventaire;
// import inventaire.InventaireFille;
// import invoice.SaleInvoice;
// import magasin.Magasin;
// import mg.cnaps.compta.ComptaSousEcriture;
// import order.Order;
// import order.OrderItem;
// import utilitaire.UtilDB;
// import vente.Vente;
// import vente.VenteDetails;
// import venteLubrifiant.VenteLubrifiant;

// import javax.ejb.EJB;

// public class MainController extends HttpServlet {

//     @EJB
//     private EcritureServiceRemote remoteBean;

//     @Override
//     protected void doGet(HttpServletRequest req, HttpServletResponse res) 
//         throws ServletException, IOException 
//     {
//         Connection connection = null;
//         try {
//             // Get database connection
//             connection = new UtilDB().GetConn();
//             connection.setAutoCommit(false);
            
//             // Create a new Vente
//             Vente vente = new Vente();

//             vente.setDesignation("^^^^^^^^^^^^ Example Sale Invoice");
//             vente.setDaty(new Date(System.currentTimeMillis())); // Current date
//             vente.setIdClient("CLI000043"); // Example client ID
//             vente.setIdMagasin("PHARM001"); // Example store ID
//             vente.setRemarque("^^^^^^^^^^^^^^^^ Example sale with multiple products");
            
//             // Create VenteDetails array
//             VenteDetails[] details = new VenteDetails[2];
            
//             // First product
//             VenteDetails detail1 = new VenteDetails();
//             detail1.setIdProduit("PROD00003949"); // Example product ID
//             detail1.setIdDevise("AR"); // Currency code
//             detail1.setQte(2); // Quantity
//             detail1.setPu(50000.0); // Unit price
//             detail1.setTva(20.0); // VAT rate
//             detail1.setTauxDeChange(1.0); // Exchange rate
//             detail1.setDesignation("^^^^^^ VENTE LUBRIFIANT ");
            
//             // Second product
//             VenteDetails detail2 = new VenteDetails();
//             detail2.setMode("modif");
//             detail2.setIdProduit("PRD004463");
//             detail2.setIdDevise("AR");
//             detail2.setQte(1);
//             detail2.setPu(75000.0);
//             detail2.setTva(20.0);
//             detail2.setTauxDeChange(1.0);
//             detail1.setDesignation("^^^^^^ VENTE LUBRIFIANT ");
            
//             // Set details array
//             details[0] = detail1;
//             details[1] = detail2;
//             vente.setVenteDetails(details);
            
//             // Create the sale record
//             String userId = "880679";
//             vente = (Vente) vente.createObject(userId, connection);
            
//             // Create the sale details
//             for (VenteDetails detail : details) {
//                 detail.setIdVente(vente.getId());
//                 detail.createObject(userId, connection);
//             }
            
//             // Validate the sale
//             vente.validerObject(userId, connection);
            
//             // Commit the transaction
//             connection.commit();
            
//             System.out.println("Sale created successfully with ID: " + vente.getId());
            
//         } 
        
//         catch (Exception e) {
//             try {
//                 if (connection != null) {
//                     connection.rollback();
//                 }
//             } catch (Exception rollbackError) {
//                 rollbackError.printStackTrace();
//             }
//             System.err.println("Error creating sale: " + e.getMessage());
//             e.printStackTrace();
//         } 
        
//         finally {
//             try {
//                 if (connection != null) {
//                     connection.close();
//                 }
//             } catch (Exception closeError) {
//                 closeError.printStackTrace();
//             }
//         }
//     }

//     @Override
//     protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
//         throws ServletException, IOException 
//     {
//         try {
            
//         } 
        
//         catch (Exception e) {
//             e.printStackTrace();
//         }
//     }
// }