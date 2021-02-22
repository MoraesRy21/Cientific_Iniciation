package br.com.pict.controler;

import br.com.pict.dao.FacialDataDAO;
import br.com.pict.dao.LogsDAO;
import br.com.pict.dao.PessoaDAO;
import br.com.pict.model.FacialData;
import br.com.pict.model.Logs;
import br.com.pict.model.Pessoa;

import com.jfoenix.controls.JFXTextField;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author Iarley
 */
public class PerfisManageController implements Initializable {

    @FXML
    private TableColumn<Node, Node> columnCheck;
    @FXML
    private TableColumn<Pessoa, Integer> columnId;
    @FXML
    private TableColumn<Pessoa, String> columnNome;
    @FXML
    private TableColumn<Pessoa, String> columnCPF;
    @FXML
    private TableColumn<Pessoa, String> columnRG;
    @FXML
    private TableColumn<Pessoa, String> columnDataN;
    @FXML
    private TableView<Pessoa> tableView;
    @FXML
    private JFXTextField txtFProcurarNome;
    @FXML
    private JFXTextField txtFProcurarCPF;
    @FXML
    private JFXTextField txtFProcurarRG;
    @FXML
    private JFXTextField txtFProcurarDtN;
    @FXML
    private CheckBox checkBoxAll;
    @FXML
    private AnchorPane perfisManage;
    
//  ======================= Serviços ==================================
    private PessoaDAO pessoaDAO = new PessoaDAO();
    private FacialDataDAO facialDataDAO = new FacialDataDAO();
    private LogsDAO logsDAO = new LogsDAO();
    
    private List<Pessoa> listaPessoa;
    private ObservableList<Pessoa> obsList;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY");

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("initalize - PerfisManage");
        txtFProcurarNome.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
//                listaPessoa = pessoaService.findId(txtFProcurar.getText());
            }
        });
        loadTableView();
    }

    private void loadTableView() {

        listaPessoa = pessoaDAO.findAll();

        columnCheck.setCellValueFactory(new PropertyValueFactory<>("checkBox"));
        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnNome.setCellValueFactory(new Callback<CellDataFeatures<Pessoa, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(CellDataFeatures<Pessoa, String> data) {
                return new ReadOnlyStringWrapper(data.getValue().getNomeCompleto());
            }
        });
        columnCPF.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        columnRG.setCellValueFactory(new PropertyValueFactory<>("rg"));
        columnDataN.setCellValueFactory(new Callback<CellDataFeatures<Pessoa, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(CellDataFeatures<Pessoa, String> data) {
                return new ReadOnlyStringWrapper(dateFormat.format(data.getValue().getNascimento()));
            }
        });

        obsList = FXCollections.observableArrayList(listaPessoa);
        tableView.setItems(obsList);
    }

    @FXML
    private void onClick$btnAtualizar() {
        limparFiltros();
        loadTableView();
    }

    @FXML
    private void onClick$btnCadastrar(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/br/com/pict/view/Cadastro.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setTitle("Cadastrar");
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void onClick$btnRemover(ActionEvent event) {
        ObservableList<Pessoa> obsListRemove = FXCollections.observableArrayList();
        List<Logs> logs = logsDAO.findAll();
        FacialData facialData = new FacialData();
        
        for (Pessoa bean : obsList) {
            if (bean.getCheckBox().isSelected()) {
                if (pessoaDAO.hasLog(bean)) {
                    JOptionPane.showMessageDialog(null, "Este usuário já é ativo!");
                } else {
                    facialData.setPessoa(bean);
                    
                    facialDataDAO.delete(facialData);
                    
                    pessoaDAO.delete(bean);
                    obsListRemove.add(bean);
                }
            }
        }
        obsList.removeAll(obsListRemove);
    }


    private void limparFiltros() {
        txtFProcurarNome.setText(null);
        txtFProcurarCPF.setText(null);
        txtFProcurarRG.setText(null);
        txtFProcurarDtN.setText(null);
    }
    
    @FXML
    private void onOk$Filter() throws ParseException{
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        
        String nome = txtFProcurarNome.getText();
        String cpf = txtFProcurarCPF.getText(); 
        String rg = txtFProcurarRG.getText();
        Date dataNacimento = null;
        if(txtFProcurarDtN.getText() != null || !txtFProcurarDtN.getText().equals(""))
            dataNacimento = format.parse(txtFProcurarDtN.getText());
        
        listaPessoa = pessoaDAO.filter(nome, cpf, rg, dataNacimento);
        
        columnCheck.setCellValueFactory(new PropertyValueFactory<>("checkBox"));
        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        columnDataN.setCellValueFactory(new PropertyValueFactory<>("nascimento"));
        columnCPF.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        columnRG.setCellValueFactory(new PropertyValueFactory<>("rg"));
        
        obsList = FXCollections.observableArrayList(listaPessoa);
        tableView.setItems(obsList);
    }
    
    @FXML
    private void onAction$checkBoxAll(ActionEvent event) {
        for (Pessoa bean : obsList) {
            if(checkBoxAll.isSelected())
                bean.getCheckBox().setSelected(true);
            else
                bean.getCheckBox().setSelected(false);
        }
    }
}
