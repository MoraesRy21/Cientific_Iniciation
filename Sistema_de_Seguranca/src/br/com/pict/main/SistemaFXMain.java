package br.com.pict.main;

import br.com.pict.authenticate.AutenticacaoDAO;
import br.com.pict.util.ConfigFactory;
import java.io.IOException;
import java.rmi.RemoteException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.opencv.core.Core;

/**
 *
 * @author Iarley
 */
public class SistemaFXMain extends Application {

    public static Stage stageLogin;
    public static String location = "local";
    
    @Override
    public void start(Stage stage) throws IOException {
        System.out.println("Start!");
        Parent root = FXMLLoader.load(getClass().getResource("/br/com/pict/view/Login.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Login");
        stage.setScene(scene);
//        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
        stageLogin = stage;
        StackPane pane = new StackPane();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws RemoteException {
        
        if(location.equals("local") || location.equals("remote") || location.equals("remote_vps_2"))
            ConfigFactory.location = location;
        else{
            System.out.println("Localização incorreta");
            System.exit(0);
        }
        
        ConfigFactory.getProp();
        
        if (AutenticacaoDAO.versao().get(0).equals("2.1.0")) {
//            System.load(Core.NATIVE_LIBRARY_NAME); // Use in case whith you prefere to use VM Options arguments.
            System.load(System.getProperty("user.dir")+"\\personalizado\\dlls_x64\\"+Core.NATIVE_LIBRARY_NAME+".dll");
            launch(args);
        } else {
            System.out.println("Versão do sistema desatualizada!");
            System.exit(0);
        }
    }
}
