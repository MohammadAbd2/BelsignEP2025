package BE;

public class Order {
    private int id;
    private String orderNumber;
    private String image;
    private String notes;
    private String status;
    private String productName;
    private String statusIcon;

    public Order(int id, String orderNumber, String image, String notes, String status, String productName, String statusIcon) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.image = image;
        this.notes = notes;
        this.status = status;
        this.productName = productName;
        this.statusIcon = statusIcon;
    }

    public Order(int id) {
    }

    public Order(int id, String orderNumber, String image, String notes, String status) {

    }

    public int getId() {
        return id;
    }

    public String getOrderNumber() {
        return orderNumber;
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

    public String getProductName() {
        return productName;
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

    public String getStatusIcon() {
        if (status == null) return "default.png";

        switch (status.toLowerCase()) {
            case "approved":
                return "check.png";
            case "pending":
                return "hourglass.png";
            case "rejected":
                return "delete.png";
            case "new":
                return "star.png";
            default:
                return "default.png";
        }
    }

    @Override
    public String toString() {
        return "Order [id=" + id + ", orderNumber=" + orderNumber + ", image=" + image + ", notes=" + notes
                + ", status=" + status + "]";
    }
}
