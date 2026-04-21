public class RoomTypeValidation {


    public static String validateTypeName(String name){
        Validator.checkStringNotEmpty(name, "Room type");
        return name.trim().toLowerCase();
    }


    public static int validateBeds(int beds){
        Validator.checkNumPositive(beds, "Number of beds");
        return beds;
    }

    public static int validateCapacity(int capacity){
        Validator.checkNumPositive(capacity, "Capacity");
        return capacity;
    }


    public static String validateRoomDescription(String description){
        Validator.checkStringNotEmpty(description, "Room Description");
        return description.trim();
    }


    public static double validatePricePerNight(double price){
        Validator.checkNumPositive(price, "Price per night");
        return price;
    }

}
