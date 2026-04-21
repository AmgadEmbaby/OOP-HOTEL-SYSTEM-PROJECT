public class Admin extends Staff{
 //------------------------- create,update,delete functions for rooms------------------
    //create
    public void createRoom( int roomFloor, String roomTypeName){
        Rooms room= new Rooms( roomFloor,roomTypeName );
        Database.getRoomList().add(room);
        System.out.println("Room created and added to database sucessfully");
    }    //the input is taken in the main(milestone 1) and added to the list by the admin only



    //update (assuming only the availability can be updated)
    public void updateAvailability(Rooms.RoomStatus status, int roomNumber){
      Rooms room= Database.findRoom(roomNumber);
      room.setStatus(status);

    }

    //delete
    public void deleteRoom( int roomNumber){
      Rooms room= Database.findRoom(roomNumber);
     if(room != null){
        Database.getRoomList().remove(room);}
    }

    //------------------------- create,update,delete functions for amenities ------------------
    //create
    public void createAmenity( String AmenityName, double AmenityCost){
       Amenity amenity= new Amenity(AmenityName,AmenityCost );
        Database.getamenitiesList().add(amenity);
        System.out.println("Amenity created and added to database sucessfully");
    }



    //update
    public void updateAmenityCost (double price, String amenityName)
    {
        Amenity amenity= Database.findAmenity(amenityName);
        amenity.setAmenityCost(price);

    }

    public void updateAmenityAvailability (boolean availability, String amenityName)
    {
        Amenity amenity= Database.findAmenity(amenityName);
        amenity.setAvailable(availability);

    }

    //delete
    public void deleteAmenity ( String amenityName)
    {
        Amenity amenity= Database.findAmenity(amenityName);
        Database.getamenitiesList().remove(amenity);

    }

    //------------------------- create,update,delete functions for room types ------------------

//create
    public void createRoomType(String typeName, int numberOfBeds, int capacity, String roomDescription, double pricePerNight){
        RoomType roomType= new RoomType( typeName,  numberOfBeds,  capacity,  roomDescription,  pricePerNight);
        Database.getAvailableRoomTypesList().add(roomType);
        System.out.println("Room Type created and added to database sucessfully");

    }

//update
    public void updateRoomTypePrice(double price, String roomTypeName){
       RoomType roomType= Database.findRoomType(roomTypeName);
       roomType.setPricePerNight(price);

    }

    //delete
    public void deleteRoomType ( String roomTypeName)
    {
        RoomType roomType= Database.findRoomType(roomTypeName);
        Database.getAvailableRoomTypesList().remove(roomType);

    }


}
