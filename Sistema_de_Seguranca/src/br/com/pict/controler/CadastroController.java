package br.com.pict.controler;

import br.com.pict.communicaction.ManageSocket;
import br.com.pict.dao.FacialDataDAO;
import br.com.pict.dao.PessoaDAO;
import br.com.pict.model.FacialData;
import br.com.pict.model.Pessoa;

import br.com.pict.util.OpenCV;
import br.com.pict.util.Utils;
import br.com.pict.util.ValidaCPF;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import org.opencv.core.Mat;

/**
 * FXML Controller class
 *
 * @author Iarley
 */
public class CadastroController implements Initializable {

    public MenuController menuController;

    @FXML
    private AnchorPane anchorPDocumental;
    @FXML
    private Button btnTirarFoto;
    @FXML
    private JFXTextField txtFNome;
    @FXML
    private ImageView imageVCamera;
    @FXML
    private JFXButton btnSalvar;
    @FXML
    private JFXTextField txtFTelefone;
    @FXML
    private JFXRadioButton radioBFacial;
    @FXML
    private JFXTextField txtFRg;
    @FXML
    private JFXTextField txtFSobrenome;
    @FXML
    private JFXTextField txtFEmail;
    @FXML
    private JFXTextField txtFCpf;
    @FXML
    private JFXRadioButton radioBDocumental;
    @FXML
    private AnchorPane anchorPFacial;
    @FXML
    private Button btnIniciarCamera;
    @FXML
    private DatePicker datePDataNasc;
    @FXML
    private AnchorPane cadastro;
    @FXML
    private CheckBox haarClassifier;
    @FXML
    private CheckBox lbpClassifier;
    @FXML
    private Button btnAceitarFoto;
    @FXML
    private Label labelCount;
    @FXML
    private ImageView imageVFotoStack;
    @FXML
    private JFXTextField txtFMatricula;
    @FXML
    private JFXComboBox<String> comboBCurso;
    @FXML
    private JFXTextField txtFAnoMatricula;
    @FXML
    private JFXTextField txtFAnoSaida;
    @FXML
    private JFXCheckBox checkBAluno;
    @FXML
    private JFXTextField txtFCodigo;
    @FXML
    private DatePicker datePDataAdmissao;
    @FXML
    private DatePicker datePDataDeslig;
    @FXML
    private JFXCheckBox checkBFuncionario;
    @FXML
    private JFXButton btnCancelarCadastro;
    @FXML
    private ImageView backArrow;
    @FXML
    private ImageView fowardArrow;
    @FXML
    private StackPane stackPaneManager;
    @FXML
    private Pane paneFacial;
    @FXML
    private Pane paneDocumental;

//  ========== Serviços ===============================
    private PessoaDAO pessoaDAO = new PessoaDAO();
    private FacialDataDAO facialDataDAO = new FacialDataDAO();

    private int paigeControl = 0;
    private int listsFaceEqualsSize;

    private Pessoa pessoa;
    private List<FacialData> listFacialData = new ArrayList<FacialData>();
    
    private ExecutorService executorService = Executors.newFixedThreadPool(1);

