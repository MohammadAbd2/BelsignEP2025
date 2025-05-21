package GUI.Controller;

import BE.QCReport;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class QCReportController {

    @FXML private ImageView frontImage, backImage, leftImage, rightImage, topImage;
    @FXML private TextArea notesArea;

    public void setReport(QCReport report) {
        // TODO: Replace dummy images when image loading is implemented
        Image placeholder = new Image("/images/placeholder.png"); // Or whatever default you have
        frontImage.setImage(placeholder);
        backImage.setImage(placeholder);
        leftImage.setImage(placeholder);
        rightImage.setImage(placeholder);
        topImage.setImage(placeholder);

        notesArea.setText(report.getNotes());
    }
}
