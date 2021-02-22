package br.com.pict.main;

import br.com.pict.controller.FaceDetectionController;
import br.com.pict.util.ConfigFactory;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.opencv.core.Core;

/**
 *
 * @author Iarley
 */
public class MainApplication extends Application{

    public static String location = "local";
    
    @Override
    public void start(Stage primaryStage) {
        try {
            // load the FXML resource
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/com/pict/view/FaceDetection.fxml"));
            Parent root = loader.load();
            root.setStyle("-fx-background-color: whitesmoke;");
            Scene scene = new Scene(root);
            primaryStage.setTitle("Autenticação");
            primaryStage.setScene(scene);
            primaryStage.show();

            FaceDetectionController controller = loader.getController();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) throws IOException {
        
        if(location.equals("local")){
            ConfigFactory.location = location;
        } else if (location.equals("remote")) {
            ConfigFactory.location = location;
        } else if (location.equals("remote_vps_2")) {
            ConfigFactory.location = location;
        } else {
            System.out.println("Localização incorreta");
            System.exit(0);
        }
        
        ConfigFactory.getProp();
        ConfigFactory.getConfig();
        
//        System.loadLibrary(Core.NATIVE_LIBRARY_NAME); // Use in case whith you prefere to use VM Options arguments.
        System.load(System.getProperty("user.dir")+"\\personalizado\\dlls_x64\\"+Core.NATIVE_LIBRARY_NAME+".dll");
        
        
        launch(args);
    }
}