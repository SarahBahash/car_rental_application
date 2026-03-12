public class SecurityValidator {

    public static int validatePassengers(int value){

        if(value <=0 || value >7)
            throw new IllegalArgumentException("Passengers must be 1-7");

        return value;
    }

    public static int validateDays(int value){

        if(value <=0 || value >365)
            throw new IllegalArgumentException("Days must be 1-365");

        return value;
    }

    public static double validateMileage(double value){

        if(value <0 || value >10000)
            throw new IllegalArgumentException("Mileage must be 0-10000");

        return value;
    }

}