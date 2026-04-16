public class Amenity {
   private String AmenityName;
   private double AmenityCost;
   private boolean IsAvailable;

    public String getAmenityName() {
        return AmenityName;
    }

    public void setAmenityName(String amenityName) {
        AmenityName = amenityName;
    }

    public double getAmenityCost() {
        return AmenityCost;
    }

    public void setAmenityCost(double amenityCost) {
        AmenityCost = amenityCost;
    }

    public boolean isAvailable() {
        return IsAvailable;
    }

    public void setAvailable(boolean available) {
        IsAvailable = available;
    }

    public Amenity(String amenityName , double amenityCost) {
        AmenityName = amenityName;
        AmenityCost=amenityCost;
    }


    public void displayAmenities(){
        System.out.println("Name: " + AmenityName);
        System.out.println("Name: " + AmenityCost);
        System.out.println("Name: " + IsAvailable);
    }
}
