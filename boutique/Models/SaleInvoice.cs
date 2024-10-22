using System;

namespace boutique.Models
{
    public class SaleInvoice
    {
        public int Id { get; set; }
        public Order Order { get; set; }
        public double TotalAmount { get; set; }
        public string InvoiceType { get; set; }

    }

    public class Order
    {
        public int Id { get; set; }
        public string IdClient { get; set; }
        public DateTime DateOrder { get; set; }
    }
}