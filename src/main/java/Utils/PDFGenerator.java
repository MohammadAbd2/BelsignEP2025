package Utils;

import BE.QCReport;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PDFGenerator {
    
    public void generateQCReportPDF(QCReport report, String outputPath) throws IOException {
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
        Paragraph header = new Paragraph("Quality Control Report")
                .setFontSize(24)
                .setTextAlignment(TextAlignment.CENTER)
                .setBold();
        document.add(header);
        
        try {
            Image logo = new Image(ImageDataFactory.create(
                getClass().getClassLoader().getResource("View/Img/BELMAN_Logo.png")));
            logo.setWidth(75);
            document.add(logo);
        } catch (Exception e) {
            // Handle logo loading error silently
        }
    }
    
    private void addOrderInformation(Document document, QCReport report) {
        Table table = new Table(2).useAllAvailableWidth();
        
        // Add order details
        table.addCell(createCell("Order Number:")).addCell(createCell(report.getOrderNumber()));
        table.addCell(createCell("Contact Email:")).addCell(createCell(report.getEmail()));
        table.addCell(createCell("Date:")).addCell(createCell(
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
        
        document.add(table);
        document.add(new Paragraph("\n"));
    }
    
    private void addImages(Document document, QCReport report) throws IOException {
        // Create a 3x2 table for images
        Table imageTable = new Table(2).useAllAvailableWidth();
        
        // Add images if they exist
        addImageToTable(imageTable, report.getFrontImage(), "Front View");
        addImageToTable(imageTable, report.getBackImage(), "Back View");
        addImageToTable(imageTable, report.getLeftImage(), "Left View");
        addImageToTable(imageTable, report.getRightImage(), "Right View");
        addImageToTable(imageTable, report.getTopImage(), "Top View");
        
        document.add(imageTable);
        document.add(new Paragraph("\n"));
    }
    
    private void addImageToTable(Table table, String imagePath, String caption) throws IOException {
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                ClassLoader classLoader = getClass().getClassLoader();
                URL imageUrl = classLoader.getResource(imagePath);
                if (imageUrl != null) {
                    Image img = new Image(ImageDataFactory.create(imageUrl));
                    img.setAutoScale(true);
                    img.setWidth(UnitValue.createPercentValue(90));
                    
                    Cell cell = new Cell().add(img)
                            .add(new Paragraph(caption).setTextAlignment(TextAlignment.CENTER));
                    table.addCell(cell);
                }
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
        document.add(new Paragraph("Notes:").setBold());
        document.add(new Paragraph(report.getNotes() != null ? report.getNotes() : "No notes provided"));
    }
    
    private void addFooter(Document document, QCReport report) {
        document.add(new Paragraph("\n"));
        Table footerTable = new Table(2).useAllAvailableWidth();
        footerTable.addCell(createCell("Generated on:"))
                .addCell(createCell(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
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