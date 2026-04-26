/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import java.util.*;
import java.time.*;


public class Database {

  private static  ArrayList<Guests> guestList= new ArrayList<>();

  private static  ArrayList<Rooms> roomList= new ArrayList<>();

  private static  ArrayList<Reservations> reservationsList= new ArrayList<>();

  private static  ArrayList<Amenity> availableAmenitiesList = new ArrayList<>();

  private static  ArrayList<RoomType> availableRoomTypesList = new ArrayList<>();

  private static  ArrayList<Invoices> InvoicesList = new ArrayList<>();

  private static ArrayList<Staff> staffList = new ArrayList<>();



  public static void addGuests(Guests guest){
    guestList.add(guest);
  }

  public static ArrayList<Guests> getGuestList(){

    return guestList;
  }

  public static ArrayList<Staff> getStaffList(){

    return staffList;
  }


  public static ArrayList<Rooms> getRoomList(){
    return roomList;
  }


  public static ArrayList<Reservations> getReservationsList(){

    return reservationsList;
  }


  public static ArrayList<Amenity> getamenitiesList(){

    return availableAmenitiesList;
  }
  static{
    //Arguments: Avilable amenties from the start
    availableAmenitiesList.add(new Amenity("Soft drink",2));
    availableAmenitiesList.add(new Amenity("Jacuzzi",150));
    availableAmenitiesList.add(new Amenity("Wifi",10));
    availableAmenitiesList.add(new Amenity("Tv-subscriptions",2));

  }


  public static ArrayList<RoomType> getAvailableRoomTypesList(){

    return availableRoomTypesList;
  }

  public static ArrayList<Invoices> getInvoicesList(){

    return InvoicesList;
  }

  //helper functions
  public static Rooms findRoom(int roomNumber){
    for(Rooms r: Database.getRoomList()){
      if(r.getRoomNumber()==roomNumber ){
        return r;
      }
    }
    return null;
  }



  public static Amenity findAmenity(String amenityName)
  {
    for(Amenity a: Database.getamenitiesList()){
      if(amenityName.equalsIgnoreCase(a.getAmenityName()) ){
        return a;
      }
    }
    return null;
  }


  public static RoomType findRoomType(String name) {
    for (RoomType rt : availableRoomTypesList) {
      if (rt.getTypeName().equalsIgnoreCase(name)) {
        return rt;
      }
    }
    return null;
  }

  public static Reservations findReservation(int reservationID) {
    for(Reservations r: Database.getReservationsList()){
      if(reservationID == r.getReservationID() ){
        return r;
      }
    }
    return null;
  }


public static int getAvailableRoomCount(String RoomTypeName, LocalDate desiredReservationDate){
    //bnshof fe kam room b nafs el requested type  w bn3dhom
    int roomCount =0;
for(Rooms r: Database.getRoomList()) {
  if (r.getRoomtype().getTypeName().equalsIgnoreCase(RoomTypeName)) {
    roomCount++; //total available physical rooms of this type  in the hotel
  }
}

  // we check if teh desired reservation date lies in a period of confirmed reservation
  int reservedCount=0;
  for(Reservations rs: getReservationsList()){
    if(rs.getTypeDesired().getTypeName().equalsIgnoreCase(RoomTypeName) &&( rs.getStatus()== Reservations.ReservationStatus.CONFIRMED||rs.getStatus()== Reservations.ReservationStatus.PENDING)){
      if(!desiredReservationDate.isBefore(rs.getCheckin() )&& desiredReservationDate.isBefore(rs.getCheckout()) ){
        reservedCount++;
      }
    }
  }
  return roomCount - reservedCount;
}


//used to ensure en mfesh duplicate bookings since el RESERVED etshal ml enum
  // this checks for only 1 room, it should be called gowa el make reservatin method ina  for loop hat loops from check in to check out dates



  static {
    // Arguments: name
    availableRoomTypesList.add(new RoomType("Single", 1 , 1, "A room designed for one guest, it offers a small but comfortable space," +
            "it's perfect for solo travellers like business guests or short stays.",  70));
    availableRoomTypesList.add(new RoomType("Double", 2 , 2, "A room more spacious than the single and designed for two guest, you can change" +
            "the two beds with one king size, it's perfect for friends or couple travelling together.",  125));
    availableRoomTypesList.add(new RoomType("Suite", 1 , 4, "A large and luxurious room , it has s separate living area with a sofa bed " +
            ",bedroom with a large king size bed and a small kitchen with a mini bar," +
            "it's perfect for a small family or guests who want to have a private and luxurious stay.",  465));

  }

  static {
    // Arguments: roomfloor, roomtype, isAvailable
    roomList.add(new Rooms(1, "Single"));
    roomList.add(new Rooms(1, "Double"));
    roomList.add(new Rooms(2, "Double"));
    roomList.add(new Rooms(2, "suite"));
    roomList.add(new Rooms(3, "suite"));
    //Dummy data for the main run :)


  }


  public static boolean isRoomAvailableForDates(Rooms room, LocalDate start, LocalDate end) {
    for (Reservations r : Database.getReservationsList()) {
      //search for this specific room in the future reservations to make sure it's free
      if (r.getRoom() != null && r.getRoom().equals(room) &&
              (r.getStatus() == Reservations.ReservationStatus.CONFIRMED ||
                      r.getStatus() == Reservations.ReservationStatus.PENDING)) {

        // checking for date overlap
        if (!(end.isBefore(r.getCheckin()) || start.isAfter(r.getCheckout()) || start.equals(r.getCheckout()))) {
          return false; // There is a clash
        }
      }
    }
    return true;
  }

}
