package BE;

public class QCReport {
    private int id;
    private String orderNumber;
    private String email;
    private String image;
    private String notes;

    public QCReport(int id, String orderNumber, String email, String image, String notes) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.email = email;
        this.image = image;
        this.notes = notes;
    }

    public int getId() {
        return id;
    }
    public String getOrderNumber() {
        return orderNumber;
    }
    public String getEmail() {
        return email;
    }
    public String getImage() {
        return image;
    }
    public String getNotes() {
        return notes;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "QCReport [id=" + id + ", orderNumber=" + orderNumber + ", email=" + email + ", image=" + image
                + ", notes=" + notes + "]";
    }
}
