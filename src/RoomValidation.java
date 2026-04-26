import java.util.HashSet;
import java.util.Set;

public class RoomValidation {

public static RoomType validateRoomTypeName(String typeName){

    Validator.checkStringNotEmpty(typeName, "Room type");

    String r = typeName.toLowerCase().trim();
    RoomType roomType = Database.findRoomType(r);
    if (roomType == null){
        throw new IllegalArgumentException("Invalid Room type");
    }

    return roomType;
}



public static int validateRoomFloor(int floor){
    Validator.checkNumPositive( floor , "Floor number"); //getRoomFloor not static ,,,
    return floor;
}



public static void validateAmenities(Rooms room){   //per room
    Validator.checkNotNull(room, "Room cannot be null.");

    if (room.getAmenities().isEmpty()){
        throw new IllegalArgumentException("Room must have at least one amenity.");
    }

    Set<String> amenityNames = new HashSet<>();

    for (Amenity a : room.getAmenities()){
        if (! Database.getamenitiesList().contains(a)){         //not in system
            throw new IllegalArgumentException("Amenity not found: " + a.getAmenityName() );
        }

        if (! amenityNames.add(a.getAmenityName().toLowerCase())){ //try to add amenityName, if fails, already exists
            throw new IllegalArgumentException("Duplicate Amenity: " + a.getAmenityName() );
        }

    }


}




public static void validateRoom(Rooms room){

    Validator.checkNotNull(room, "Room cannot be null");

    validateRoomFloor(room.getRoomFloor());

    if (room.getRoomtype() == null){
        throw new IllegalArgumentException("Room type must be set");
    }

    validateAmenities(room);
}


}
