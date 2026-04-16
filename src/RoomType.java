public class RoomType {
private String TypeName;
private int NumberOfBeds;
private int Capacity;
private String RoomDescription;
private double PricePerNight;
private double singlePrice =70;
private double doublePrice =125;
private double suitePrice=465;





    public double getSinglePrice() {
        return singlePrice;
    }

    public void setSinglePrice(double singlePrice) {
        this.singlePrice = singlePrice;
    }

    public double getSuitePrice() {
        return suitePrice;
    }

    public void setSuitePrice(double suitePrice) {
        this.suitePrice = suitePrice;
    }

    public double getDoublePrice() {
        return doublePrice;
    }

    public void setDoublePrice(double doublePrice) {
        this.doublePrice = doublePrice;
    }

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


    public RoomType(String typeName) {
        TypeName = typeName;
       if(TypeName.equalsIgnoreCase("single")){
          this.NumberOfBeds=1;
          this.Capacity=1;
          this.PricePerNight=singlePrice; //prices in dollars($)
           this.RoomDescription="A room designed for one guest, it offers a small but comfortable space," +
                   "it's perfect for solo travellers like business guests or short stays.";
       }
       if(TypeName.equalsIgnoreCase("double")){
           this.NumberOfBeds=2;
           this.Capacity=2;
           this.PricePerNight=doublePrice;
           this.RoomDescription="A room more spacious than the single and designed for two guest, you can change" +
                   "the two beds with one king size, it's perfect for friends or couple travelling together.";
       }
       if(TypeName.equalsIgnoreCase("suite")){
           this.NumberOfBeds=1;
           this.Capacity=4;
           this.PricePerNight=doublePrice;
           this.RoomDescription="A large and luxurious room , it has s separate living area with a sofa bed " +
                   ",bedroom with a large king size bed and a small kitchen with a mini bar," +
                   "it's perfect for a small family or guests who want to have a private and luxurious stay.";

       }
    }

}
