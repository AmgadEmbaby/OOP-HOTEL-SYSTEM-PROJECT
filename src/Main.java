public class Main {
    public static void main(String[] args) {
        System.out.println("Hotel System Initialized!");


        Guests test = new Guests();
        test.Register();

        System.out.println("\n--- Guest Registration Details ---");
        System.out.println("Username:      " + test.getUserName());
        System.out.println("Password:      " + test.getPassWord()); // Usually kept hidden, but fine for testing!
        System.out.println("Gender:        " + test.getGender());
        System.out.println("Date of Birth: " + test.getDateOfBirth());
        System.out.println("Address:       " + test.getAddress());
        System.out.println("Balance:       " + test.getBalance());
        System.out.println("----------------------------------");

        for (Rooms room : Database.getRoomList()) {

            room.DisplayRoomInfo();}
    }
}

