package br.com.pict.util;

import br.com.pict.camera.Utils;
import br.com.pict.communication.ManageSocket;
import br.com.pict.controller.FaceDetectionController;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import org.opencv.core.Mat;

/**
 * Routine that make the face crop when face is detected.
 *
 * @author Iarley
 */
public class CropFaceTask implements Runnable {

    private Mat frame;
    private FaceDetectionController fdc;
    private int id;

    public CropFaceTask() {

    }

    public CropFaceTask(Mat frame) {
        this.frame = frame;
    }
    
    public CropFaceTask(Mat frame, FaceDetectionController fdc) {
        this.frame = frame;
        this.fdc = fdc;
    }

    @Override
    public void run() {
        try {
            if (frame != null && !FaceDetectionController.getPossoVerificar()) {
                byte[] imageByteArray = Utils.matToArrayBytes(frame);
                Callable<Integer> callable = new ManageSocket(imageByteArray);
                
                Future<Integer> future = FaceDetectionController.executorServiceCallable.submit(callable);
                Integer id = future.get(); //Bloequeante

                System.out.println("Id no crop Task = " + id);
                this.id = id;
                Platform.runLater(new Runnable(){
                    @Override
                    public void run() {
                        fdc.renderlabel(getId());
                    }
                });
            }
        } catch (ExecutionException | InterruptedException | IOException ex) {
            ex.printStackTrace();
            Logger.getLogger(CropFaceTask.class.getName()).log(Level.SEVERE, null, ex);
            if (ex instanceof ExecutionException) {
                FaceDetectionController.executorServiceCallable.shutdown();
            }
        }
    }

    private int getId(){
        return this.id;
    }
}
