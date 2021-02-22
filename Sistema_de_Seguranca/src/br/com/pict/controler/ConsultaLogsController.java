package br.com.pict.controler;

import br.com.pict.dao.LogsDAO;
import br.com.pict.model.Logs;

import com.jfoenix.controls.JFXTextField;

import java.net.URL;
import java.sql.Date;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author Iarley
 */
public class ConsultaLogsController implements Initializable {
    
    private LogsDAO logsDAO = new LogsDAO();
    private List<Logs> listaLogs;
    private ObservableList<Logs> obsList;

    @FXML
    private JFXTextField txtFProcurarNome;
    @FXML
    private JFXTextField txtFProcurarCPF;
    @FXML
    private JFXTextField txtFProcurarRG;
    @FXML
    private TableView<Logs> tableView;
    @FXML
    private TableColumn<Logs, Integer> columnId;
    @FXML
    private TableColumn<Logs, String> columnNome;
    @FXML
    private TableColumn<Logs, String> columnCPF;
    @FXML
    private TableColumn<Logs, String> columnRG;
    @FXML
    private TableColumn<Logs, Date> columnDataEntrada;
    @FXML
    private TableColumn<Logs, Date> columnDataSaida;
    @FXML
    private AnchorPane consultaLogs;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadTableView();
    }
    
    private void loadTableView(){
        
        listaLogs =  logsDAO.findAll();
        
        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnDataEntrada.setCellValueFactory(new PropertyValueFactory<>("dataEntrada"));
        columnDataSaida.setCellValueFactory(new PropertyValueFactory<>("dataSaida"));
        columnNome.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Logs, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Logs, String> data) {
                return new ReadOnlyStringWrapper(data.getValue().getPessoa().getNome());
            }
        });
        columnCPF.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Logs, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Logs, String> data) {
                return new ReadOnlyStringWrapper(data.getValue().getPessoa().getCpf());
            }
        });
        columnRG.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Logs, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Logs, String> data) {
                return new ReadOnlyStringWrapper(data.getValue().getPessoa().getRg());
            }
        });
        
        obsList = FXCollections.observableArrayList(listaLogs);
        tableView.setItems(obsList);
    }
    
    private void limparFiltros() {
        txtFProcurarNome.setText(null);
        txtFProcurarCPF.setText(null);
        txtFProcurarRG.setText(null);
    }

    @FXML
    private void onClick$btnAtualizar(ActionEvent event) {
        limparFiltros();
        loadTableView();
    }

    @FXML
    private void onClick$btnSair(ActionEvent event) {
        Stage stage = (Stage) consultaLogs.getScene().getWindow();
        stage.close();
    }


    @FXML
    private void onOk$Filter(ActionEvent event) {
        String nome = txtFProcurarNome.getText();
        String cpf = txtFProcurarCPF.getText(); 
        String rg = txtFProcurarRG.getText();
        
        listaLogs = logsDAO.filter(nome, cpf, rg);
        
        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnDataEntrada.setCellValueFactory(new PropertyValueFactory<>("dataEntrada"));
        columnDataSaida.setCellValueFactory(new PropertyValueFactory<>("dataSaida"));
        columnNome.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Logs, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Logs, String> data) {
                return new ReadOnlyStringWrapper(data.getValue().getPessoa().getNome());
            }
        });
        columnCPF.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Logs, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Logs, String> data) {
                return new ReadOnlyStringWrapper(data.getValue().getPessoa().getCpf());
            }
        });
        columnRG.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Logs, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Logs, String> data) {
                return new ReadOnlyStringWrapper(data.getValue().getPessoa().getRg());
            }
        });
        
        obsList = FXCollections.observableArrayList(listaLogs);
        tableView.setItems(obsList);
    }
    
}
