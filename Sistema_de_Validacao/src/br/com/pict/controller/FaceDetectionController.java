package br.com.pict.controller;

import br.com.pict.camera.Utils;
import br.com.pict.dao.ComposeDAO;
import br.com.pict.model.Compose;
import br.com.pict.util.CropFaceTask;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;

import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

/**
 * The controller associated with the only view of our application. The
 * application logic is implemented here. It handles the button for
 * starting/stopping the camera, the acquired video stream, the relative
 * controls and the face detection/tracking.
 *
 * @author <a href="mailto:luigi.derussis@polito.it">Luigi De Russis</a>
 * @version 1.1 (2015-11-10)
 * @since 1.0 (2014-01-10)
 *
 */
public class FaceDetectionController implements Initializable {

    
    private VideoCapture capture; // the OpenCV object that performs the video capture
    private boolean cameraActive; // a flag to change the button behavior
    private ScheduledExecutorService timer; // a timer for acquiring the video stream
    private CascadeClassifier faceCascade; // face cascade classifier
    private int absoluteFaceSize;

    @FXML
    private Button cameraButton;
    @FXML
    private ImageView originalFrame; // the FXML area for showing the current frame
    @FXML
    private ImageView imageView;
    @FXML
    private Label labelNome;
    @FXML
    private Label labelCPF;

    private static ComposeDAO composeDAO = new ComposeDAO();
    
    public static ExecutorService executorServiceCallable;
    public ExecutorService crop;
    private Timer counterTimer;
//    public ScheduledExecutorService cropTimer;
    
    
    
    private static Mat face;
    
    public static synchronized Mat getFace(){
        return face;
    }
    public static synchronized void setFace(Mat frame){
        FaceDetectionController.face = frame.clone();
    }
    
    
    
    private static boolean possoVerificar = false;
    
    public static synchronized boolean getPossoVerificar(){
        return possoVerificar;
    }
    public static synchronized void setPossoVerificar(boolean possoVerificar){
        FaceDetectionController.possoVerificar = possoVerificar;
    }
    
    
    
