package Utils;

import BE.QCReport;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.*;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PDFGenerator {
    
    public void generateQCReport(QCReport report, String outputPath) throws IOException {

        // Initialize PDF writer and document
        PdfWriter writer = new PdfWriter(outputPath);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, PageSize.A4);
        
        try {
            // Add header
            addHeader(document, report);
            
            // Add order information
            addOrderInformation(document, report);
            
            // Add images
            addImages(document, report);
            
            // Add notes
            addNotes(document, report);
            
            // Add footer
            addFooter(document, report);
            
        } finally {
            document.close();
        }
    }

    private void addHeader(Document document, QCReport report) {
        Table headerTable = new Table(new float[]{1, 2, 1});
        headerTable.setWidth(UnitValue.createPercentValue(100));

        try {
            Image logo = new Image(ImageDataFactory.create(
                    getClass().getClassLoader().getResource("View/Img/BELMAN_Logo.png")));
            logo.setWidth(75);
            logo.setAutoScale(true);
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

        headerTable.addCell(new Cell().setBorder(Border.NO_BORDER)); // empty right cell

        document.add(headerTable);
        document.add(new LineSeparator(new SolidLine()));
    }

    private void addOrderInformation(Document document, QCReport report) {
        Table table = new Table(2).useAllAvailableWidth();

        table.addCell(createCell("Order Number:")).addCell(createCell(report.getOrderNumber()));
        table.addCell(createCell("Date:")).addCell(createCell(
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))));
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

        // Full-width for top image (span across two columns)
        Cell topImageCell = new Cell(1, 2);
        if (report.getTopImage() != null && !report.getTopImage().isEmpty()) {
            Image img = new Image(ImageDataFactory.create(
                    getClass().getClassLoader().getResource(report.getTopImage())))
                    .setAutoScale(true)
                    .setWidth(UnitValue.createPercentValue(60));
            topImageCell.add(img).add(new Paragraph("Top").setTextAlignment(TextAlignment.CENTER));
        } else {
            topImageCell.add(new Paragraph("No Top image"));
        }
        imageTable.addCell(topImageCell);

        document.add(imageTable);
    }
    
    private void addImageToTable(Table table, String imagePath, String caption) throws IOException {
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                Image img = new Image(ImageDataFactory.create(imagePath));
                img.setAutoScale(true);
                img.setWidth(UnitValue.createPercentValue(90));

                Cell cell = new Cell().add(img)
                        .add(new Paragraph(caption).setTextAlignment(TextAlignment.CENTER));
                table.addCell(cell);
            } catch (Exception e) {
                Cell cell = new Cell().add(new Paragraph("Image not available"));
                table.addCell(cell);
            }
        } else {
            Cell cell = new Cell().add(new Paragraph("No image"));
            table.addCell(cell);
        }
    }

    private void addNotes(Document document, QCReport report) {
        document.add(new Paragraph("Notes:")
                .setBold()
                .setFontSize(12)
                .setMarginTop(20));
        document.add(new Paragraph(report.getNotes() != null && !report.getNotes().isEmpty()
                ? report.getNotes()
                : "No notes provided")
                .setTextAlignment(TextAlignment.LEFT));
    }
    
    private void addFooter(Document document, QCReport report) {
        document.add(new Paragraph("\n"));
        Table footerTable = new Table(2).useAllAvailableWidth();
        footerTable.addCell(createCell("Generated on:"))
                .addCell(createCell(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))));
        footerTable.addCell(createCell("Contact Email:"))
                .addCell(createCell(report.getEmail()));
        
        document.add(footerTable);
    }
    
    private Cell createCell(String content) {
        Cell cell = new Cell();
        cell.add(new Paragraph(content));
        return cell;
    }
}