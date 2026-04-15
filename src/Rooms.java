import java.util.ArrayList;

public class Rooms {
 private static int RoomNumber=1;
 private int RoomFloor;
 private RoomType roomtype;
 private double PricePerNight;
 private boolean IsAvailable;
 private String RequiredAmenity;
 ArrayList<Amenity> amenities = new ArrayList<>();

    public Rooms() {
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

    public void MarkAvailable()
    {
        IsAvailable=true;

    }
    public void MarkBooked()
    {
        IsAvailable=false;
    }


    public void RoomAvailability(boolean available)
    {
        if (available == true){
            System.out.println("Room is available for booking");
        }
        else
        {
            System.out.println("Room is already booked");
        }
    }
    public void RoomAmenities(String reqAmenity,ArrayList<Amenity> amenities , int roomnum){
        switch (reqAmenity){
            case "wifi" , "Wifi" :
                amenities.get(roomnum).setWifi(true);
                break;
            case "housekeeping","Housekeeping":
                amenities.get(roomnum).setHousekeeping(true);
                break;
            case "laundry service","Laundry service":
                amenities.get(roomnum).setLaundryService(true);
                break;
            case "minibar","Minibar":
                amenities.get(roomnum).setMinibar(true);
                break;
            case "digital safe","Digital safe":
                amenities.get(roomnum).setRoomDigitalSafe(true);
                break;
            case "break fast in bed","Breakfast in bed":
                amenities.get(roomnum).setBreakfastInBed(true);
                break;
            case "extra bed"," Extra bed":
                amenities.get(roomnum).setAddExtraBed(true);
                break;
            case "extra pillows","Extra pillows":
                amenities.get(roomnum).setExtraPillows(true);
                break;
            case "extra toiletries","Extra toiletries":
                amenities.get(roomnum).setExtraToiletries(true);
                break;
            case "extra towels","Extra towels":
                amenities.get(roomnum).setExtraTowels(true);
                break;
            case "bath set"," Bath set":
                amenities.get(roomnum).setBathSet(true);
                break;
            case "sewing kit","Sewing kit":
                amenities.get(roomnum).setSewingKit(true);
                break;
            case "coffee machine","Coffee machine":
                amenities.get(roomnum).setCoffeeMachine(true);
                break;
            case "bluetooth speaker","Bluetooth speaker":
                amenities.get(roomnum).setBluetoothSpeaker(true);
                break;
            case "jacuzzi","Jacuzzi":
                amenities.get(roomnum).setJacuzzi(true);
                break;
            case "spa","Spa":
                amenities.get(roomnum).setSpa(true);
                break;
            case "private pool","Private pool":
                amenities.get(roomnum).setPrivatePool(true);
                break;
            case "late check out","Late check out":
                amenities.get(roomnum).setLateCheckOut(true);
                break;

        }

    }

    public void PrintAvailableAmenities(int roomnum,ArrayList<Amenity> amenities){
        Amenity a = amenities.get(roomnum);
       if(a.isWifi())
       {
           System.out.println("Wifi is available in the room.");
       }
        if(a.isHousekeeping())
        {
            System.out.println("Housekeeping is available in the room.");
        }
        if(a.isLaundryService())
        {
            System.out.println("Laundry Service is available in the room.");
        }
        if(a.isMinibar())
        {
            System.out.println("A minibar is available in the room.");
        }
        if(a.isRoomDigitalSafe())
        {
            System.out.println("A digital safe is available in the room.");
        }
        if(a.isBreakfastInBed())
        {
            System.out.println("Breakfast in bed is available in the room.");
        }
        if(a.isAddExtraBed())
        {
            System.out.println("An extra bed will be provided in the room.");
        }
        if(a.isExtraPillows())
        {
            System.out.println("Extra pillows will be provided to the room.");
        }
        if(a.isExtraToiletries())
        {
            System.out.println("Extra toiletries will be provided to the room.");
        }
        if(a.isExtraTowels())
        {
            System.out.println("Extra towels will be provided to the room.");
        }
        if(a.isBathSet())
        {
            System.out.println("A bath set is available in the room.");
        }
        if(a.isSewingKit())
        {
            System.out.println("A sewing kit is available in the room.");
        }
        if(a.isCoffeeMachine())
        {
            System.out.println("A coffee machine is available in the room.");
        }
        if(a.isBluetoothSpeaker())
        {
            System.out.println("A Bluetooth speaker is available in the room.");
        }
        if(a.isJacuzzi())
        {
            System.out.println("A jacuzzi is available in the room.");
        }
        if(a.isSpa())
        {
            System.out.println("A spa session is booked in the room.");
        }
        if(a.isPrivatePool())
        {
            System.out.println("A private pool is available for this room.");
        }
        if(a.isLateCheckOut())
        {
            System.out.println("This room has a late checkout availability.");
        }
        else
        {
            System.out.println("No extra amenities available for this room.");
        }







    }



}
