import java.time.LocalDate;

public class Invoices implements Payable {

    private static int idCounter = 1000;

    private String invoiceId;
    private double totalamount;
    private PaymentMethod paymentmethod;
    private LocalDate paymentdate;
    private Reservations reservation;

    public enum PaymentMethod{
        CASH , CREDIT_CARD , ONLINE
    }

    public Invoices(double totalamount, PaymentMethod paymentmethod , Reservations reservation)
            throws InvalidPaymentException {

            if (totalamount < 0) {
                // This is the custom exception requested in the project brief
                throw new InvalidPaymentException("Hotel Rule: Total amount cannot be negative.");
            }
        this.reservation=reservation;
        this.totalamount = totalamount;
        this.paymentmethod = paymentmethod;
        this.paymentdate = LocalDate.now();
        this.invoiceId = "INV-" + idCounter;
        idCounter++;// Sets the date to today
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
        return "Invoice { " +
                "amount=" + totalamount +
                ", method=" + paymentmethod +
                ", date=" + paymentdate +
                '}';
    }
}
