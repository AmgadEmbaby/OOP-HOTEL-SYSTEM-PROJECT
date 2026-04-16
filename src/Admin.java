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

    //update functions
    public void updatePrice(Rooms room, int price, int roomNumber){
      findRoom(roomNumber);

    }





}
