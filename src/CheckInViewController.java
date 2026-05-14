import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

public class CheckInViewController {

    @FXML private TextField reservationIdField;
    @FXML private VBox reservationInfoBox;
    @FXML private Label guestNameLabel;
    @FXML private Label roomTypeLabel;
    @FXML private Label checkInDateLabel;
    @FXML private Label checkOutDateLabel;
    @FXML private Label statusLabel;
    @FXML private VBox assignmentBox;
    @FXML private ComboBox<String> roomComboBox;
    @FXML private Label messageLabel;
    @FXML private Button confirmPaymentButton;
    @FXML private VBox successBox;
    @FXML private Label assignedRoomLabel;
    @FXML private Label assignedFloorLabel;
    @FXML private Button resetButton;
    @FXML private ImageView backgroundImage;


    private Reservations currentReservation;
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd MMM yyyy");

    @FXML
    public void initialize() {
        // 1. Setup Background Scaling
        if (backgroundImage != null) {
            backgroundImage.fitWidthProperty().bind(javafx.beans.binding.Bindings.selectDouble(backgroundImage.sceneProperty(), "width"));
            backgroundImage.fitHeightProperty().bind(javafx.beans.binding.Bindings.selectDouble(backgroundImage.sceneProperty(), "height"));

            // Add a blur effect to the background image only, making the UI pop
            javafx.scene.effect.GaussianBlur blur = new javafx.scene.effect.GaussianBlur(15);
            backgroundImage.setEffect(blur);
        }
    }


    @FXML
    private void handleCheckIn() {

        String selectedValue = roomComboBox.getValue();

        if (selectedValue == null) {
            showMessage("Please select a room to proceed.", "error");
            return;
        }

        String roomNum = selectedValue.split(" ")[1];

        Rooms assignedRoom = null;
        for (Rooms r : Database.getRoomList()) {
            if (r.getRoomNumber().equals(roomNum)) {
                // update the room status in the central database
                r.setStatus(Rooms.RoomStatus.OCCUPIED);
                assignedRoom = r;
                break;
            }
        }

        if (currentReservation != null && assignedRoom != null) {
            // LINK THE ROOM
            currentReservation.setRoom(assignedRoom);

            currentReservation.setStatus(Reservations.ReservationStatus.CONFIRMED);

            assignedRoomLabel.setText(assignedRoom.getRoomNumber());
            assignedFloorLabel.setText("Floor " + assignedRoom.getRoomFloor());

            hide(assignmentBox);
            hide(reservationInfoBox);
            show(successBox);
            show(resetButton);

            Database.addActivity("Check-in: Room " + assignedRoom.getRoomNumber() + " assigned to " + currentReservation.getGuest().getUserName());
        }
    }


    @FXML
    private void handleLookup() {
        String input = reservationIdField.getText().trim();
        resetUI();

        if (input.isEmpty()) {
            showMessage("Please enter a reservation ID.", "error");
            return;
        }

        int resId;
        try {
            resId = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            showMessage("Reservation ID must be a number.", "error");
            return;
        }

        Reservations res = Database.findReservation(resId);
        if (res == null) {
            showMessage("No reservation found with ID: " + resId, "error");
            return;
        }

        currentReservation = res;
        updateReservationDetails(res);
        show(reservationInfoBox);

        LocalDate today = LocalDate.now();


        //  CANCELLED
        if (res.getStatus() == Reservations.ReservationStatus.CANCELLED) {
            showMessage("This reservation is cancelled.", "error");
            show(resetButton);
            return;
        }

        //  COMPLETED
        if (res.getStatus() == Reservations.ReservationStatus.COMPLETED) {
            showMessage("This reservation is already completed (checked out).", "warning");
            show(resetButton);
            return;
        }

        //  DATE VALIDATION
        if (today.isBefore(res.getCheckin())) {
            showMessage("Guest arrived too early. Check-in starts on " + res.getCheckin().format(FMT), "warning");
            show(resetButton);
            return;
        }

        if (today.isAfter(res.getCheckout())) {
            showMessage("This reservation has expired.", "error");
            show(resetButton);
            return;
        }

        // 4. PENDING (Needs Payment)
        if (res.getStatus() == Reservations.ReservationStatus.PENDING) {
            showMessage("Payment required. Please collect payment to confirm.", "warning");
            show(confirmPaymentButton);
            loadAvailableRooms();
            show(resetButton);
            return;
        }

        // 5. CONFIRMED (Ready for Room Assignment)
        if (res.getStatus() == Reservations.ReservationStatus.CONFIRMED) {

            if (res.getRoom() != null) {
                showMessage("Guest is already assigned to Room " + res.getRoom().getRoomNumber(), "info");
                show(resetButton);
            } else {
                loadAvailableRooms();
            }
        }
    }