    /**
     * Init the controller, at start time
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.capture = new VideoCapture();
        this.faceCascade = new CascadeClassifier();
        this.absoluteFaceSize = 0;

        String lbpcascadeClassifier = "resources/lbpcascades/lbpcascade_frontalface.xml";
        String haarcascadeClassifier = "resources/haarcascades/haarcascade_frontalface_alt.xml";
        slectClassifier(haarcascadeClassifier);

        originalFrame.setFitWidth(600); // set a fixed width for the frame
        originalFrame.setPreserveRatio(true); // preserve image ratio
        imageView.setPreserveRatio(true); // preserve image ratio
    }

    /**
     * The action triggered by pushing the button on the GUI
     */
    @FXML
    protected void startCamera() {
        if (!this.cameraActive) {

            // start the video capture
            this.capture.open(0);

            // is the video stream available?
            if (this.capture.isOpened()) {
                this.cameraActive = true;

                // grab a frame every 33 ms (30 frames/sec)
                Runnable frameGrabber = new Runnable() {

                    @Override
                    public void run() {
                        // effectively grab and process a single frame
                        Mat frame = grabFrame();
                        // convert and show the frame
                        Image imageToShow = Utils.mat2Image(frame);
                        updateImageView(originalFrame, imageToShow);
                    }
                };
                
                TimerTask task = new TimerTask() {

                    @Override
                    public void run() {
                        setPossoVerificar(true);
                    }
                };
                
                counterTimer = new Timer(true);
                counterTimer.scheduleAtFixedRate(task, 5000, 10000);
                
                this.timer = Executors.newSingleThreadScheduledExecutor();
//                this.cropTimer = Executors.newSingleThreadScheduledExecutor();
                this.crop = Executors.newSingleThreadExecutor();
                this.executorServiceCallable = Executors.newCachedThreadPool();
                
                timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);
//                cropTimer.scheduleAtFixedRate(new CropFaceTask(), 2, 3, TimeUnit.SECONDS);
                
                

                // update the button content
                this.cameraButton.setText("Parar Sistema");
            } else {
                // log the error
                System.err.println("Failed to open the camera connection...");
            }
        } else {
            this.cameraActive = false; // the camera is not active at this point
            this.cameraButton.setText("Iniciar Sistema"); // update again the button content

            // stop the timer
            this.stopAcquisition();
        }
    }

    /**
     * Get a frame from the opened video stream (if any)
     *
     * @return the {@link Image} to show
     */
    private Mat grabFrame() {
        Mat frame = new Mat();

        // check if the capture is open
        if (this.capture.isOpened()) {
            try {
                // read the current frame
                this.capture.read(frame);

                // if the frame is not empty, process it
                if (!frame.empty()) {
                    // face detection
                    this.detectAndDisplay(frame);
                } else {
                    System.out.println("frame null");
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
    private void detectAndDisplay(Mat frame) {
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
                new Size(this.absoluteFaceSize, this.absoluteFaceSize), new Size(224, 224));

        if (possoVerificar && !faces.empty()) {
            Mat cloneFrame = frame.clone();
            setPossoVerificar(false);
            System.out.println("Podendo Verificar !");
            crop.execute(new CropFaceTask(detectFace(cloneFrame), this));
        }

        // each rectangle in faces is a face: draw them!
        Rect[] facesArray = faces.toArray();
        if (facesArray.length > 1) {
            System.out.println("facesArray = " + facesArray.length);
        }

        for (int i = 0; i < facesArray.length; i++) {
            Imgproc.rectangle(frame, facesArray[i].tl(), facesArray[i].br(), new Scalar(0, 255, 0), 3);
        }

    }
    
    /**
     * Pega o mesmo frame capturado pela camera e 
     * passa ele para extrair o rosto
     * @param frame
     * @return Mat
     */
    public synchronized Mat detectFace(Mat frame) {
        Mat faceImage = null;
        MatOfRect matFaceList = new MatOfRect();

        this.faceCascade.detectMultiScale(frame, matFaceList, 1.05, 7, 0 | Objdetect.CASCADE_SCALE_IMAGE,
                new Size(this.absoluteFaceSize, this.absoluteFaceSize), new Size());

        if (matFaceList.empty())
            System.out.println("Rosto na detecção vazio");
        else
            System.out.println("Rosto na detecção preenchido");
            
        if (!matFaceList.empty()) {
            for (Rect faceRectangle : matFaceList.toArray()) {
                faceImage = frame.submat(faceRectangle);
            }
        }
        return faceImage;
    }

    /**
     * Method for loading a classifier trained set from disk
     *
     * @param classifierPath the path on disk where a classifier trained set is
     * located
     */
    private void slectClassifier(String classifierPath) {
        // load the classifier(s)
        this.faceCascade.load(classifierPath);
    }

    /**
     * Stop the acquisition from the camera and release all the resources
     */
    private void stopAcquisition() {
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
        
        if(this.crop != null && !this.crop.isShutdown()){
            try{
                this.crop.shutdown();
                this.crop.awaitTermination(2, TimeUnit.SECONDS);
            } catch (InterruptedException ex) {
                System.err.println("Exception in stopping the crop timer, trying to release the task now... " + ex);
            }
        }
        
        if(this.executorServiceCallable != null && !this.executorServiceCallable.isShutdown()){
            try{
                this.executorServiceCallable.shutdown();
                this.executorServiceCallable.awaitTermination(2, TimeUnit.SECONDS);
            } catch (InterruptedException ex) {
                System.err.println("Exception in stopping the crop future, " + ex);
            }
        }
        
        if(this.counterTimer != null){
            counterTimer.cancel();
            setPossoVerificar(false);
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
    private void updateImageView(ImageView view, Image image) {
        Utils.onFXThread(view.imageProperty(), image);
    }

    /**
     * On application close, stop the acquisition from the camera
     */
    protected void setClosed() {
        this.stopAcquisition();
    }
    
    public void auxRender(Integer id){
        renderlabel(id);
    }
    
    /**
     * 
     * @param id 
     */
    public void renderlabel(Integer id) {
        if(id != -1){
            System.out.println("If");
            Compose comp = composeDAO.findById(id);
            if(comp != null){
                try {
                    BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(comp.getArquivo()));
                    Utils.onFXThread(imageView.imageProperty(), SwingFXUtils.toFXImage(bufferedImage, null));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                labelNome.setText(comp.getNomeCompleto());
                labelCPF.setText(comp.getCpf());
            }
        }else{
            System.out.println("Else");
            labelNome.setText("Pessoa não Indetificada !");
            labelCPF.setText("Não Indetificado");
        }
    }

}
