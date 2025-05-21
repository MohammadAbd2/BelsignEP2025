package BE;

public class QCReport {
    private int id;
    private String orderNumber;
    private String email;
    private String frontImage;
    private String backImage;
    private String leftImage;
    private String rightImage;
    private String topImage;
    private String notes;

    public QCReport(int id, String orderNumber, String email,
                    String frontImage, String backImage,
                    String leftImage, String rightImage,
                    String topImage, String notes) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.email = email;
        this.frontImage = frontImage;
        this.backImage = backImage;
        this.leftImage = leftImage;
        this.rightImage = rightImage;
        this.topImage = topImage;
        this.notes = notes;
    }

    // Getters
    public int getId() { return id; }
    public String getOrderNumber() { return orderNumber; }
    public String getEmail() { return email; }
    public String getFrontImage() { return frontImage; }
    public String getBackImage() { return backImage; }
    public String getLeftImage() { return leftImage; }
    public String getRightImage() { return rightImage; }
    public String getTopImage() { return topImage; }
    public String getNotes() { return notes; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
    public void setEmail(String email) { this.email = email; }
    public void setFrontImage(String frontImage) { this.frontImage = frontImage; }
    public void setBackImage(String backImage) { this.backImage = backImage; }
    public void setLeftImage(String leftImage) { this.leftImage = leftImage; }
    public void setRightImage(String rightImage) { this.rightImage = rightImage; }
    public void setTopImage(String topImage) { this.topImage = topImage; }
    public void setNotes(String notes) { this.notes = notes; }

    @Override
    public String toString() {
        return "QCReport{" +
                "id=" + id +
                ", orderNumber='" + orderNumber + '\'' +
                ", email='" + email + '\'' +
                ", frontImage='" + frontImage + '\'' +
                ", backImage='" + backImage + '\'' +
                ", leftImage='" + leftImage + '\'' +
                ", rightImage='" + rightImage + '\'' +
                ", topImage='" + topImage + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }
}
