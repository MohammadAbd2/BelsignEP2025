package BE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Order {
    private int id;
    private String order_number;
    private List<String> images; // Changed from single image to list of images
    private String notes;
    private String status;
    private String order_name;
    private String statusIcon;

    public Order(int id, String order_number, List<String> images, String notes, String status, String order_name) {
        this.id = id;
        this.order_number = order_number;
        this.images = images;
        this.notes = notes;
        this.status = status;
        this.order_name = order_name;
    }

    public Order(int id) {
        this.id = id;
    }

    public Order(int id, String order_number, List<String> images, String notes, String status) {
        this.id = id;
        this.order_number = order_number;
        this.images = images;
        this.notes = notes;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getOrder_number() {
        return order_number;
    }

    public List<String> getImages() {
        return images;
    }

    public String getNotes() {
        return notes;
    }

    public String getStatus() {
        return status;
    }

    public String getOrder_name() {
        return order_name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public void updateImages(int position, String base64Image) {
        if (images == null) {
            images = new ArrayList<>(Arrays.asList("", "", "", "", "")); // Initialize with empty strings
        }
        while (images.size() <= position) {
            images.add("");
        }
        images.set(position, base64Image);
    }


    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setOrder_name(String order_name) {
        this.order_name = order_name;
    }

    public String getStatusIcon() {
        if (status == null || status.isEmpty() || status.equalsIgnoreCase("new")) {
            return null;
        }

        switch (status.toLowerCase()) {
            case "approved":
                return "check.png";
            case "pending":
                return "hourglass.png";
            case "rejected":
                return "delete.png";
            default:
                return "default.png";
        }
    }

    @Override
    public String toString() {
        return "Order [id=" + id +
                ", orderNumber=" + order_number +
                ", images=" + images +
                ", notes=" + notes +
                ", status=" + status +
                ", orderName=" + order_name + "]";
    }
}
