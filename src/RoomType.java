public class RoomType {
private String TypeName;
private int NumberOfBeds;
private int Capacity;
private String RoomDescription;
private double PricePerNight;

    public RoomType(String typeName, int numberOfBeds, int capacity, String roomDescription, double pricePerNight) {
        TypeName = RoomTypeValidation.validateTypeName(typeName);
        NumberOfBeds = RoomTypeValidation.validateBeds(numberOfBeds);
        Capacity = RoomTypeValidation.validateCapacity(capacity);
        RoomDescription = RoomTypeValidation.validateRoomDescription(roomDescription);
        PricePerNight = RoomTypeValidation.validatePricePerNight(pricePerNight);
    }

    public String getTypeName() {
        return TypeName;
    }
    public int getNumberOfBeds() {
        return NumberOfBeds;
    }
    public int getCapacity() {

        return Capacity;
    }

    public String getRoomDescription() {

        return RoomDescription;
    }
    public double getPricePerNight() {

        return PricePerNight;
    }




    public void setRoomDescription(String roomDescription) {

        RoomDescription = RoomTypeValidation.validateRoomDescription(roomDescription);
    }



    public void setPricePerNight(double pricePerNight) {

        PricePerNight = RoomTypeValidation.validatePricePerNight(pricePerNight);
    }


    public void displayRoomType() {
        System.out.println("--- ROOM TYPE DETAILS ---");
        System.out.println("Name: " + TypeName);
        System.out.println("Beds: " + NumberOfBeds);
        System.out.println("Capacity: " + this.Capacity);
        System.out.println("RoomDescription: " + RoomDescription);
        System.out.println("PricePerNight: " + PricePerNight);
        System.out.println("---------------------------");
    }
}



