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


    // -------------------------------Amenity validation---------------------------------------------------------------

    public static String validateAmenityName(String name){
        Validator.checkStringNotEmpty(name, "Amenity name");
        return name.trim().toLowerCase();
    }

    public static String validateUniqueAmenity(String name){
        String n = validateAmenityName(name);
        if (Database.findAmenity(n) != null){ //found amenity in database
            throw new IllegalArgumentException("Amenity already exists.");
        }
        return n;
    }

    public static double valdiateAmenityCost (double cost){
        Validator.checkNumPositive(cost, "Amenity cost");
        return cost;
    }

}
