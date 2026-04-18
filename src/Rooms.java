import java.util.ArrayList;

public class Rooms {
    private static int RoomNumber = 1;
    private static int RoomCount = 0;
    private final int ActualRoomNumber;
    private int RoomFloor;
    private RoomType roomtype;
    private boolean IsAvailable;
    private double TotalAmenityCost = 0;
    ArrayList<Amenity> amenities = new ArrayList<>();

    public Rooms() {
        RoomCount++;
        this.ActualRoomNumber = RoomNumber++;
    }
    public Rooms(int roomFloor, String roomtype, boolean isAvailable) {
        this.roomtype =new RoomType(roomtype);
        RoomCount++;

        this.ActualRoomNumber = RoomNumber++;

        this.RoomFloor = roomFloor;
        this.IsAvailable = isAvailable;

    }

    public void setAvailable(boolean available) {
        IsAvailable = available;
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

    public void MarkAvailable() {
        IsAvailable = true;
    }

    public void MarkBooked() {
        IsAvailable = false;
    }


    public void RoomAvailability(boolean available) {
        if (available) {
            System.out.println("Room is available for booking");
        } else {
            System.out.println("Room is already booked");
        }
    }

    public void AddAmenity(String amenityname, double amenityprice) {
        amenities.add(new Amenity(amenityname, amenityprice));
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
        if (IsAvailable) {
            if (NumberOfGuests <= roomtype.getCapacity()) {
                System.out.println("This room has the capacity for this number of guests.");
            } else {
                System.out.println("This room's capacity is not enough , please check another room.");
            }
        }
    }

public void DisplayRoomInfo(){
        System.out.println("-----------------------------");
        System.out.println("       ROOM INFORMATION      ");
        System.out.println("-----------------------------");

        System.out.println("Room Number: "+ ActualRoomNumber);
        System.out.println("Room Floor: "+RoomFloor);
        System.out.println("Status: " + (IsAvailable ? "Available" : "Occupied"));
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






