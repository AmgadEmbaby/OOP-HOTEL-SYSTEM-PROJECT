import java.time.LocalDate;

public class Invoices implements Payable {

    private static int idCounter = 1000;

    private String invoiceId;
    private double totalamount;
    private PaymentMethod paymentmethod;
    private LocalDate paymentdate;
    private Reservations reservation;
    private InvoiceType type;
    private InvoiceStatus status;



    public enum PaymentMethod{
        CASH , CREDIT_CARD , ONLINE
    }

    public enum InvoiceType { BOOKING, PENALTY, REFUND, ROOM_SERVICE }

    public enum InvoiceStatus { PAID, UNPAID, CANCELLED }

    public Invoices(double totalamount, PaymentMethod paymentmethod , Reservations reservation , InvoiceType type , InvoiceStatus status)
            throws InvalidPaymentException {

            if (totalamount < 0) {
                // This is the custom exception requested in the project brief
                throw new InvalidPaymentException("Hotel Rule: Total amount cannot be negative.");
            }
        this.reservation=reservation;
        this.totalamount = totalamount;
        this.paymentmethod = paymentmethod;
        this.type=type;
        this.status=status;
        this.paymentdate = LocalDate.now();
        this.invoiceId = "INV-" + idCounter;
        idCounter++;// Sets the date to today

        Guests guest = reservation.getGuest();
        ReservationsValidation.validateInvoice(guest, this,paymentmethod);
        addInvoiceToGuestsArray();
    }

    public void addInvoiceToGuestsArray(){
        this.reservation.getGuest().addNewInvoiceForTheGuestList(this);
    }


    @Override// this method takes the amount in an invoice and adds taxes to it
    public double CalculateTotal() {
        double tax = this.totalamount * 0.14;
        return this.totalamount + tax;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public double getTotalamount() {
        return totalamount;
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

        Validator.checkNotNull(paymentmethod, "Payment method cannot be null.");
        this.paymentmethod = paymentmethod;
    }

    public LocalDate getPaymentdate() {
        return paymentdate;
    }

    public void setPaymentdate(LocalDate paymentdate) {

        Validator.checkNotNull(paymentdate, "Payment date cannot be null.");
        this.paymentdate = paymentdate;
    }

    public Reservations getReservation() {
        return reservation;
    }

    public InvoiceType getType() {
        return type;
    }

    public void setType(InvoiceType type) {
        this.type = type;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    public void setStatus(InvoiceStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Invoice { " +
                "amount=" + totalamount +
                ", method=" + paymentmethod +
                ", date=" + paymentdate +
                '}';
    }
}
