public class DatabaseValidation {



    //---------------------------- Room -------------------------------------------------------------------------
    public static void validateRoomExists(Rooms room){
        Validator.checkNotNull(room, "Room does not exist");
    }

    public static void validateRoomTypeExists(String typeName) { //b4 assigning to room
        if (Database.findRoomType(typeName) == null){
            throw new IllegalArgumentException("Room type not found.");
        }
    }

    //------------------------- Amenity -------------------------------------------------------------------------
    public static void validateAmenityExists(Amenity amenity){
        Validator.checkNotNull(amenity, "Amenity not found");
    }

    public static void validateUniqueAmenity(String amenityName) { //b4 adding to list of all amenities
        if (Database.findAmenity(amenityName) != null){
            throw new IllegalArgumentException("Amenity already exists in system.");
        }
    }

    public static void validateAmenityData(String amenityName, double cost){
        Validator.checkStringNotEmpty(amenityName, "Amenity name");
        Validator.checkNumPositive(cost, "Amenity cost");
    }

//----------------------------- Roomtype ----------------------------------------------------------------------
    public static void validateRoomTypeExists(RoomType roomtype){
        Validator.checkNotNull(roomtype, "Room type not found.");
    }

    public static void validateUniqueRoomType(String name) { //b4 adding to list of all amenities
        if (Database.findRoomType(name) != null){
            throw new IllegalArgumentException("Room type already exists in system.");
        }
    }

    public static void validateRoomTypeData(String name, int beds, int capacity, double price){
        Validator.checkStringNotEmpty(name,"Room type");
        Validator.checkNumPositive(beds, "Beds");
        Validator.checkNumPositive(capacity,"Capacity");
        Validator.checkNumPositive(price, "Price");
    }


}
