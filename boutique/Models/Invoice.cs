using System;
using System.Collections.Generic;

namespace boutique.Models
{
    public class Invoice
    {
        public int IdInvoice { get; set; }
        public int OrderId { get; set; }
        public string ClientId { get; set; }
        public DateTime OrderDate { get; set; }
        public List<InvoiceItem> Items { get; set; } = new List<InvoiceItem>();
        public double TotalInvoiceAmount { get; set; }
    }

    public class InvoiceItem
    {
        public int IdInvoice { get; set; }
        public int OrderId { get; set; }
        public string ClientId { get; set; }
        public string OrderDate { get; set; }
        public int IdProduct { get; set; }
        public string ProductName { get; set; }
        public int ProductQuantity { get; set; }
        public double UnitPrice { get; set; }
        public double LineTotal { get; set; }
        public double TotalInvoiceAmount { get; set; }
    }
}