    /**
     * Injeção do controller do Cadastro para funcionar as abas filhas.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        radioBDocumental.setSelectedColor(Color.web("#0000ff"));
        radioBFacial.setSelectedColor(Color.web("#0000ff"));
        btnSalvar.setVisible(false);
        inicializarCursos();
        // preserve image ratio
        imageVCamera.setPreserveRatio(true);
        // set a fixed width for the frame
        imageVCamera.setFitWidth(715);
    }

//  ================================= Auxiliar Controller =======================================
    private boolean isValidFormDocumental() {
        boolean status = true;
        if (txtFNome == null || txtFNome.getText().equals("") || txtFSobrenome == null || txtFSobrenome.getText().equals("")
                || txtFCpf == null || txtFCpf.getText().equals("") || datePDataNasc.getValue() == null) {
            estadoCadastramento(1, 0);
            return false;
        }

        if (!ValidaCPF.isCPF(txtFCpf.getText())) {
            estadoCadastramento(1, 0);
            return false;
        }
        
        if(checkBAluno.isSelected()){
            if(txtFMatricula == null || txtFMatricula.getText().equals("") || comboBCurso.getValue() == null 
                    || (txtFAnoMatricula == null && txtFAnoMatricula .getText().matches("20\\d{2}")) 
                    ||(txtFAnoSaida == null && txtFAnoSaida.getText().matches("20\\d{2}")) ){
                estadoCadastramento(0, 1);
                return false;
            }
        }
        
        if(checkBFuncionario.isSelected()){
            if(txtFCodigo == null || txtFCodigo.getText().equals("") || 
                    datePDataAdmissao.getValue() == null || datePDataDeslig.getValue() == null ) {
                estadoCadastramento(0, 1);
                return false;
            }
        }
        
        estadoCadastramento(1, 2);
        radioBFacial.setSelected(status);
        return status;
    }

    private boolean isValidFormFacial() {
        boolean status = true;
        if (listFacialData.size() < 2) {
            estadoCadastramento(2, 0); // Erro
            return false;
        }
        
        estadoCadastramento(2, 2);
        listsFaceEqualsSize = listFacialData.size();
        return status;
    }

    /**
     * Define o estado das fases cadastrais
     *
     * @param estado 0: Vermelho, 1: Azul, 2: Verde
     * @param fase 1: Documental, 2: Facial, 3: Biometrico
     */
    private void estadoCadastramento(int fase, int estado) {
        if (fase == 1) {
            switch (estado) {
                case 0:
                    radioBDocumental.setSelectedColor(Color.web("#ff0000")); //Vermelho
                    break;
                case 1:
                    radioBDocumental.setSelectedColor(Color.web("#0000ff"));// Azul
                    break;
                case 2:
                    radioBDocumental.setSelectedColor(Color.web("#00ff00"));// Verde
                    break;
            }
        } else if (fase == 2) {
            switch (estado) {
                case 0:
                    radioBFacial.setSelectedColor(Color.web("#ff0000"));
                    break;
                case 1:
                    radioBFacial.setSelectedColor(Color.web("#0000ff"));
                    break;
                case 2:
                    radioBFacial.setSelectedColor(Color.web("#00ff00"));
                    break;
            }
        }
    }

