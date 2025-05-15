package BE;

public class QCReport {
    private int id;
    private String order_number;
    private String email;
    private String image;
    private String notes;

    public QCReport(int id, String order_number, String email, String image, String notes) {
        this.id = id;
        this.order_number = order_number;
        this.email = email;
        this.image = image;
        this.notes = notes;
    }

    public int getId() {
        return id;
    }
    public String getOrder_number() {
        return order_number;
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
    public void setOrder_number(String order_number) {
        this.order_number = order_number;
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
        return "QCReport [id=" + id + ", orderNumber=" + order_number + ", email=" + email + ", image=" + image
                + ", notes=" + notes + "]";
    }
}
