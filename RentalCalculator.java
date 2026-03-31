import java.util.ArrayList;
import java.util.List;

public class RentalCalculator {

    private static final double GAS_PRICE = 2.25;
    private static final double EPSILON = 0.01;

    public static List<Car> findBestCar(List<Car> cars, int passengers, int days, double mileage) {

        List<Car> validCars = new ArrayList<>();

        // Keep only cars that can fit the required number of passengers
        for (Car car : cars) {
            if (car.getMaxPassengers() >= passengers) {
                validCars.add(car);
            }
        }

        if (validCars.isEmpty()) {
            return validCars;
        }

        // Find the minimum total cost among valid cars
        double minCost = Double.MAX_VALUE;
        for (Car car : validCars) {
            double totalCost = car.totalCost(days, mileage, GAS_PRICE);
            if (totalCost < minCost) {
                minCost = totalCost;
            }
        }

        // Keep only cars with the minimum total cost
        List<Car> cheapestCars = new ArrayList<>();
        for (Car car : validCars) {
            double totalCost = car.totalCost(days, mileage, GAS_PRICE);
            if (Math.abs(totalCost - minCost) < EPSILON) {
                cheapestCars.add(car);
            }
        }

        // Among the cheapest cars, find the highest comfort level
        int bestComfort = Integer.MIN_VALUE;
        for (Car car : cheapestCars) {
            if (car.getComfortLevel() > bestComfort) {
                bestComfort = car.getComfortLevel();
            }
        }

        // Return all cars that have both minimum cost and best comfort
        List<Car> result = new ArrayList<>();
        for (Car car : cheapestCars) {
            if (car.getComfortLevel() == bestComfort) {
                result.add(car);
            }
        }

        return result;
    }
}