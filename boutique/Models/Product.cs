namespace boutique.Models
{
    
    public class Product
    {
        public int Id { get; set; }
        public Unit Unit { get; set; }
        public string Name { get; set; }
        public double PuAchat { get; set; }
        public double PuVente { get; set; }
        public string TypeProduct { get; set; }
        public double QteInitial { get; set; }
    }

    public class Unit
    {
        public int Id { get; set; }
        public string Name { get; set; }
    }
}