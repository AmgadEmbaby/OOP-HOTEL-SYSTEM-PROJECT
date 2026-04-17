public class Admin extends Staff{

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
    public void updateAvailability( boolean available, int roomNumber){
      Rooms room= findRoom(roomNumber);
      room.setAvailable(available);

    }

    //delete
    public void deleteRoom( int roomNumber){
      Rooms room= findRoom(roomNumber);
     //SHOULD I ADD I ROOM DOESNT EQUAL NULL??
        Database.getRoomList().remove(room);
    }





}
