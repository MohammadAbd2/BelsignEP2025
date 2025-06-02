package Utils;

import javafx.scene.image.Image;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Base64;
import javafx.embed.swing.SwingFXUtils;

public class ImageHandler {
    private static final int MAX_IMAGE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final float COMPRESSION_QUALITY = 0.7f;

    public static String saveImageToDatabase(Image image) throws IOException {
        if (image == null) {
            throw new IllegalArgumentException("Image cannot be null");
        }

        // Convert JavaFX Image to BufferedImage
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);

        // Compress the image
        ByteArrayOutputStream compressedImageStream = new ByteArrayOutputStream();
        compressImage(bufferedImage, compressedImageStream);

        // Convert to Base64
        byte[] imageData = compressedImageStream.toByteArray();
        if (imageData.length > MAX_IMAGE_SIZE) {
            throw new IOException("Image size too large after compression");
        }

        return Base64.getEncoder().encodeToString(imageData);
    }

    public static Image loadImageFromDatabase(String base64String) throws IOException {
        if (base64String == null || base64String.isEmpty()) {
            throw new IllegalArgumentException("Base64 string cannot be null or empty");
        }

        byte[] imageData = Base64.getDecoder().decode(base64String);
        ByteArrayInputStream bis = new ByteArrayInputStream(imageData);
        BufferedImage bufferedImage = ImageIO.read(bis);

        return SwingFXUtils.toFXImage(bufferedImage, null);
    }

    private static void compressImage(BufferedImage image, OutputStream outputStream) throws IOException {
        ImageWriter jpgWriter = ImageIO.getImageWritersByFormatName("jpg").next();
        ImageWriteParam jpgWriteParam = jpgWriter.getDefaultWriteParam();
        jpgWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        jpgWriteParam.setCompressionQuality(COMPRESSION_QUALITY);

        try (ImageOutputStream output = ImageIO.createImageOutputStream(outputStream)) {
            jpgWriter.setOutput(output);
            jpgWriter.write(null, new IIOImage(image, null, null), jpgWriteParam);
        } finally {
            jpgWriter.dispose();
        }
    }

    public static boolean validateImage(Image image) {
        if (image == null) return false;

        // Check dimensions
        if (image.getWidth() <= 0 || image.getHeight() <= 0) return false;

        // Check if image is loaded
        if (image.isError()) return false;

        return true;
    }
}

