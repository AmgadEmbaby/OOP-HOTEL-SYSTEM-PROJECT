

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
    Validator.checkNumPositive( Rooms.getRoomFloor() , "Floor number");
    return floor;
}



public static void validateAmenities(Rooms room){

}


}
