public class Amenity {
   private String AmenityName;
   private double AmenityCost;
   private boolean IsAvailable;
    private String imagePath;

    //CONSTRUCTORS
    public Amenity(String amenityName , double amenityCost) {
        this.AmenityName = RoomTypeValidation.validateUniqueAmenity(amenityName);
        this.AmenityCost= RoomTypeValidation.validateAmenityCost(amenityCost);
        this.IsAvailable=true;
        imagePath = "/fallback.jpg";
    }

    //GETTERS
    public String getAmenityName() {
        return AmenityName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public double getAmenityCost() {
        return AmenityCost;
    }



    //GETTERS
    public void setAmenityName(String amenityName) {
        AmenityName = RoomTypeValidation.validateAmenityName(amenityName);
    }



    public void setAmenityCost(double amenityCost) {
        AmenityCost = RoomTypeValidation.validateAmenityCost(amenityCost);
    }

    public boolean isAvailable() {
        return IsAvailable;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    public void setAvailable(boolean available) {
        IsAvailable = available;
    }





    public void displayAmenities(){
        System.out.println("Name: " + AmenityName);
        System.out.println("Cost: " + AmenityCost);
        System.out.println("Availability: " + IsAvailable);
    }
}
