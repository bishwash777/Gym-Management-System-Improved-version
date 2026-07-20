
public class PremiumMember extends GymMember {
    private double premiumCharge;
    private String personalTrainer;
    private boolean isFullPayment;
    private double paidAmount;
    private double discountAmount;

    public PremiumMember(int id, String name, String location, String phone,
            String email, String DOB, String gender, String membershipStartDate, String personalTrainer,
            double premiumCharge, double paidAmount, double discountAmount, boolean isFullPayment, int initialAttendance, double initialLoyaltyPoints, boolean initialActiveStatus) {
        super(id, name, location, phone, email, DOB, gender, membershipStartDate, initialAttendance, initialLoyaltyPoints, initialActiveStatus);
        this.premiumCharge = premiumCharge;
        this.paidAmount = paidAmount;
        this.discountAmount = discountAmount;
        this.personalTrainer = personalTrainer;
        this.isFullPayment = isFullPayment;
    }

    public double getPremiumCharge() {
        return premiumCharge;
    }

    public String getPersonalTrainer() {
        return personalTrainer;
    }

    public boolean getIsFullPayment() {
        return isFullPayment;
    }

    public double getPaidAmount() {
        return paidAmount;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public double getRemainingAmount() {
        return premiumCharge - paidAmount - discountAmount;
    }

    public String payDueAmount(double paidAmount) {
        if (this.isFullPayment) {
            return "You've already made the full payment.";
        }

        double totalRequired = premiumCharge;

        if (this.paidAmount + paidAmount > totalRequired) {
            return "Your payment is more than the premium charge, please recheck the amount.";
        }

        this.paidAmount += paidAmount;

        if (this.paidAmount == premiumCharge) {
            this.isFullPayment = true;
            // Calculate and apply discount
            calculateDiscount();
            // Set paid amount to the discounted total to make remaining amount 0
            this.paidAmount = premiumCharge - discountAmount;
        }

        double remainingAmount = getRemainingAmount();
        return String.format("Payment successful. The remaining amount is: Rs%.2f", remainingAmount);
    }

    public void calculateDiscount() {
        if (isFullPayment) {
            discountAmount = premiumCharge * 0.10; // 10% discount
            System.out.printf("You've got the special discount of Rs%.2f%n", discountAmount);
        } else {
            System.out.println("No discount is available until the full payment");
        }
    }

    public void revertPremiumMember() {
        super.resetMember();
        personalTrainer = "";
        isFullPayment = false;
        paidAmount = 0;
        discountAmount = 0;
    }

    @Override
    public String display() {
        StringBuilder sb = new StringBuilder(super.display());
        sb.append("Personal Trainer: ").append(personalTrainer).append("\n");
        sb.append("Paid Amount: ").append(paidAmount).append("\n");
        sb.append("Discount Amount: ").append(discountAmount).append("\n");
        sb.append("Full Payment: ").append(isFullPayment ? "Yes" : "No").append("\n");
        return sb.toString();
    }

    @Override
    public void markAttendance() {
        this.attendance++;
        System.out.println("Attendance is marked for " + name + ". Total attendance: " + attendance + ".");
    }
}
