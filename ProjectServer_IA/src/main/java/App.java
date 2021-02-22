
import Presentation.MainView.ControllerIA;
import br.com.pict.authenticate.AutenticacaoDAO;
import br.com.pict.communication.ManageServer;
import br.com.pict.util.ConfigFactory;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App extends Application {
     
     public ControllerIA controllerIA;
     
     @Override
     public void start(Stage primaryStage) throws Exception {
          
          controllerIA = new ControllerIA();
          
          ManageServer ms = new ManageServer(controllerIA);
          ms.makeConnection();
          ms.shutDown();
     }
     
     public static void main(String[] args) {
          String location = "local";
          
          if (location.equals("local")) {
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
          if (!AutenticacaoDAO.versao().get(0).equals("2.1.0")) {
               System.out.println("Vesão do sistema desatualizada!");
               System.exit(0);
          }
          
          launch(args);
     }
}
