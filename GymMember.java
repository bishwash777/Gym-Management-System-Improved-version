
public abstract class GymMember {
    protected int id;
    protected String name;
    protected String location;
    protected String phone;
    protected String email;
    protected String gender;
    protected String DOB;
    protected String membershipStartDate;
    protected int attendance;
    protected double loyaltyPoints;
    protected boolean activeStatus;

    public GymMember(int id, String name, String location, String phone, String email, String gender, String DOB,
            String membershipStartDate, int attendance, double loyaltyPoints, boolean activeStatus) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.phone = phone;
        this.email = email;
        this.gender = gender;
        this.DOB = DOB;
        this.membershipStartDate = membershipStartDate;
        this.attendance = attendance;
        this.loyaltyPoints = loyaltyPoints;
        this.activeStatus = activeStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }

    public String getDOB() {
        return DOB;
    }

    public String getMembershipStartDate() {
        return membershipStartDate;
    }

    public int getAttendance() {
        return attendance;
    }

    public double getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public boolean isActiveStatus() {
        return activeStatus;
    }

    public abstract void markAttendance();

    public void activateMembership() {
        activeStatus = true;
        System.out.println("Membership activated for: " + name);
        // loyaltyPoints = 0.00;
    }

    public void deactivateMembership() {
        if (activeStatus) {
            activeStatus = false;
            System.out.println("Membership cancelled for: " + name);
        } else {
            System.out.println("Membership is not active for: " + name);
        }
    }

    public void resetMember() {
        activeStatus = false;
        attendance = 0;
        loyaltyPoints = 0.00;
    }

    protected String membershipPlan;

    public String getMembershipPlan() {
        return membershipPlan;
    }

    public String display() {
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(id).append("\n");
        sb.append("Name: ").append(name).append("\n");
        sb.append("Location: ").append(location).append("\n");
        sb.append("Phone: ").append(phone).append("\n");
        sb.append("Email: ").append(email).append("\n");
        sb.append("Gender: ").append(gender).append("\n");
        sb.append("DOB: ").append(DOB).append("\n");
        sb.append("Membership Start Date: ").append(membershipStartDate).append("\n");
        sb.append("Attendance: ").append(attendance).append("\n");
        sb.append("Loyalty Points: ").append(loyaltyPoints).append("\n");
        sb.append("Status: ").append(activeStatus ? "Active" : "Inactive").append("\n");
        return sb.toString();
    }

}