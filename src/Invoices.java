import java.time.LocalDate;

public class Invoices implements Payable {

    private static int idCounter = 1000;

    private String invoiceId;
    private double totalamount;
    private PaymentMethod paymentmethod;
    private LocalDate paymentdate;
    private Reservations reservation;
    private String cardNumber; // store the card number

    public enum PaymentMethod{
        CASH , CREDIT_CARD , ONLINE
    }

    public Invoices(double totalamount, Reservations reservation)
            throws InvalidPaymentException {

        // This 'this' keyword calls Version 2 below.
        // It automatically passes CASH and "N/A" for you!
        this(totalamount, PaymentMethod.CASH, reservation, "N/A");
    }

    public Invoices(double totalamount, PaymentMethod paymentmethod , Reservations reservation,String cardNum)
            throws InvalidPaymentException {

            if (totalamount < 0) {
                // This is the custom exception requested in the project brief
                throw new InvalidPaymentException("Hotel Rule: Total amount cannot be negative.");
            }

        if (paymentmethod != PaymentMethod.CASH) {
            if (cardNum == null || cardNum.isEmpty()) {
                throw new InvalidPaymentException("Error: Card number is required for " + method);
            }
        }
        this.reservation=reservation;
        this.totalamount = totalamount;
        this.paymentmethod = paymentmethod;
        this.paymentdate = LocalDate.now();
            this.cardNumber = cardNum;
        this.invoiceId = "INV-" + idCounter;

        idCounter++;
    }


    @Override
    public double CalculateTotal() {
        return this.totalamount;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public double getTotalamount() {
        return totalamount;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setTotalAmount(double totalamount) {
        if (totalamount < 0) {
            throw new IllegalArgumentException("Total amount cannot be negative.");
        }
        this.totalamount = totalamount;
    }

    public PaymentMethod getPaymentmethod() {
        return paymentmethod;
    }

    public void setPaymentmethod(PaymentMethod paymentmethod) {
        this.paymentmethod = paymentmethod;
    }

    public LocalDate getPaymentdate() {
        return paymentdate;
    }

    public void setPaymentdate(LocalDate paymentdate) {
        this.paymentdate = paymentdate;
    }

    public Reservations getReservation() {
        return reservation;
    }

    @Override
    public String toString() {
        return "Invoice ID: " + invoiceId +
                "\nDate: " + paymentdate +
                "\nMethod: " + paymentmethod +
                "\nTotal: " + totalamount + " EGP"
    }
}
