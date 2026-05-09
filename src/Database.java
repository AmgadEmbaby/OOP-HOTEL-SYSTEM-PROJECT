/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import java.util.*;
import java.time.*;


public class Database {

  private static ArrayList<Guests> guestList = new ArrayList<>();
  private static ArrayList<Rooms> roomList = new ArrayList<>();
  private static ArrayList<Reservations> reservationsList = new ArrayList<>();
  private static ArrayList<Amenity> availableAmenitiesList = new ArrayList<>();
  private static ArrayList<RoomType> availableRoomTypesList = new ArrayList<>();
  private static ArrayList<Invoices> InvoicesList = new ArrayList<>();
  private static ArrayList<Staff> staffList = new ArrayList<>();
  private static ArrayList<String> activityFeed = new ArrayList<>();

  private static Admin admin = new Admin("admin", "Halla", "admin123",
          LocalDate.of(1999, 5, 9), Staff.Role.ADMIN, 8);

  // ================= 1. ROOM TYPES =================
  static {
    availableRoomTypesList.add(new RoomType("Single", 1, 1,
            "A room designed for one guest, it offers a small but comfortable space," +
                    "it's perfect for solo travellers like business guests or short stays.", 70));
    availableRoomTypesList.add(new RoomType("Double", 2, 2,
            "A room more spacious than the single and designed for two guests, you can change" +
                    " the two beds with one king size, it's perfect for friends or couples travelling together.", 125));
    availableRoomTypesList.add(new RoomType("Suite", 1, 4,
            "A large and luxurious room, it has a separate living area with a sofa bed," +
                    " bedroom with a large king size bed and a small kitchen with a mini bar," +
                    " it's perfect for a small family or guests who want a private and luxurious stay.", 465));
  }

  // ================= 2. GUESTS =================
  static {
    guestList.add(new Guests("Nour",  "Cairo",      LocalDate.of(2000, 5, 10), "pass123", Guests.Gender.female));
    guestList.add(new Guests("Halla", "Tanta",      LocalDate.of(1999, 3, 15), "pass456", Guests.Gender.female));
    guestList.add(new Guests("Amgad", "Cairo",      LocalDate.of(2001, 8, 20), "pass789", Guests.Gender.male));
  }

  // ================= 3. ROOMS =================
  static {
    roomList.add(new Rooms(1, "Single"));
    roomList.add(new Rooms(1, "Double"));
    roomList.add(new Rooms(2, "Double"));
    roomList.add(new Rooms(2, "Suite"));
    roomList.add(new Rooms(3, "Suite"));
  }

  // ================= 4. AMENITIES =================
  static {
    availableAmenitiesList.add(new Amenity("Soft drink",      2));
    availableAmenitiesList.add(new Amenity("Jacuzzi",         150));
    availableAmenitiesList.add(new Amenity("Wifi",            10));
    availableAmenitiesList.add(new Amenity("Tv-subscriptions",100));
    availableAmenitiesList.add(new Amenity("HouseKeeping",    25));
    availableAmenitiesList.add(new Amenity("Mini Bar",        25));
    availableAmenitiesList.add(new Amenity("Breakfast in Bed",45));
    availableAmenitiesList.add(new Amenity("Extra Pillows",   25));
    availableAmenitiesList.add(new Amenity("Bath Set",        12));

    for (Amenity a : availableAmenitiesList) {
      String path = "/" + a.getAmenityName().toLowerCase().trim() + ".jpg";
      if (Database.class.getResourceAsStream(path) != null) {
        a.setImagePath(path);
      }
    }
  }

