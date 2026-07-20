import java.util.HashMap;
import java.util.Map;

public class RegularMember extends GymMember {
    private final int attendanceLimit;
    private boolean isEligibleForUpgrade;
    private String removalReason;
    private String referralSource;
    private String plan;
    private double price;

    public RegularMember(int id, String name, String location, String phone, String email, String gender, String DOB,
            String membershipStartDate, String referralSource, String plan, double price, int initialAttendance, double initialLoyaltyPoints, boolean initialActiveStatus) {
        super(id, name, location, phone, email, gender, DOB, membershipStartDate, initialAttendance, initialLoyaltyPoints, initialActiveStatus);
        this.attendanceLimit = 30;
        this.isEligibleForUpgrade = false;
        this.plan = plan;
        this.price = price;
        this.removalReason = "";
        this.referralSource = referralSource;
    }

    public int getAttendance() {
        return this.attendance;
    }

    public int getAttendanceLimit() {
        return attendanceLimit;
    }

    public boolean isEligibleForUpgrade() {
        return isEligibleForUpgrade;
    }

    public String getPlan() {
        return plan;
    }

    public double getPrice() {
        return price;
    }

    public String getRemovalReason() {
        return removalReason;
    }

    public String getReferralSource() {
        return referralSource;
    }

    @Override
    public void markAttendance() {
        attendance++;  
              loyaltyPoints += 5;
    }

    public String upgradePlan(String newPlan) {
        // Convert input to lowercase for consistent comparison
        newPlan = newPlan.toLowerCase();

        // Check attendance requirement
        if (this.attendance < 30) {
            return "Upgrade failed! Minimum 30 attendance required. Current: " + this.attendance;
        }

        // Check if already at highest plan
        if (this.plan.equalsIgnoreCase("deluxe")) {
            return "You already have the highest tier plan!";
        }

        // Check valid upgrade path
        if (!isValidUpgradePath(newPlan)) {
            return "Invalid upgrade path from " + this.plan + " to " + newPlan +
                    ". Valid progression: Basic → Standard → Deluxe";
        }

        double newPrice = getPlanPrice(newPlan);
        if (newPrice == -1) {
            return "Invalid plan specified";
        }

        // Update plan and price (store in lowercase)
        this.plan = newPlan.toLowerCase();
        this.price = newPrice;
        return "Successfully upgraded to " + newPlan + " plan! New monthly fee: Rs" + newPrice;
    }

    private boolean isValidUpgradePath(String newPlan) {
        // Define valid upgrade paths with lowercase keys
        Map<String, String> upgradePaths = new HashMap<>();
        upgradePaths.put("basic", "standard");
        upgradePaths.put("standard", "deluxe");

        // Get current plan in lowercase for comparison
        String currentPlan = this.plan.toLowerCase();

        // Check if new plan is the next valid tier
        return upgradePaths.containsKey(currentPlan) &&
                upgradePaths.get(currentPlan).equalsIgnoreCase(newPlan);
    }

    private double getPlanPrice(String plan) {
        switch (plan.toLowerCase()) {
            case "basic":
                return 6500;
            case "standard":
                return 12500;
            case "deluxe":
                return 18500;
            default:
                return -1;
        }
    }

    public void revertRegularMember(String removalReason) {
        resetMember();
        this.plan = "basic";
        this.price = 6500;
        this.removalReason = removalReason;
    }

    @Override
    public String display() {
        StringBuilder sb = new StringBuilder(super.display());
        sb.append("Plan: ").append(plan).append("\n");
        sb.append("Price: ").append(price).append("\n");
        sb.append("Referral Source: ").append(referralSource).append("\n");
        return sb.toString();
    }
}
