
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
            g.displayGuestInfo();

        }
    }



    public void viewRooms(){
        for(Rooms r: Database.getRoomList()){
            r.DisplayRoomInfo();

        }
    }


    public void viewReservations(){
        for(Reservations r: Database.getReservationsList()){
            r.displayReservation();

        }
    }


    public void viewAmenities(){
        for(Amenity a: Database.getamenitiesList()){
            a.displayAmenities();

        }
    }


}
