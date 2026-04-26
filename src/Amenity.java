public class Amenity {
   private String AmenityName;
   private double AmenityCost;
   private boolean IsAvailable;

    public String getAmenityName() {
        return AmenityName;
    }

    public void setAmenityName(String amenityName) {
        AmenityName = RoomTypeValidation.validateAmenityName(amenityName);
    }

    public double getAmenityCost() {
        return AmenityCost;
    }

    public void setAmenityCost(double amenityCost) {
        AmenityCost = RoomTypeValidation.validateAmenityCost(amenityCost);
    }

    public boolean isAvailable() {
        return IsAvailable;
    }

    public void setAvailable(boolean available) {
        IsAvailable = available;
    }

    public Amenity(String amenityName , double amenityCost) {
        this.AmenityName = RoomTypeValidation.validateUniqueAmenity(amenityName);
        this.AmenityCost= RoomTypeValidation.validateAmenityCost(amenityCost);
        this.IsAvailable=true;
    }



    public void displayAmenities(){
        System.out.println("Name: " + AmenityName);
        System.out.println("Cost: " + AmenityCost);
        System.out.println("Availability: " + IsAvailable);
    }
}
