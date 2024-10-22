using System;
using System.Collections.Generic;

namespace boutique.Models
{
    public class SaleForm
    {
        public DateTime SaleDate { get; set; }
        public string SelectedClientId { get; set; }
        public int SelectedProductId { get; set; }
        public int Quantity { get; set; }
        public required List<Client> Clients { get; set; }
        public required List<Product> Products { get; set; }
        public List<CartItem> Cart { get; set; } = new List<CartItem>();
    }

    public class Client
    {
        public required string Id { get; set; }
        public required string Nom { get; set; }  
        public string? Telephone { get; set; }
        public string? Adresse { get; set; }
        public string? Mail { get; set; }
        public string? Remarque { get; set; }
        public string? Compte { get; set; }
    }

    public class CartItem
    {
        public int ProductId { get; set; }
        public required string ProductName { get; set; }
        public int Quantity { get; set; }
        public double UnitPrice { get; set; }
        public double TotalPrice => Quantity * UnitPrice;
    }
}