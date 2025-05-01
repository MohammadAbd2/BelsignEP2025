package BE;

public class Order {
    private int id;
    private String orderNumber;
    private String image;
    private String notes;
    private String status;

    public Order(int id, String orderNumber, String image, String notes, String status) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.image = image;
        this.notes = notes;
        this.status = status;
    }

    public Order(int id, String mohammad) {
    }

    public int getId() {
        return id;
    }
    public String getOrderNumber() {
        return orderNumber;
    }
    public int getOrderId(){
        return id;
    }
    public String getImage() {
        return image;
    }
    public String getNotes() {
        return notes;
    }
    public String getStatus() {
        return status;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Order [id=" + id + ", orderNumber=" + orderNumber + ", image=" + image + ", notes=" + notes
                + ", status=" + status + "]";
    }
}
