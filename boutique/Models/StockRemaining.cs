using System;

namespace boutique.Models
{
    public class StockRemaining
    {
        public int ProductId { get; set; }
        public string ProductName { get; set; }
        public double InitialQuantity { get; set; }
        public double TotalQuantityIn { get; set; }
        public double TotalQuantityOut { get; set; }
        public double RemainingQuantity { get; set; }
    }
}
