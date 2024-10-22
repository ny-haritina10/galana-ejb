using System;

namespace boutique.Models
{
    public class StockDetail
    {
        public int StockId { get; set; }
        public DateTime DateSession { get; set; }
        public double QuantityIn { get; set; }
        public double QuantityOut { get; set; }
        public int ProductId { get; set; }
        public string ProductName { get; set; }
        public double PurchasePrice { get; set; }
        public double SalePrice { get; set; }
        public string TypeProduct { get; set; }
        public double InitialQuantity { get; set; }
    }
}