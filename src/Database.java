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
  static {
    try {
      Guests g1 = guestList.get(0);
      Guests g2 = guestList.get(1);
      Guests g3 = guestList.get(2);



// ================= CHECK-IN TEST CASES =================

// R1: Standard Confirmed Check-In (Ready to move into a room)
      Reservations r1 = new Reservations(guestList.get(0), availableRoomTypesList.get(0),
              LocalDate.now(), LocalDate.now().plusDays(2), Invoices.PaymentMethod.CREDIT_CARD);
      r1.setStatus(Reservations.ReservationStatus.CONFIRMED);
      reservationsList.add(r1);

// R2: Pending Payment (Test the "Confirm Payment" button logic)
      Reservations r2 = new Reservations(guestList.get(1), availableRoomTypesList.get(1),
              LocalDate.now(), LocalDate.now().plusDays(3), Invoices.PaymentMethod.CASH);
      r2.setStatus(Reservations.ReservationStatus.PENDING);
      reservationsList.add(r2);

// R3: Cancelled Case (Test how your search handles non-active bookings)
      Reservations r3 = new Reservations(guestList.get(0), availableRoomTypesList.get(0),
              LocalDate.now(), LocalDate.now().plusDays(1), Invoices.PaymentMethod.CASH);
      r3.setStatus(Reservations.ReservationStatus.CANCELLED);
      reservationsList.add(r3);


// ================= CHECK-OUT TEST CASES =================

// R4: Standard Check-Out (Guest is currently in the room)
      Reservations r4 = new Reservations(guestList.get(1), availableRoomTypesList.get(0),
              LocalDate.now().minusDays(3), LocalDate.now(), Invoices.PaymentMethod.CREDIT_CARD);
      r4.setStatus(Reservations.ReservationStatus.CONFIRMED);
      r4.setRoom(roomList.get(0)); // Link to Room 101
      roomList.get(0).setStatus(Rooms.RoomStatus.OCCUPIED);
      reservationsList.add(r4);

// R5: Late Check-Out (Penalty Case - 1 day late)
      Reservations r5 = new Reservations(guestList.get(0), availableRoomTypesList.get(1),
              LocalDate.now().minusDays(5), LocalDate.now().minusDays(1), Invoices.PaymentMethod.CASH);
      r5.setStatus(Reservations.ReservationStatus.CONFIRMED);
      r5.setRoom(roomList.get(1)); // Link to Room 102
      roomList.get(1).setStatus(Rooms.RoomStatus.OCCUPIED);
      reservationsList.add(r5);

// R6: Already Finished (Test a historical record)
      Reservations r6 = new Reservations(guestList.get(1), availableRoomTypesList.get(2),
              LocalDate.now().minusDays(10), LocalDate.now().minusDays(7), Invoices.PaymentMethod.CREDIT_CARD);
      r6.setStatus(Reservations.ReservationStatus.COMPLETED);
      reservationsList.add(r6);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  //receptionist
  static{
    staffList.add(new Receptionist("ahmed", "ahmed mohamed", "ahmed123*",
            LocalDate.of(2000, 1, 1),Staff.Role.RECEPTIONIST,8));
    staffList.add(new Receptionist("nourhan", "nourhan ali", "nourhan123*",
            LocalDate.of(1998, 6, 12), Staff.Role.RECEPTIONIST,5));
  }
  // ================= GETTERS =================

  public static Admin getAdmin() { return admin; }

  public static ArrayList<Guests> getGuestList() { return guestList; }

  public static ArrayList<Staff> getStaffList() { return staffList; }

  public static ArrayList<Rooms> getRoomList() { return roomList; }

  public static ArrayList<Reservations> getReservationsList() { return reservationsList; }

  public static ArrayList<Amenity> getamenitiesList() { return availableAmenitiesList; }

  public static ArrayList<RoomType> getAvailableRoomTypesList() { return availableRoomTypesList; }

  public static ArrayList<Invoices> getInvoicesList() { return InvoicesList; }

  public static ArrayList<String> getActivityFeed() { return activityFeed; }

  public static void addGuests(Guests guest) { guestList.add(guest); }

  // ================= FINDERS =================

  public static Rooms findRoom(int roomNumber) {
    for (Rooms r : roomList) {
      if (r.getRoomNumber().equals(roomNumber)) return r;
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

  // ================= BUSINESS LOGIC =================

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

  public static void addActivity(String message) {
    activityFeed.add(0, message);
    if (activityFeed.size() > 2) activityFeed.remove(activityFeed.size() - 1);
    AdminController.refreshUI();
  }

  private static Staff loggedInStaff;

  public static void setLoggedInStaff(Staff staff) {
    loggedInStaff = staff;

    // Optional: Log who just signed in for debugging
    if (staff != null) {
      System.out.println("DEBUG: Session started for " + staff.getName() + " (" + staff.getRole() + ")");
    }
  }

  public static Staff getLoggedInStaff() {
    return loggedInStaff;
  }
}