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
import org.opencv.core.MatOfInt;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

public class CameraCapture {

    private VideoCapture camera;
    private String picturePath;
    private Image capturedImage;
    private Mat capturedFrame;

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
                while (stage.isShowing()) {
                    if (camera.read(frame)) {
                        BufferedImage bufferedImage = matToBufferedImage(frame);
                        WritableImage fxImage = SwingFXUtils.toFXImage(bufferedImage, null);
                        Platform.runLater(() -> imageView.setImage(fxImage));
                    }
                }
                frame.release();
            });
            cameraThread.setDaemon(true);
            cameraThread.start();

            scene.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.SPACE) {
                    capturedFrame = new Mat();
                    if (camera.read(capturedFrame)) {
                        BufferedImage bufferedImage = matToBufferedImage(capturedFrame);
                        capturedImage = SwingFXUtils.toFXImage(bufferedImage, null);

                        System.out.println("✅ Image captured and saved.");
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
            File dir = new File("Img");
            if (!dir.exists()) dir.mkdirs();

            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String filename = "Img/picture_" + timestamp + ".jpeg";

            MatOfInt params = new MatOfInt(Imgcodecs.IMWRITE_JPEG_QUALITY, 95);
            boolean success = Imgcodecs.imwrite(filename, capturedFrame, params);

            if (success) {
                picturePath = filename;
                System.out.println("✅ Image saved to: " + picturePath);
            } else {
                System.err.println("❌ Failed to save image.");
            }
            return success;

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

        BufferedImage image;
        if (channels == 3) {
            image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        } else if (channels == 1) {
            image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        } else {
            throw new IllegalArgumentException("Unsupported number of channels: " + channels);
        }

        byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(sourcePixels, 0, targetPixels, 0, sourcePixels.length);
        return image;
    }

    private void release() {
        if (camera != null && camera.isOpened()) {
            camera.release();
        }
    }
}