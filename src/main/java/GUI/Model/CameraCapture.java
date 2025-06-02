package GUI.Model;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfInt;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

import static org.opencv.imgproc.Imgproc.COLOR_BGR2RGB;

public class CameraCapture {

    private VideoCapture camera;
    private String picturePath;
    private Image capturedImage;
    private Mat capturedFrame; // store for later save

    static {
        String opencvPath = new File("libs/opencv_java490.dll").getAbsolutePath();
        System.load(opencvPath);
    }

    public String getPicturePath() {
        return picturePath;
    }

    public Image getCapturedImage() {
        return capturedImage;
    }

    public void startCameraAndCapture() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            camera = new VideoCapture(0);

            if (!camera.isOpened()) {
                System.err.println("❌ Error: Cannot open camera.");
                latch.countDown();
                return;
            }

            ImageView imageView = new ImageView();
            StackPane root = new StackPane(imageView);
            Scene scene = new Scene(root, 640, 480);
            Stage stage = new Stage();
            stage.setTitle("Camera - Press SPACE to capture");
            stage.setScene(scene);
            stage.show();

            Thread cameraThread = new Thread(() -> {
                Mat frame = new Mat();
                Mat rgbFrame = new Mat();
                while (stage.isShowing()) {
                    if (camera.read(frame)) {
                        Imgproc.cvtColor(frame, rgbFrame, COLOR_BGR2RGB);
                        BufferedImage bufferedImage = matToBufferedImage(rgbFrame);
                        WritableImage fxImage = SwingFXUtils.toFXImage(bufferedImage, null);

                        Platform.runLater(() -> imageView.setImage(fxImage));
                    }
                }
                frame.release();
                rgbFrame.release();
            });
            cameraThread.setDaemon(true);
            cameraThread.start();

            scene.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.SPACE) {
                    capturedFrame = new Mat();
                    if (camera.read(capturedFrame)) {
                        Mat rgbCapturedFrame = new Mat();
                        Imgproc.cvtColor(capturedFrame, rgbCapturedFrame, COLOR_BGR2RGB);

                        BufferedImage bufferedImage = matToBufferedImage(rgbCapturedFrame);
                        capturedImage = SwingFXUtils.toFXImage(bufferedImage, null);

                        rgbCapturedFrame.release();
                        System.out.println("✅ Image captured. Call saveCapturedImage() to save.");
                    } else {
                        System.err.println("❌ Failed to capture frame.");
                    }
                    stage.close();
                    release();
                    latch.countDown();
                }
            });

            stage.setOnCloseRequest(e -> {
                release();
                latch.countDown();
            });
        });

        latch.await();
    }

    public boolean saveCapturedImage() {
        if (capturedFrame == null || capturedFrame.empty()) {
            System.err.println("❌ No image captured to save.");
            return false;
        }

        try {
            // Convert BGR to RGB format
            Mat rgbFrame = new Mat();
            Imgproc.cvtColor(capturedFrame, rgbFrame, Imgproc.COLOR_BGR2RGB);

            // Create parameters for JPEG encoding
            MatOfInt params = new MatOfInt();
            params.fromArray(Imgcodecs.IMWRITE_JPEG_QUALITY, 95);

            // Create a temporary file to store the image
            File tempFile = File.createTempFile("capture", ".jpg");
            tempFile.deleteOnExit();

            // Save the image to the temporary file
            Imgcodecs.imwrite(tempFile.getAbsolutePath(), rgbFrame);

            // Read the file into a byte array
            byte[] imageBytes = java.nio.file.Files.readAllBytes(tempFile.toPath());

            // Convert to Base64
            picturePath = Base64.getEncoder().encodeToString(imageBytes);

            // Clean up
            rgbFrame.release();
            params.release();
            tempFile.delete();

            System.out.println("✅ Image saved successfully");
            return true;
        } catch (Exception e) {
            System.err.println("❌ Error saving image: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private BufferedImage matToBufferedImage(Mat matrix) {
        int width = matrix.width();
        int height = matrix.height();
        int channels = matrix.channels();
        byte[] sourcePixels = new byte[width * height * channels];
        matrix.get(0, 0, sourcePixels);
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // Direct pixel manipulation for TYPE_INT_RGB
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int baseIndex = (y * width + x) * channels;
                int r = sourcePixels[baseIndex + 2] & 0xFF;  // R component
                int g = sourcePixels[baseIndex + 1] & 0xFF;  // G component
                int b = sourcePixels[baseIndex] & 0xFF;      // B component

                // Combine RGB components
                int rgb = (r << 16) | (g << 8) | b;
                image.setRGB(x, y, rgb);
            }
        }

        return image;
    }




    private void release() {
        if (camera != null && camera.isOpened()) {
            camera.release();
        }
    }
}