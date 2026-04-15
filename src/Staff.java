
import java.time.*;

public abstract class Staff {

    //attributes
    enum Role {ADMIN, RECEPTIONIST}
    private String userName;
    private String passWord;
    private LocalDate dateOfBirth;
    private Role role;
    private int workingHours;

    //setters
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setWorkingHours(int workingHours) {
        this.workingHours = workingHours;
    }

    //getters


    public String getUserName() {
        return userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public Role getRole() {
        return role;
    }

    public int getWorkingHours() {
        return workingHours;
    }


    //methods
    public void viewGuests(){
        for(Guests g: Database.getGuestList()){
            System.out.println( "Username: " + g.getUserName());
            System.out.println( "Address: " + g.getAdress());
            System.out.println( "Gender: " + g.getGender());
            System.out.println( "Balance: " + g.getBalance());

        }
    }



    public void viewRooms(){
        for(Rooms r: Database.getRoomList()){
            System.out.println( "Room Number: " + r.getRoomNumber());
            System.out.println( "Room Floor: " + r.getRoomFloor());
            System.out.println( "Room Type: " + r.getRoomtype().getTypeName());


        }
    }


    //--------ADD THE VIEW RESERVATIONS--------//





}
