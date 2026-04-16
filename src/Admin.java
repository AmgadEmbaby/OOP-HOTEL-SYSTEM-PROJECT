public class Admin extends Staff{
 //------------------------- create,update,delete functions for rooms------------------
    //create
    public void addRoom(Rooms room){
        Database.getRoomList().add(room);
    }    //the room is created in the main and added only by the admin

    //helper func
    public Rooms findRoom(int roomNumber){
        for(Rooms r: Database.getRoomList()){
            if(r.getRoomNumber()==roomNumber ){
                return r;
            }
        }
        return null;
    }

    //update (assuming only the availability can be updated)
    public void updateAvailability(Rooms.RoomStatus status, int roomNumber){
      Rooms room= findRoom(roomNumber);
      room.setStatus(status);

    }

    //delete
    public void deleteRoom( int roomNumber){
      Rooms room= findRoom(roomNumber);
     //SHOULD I ADD I ROOM DOESNT EQUAL NULL??
        Database.getRoomList().remove(room);
    }

    //------------------------- create,update,delete functions for amenities ------------------
    //create
    public void addAmenity(Amenity amenity){
        Database.getamenitiesList().add(amenity);
    }

    //helper
    public Amenity findAmenity(String amenityName)
    {
        for(Amenity a: Database.getamenitiesList()){
            if(amenityName.equalsIgnoreCase(a.getAmenityName()) ){
                return a;
            }
        }
        return null;
    }

    //update
    public void updateAmenityCost (int price, String amenityName)
    {
        Amenity amenity= findAmenity(amenityName);
        amenity.setAmenityCost(price);

    }

    public void updateAmenityAvailability (boolean availability, String amenityName)
    {
        Amenity amenity= findAmenity(amenityName);
        amenity.setAvailable(availability);

    }

    //delete
    public void deleteAmenity ( String amenityName)
    {
        Amenity amenity= findAmenity(amenityName);
        Database.getamenitiesList().remove(amenity);

    }





}