  // ================= 5. RESERVATIONS =================
  // ================= 5. RESERVATIONS =================
  static {
    try {
      Guests g1 = guestList.get(0);
      Guests g2 = guestList.get(1);
      Guests g3 = guestList.get(2);

      // Today's arrivals (for dashboard testing)
      Reservations r1 = new Reservations(g1,
              availableRoomTypesList.get(0),
              LocalDate.now(),
              LocalDate.now().plusDays(2),
              Invoices.PaymentMethod.ONLINE);
      r1.setStatus(Reservations.ReservationStatus.CONFIRMED);
      reservationsList.add(r1);

      Reservations r2 = new Reservations(g2,
              availableRoomTypesList.get(1),
              LocalDate.now(),
              LocalDate.now().plusDays(3),
              Invoices.PaymentMethod.CASH);
      r2.setStatus(Reservations.ReservationStatus.CONFIRMED);
      reservationsList.add(r2);

      // Future reservations
      Reservations r3 = new Reservations(g3,
              availableRoomTypesList.get(2),
              LocalDate.now().plusDays(5),
              LocalDate.now().plusDays(10),
              Invoices.PaymentMethod.CREDIT_CARD);
      r3.setStatus(Reservations.ReservationStatus.CONFIRMED);
      reservationsList.add(r3);

      Reservations r4 = new Reservations(g1,
              availableRoomTypesList.get(1),
              LocalDate.now().plusDays(7),
              LocalDate.now().plusDays(9),
              Invoices.PaymentMethod.ONLINE);
      r4.setStatus(Reservations.ReservationStatus.PENDING);
      reservationsList.add(r4);

      Reservations r5 = new Reservations(g2,
              availableRoomTypesList.get(2),
              LocalDate.now().plusDays(14),
              LocalDate.now().plusDays(18),
              Invoices.PaymentMethod.CASH);
      r5.setStatus(Reservations.ReservationStatus.CONFIRMED);
      reservationsList.add(r5);

      // Past reservations
      Reservations r6 = new Reservations(g3,
              availableRoomTypesList.get(0),
              LocalDate.now().minusDays(10),
              LocalDate.now().minusDays(7),
              Invoices.PaymentMethod.ONLINE);
      r6.setStatus(Reservations.ReservationStatus.CONFIRMED);
      reservationsList.add(r6);

      Reservations r7 = new Reservations(g1,
              availableRoomTypesList.get(2),
              LocalDate.now().minusDays(5),
              LocalDate.now().minusDays(2),
              Invoices.PaymentMethod.CREDIT_CARD);
      r7.setStatus(Reservations.ReservationStatus.CONFIRMED);
      reservationsList.add(r7);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // ================= GETTERS =================

  public static Admin getAdmin() { return admin; }

  public static ArrayList<Guests> getGuestList() { return guestList; }

  public static ArrayList<Staff> getStaffList() { return staffList; }

  public static ArrayList<Rooms> getRoomList() { return roomList; }

  public static ArrayList<Reservations> getReservationsList() { return reservationsList; }

  public static ArrayList<Amenity> getamenitiesList() { return availableAmenitiesList; }

  public static ArrayList<RoomType> getAvailableRoomTypesList() { return availableRoomTypesList; }

  //helper functions
  public static Rooms findRoom(int roomNumber){
    for(Rooms r: Database.getRoomList()){
      if(r.getRoomNumber().equals(roomNumber) ){
        return r;
      }
  public static ArrayList<Invoices> getInvoicesList() { return InvoicesList; }

  public static ArrayList<String> getActivityFeed() { return activityFeed; }

  public static void addGuests(Guests guest) { guestList.add(guest); }

  // ================= FINDERS =================

  public static Rooms findRoom(int roomNumber) {
    for (Rooms r : roomList) {
      if (r.getRoomNumber() == roomNumber) return r;
    }
    return null;
  }

  public static Amenity findAmenity(String amenityName) {
    for (Amenity a : availableAmenitiesList) {
      if (amenityName.equalsIgnoreCase(a.getAmenityName())) return a;
    }
    return null;
  }

  public static RoomType findRoomType(String name) {
    for (RoomType rt : availableRoomTypesList) {
      if (rt.getTypeName().equalsIgnoreCase(name)) return rt;
    }
    return null;
  }

  public static Reservations findReservation(int reservationID) {
    for (Reservations r : reservationsList) {
      if (r.getReservationID() == reservationID) return r;
    }
    return null;
  }


  public static int getAvailableRoomCount(String roomTypeName, LocalDate desiredDate) {
    int roomCount = 0;
    for (Rooms r : roomList) {
      if (r.getRoomtype().getTypeName().equalsIgnoreCase(roomTypeName)) roomCount++;
    }

    int reservedCount = 0;
    for (Reservations rs : reservationsList) {
      if (rs.getTypeDesired().getTypeName().equalsIgnoreCase(roomTypeName) &&
              (rs.getStatus() == Reservations.ReservationStatus.CONFIRMED ||
                      rs.getStatus() == Reservations.ReservationStatus.PENDING)) {
        if (!desiredDate.isBefore(rs.getCheckin()) && desiredDate.isBefore(rs.getCheckout())) {
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
    for (Reservations r : reservationsList) {
      if (r.getRoom() != null && r.getRoom().equals(room) &&
              (r.getStatus() == Reservations.ReservationStatus.CONFIRMED ||
                      r.getStatus() == Reservations.ReservationStatus.PENDING)) {
        if (!(end.isBefore(r.getCheckin()) || start.isAfter(r.getCheckout()) ||
                start.equals(r.getCheckout()))) {
          return false;
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
    return grandTotal;
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

// R6
    Reservations r6 = new Reservations(guest1, single, LocalDate.now().minusDays(2), LocalDate.now(), Invoices.PaymentMethod.CREDIT_CARD);
    r6.setStatus(Reservations.ReservationStatus.CONFIRMED);
    r6.setRoom(room101);
    room101.setStatus(Rooms.RoomStatus.OCCUPIED);
    reservationsList.add(r6);
    System.out.println("TEST CHECK-OUT ID: " + r6.getReservationID());

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
  private static Staff loggedInStaff;

  public static void setLoggedInStaff(Staff staff) {
    loggedInStaff = staff;
  }

  public static Staff getLoggedInStaff() {
    return loggedInStaff;
  }


  public static void addActivity(String message) {
    activityFeed.add(0, message);
    if (activityFeed.size() > 25) activityFeed.remove(activityFeed.size() - 1);
    AdminController.refreshUI();
  }
}
