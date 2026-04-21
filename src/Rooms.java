import java.util.ArrayList;

public class Rooms {
    private static int RoomNumber = 1;
    private static int RoomCount = 0;
    private final int ActualRoomNumber;
    private int RoomFloor;
    private RoomStatus status;
    private RoomType roomtype;
    private double TotalAmenityCost = 0;
    ArrayList<Amenity> amenities = new ArrayList<>();

    public enum RoomStatus {
        AVAILABLE,
        OCCUPIED
    }
    public Rooms() {
        RoomCount++;
        this.ActualRoomNumber = RoomNumber++;
        this.status = RoomStatus.AVAILABLE;
    }

    public Rooms( int roomFloor, String roomTypeName) {
        RoomCount++;
        this.ActualRoomNumber = RoomNumber++;
        this.RoomFloor = roomFloor;
        this.roomtype= Database.findRoomType(roomTypeName);
        this.status = RoomStatus.AVAILABLE;
    }




    public  int getRoomNumber() {
        return this.ActualRoomNumber;
    }

    public int getRoomFloor() {
        return RoomFloor;
    }

    public void setRoomFloor(int roomFloor) {
        RoomFloor = roomFloor;
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
    }

    public RoomStatus getStatus() {
        return this.status;
    }


    public void RoomAvailability() {
        if (this.status == RoomStatus.AVAILABLE) {
            System.out.println("Room " + ActualRoomNumber + " is available for booking.");
        }
         else {
            System.out.println("Room " + ActualRoomNumber + " is already Occupied.");
        }
    }

    public void AddAmenity(Amenity Amenity) {
        amenities.add(Amenity);

    }
    public void RemoveAmenity(Amenity Amenity){
        amenities.remove(Amenity);
    }

    public void SetAmenity(String amenityname, boolean isavailable) {
        for (Amenity amenity : amenities) {
            if (amenity.getAmenityName().equals(amenityname)) {
                amenity.setAvailable(isavailable);
            }

        }

    }

    public void CalculateTotalAmenityCost() {
        TotalAmenityCost =0;
        for (Amenity amenity : amenities) {
            if(amenity.isAvailable()){
                TotalAmenityCost += amenity.getAmenityCost();
            }

        }
    }

    public double getTotalAmenityCost() {
        return TotalAmenityCost;
    }


    public RoomType getRoomtype() {
        return roomtype;
    }

    public void setRoomtype(RoomType roomtype) {
        this.roomtype = roomtype;
    }


    public void CanStay(int NumberOfGuests) {
        if (this.status == RoomStatus.AVAILABLE) {
            if (NumberOfGuests <= roomtype.getCapacity()) {
                System.out.println("Capacity is sufficient.");
            } else {
                System.out.println("Capacity exceeded.");
            }
        } else {
            System.out.println("Room is currently " + this.status + " and cannot be assigned.");
        }
    }

public void DisplayRoomInfo(){
        System.out.println("-----------------------------");
        System.out.println("       ROOM INFORMATION      ");
        System.out.println("-----------------------------");

        System.out.println("Room Number: "+ ActualRoomNumber);
        System.out.println("Room Floor: "+RoomFloor);
        System.out.println("Status: " + this.status);
        System.out.println("Room Type: "+roomtype.getTypeName());
        System.out.println("Room Description: "+roomtype.getRoomDescription());
        System.out.println("Maximum Capacity: "+roomtype.getCapacity());
        System.out.println("Price Per Night: "+roomtype.getPricePerNight());
        System.out.print("Amenities in the room: ");
        for(Amenity amenity:amenities){
            System.out.print(amenity.getAmenityName() + " , ");
        }
        System.out.println("\n");
        System.out.println("Total Amenities Cost: $ "+ TotalAmenityCost);
    }




}






