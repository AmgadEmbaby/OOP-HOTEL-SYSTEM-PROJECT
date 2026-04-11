import java.util.Date;

public class Guests {

    private String userName;
    private String passWord;
    private Date dateOfBirth;
    private double Balance;
    private String adress;
    private Gender gender;
    //Room Prefrence lama n3ml el Room class




    enum Gender{
        male,female;

    }

    public Guests(String userName, String adress, double balance, Date dateOfBirth, String passWord,Gender gender) {
        this.userName = userName;
        this.adress = adress;
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

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }
    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
}
