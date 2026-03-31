public class Car {

    private String name;
    private String category;
    private int maxPassengers;
    private int comfortLevel;
    private double pricePerDay;
    private double mpg;
    private String imagePath;
    private String type; // Hierarchical type (Economy, Standard, etc.)

    // Constructor
    public Car(String name, String category, int maxPassengers,
               int comfortLevel, double pricePerDay, double mpg,
               String imagePath, String type) {

        this.name = name;
        this.category = category;
        this.maxPassengers = maxPassengers;
        this.comfortLevel = comfortLevel;
        this.pricePerDay = pricePerDay;
        this.mpg = mpg;
        this.imagePath = imagePath;
        this.type = type;
    }

    // Calculates rental cost based on number of days
    public double rentalCost(int days) {
        return days * pricePerDay;
    }

    // Calculates fuel cost based on mileage and gas price
    public double gasCost(double mileage, double gasPrice) {
        return (mileage / mpg) * gasPrice;
    }

    // Calculates total trip cost (rental + fuel)
    public double totalCost(int days, double mileage, double gasPrice) {
        return rentalCost(days) + gasCost(mileage, gasPrice);
    }

    // Getters
    public String getName() { return name; }
    public String getCategory() { return category; }
    public int getMaxPassengers() { return maxPassengers; }
    public int getComfortLevel() { return comfortLevel; }
    public String getImagePath() { return imagePath; }
    public double getPriceDay() { return pricePerDay; }
    public double getMpg() { return mpg; }
    public String getType() { return type; }
}