    private void documentalBinder() {
        Pessoa pessoa = new Pessoa();

        pessoa.setNome(txtFNome.getText());
        pessoa.setSobrenome(txtFSobrenome.getText());
        pessoa.setCpf(txtFCpf.getText());
        ZoneId defaultZoneId = ZoneId.systemDefault();
        pessoa.setNascimento(Date.from(datePDataNasc.getValue().atStartOfDay(defaultZoneId).toInstant()));

        if (!txtFEmail.getText().equals("") && txtFEmail != null) {
            pessoa.setEmail(txtFEmail.getText());
        }
        if (!txtFTelefone.getText().equals("") && txtFTelefone != null) {
            pessoa.setTelefone(txtFTelefone.getText());
        }
        if (!txtFRg.getText().equals("") && txtFRg != null) {
            pessoa.setRg(txtFRg.getText());
        }
        
        if(checkBAluno.isSelected()){
            pessoa.setAlunoMatricula(txtFMatricula.getText());
            pessoa.setAlunoCurso((String) comboBCurso.getValue());
            pessoa.setAlunoAnoMatricula(Integer.parseInt(txtFAnoMatricula.getText()));
            pessoa.setAlunoAnoSaida(Integer.parseInt(txtFAnoSaida.getText()));
        }else{
            pessoa.setAlunoMatricula(null);
            pessoa.setAlunoCurso(null);
            pessoa.setAlunoAnoMatricula(null);
            pessoa.setAlunoAnoSaida(null);
        }
        
        if(checkBFuncionario.isSelected()){
            pessoa.setFuncionarioCodigo(txtFCodigo.getText());
            try {
                pessoa.setFuncionarioDataAdmissao(new SimpleDateFormat("yyyy-MM-dd").parse(datePDataAdmissao.getValue().toString()));
                pessoa.setFuncionarioDataDemissao(new SimpleDateFormat("yyyy-MM-dd").parse(datePDataDeslig.getValue().toString()));
            } catch (ParseException ex) {
                Logger.getLogger(CadastroController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            pessoa.setFuncionarioCodigo(null);
            pessoa.setFuncionarioDataAdmissao(null);
            pessoa.setFuncionarioDataDemissao(null);
        }
        this.pessoa = pessoa;
    }

    private void facialBinder() {
        for (int i = 0; i < listsFaceEqualsSize; i++) {
            listFacialData.get(i).setPessoa(pessoa);
        }
    }

    private void limparController() {
        pessoa = null;
        facialData = null;
        listFacialData = null;
        listsFaceEqualsSize = 0;
        onClick$backArrow();
    }
    
    private void inicializarCursos(){
        List<String> listaCursos =  new ArrayList<>();
        listaCursos.add("Engenharia da Computação");
        listaCursos.add("Engenharia Civil");
        listaCursos.add("Engenharia de Produção");
        listaCursos.add("Engenharia Elétrica");
        listaCursos.add("Engenharia de Automação e Controle");
        listaCursos.add("Engenharia Química");
        listaCursos.add("Engenharia Ambiental");
        listaCursos.add("Engenharia Ambiental e Sanitária");
        ObservableList<String> obsList;
        obsList = FXCollections.observableArrayList(listaCursos);
        
        comboBCurso.setItems(obsList);
    }
    
    private void setDisableAluno(boolean condicao){
        if (condicao){
            txtFMatricula.setDisable(false);
            comboBCurso.setDisable(false);
            txtFAnoMatricula.setDisable(false);
            txtFAnoSaida.setDisable(false);
        }else{
            txtFMatricula.setDisable(true);
            comboBCurso.setDisable(true);
            txtFAnoMatricula.setDisable(true);
            txtFAnoSaida.setDisable(true);
        }
    }
    
    private void setDisableFuncionario(boolean condicao){
        if (condicao){
            txtFCodigo.setDisable(false);
            datePDataAdmissao.setDisable(false);
            datePDataDeslig.setDisable(false);
        }else{
            txtFCodigo.setDisable(true);
            datePDataAdmissao.setDisable(true);
            datePDataDeslig.setDisable(true);
        }
    }

    private void closeCadastro() {
        Stage stage = (Stage) cadastro.getScene().getWindow();
        stage.close();
    }
//  ============================= Action Controllers @FXML ======================================    

    @FXML
    private void onClick$fowardArrow() {
        if (paigeControl == 0) {
            if (isValidFormDocumental()) {
                anchorPDocumental.setVisible(false);
                anchorPFacial.setVisible(true);
                btnSalvar.setVisible(true);
                paigeControl++;
            }
        }
    }

    @FXML
    private void onClick$backArrow() {
        if (paigeControl == 1) {
            estadoCadastramento(1, 1);
            anchorPDocumental.setVisible(true);
            anchorPFacial.setVisible(false);
            btnSalvar.setVisible(false);
            radioBFacial.setSelected(false);
            paigeControl--;
        }
    }
    
    @FXML
    private void onChecked$checkBAluno(){
        System.out.println("Passei Aluno");
        if(checkBAluno.isIndeterminate() || checkBAluno.isSelected()){
            setDisableAluno(true);
        }else{
            setDisableAluno(false);
        }
    }
    
    @FXML
    private void onChecked$checkBFuncionario(){
        System.out.println("Passei Funcionario");
        if(checkBFuncionario.isSelected()){
            setDisableFuncionario(true);
        }else{
            setDisableFuncionario(false);
        }
    }

    @FXML
    private void onClick$btnSalvar() {
        if (isValidFormFacial()) {
            try{
                
            documentalBinder();
            if (pessoa != null)
                pessoaDAO.create(pessoa);

            facialBinder();
            for (int i = 0; i < listsFaceEqualsSize; i++) {
                FacialData getFacialData = listFacialData.get(i);
                facialDataDAO.create(getFacialData);
            }
            
            // Send a message to server to start traning
            executorService.execute(new ManageSocket());
            executorService.shutdown();
            
            limparController();

            JOptionPane.showMessageDialog(null, "Cadastro realizado com sucesso!");
            closeCadastro();
            }catch(Exception ex){
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Problema ao cadastrar cadastrar pessoa!");
            }
        }
    }

    @FXML
    private void onClick$btnCancelarCadastro() {
        limparController();
        closeCadastro();
    }

//  =============================== OpenCV =============================================
    /**
     * O código foi dividido para melhor utilização do código
     */
    private OpenCV openCV = new OpenCV();
    private Mat pickFrame = null;

    /**
     * The action triggered by pushing the button on the GUI
     */
    @FXML
    protected void startCamera() {
        if (!OpenCV.cameraActive) {
            // disable setting checkboxes
            this.haarClassifier.setDisable(true);
            this.lbpClassifier.setDisable(true);
            this.btnTirarFoto.setDisable(false);

            // start the video capture
            openCV.getCapture().open(0);

            // is the video stream available?
            if (openCV.getCapture().isOpened()) {
                OpenCV.cameraActive = true;

                // grab a frame every 33 ms (30 frames/sec)
                Runnable frameGrabber = new Runnable() {

                    @Override
                    public void run() {
                        // effectively grab and process a single frame
                        Mat frame = openCV.grabFrame();
                        // convert and show the frame
                        Image imageToShow = Utils.mat2Image(frame);
                        openCV.updateImageView(imageVCamera, imageToShow);
                    }
                };

                openCV.setTimer(Executors.newSingleThreadScheduledExecutor());
                openCV.getTimer().scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);

                // update the button content
                this.btnIniciarCamera.setText("Stop Camera");
            } else {
                // log the error
                System.err.println("Failed to open the camera connection...");
            }
        } else {
            // the camera is not active at this point
            OpenCV.cameraActive = false;
            // update again the button content
            this.btnIniciarCamera.setText("Start Camera");
            // enable classifiers checkboxes
            this.haarClassifier.setDisable(false);
            this.lbpClassifier.setDisable(false);
            this.btnTirarFoto.setDisable(true);

            // stop the timer
            openCV.stopAcquisition();
        }
    }

    /**
     * The action triggered by selecting the Haar Classifier checkbox. It loads
     * the trained set to be used for frontal face detection.
     */
    @FXML
    protected void haarSelected(Event event) {
        // check whether the lpb checkbox is selected and deselect it
        if (this.lbpClassifier.isSelected()) {
            this.lbpClassifier.setSelected(false);
        }
//        classifier = 2;
        this.checkboxSelection("resources/haarcascades/haarcascade_frontalface_alt.xml");
    }

    /**
     * The action triggered by selecting the LBP Classifier checkbox. It loads
     * the trained set to be used for frontal face detection.
     */
    @FXML
    protected void lbpSelected(Event event) {
        // check whether the haar checkbox is selected and deselect it
        if (this.haarClassifier.isSelected()) {
            this.haarClassifier.setSelected(false);
        }
//        classifier = 1;
        this.checkboxSelection("resources/lbpcascades/lbpcascade_frontalface.xml");
    }

    /**
     * Method for loading a classifier trained set from disk
     *
     * @param classifierPath the path on disk where a classifier trained set is
     * located
     */
    private void checkboxSelection(String classifierPath) {
        // load the classifier(s)
        openCV.getFaceCascade().load(classifierPath);

        if (!this.haarClassifier.isSelected() && !this.lbpClassifier.isSelected()) {
            this.btnIniciarCamera.setDisable(true);
        } else { // now the video capture can start
            this.btnIniciarCamera.setDisable(false);
        }
    }

//  ================================= OpenCV Crop ==============================
    private FacialData facialData;
    private int cont = 0;
    private Mat faceImage;

    protected void makeObject(Mat frame) {
        //Creating Objetc to store frame data on database
        byte[] arrayByte = Utils.matToArrayBytes(frame);
        facialData = new FacialData();
        facialData.setArquivo(arrayByte);
        if (frame.getClass().equals(Mat.class)) {
            facialData.setTipoObjeto("Mat");
        }
    }

    @FXML
    protected void imageProc() {
//        openCV.detectAndDisplay(pickFrame);
//        openCV.possoVerificar = true;
        faceImage = openCV.detectFace(openCV.auxFrame);
        Image imageFaceToShow = Utils.mat2Image(faceImage);
        openCV.updateImageView(imageVFotoStack, imageFaceToShow);
        btnAceitarFoto.setDisable(false);
    }

    @FXML
    protected void AcceptPhoto() {
        if (faceImage != null) {
            makeObject(faceImage);
            listFacialData.add(facialData);
            cont++;
            labelCount.setText(Integer.toString(cont));
        }
        btnAceitarFoto.setDisable(true);
    }

}
