public class RoomType {
private String TypeName;
private int NumberOfBeds;
private int Capacity;
private String RoomDescription;
private double PricePerNight;

    public String getTypeName() {
        return TypeName;
    }

    public void setTypeName(String typeName) {
        TypeName = typeName;
    }

    public RoomType() {
    }

    public int getNumberOfBeds() {
        return NumberOfBeds;
    }

    public void setNumberOfBeds(int numberOfBeds) {
        NumberOfBeds = numberOfBeds;
    }

    public int getCapacity() {
        return Capacity;
    }

    public void setCapacity(int capacity) {
        Capacity = capacity;
    }

    public String getRoomDescription() {
        return RoomDescription;
    }

    public void setRoomDescription(String roomDescription) {
        RoomDescription = roomDescription;
    }

    public double getPricePerNight() {
        return PricePerNight;
    }

    public void setPricePerNight(double pricePerNight) {
        PricePerNight = pricePerNight;
    }


    public RoomType(String typeName, int numberOfBeds,int capacity,double pricePerNight) {
        TypeName = typeName;
        NumberOfBeds = numberOfBeds;
        Capacity=capacity;
        PricePerNight=pricePerNight;
    }

}