    @FXML
    private void handleConfirmPayment() {
        if (currentReservation != null) {
            currentReservation.setStatus(Reservations.ReservationStatus.CONFIRMED);

            try {
                double amount = currentReservation.getRoomType().getPricePerNight();


                Invoices.PaymentMethod method = Invoices.PaymentMethod.valueOf(currentReservation.getMethod().toString());

                Invoices checkInInvoice = new Invoices(
                        amount,
                        method,
                        currentReservation,
                        Invoices.InvoiceType.BOOKING,
                        Invoices.InvoiceStatus.PAID
                );

                Database.getInvoicesList().add(checkInInvoice);
                System.out.println("DEBUG: Invoice " + checkInInvoice.getInvoiceId() + " generated successfully.");

            } catch (Exception e) {
                System.err.println("Invoice Error: " + e.getMessage());
            }

            if (messageLabel != null) messageLabel.setVisible(false);
            if (confirmPaymentButton != null) confirmPaymentButton.setVisible(false);

            loadAvailableRooms();

            if (statusLabel != null) {
                statusLabel.setText("CONFIRMED");
                statusLabel.setStyle("-fx-text-fill: #A5D6A7; -fx-border-color: #A5D6A7;");
            }
        }
    }


    @FXML
    private void handleAssign() {
        if (currentReservation == null) return;

        String selected = roomComboBox.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showMessage("Please select a room.", "error");
            return;
        }

        int roomNumber = Integer.parseInt(selected.split(" ")[1]);
        Rooms room = Database.findRoom(roomNumber);

        if (room != null) {
            // Update Database State
            currentReservation.setRoom(room);
            room.setStatus(Rooms.RoomStatus.OCCUPIED);

            // We keep it as CONFIRMED while they are staying.
            // It only becomes COMPLETED when they check out.

            assignedRoomLabel.setText(String.valueOf(room.getRoomNumber()));
            assignedFloorLabel.setText("Floor " + room.getRoomFloor());

            hide(assignmentBox);
            show(successBox);
            show(resetButton);
        }
    }

    private void loadAvailableRooms() {
        ArrayList<String> options = new ArrayList<>();
        String typeNeeded = currentReservation.getTypeDesired().getTypeName();

        for (Rooms r : Database.getRoomList()) {
            if (r.getRoomtype().getTypeName().equalsIgnoreCase(typeNeeded) &&
                    r.getStatus() == Rooms.RoomStatus.AVAILABLE) {
                options.add("Room " + r.getRoomNumber() + " - Floor " + r.getRoomFloor());
            }
        }

        if (options.isEmpty()) {
            showMessage("No " + typeNeeded + " rooms available today.", "error");
            show(resetButton);
        } else {
            roomComboBox.getItems().setAll(options);
            show(assignmentBox);
        }
    }

    private void updateReservationDetails(Reservations res) {
        if (guestNameLabel != null) guestNameLabel.setText(res.getGuest().getUserName());
        if (roomTypeLabel != null) roomTypeLabel.setText(res.getTypeDesired().getTypeName());
        if (checkInDateLabel != null) checkInDateLabel.setText(res.getCheckin().format(FMT));
        if (checkOutDateLabel != null) checkOutDateLabel.setText(res.getCheckout().format(FMT));
        if (statusLabel != null) statusLabel.setText(res.getStatus().toString());
    }


    private void resetUI() {
        if (reservationInfoBox != null) hide(reservationInfoBox);
        if (assignmentBox != null) hide(assignmentBox);
        if (successBox != null) hide(successBox);
        if (resetButton != null) hide(resetButton);
        if (messageLabel != null) hide(messageLabel);
        if (confirmPaymentButton != null)   hide(confirmPaymentButton);


    }

    private void show(javafx.scene.Node n) {
        n.setVisible(true);
        n.setManaged(true);

        javafx.animation.FadeTransition ft = new javafx.animation.FadeTransition(javafx.util.Duration.millis(600), n);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);

        // 3D Scale Transition (Makes it "pop" out)
        javafx.animation.ScaleTransition st = new javafx.animation.ScaleTransition(javafx.util.Duration.millis(400), n);
        st.setFromX(0.95);
        st.setFromY(0.95);
        st.setToX(1.0);
        st.setToY(1.0);

        ft.play();
        st.play();
    }

    private void hide(javafx.scene.Node n) { n.setVisible(false); n.setManaged(false); }

    private void showMessage(String text, String type) {
        messageLabel.setText(text);
        show(messageLabel);
        // Style logic remains the same as your previous snippet
    }

    @FXML private void handleReset() {
        reservationIdField.clear();
        resetUI();
    }
}