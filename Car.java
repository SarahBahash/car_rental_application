public class Car {

    private String name;
    private String category;
    private int maxPassengers;
    private int comfortLevel;
    private double pricePerDay;
    private double mpg;
    private String imagePath;

    public Car(String name, String category, int maxPassengers,
               int comfortLevel, double pricePerDay, double mpg, String imagePath){

        this.name = name;
        this.category = category;
        this.maxPassengers = maxPassengers;
        this.comfortLevel = comfortLevel;
        this.pricePerDay = pricePerDay;
        this.mpg = mpg;
        this.imagePath = imagePath;
    }

    public double rentalCost(int days){
        return days * pricePerDay;
    }

    public double gasCost(double mileage,double gasPrice){
        return (mileage / mpg) * gasPrice;
    }

    public double totalCost(int days,double mileage,double gasPrice){
        return rentalCost(days) + gasCost(mileage,gasPrice);
    }

    public String getName(){ return name; }
    public String getCategory(){ return category; }
    public int getMaxPassengers(){ return maxPassengers; }
    public int getComfortLevel(){ return comfortLevel; }
    public String getImagePath(){ return imagePath; }
}