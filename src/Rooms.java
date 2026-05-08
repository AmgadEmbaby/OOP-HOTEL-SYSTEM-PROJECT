import java.time.LocalDate;
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
    ArrayList<Reservations> reservationsList = new ArrayList<>(); // list of reservations for this one room

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

        this.RoomFloor = RoomValidation.validateRoomFloor(roomFloor);
        this.roomtype= RoomValidation.validateRoomTypeName(roomTypeName);  //Database.findRoomType INSIDE validator function

        this.status = RoomStatus.AVAILABLE;
    }


    public int getRoomNumber() {
        return this.ActualRoomNumber;
    }

    public int getRoomFloor() {
        return RoomFloor;
    }

    public void setRoomFloor(int roomFloor) {

        this.RoomFloor = RoomValidation.validateRoomFloor(roomFloor);

    }

    public void setStatus(RoomStatus status) {

        Validator.checkNotNull(status, "Status cannot be null");
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
        Validator.checkNotNull(Amenity,"Amenity cannot be null" );
        RoomValidation.validateAmenities(this);

        amenities.add(Amenity);

        }

    public void RemoveAmenity(Amenity Amenity){
        amenities.remove(Amenity);
    }

    public void SetAmenity(String amenityname, boolean isavailable) {
        Validator.checkStringNotEmpty(amenityname, "Amenity name");
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



    public ArrayList<Amenity> getAmenities() {
        return amenities;
    }

    // ----------------------------------------- for validation ---------------------------------------------------
    //checkin is new reservation
    // r.getCheckIn is existing reservation
    public boolean isBooked (LocalDate checkin, LocalDate checkout){
        for (Reservations r : reservationsList) {
            if (checkin.isBefore(r.getCheckout()) && checkout.isAfter(r.getCheckin())) {
                return true;
            }
        }
        return false;
    }

    //prevents double booking
    public void addReservation(Reservations r){  // to list of reservations of THIS ONE room
        if (isBooked(r.getCheckin(), r.getCheckout())){
            throw new IllegalArgumentException("Room Already Booked");
        }
        reservationsList.add(r);
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






