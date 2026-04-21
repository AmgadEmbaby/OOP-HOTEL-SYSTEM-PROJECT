public class RoomType {
private String TypeName;
private int NumberOfBeds;
private int Capacity;
private String RoomDescription;
private double PricePerNight;


    @Override
    public String toString() {
        return this.getTypeName();
    }

    public RoomType(String typeName, int numberOfBeds, int capacity, String roomDescription, double pricePerNight) {
        TypeName = typeName;
        NumberOfBeds = numberOfBeds;
        Capacity = capacity;
        RoomDescription = roomDescription;
        PricePerNight = pricePerNight;
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

        RoomDescription = roomDescription;
    }



    public void setPricePerNight(double pricePerNight) {

        PricePerNight = pricePerNight;
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



