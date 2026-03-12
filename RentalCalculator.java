import java.util.*;

public class RentalCalculator {

    private static final double GAS_PRICE = 2.25;

    public static List<Car> findBestCar(List<Car> cars,
                                        int passengers,
                                        int days,
                                        double mileage){

        List<Car> validCars = new ArrayList<>();

        for(Car car : cars){

            if(passengers <= car.getMaxPassengers())
                validCars.add(car);

        }

        if(validCars.isEmpty())
            return validCars;

        validCars.sort((c1,c2)->{

            double cost1 = c1.totalCost(days,mileage,GAS_PRICE);
            double cost2 = c2.totalCost(days,mileage,GAS_PRICE);

            if(cost1 == cost2)
                return c2.getComfortLevel() - c1.getComfortLevel();

            return Double.compare(cost1,cost2);
        });

        List<Car> result = new ArrayList<>();
        result.add(validCars.get(0));

        if(validCars.size()>1){

            double c1 = validCars.get(0).totalCost(days,mileage,GAS_PRICE);
            double c2 = validCars.get(1).totalCost(days,mileage,GAS_PRICE);

            if(c1==c2 && validCars.get(0).getComfortLevel()==validCars.get(1).getComfortLevel())
                result.add(validCars.get(1));
        }

        return result;
    }
}