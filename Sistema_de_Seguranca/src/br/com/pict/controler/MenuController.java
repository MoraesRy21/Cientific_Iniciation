package br.com.pict.controler;

import br.com.pict.authenticate.LoginController;
import br.com.pict.main.SistemaFXMain;
import br.com.pict.util.OpenCV;
import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author Iarley
 */
public class MenuController implements Initializable {

    @FXML
    private AnchorPane menu;
    @FXML
    private HBox hboxRelatorios;
    @FXML
    private Button btnLogout;
    @FXML
    private HBox hboxCadastrar;
    @FXML
    private AnchorPane AnchorPanePerfis;
    @FXML
    private JFXButton btnSair;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    private void onClick$hboxCadastrar(MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/br/com/pict/view/Cadastro.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setTitle("Cadastrar");
            stage.setScene(scene);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void onClick$btnLogout(ActionEvent event) {
        if (OpenCV.cameraActive) {
            JOptionPane.showMessageDialog(null, "Sistema não foi desilgado!");
        } else {
            LoginController.stageMenu.close();
            SistemaFXMain.stageLogin.show();
        }
    }

    @FXML
    private void onClick$hboxRelatorios(MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/br/com/pict/view/ConsultaLogs.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setTitle("Cosultas");
            stage.setScene(scene);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void onClick$btnSair() {
        Stage stage = (Stage) menu.getScene().getWindow();
        stage.close();
    }
}
