package GUI.Model;

public class ReportData {
    private int id;
    private String orderNumber;
    private String frontImagePath;
    private String backImagePath;
    private String leftImagePath;
    private String rightImagePath;
    private String topImagePath;
    private String email;
    private String notes;

    public ReportData(int id, String orderNumber,
                      String frontImagePath, String backImagePath,
                      String leftImagePath, String rightImagePath, String topImagePath,
                      String email, String notes) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.frontImagePath = frontImagePath;
        this.backImagePath = backImagePath;
        this.leftImagePath = leftImagePath;
        this.rightImagePath = rightImagePath;
        this.topImagePath = topImagePath;
        this.email = email;
        this.notes = notes;
    }

    // Getters
    public int getId() { return id; }
    public String getOrderNumber() { return orderNumber; }
    public String getFrontImagePath() { return frontImagePath; }
    public String getBackImagePath() { return backImagePath; }
    public String getLeftImagePath() { return leftImagePath; }
    public String getRightImagePath() { return rightImagePath; }
    public String getTopImagePath() { return topImagePath; }
    public String getEmail() { return email; }
    public String getNotes() { return notes; }
}
