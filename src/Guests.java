import java.util.Date;
import java.util.Scanner;

public class Guests {

    private String userName;
    private String passWord;
    private Date dateOfBirth;
    private double Balance;
    private String address;
    private Gender gender;
    //Room Prefrence lama n3ml el Room class

    Scanner input = new Scanner(System.in);


    enum Gender{
        male,female;

    }

    public Guests(String userName, String address, double balance, Date dateOfBirth, String passWord, Gender gender) {
        this.userName = userName;
        this.address = address;
        Balance = balance;
        this.dateOfBirth = dateOfBirth;
        this.passWord = passWord;
        this.gender = gender;
    }

    public Guests() {
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public double getBalance() {
        return Balance;
    }

    public void setBalance(double balance) {
        Balance = balance;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void Register(){
        Guests.Gender tempGender = null;
        System.out.println("Welcome to the our hotels app!!! ");
        System.out.print("Please enter your desired username: ");
        System.out.print("Enter your gender: ");
        String InputTempGender = input.nextLine();
        if(InputTempGender.equalsIgnoreCase("male")){
            tempGender = Guests.Gender.male;
        }
        else{
            tempGender = Guests.Gender.female;
        }
        // VALIDATION FOR INPUT HERE TO BE USED LATER
       String tempUsername =input.nextLine();
        System.out.print("Enter your date of birth: ");
       // String tempDOB = input.next
        System.out.print("Enter your password: ");
        String tempPassword = input.nextLine();
        System.out.print("Enter your address: ");
        String tempAddress = input.nextLine();
        System.out.print("Enter your balance: ");
        int tempBalance = input.nextInt();

        //Guests guests = new Guests(tempUsername,tempAddress,tempBalance,tempDOB,tempPassword,tempGender);
        //public Guests(String userName, String address, double balance, Date dateOfBirth, String passWord, Gender gender)




}
