package Utils;

import BE.QCReport;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;  // iText Image
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PDFGenerator {
    private QCReport report;
    private File pdfFile;

    public void setReport(QCReport report) {
        this.report = report;
    }

    public QCReport getReport() {
        return report;
    }

    public void generateQCReport(QCReport report, String outputPath) throws IOException {
        PdfWriter writer = new PdfWriter(outputPath);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, PageSize.A4);

        try {
            addHeader(document, report);
            addOrderInformation(document, report);
            addImages(document, report);
            addNotes(document, report);
            addFooter(document, report);
        } finally {
            document.close();
        }
    }

    private void addHeader(Document document, QCReport report) {
        Table headerTable = new Table(new float[]{1, 2, 1});
        headerTable.setWidth(UnitValue.createPercentValue(100));

        try {
            String logoPath = getClass().getClassLoader().getResource("View/Img/BELMAN_Logo.png").toExternalForm();
            ImageData logoData = ImageDataFactory.create(logoPath);
            Image logo = new Image(logoData);
            logo.setWidth(75);
            headerTable.addCell(new Cell().add(logo).setBorder(Border.NO_BORDER));
        } catch (Exception e) {
            headerTable.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        }

        Paragraph title = new Paragraph("Order " + report.getOrderNumber() + " Report")
                .setFontSize(20)
                .setTextAlignment(TextAlignment.CENTER)
                .setBold();

        headerTable.addCell(new Cell().add(title)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setTextAlignment(TextAlignment.CENTER)
                .setBorder(Border.NO_BORDER));

        headerTable.addCell(new Cell().setBorder(Border.NO_BORDER));

        document.add(headerTable);
        document.add(new LineSeparator(new SolidLine()));
    }

    private void addOrderInformation(Document document, QCReport report) {
        Table table = new Table(2).useAllAvailableWidth();

        table.addCell(createCell("Order Number:")).addCell(createCell(report.getOrderNumber()));
        table.addCell(createCell("Date:")).addCell(createCell(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))));
        table.addCell(createCell("Quality Assurance:")).addCell(createCell(report.getQaName()));

        document.add(table);
        document.add(new Paragraph("\n"));
    }

    private void addImages(Document document, QCReport report) throws IOException {
        document.add(new Paragraph("Images").setBold().setFontSize(14).setMarginBottom(10));

        Table imageTable = new Table(2).useAllAvailableWidth();

        addImageToTable(imageTable, report.getFrontImage(), "Front");
        addImageToTable(imageTable, report.getBackImage(), "Back");
        addImageToTable(imageTable, report.getLeftImage(), "Left");
        addImageToTable(imageTable, report.getRightImage(), "Right");

        if (report.getTopImage() != null && !report.getTopImage().isEmpty()) {
            Cell topImageCell = new Cell(1, 2);
            try {
                ImageData topImageData = ImageDataFactory.create(report.getTopImage());
                Image topImg = new Image(topImageData);
                topImg.setAutoScale(true);
                topImageCell.add(topImg).add(new Paragraph("Top").setTextAlignment(TextAlignment.CENTER));
            } catch (Exception e) {
                topImageCell.add(new Paragraph("Top image not available"));
            }
            imageTable.addCell(topImageCell);
        }

        document.add(imageTable);
    }

    private void addImageToTable(Table table, String imagePath, String caption) {
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                ImageData imageData = ImageDataFactory.create(imagePath);
                Image pdfImage = new Image(imageData);
                pdfImage.setAutoScale(true);
                Cell cell = new Cell().add(pdfImage)
                        .add(new Paragraph(caption).setTextAlignment(TextAlignment.CENTER));
                table.addCell(cell);
            } catch (Exception e) {
                table.addCell(new Cell().add(new Paragraph("Image not available")));
            }
        } else {
            table.addCell(new Cell().add(new Paragraph("No image")));
        }
    }

    private void addNotes(Document document, QCReport report) {
        document.add(new Paragraph("Notes:").setBold().setFontSize(12).setMarginTop(20));
        String notes = report.getNotes();
        document.add(new Paragraph((notes != null && !notes.isEmpty()) ? notes : "No notes provided")
                .setTextAlignment(TextAlignment.LEFT));
    }

    private void addFooter(Document document, QCReport report) {
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("Generated on: " +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))));
        document.add(new Paragraph("Contact Email: " + report.getEmail()));
    }

    private Cell createCell(String text) {
        return new Cell().add(new Paragraph(text)).setBorder(Border.NO_BORDER);
    }

    /**
     * Generates a responsive JavaFX Node view of the PDF report inside a ScrollPane.
     * The images scale responsively according to window size, supporting tablets and mobiles.
     * Each page is rendered as an image with preserved aspect ratio inside a VBox.
     *
     * @return ScrollPane containing all PDF pages as images with responsive behavior.
     * @throws IOException if PDF rendering or file operations fail.
     */
    public Node getReportView() throws IOException {
        if (report == null) {
            throw new IllegalStateException("No report is set");
        }

        // Generate the PDF report to a temporary file
        File tempFile = File.createTempFile("QC_Report_" + report.getOrderNumber(), ".pdf");
        tempFile.deleteOnExit();
        generateQCReport(report, tempFile.getAbsolutePath());

        // Load the PDF document and create a renderer
        PDDocument pdDocument = PDDocument.load(tempFile);
        PDFRenderer renderer = new PDFRenderer(pdDocument);

        // VBox to hold all page images vertically with spacing
        VBox imageContainer = new VBox(15); // 15 px spacing between images
        imageContainer.setStyle("-fx-background-color: white; -fx-padding: 10;");

        // ScrollPane to enable vertical scrolling when there are many pages
        ScrollPane scrollPane = new ScrollPane(imageContainer);
        scrollPane.setFitToWidth(true);  // VBox width tracks ScrollPane width
        scrollPane.setPannable(true);    // Allow click-drag scrolling

        // Render each PDF page as an image and add to VBox inside ScrollPane
        for (int i = 0; i < pdDocument.getNumberOfPages(); i++) {
            BufferedImage bufferedImage = renderer.renderImageWithDPI(i, 150);
            javafx.scene.image.Image fxImage = SwingFXUtils.toFXImage(bufferedImage, null);
            ImageView imageView = new ImageView(fxImage);

            // Preserve aspect ratio and enable smoothing and caching for quality and performance
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);
            imageView.setCache(true);

            // Bind image width to ScrollPane width minus padding for responsiveness
            imageView.fitWidthProperty().bind(scrollPane.widthProperty().subtract(20));

            imageContainer.getChildren().add(imageView);
        }

        // Close PDF document to free resources
        pdDocument.close();

        // Return ScrollPane containing all page images with responsive sizing and scrolling
        return scrollPane;
    }
    public File getReportFile(QCReport report) {
        try {
            // Create a temporary PDF file based on the order number
            File tempFile = File.createTempFile("QC_Report_" + report.getOrderNumber(), ".pdf");
            tempFile.deleteOnExit(); // Automatically delete the file when the program exits

            // Generate the PDF report and write it to the temporary file
            generateQCReport(report, tempFile.getAbsolutePath());

            // Return the generated PDF file
            return tempFile;
        } catch (IOException e) {
            e.printStackTrace();
            return null; // Alternatively, throw a RuntimeException to handle it externally
        }
    }

}
