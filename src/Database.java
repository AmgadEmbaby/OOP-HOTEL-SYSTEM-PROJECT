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

  private static ArrayList<String> activityFeed = new ArrayList<>();

  private static Admin admin = new Admin("admin", "Halla", "admin123",   LocalDate.of(1999,5,9), Staff.Role.ADMIN,  8);     //one admin for the whole system

  public static Admin getAdmin() {
    return admin;
  }

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
  static {
    availableAmenitiesList.add(new Amenity("Soft drink", 2));
    availableAmenitiesList.add(new Amenity("Jacuzzi", 150));
    availableAmenitiesList.add(new Amenity("Wifi", 10));
    availableAmenitiesList.add(new Amenity("Tv-subscriptions", 100));
    availableAmenitiesList.add(new Amenity("HouseKeeping", 25));
    availableAmenitiesList.add(new Amenity("Mini Bar", 25));
    availableAmenitiesList.add(new Amenity("Breakfast in Bed", 45));
    availableAmenitiesList.add(new Amenity("Extra Pillows", 25));
    availableAmenitiesList.add(new Amenity("Bath Set", 12));

    // Assign images by name
    for (Amenity a : availableAmenitiesList) {
      String path = "/" + a.getAmenityName().toLowerCase().trim() + ".jpg";
      if (Database.class.getResourceAsStream(path) != null) {
        a.setImagePath(path);
      }
      // if file not found, constructor already set
    }
  }







  public static ArrayList<RoomType> getAvailableRoomTypesList(){

    return availableRoomTypesList;
  }

  public static ArrayList<Invoices> getInvoicesList(){

    return InvoicesList;
  }



  public static ArrayList<String> getActivityFeed() {
    return activityFeed;
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
    // Arguments: userName, address, dateOfBirth, passWord, gender
    Guests guest1 = new Guests("Nour", "Cairo", LocalDate.of(2000, 5, 10), "pass123", Guests.Gender.female);
    Guests guest2 = new Guests("Halla", "Tanta", LocalDate.of(1999, 3, 15), "pass456", Guests.Gender.female);
    Guests guest3 = new Guests("Amgad", "Cairo", LocalDate.of(2001, 8, 20), "pass789", Guests.Gender.male); // Added Amgad for you!

    guestList.add(guest1);
    guestList.add(guest2);
    guestList.add(guest3);
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
//  static {
//    // Arguments: roomfloor, roomtype, isAvailable
//    reservationsList.add(new Reservations(main().guest1, ));
//    roomList.add(new Rooms(1, "Double"));
//    roomList.add(new Rooms(2, "Double"));
//    roomList.add(new Rooms(2, "suite"));
//    roomList.add(new Rooms(3, "suite"));
//    //Dummy data for the main run :)
//
//
//  }
//  Guests guest, RoomType roomType, LocalDate in, LocalDate out,Invoices.PaymentMethod method

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








  public static double calculateReservationTotal(int resID) {
    double grandTotal = 0;

    for (Invoices inv : InvoicesList) {

      if (inv.getReservation().getReservationID() == resID &&
              inv.getStatus() == Invoices.InvoiceStatus.UNPAID) {


        grandTotal += inv.CalculateTotal();
      }
    }
    return grandTotal; // this returns the final sum of all Invoices + Taxes
  }



  public static boolean authenticateAdmin(String username, String password) {
    return admin.getUserName().equals(username) &&
            admin.getPassWord().equals(password);
  }



  public static void addActivity(String message) {
    activityFeed.add(0, message);

    if (activityFeed.size() > 25) {
      activityFeed.remove(activityFeed.size() - 1);
    }
    AdminController.refreshUI();
  }

}
