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
    guest3.setBalance(1000.0);
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

  static {
    staffList.add(new Receptionist(
            "ahmed",             // The Username
            "ahmed mohamed",           // Full Name
            "ahmed123*",                // The Password
            LocalDate.of(2000, 1, 1),
            Staff.Role.RECEPTIONIST,
            8
    ));

    staffList.add(new Receptionist(
            "nourhan",             // The Username
            "nourhan ahmed*",           // Full Name
            "nourhan123*",                // The Password
            LocalDate.of(2000, 1, 1),
            Staff.Role.RECEPTIONIST,
            8
    ));

    staffList.add(new Receptionist(
            "youssef",             // The Username
            "youssef ali",           // Full Name
            "youssef123*",                // The Password
            LocalDate.of(2000, 1, 1),
            Staff.Role.RECEPTIONIST,
            8
    ));


  }


  static {
    // ── GUESTS ──
    Guests guest1 = new Guests("Nour Youssef",   "123 Cairo St",   LocalDate.of(1990, 3, 15), "pass123", Guests.Gender.female);
    Guests guest2 = new Guests("Halla Reda",      "456 Alex Rd",    LocalDate.of(1985, 7, 22), "pass456", Guests.Gender.male);
    Guests guest3 = new Guests("Amgad Ismail",    "789 Giza Ave",   LocalDate.of(1995, 1, 10), "pass789", Guests.Gender.female);
    Guests guest4 = new Guests("Hanna Shaker",    "321 Tanta Blvd", LocalDate.of(1988, 11, 5), "pass321", Guests.Gender.male);
    Guests guest5 = new Guests("Mariam Sherief",  "654 Luxor St",   LocalDate.of(1992, 6, 30), "pass654", Guests.Gender.female);
    Guests guest6 = new Guests("Omar Farouk",     "12 Nasr City",   LocalDate.of(1993, 4, 18), "pass111", Guests.Gender.male);
    Guests guest7 = new Guests("Sara Mahmoud",    "88 Mohandessin", LocalDate.of(1997, 9, 25), "pass222", Guests.Gender.female);
    Guests guest8 = new Guests("Karim Adel",      "5 Heliopolis",   LocalDate.of(1991, 2, 14), "pass333", Guests.Gender.male);

    guestList.add(guest1);
    guestList.add(guest2);
    guestList.add(guest3);
    guestList.add(guest4);
    guestList.add(guest5);
    guestList.add(guest6);
    guestList.add(guest7);
    guestList.add(guest8);

    //  ROOM TYPES
    RoomType single = findRoomType("Single");
    RoomType doble  = findRoomType("Double");
    RoomType suite  = findRoomType("Suite");

    Rooms room101 = roomList.get(0); // Single,  Floor 1
    Rooms room102 = roomList.get(1); // Double,  Floor 1
    Rooms room201 = roomList.get(2); // Double,  Floor 2
    Rooms room202 = roomList.get(3); // Suite,   Floor 2
    Rooms room301 = roomList.get(4); // Suite,   Floor 3



    // R1 — CONFIRMED, check-in today, paid online , ready for room assignment
    Reservations r1 = new Reservations(guest1, single,
            LocalDate.now(), LocalDate.now().plusDays(3),
            Invoices.PaymentMethod.CREDIT_CARD);
    r1.setStatus(Reservations.ReservationStatus.CONFIRMED);

    // R2 — PENDING, check-in today, paying cash at desk , needs payment first
    Reservations r2 = new Reservations(guest2, doble,
            LocalDate.now(), LocalDate.now().plusDays(5),
            Invoices.PaymentMethod.CASH);
    // status left PENDING

    // R3 — CONFIRMED, check-in in 2 days , too early
    Reservations r3 = new Reservations(guest3, suite,
            LocalDate.now().plusDays(2), LocalDate.now().plusDays(6),
            Invoices.PaymentMethod.CREDIT_CARD);
    r3.setStatus(Reservations.ReservationStatus.CONFIRMED);

    // R4 — CONFIRMED, check-in today, cash , ready for room assignment
    Reservations r4 = new Reservations(guest4, doble,
            LocalDate.now(), LocalDate.now().plusDays(2),
            Invoices.PaymentMethod.CASH);
    r4.setStatus(Reservations.ReservationStatus.CONFIRMED);

    // R5 — PENDING, check-in in 10 days → too early
    Reservations r5 = new Reservations(guest5, single,
            LocalDate.now().plusDays(10), LocalDate.now().plusDays(14),
            Invoices.PaymentMethod.CASH);


    // R6 — ON-TIME checkout (checkout = today) , no fine
    Reservations r6 = new Reservations(guest6, single,
            LocalDate.now().minusDays(3), LocalDate.now(),
            Invoices.PaymentMethod.ONLINE);
    r6.setStatus(Reservations.ReservationStatus.CONFIRMED);
    r6.setRoomDirect(room101);
    room101.setStatus(Rooms.RoomStatus.OCCUPIED);

    // R7 — LATE checkout (checkout was yesterday) , 100 fine
    Reservations r7 = new Reservations(guest7, doble,
            LocalDate.now().minusDays(5), LocalDate.now().minusDays(1),
            Invoices.PaymentMethod.CASH);
      r7.setStatus(Reservations.ReservationStatus.CONFIRMED);
    r7.setRoomDirect(room102);
    room102.setStatus(Rooms.RoomStatus.OCCUPIED);

    // R8 — EARLY checkout (checkout is 3 days from now) , $70 fine
    Reservations r8 = new Reservations(guest8, suite,
            LocalDate.now().minusDays(2), LocalDate.now().plusDays(3),
            Invoices.PaymentMethod.CREDIT_CARD);
    r8.setStatus(Reservations.ReservationStatus.CONFIRMED);
    r8.setRoom(room202);
    room202.setStatus(Rooms.RoomStatus.OCCUPIED);
    room202.getAmenities().add(Database.findAmenity("Jacuzzi"));
    room202.getAmenities().add(Database.findAmenity("Soft drink"));

    reservationsList.add(r1);
    reservationsList.add(r2);
    reservationsList.add(r3);
    reservationsList.add(r4);
    reservationsList.add(r5);
    reservationsList.add(r6);
    reservationsList.add(r7);
    reservationsList.add(r8);

    System.out.println("========= RESERVATION IDs =========");
    System.out.println("--- CHECK-IN CASES ---");
    System.out.println("R1 (Confirmed, today, Single):         " + r1.getReservationID());
    System.out.println("R2 (Pending cash, today, Double):      " + r2.getReservationID());
    System.out.println("R3 (Confirmed, too early, Suite):      " + r3.getReservationID());
    System.out.println("R4 (Confirmed, today, Double):         " + r4.getReservationID());
    System.out.println("R5 (Pending, too early, Single):       " + r5.getReservationID());
    System.out.println("--- CHECK-OUT CASES ---");
    System.out.println("R6 (On-time, Single, no fine):         " + r6.getReservationID());
    System.out.println("R7 (Late, Double, $100 fine):          " + r7.getReservationID());
    System.out.println("R8 (Early, Suite+amenities, $70 fine): " + r8.getReservationID());
    System.out.println("===================================");
  }

  public static void addActivity(String message) {
    activityFeed.add(0, message);

    if (activityFeed.size() > 25) {
      activityFeed.remove(activityFeed.size() - 1);
    }
  }

}
