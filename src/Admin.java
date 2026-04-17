public class Admin extends Staff{
 //------------------------- create,update,delete functions for rooms------------------
    //create
    public void createRoom( int roomFloor, String roomTypeName){
        Rooms room= new Rooms( roomFloor,roomTypeName );
        Database.getRoomList().add(room);
    }    //the input is taken in the main(milestone 1) and added to the list by the admin only



    //update (assuming only the availability can be updated)
    public void updateAvailability(Rooms.RoomStatus status, int roomNumber){
      Rooms room= Database.findRoom(roomNumber);
      room.setStatus(status);

    }

    //delete
    public void deleteRoom( int roomNumber){
      Rooms room= Database.findRoom(roomNumber);
     //SHOULD I ADD I ROOM DOESNT EQUAL NULL??
        Database.getRoomList().remove(room);
    }

    //------------------------- create,update,delete functions for amenities ------------------
    //create
    public void createAmenity( String AmenityName, double AmenityCost){
       Amenity amenity= new Amenity(AmenityName,AmenityCost );
        Database.getamenitiesList().add(amenity);
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
    public void addRoomType(String typeName, int numberOfBeds, int capacity, String roomDescription, double pricePerNight){
        RoomType roomType= new RoomType( typeName,  numberOfBeds,  capacity,  roomDescription,  pricePerNight);
        Database.getAvailableRoomTypesList().add(roomType);

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
