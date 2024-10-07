package mesurement;

public class Mesure {
    
    private double value;
    private String unit;

    public Mesure(double value, String unit) {
        this.setUnit(unit);
        this.setValue(value);
    }

    public static Mesure parse(String str) {
        String regex = "(\\d+(?:\\.\\d+)?)(\\D+)";
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
        java.util.regex.Matcher matcher = pattern.matcher(str);

        if (matcher.matches()) {
            double value = Double.parseDouble(matcher.group(1)); // numeric part
            String unit = matcher.group(2).trim(); // unit part
            
            return new Mesure(value, unit);
        } 
        
        else 
        { throw new IllegalArgumentException("Invalid measurement format"); }
    }

    public double getValueInCM() {
        switch (unit.toLowerCase()) {
            case "cm":
                return value; 
            case "m":
                return value * 100; // Convert meters to centimeters
            case "mm":
                return value / 10; // Convert millimeters to centimeters
            case "km":
                return value * 100000; // Convert kilometers to centimeters
            case "in":
                return value * 2.54; // Convert inches to centimeters
            case "ft":
                return value * 30.48; // Convert feet to centimeters
            case "yd":
                return value * 91.44; // Convert yards to centimeters
            default:
                throw new IllegalArgumentException("Unsupported unit: " + unit);
        }
    }

    public double getValue() 
    { return value; }

    public void setValue(double value) 
    { this.value = value; }

    public String getUnit() 
    { return unit; }

    public void setUnit(String unit) 
    { this.unit = unit; }
}