import java.util.ArrayList;

public class Rooms {
    private static int RoomNumber = 1;
    private static int RoomCount = 0;
    private int RoomFloor;
    private RoomType roomtype;
    private double PricePerNight;
    private boolean IsAvailable;
    private String RequiredAmenity;
    private double TotalAmenityCost = 0;
    ArrayList<Amenity> amenities = new ArrayList<>();

    public Rooms() {
        RoomCount++;
        RoomNumber++;
    }

    public static int getRoomNumber() {
        return RoomNumber;
    }

    public static void setRoomNumber(int roomNumber) {
        RoomNumber = roomNumber;
    }

    public double getPricePerNight() {
        return PricePerNight;
    }

    public void setPricePerNight(double pricePerNight) {
        PricePerNight = pricePerNight;
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
        for (Amenity amenity : amenities) {
            if(amenity.isAvailable()){
                TotalAmenityCost += amenity.getAmenityCost();
            }

        }
    }

    public double getTotalAmenityCost() {
        return TotalAmenityCost;
    }
}






