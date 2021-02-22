package br.com.pict.util;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
import org.opencv.videoio.VideoCapture;

/**
 *
 * @author Iarley
 */
public class OpenCV {
    
    // a timer for acquiring the video stream
    private ScheduledExecutorService timer;
    // the OpenCV object that performs the video capture
    private VideoCapture capture;
    // a flag to change the button behavior
    public static boolean cameraActive;

    // face cascade classifier
    private CascadeClassifier faceCascade;
    private int absoluteFaceSize;
    
    public Mat auxFrame = null;
//    public static boolean possoVerificar = false;
    
    public OpenCV() {
        this.capture = new VideoCapture();
        this.faceCascade = new CascadeClassifier();
        this.absoluteFaceSize = 0;
    }

    public ScheduledExecutorService getTimer() {
        return timer;
    }

    public void setTimer(ScheduledExecutorService timer) {
        this.timer = timer;
    }

    public VideoCapture getCapture() {
        return capture;
    }

    public void setCapture(VideoCapture capture) {
        this.capture = capture;
    }

    public CascadeClassifier getFaceCascade() {
        return faceCascade;
    }

    public void setFaceCascade(CascadeClassifier faceCascade) {
        this.faceCascade = faceCascade;
    }

    public int getAbsoluteFaceSize() {
        return absoluteFaceSize;
    }

    public void setAbsoluteFaceSize(int absoluteFaceSize) {
        this.absoluteFaceSize = absoluteFaceSize;
    }
    
    /**
     * Get a frame from the opened video stream (if any)
     *
     * @return the {@link Image} to show
     */
    public Mat grabFrame() {
        Mat frame = new Mat();
        auxFrame = new Mat();
        
        // check if the capture is open
        if (this.capture.isOpened()) {
            try {
                // read the current frame
                this.capture.read(frame);
                this.capture.read(auxFrame);
                
                // if the frame is not empty, process it
                if (!frame.empty()) {
                    // face detection
                    this.detectAndDisplay(frame);
                }

            } catch (Exception e) {
                // log the (full) error
                System.err.println("Exception during the image elaboration: " + e);
            }
        }

        return frame;
    }

    /**
     * Method for face detection and tracking
     *
     * @param frame it looks for faces in this frame
     */
    public void detectAndDisplay(Mat frame) {
        MatOfRect faces = new MatOfRect();
        Mat grayFrame = new Mat();

        // convert the frame in gray scale
        Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);
        // equalize the frame histogram to improve the result
        Imgproc.equalizeHist(grayFrame, grayFrame);

        // compute minimum face size (20% of the frame height, in our case)
        if (this.absoluteFaceSize == 0) {
            int height = grayFrame.rows();
            if (Math.round(height * 0.2f) > 0) {
                this.absoluteFaceSize = Math.round(height * 0.2f);
            }
        }

        // detect faces
        this.faceCascade.detectMultiScale(grayFrame, faces, 1.05, 7, 0 | Objdetect.CASCADE_SCALE_IMAGE,
                new Size(this.absoluteFaceSize, this.absoluteFaceSize), new Size(244, 244));

        // each rectangle in faces is a face: draw them!
        Rect[] facesArray = faces.toArray();
        for (int i = 0; i < facesArray.length; i++) {
            Imgproc.rectangle(frame, facesArray[i].tl(), facesArray[i].br(), new Scalar(0, 255, 0), 3);
        }

    }
    
    public Mat detectFace(Mat frame){
        Mat faceImage = null;
        MatOfRect matFaceList = new MatOfRect();

        this.faceCascade.detectMultiScale(frame, matFaceList, 1.05, 7, 0 | Objdetect.CASCADE_SCALE_IMAGE,
                new Size(this.absoluteFaceSize, this.absoluteFaceSize), new Size());

        if (!matFaceList.empty()) {
            for (Rect faceRectangle : matFaceList.toArray()) {
                faceImage = frame.submat(faceRectangle);
            }
        }
        return faceImage;
    }
    
    /**
     * Stop the acquisition from the camera and release all the resources
     */
    public void stopAcquisition() {
        if (this.timer != null && !this.timer.isShutdown()) {
            try {
                // stop the timer
                this.timer.shutdown();
                this.timer.awaitTermination(33, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                // log any exception
                System.err.println("Exception in stopping the frame capture, trying to release the camera now... " + e);
            }
        }

        if (this.capture.isOpened()) {
            // release the camera
            this.capture.release();
        }
    }

    /**
     * Update the {@link ImageView} in the JavaFX main thread
     *
     * @param view the {@link ImageView} to update
     * @param image the {@link Image} to show
     */
    public void updateImageView(ImageView view, Image image) {
        Utils.onFXThread(view.imageProperty(), image);
    }

    /**
     * On application close, stop the acquisition from the camera
     */
    protected void setClosed() {
        this.stopAcquisition();
    }
}
