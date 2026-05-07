import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.LocalDate;

public class RegisterController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField addressField;

    @FXML
    private DatePicker dobPicker;

    @FXML
    private ComboBox<Guests.Gender> genderBox;

    @FXML
    private TextField balanceField;

    @FXML
    private Label errorLabel;

    @FXML
    public void initialize() {

        genderBox.getItems().addAll(
                Guests.Gender.male,
                Guests.Gender.female
        );
    }

    @FXML
    private void handleRegister() {

        try {

            String username = usernameField.getText();
            String password = passwordField.getText();
            String address = addressField.getText();
            LocalDate dob = dobPicker.getValue();
            Guests.Gender gender = genderBox.getValue();

            double balance = Double.parseDouble(balanceField.getText());

            Guests guest = new Guests(
                    username,
                    address,
                    dob,
                    password,
                    gender
            );

            guest.setBalance(balance);

            // YOUR VALIDATION
            GuestValidation.validateRegister(guest);

            // ADD TO DATABASE
            Database.addGuests(guest);

            errorLabel.setStyle("-fx-text-fill: #b8d6b8;");
            errorLabel.setText("Account created successfully.");

            clearFields();

        }
        catch (NumberFormatException e) {

            errorLabel.setText("Balance must be a valid number.");

        }
        catch (IllegalArgumentException e) {

            errorLabel.setText(e.getMessage());

        }
        catch (Exception e) {

            errorLabel.setText("Registration failed.");

        }
    }

    private void clearFields() {

        usernameField.clear();
        passwordField.clear();
        addressField.clear();
        balanceField.clear();
        dobPicker.setValue(null);
        genderBox.setValue(null);
    }
}
