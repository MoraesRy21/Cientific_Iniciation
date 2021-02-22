//============================================================================
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.pict.authenticate;

import br.com.pict.main.SistemaFXMain;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javax.swing.JOptionPane;
import java.io.IOException;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Iarley
 */
public class LoginController implements Initializable {

    @FXML
    private AnchorPane rootAnchorPane;
    @FXML
    private JFXButton buttonLogin;
    @FXML
    private JFXTextField textFildUser;
    @FXML
    private JFXPasswordField textFieldPass;

    public static Stage stageMenu;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    public void onOK$textFieldPass(Event event) throws IOException{
        onMouseClicked$buttonLogin(null);
    }
    
    @FXML
    public void onMouseClicked$buttonLogin(MouseEvent event) throws IOException {
        Login loginUI = new Login();
        List<Login> loginDAO = null;

        loginUI.setUsuario(textFildUser.getText());
        loginUI.setSenha(textFieldPass.getText());

        loginDAO = LoginDAO.authenticationLogin(loginUI);

        if(!loginDAO.isEmpty() && loginDAO.get(0) != null){
            if (loginDAO.get(0).getUsuario().equals(loginUI.getUsuario()) && loginDAO.get(0).getSenha().equals(loginUI.getSenha())) {
                if (stageMenu == null) {
                    closeWindow();
                    openMenuWindow();
                } else {
                    closeWindow();
                    stageMenu.show();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Usuário ou senha incorretos!");
            }
        }else{
            JOptionPane.showMessageDialog(null, "Usuário ou senha não cadastrados na base!");
        }
    }

    public void closeWindow() {
        SistemaFXMain.stageLogin.close();
    }

    public void openMenuWindow() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/br/com/pict/view/Menu.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setTitle("Menu");
        stage.setScene(scene);
        stage.show();
        stageMenu = stage;
    }
}
