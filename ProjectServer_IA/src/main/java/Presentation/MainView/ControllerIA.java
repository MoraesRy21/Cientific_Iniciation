package Presentation.MainView;

import Core.Math.EuclideanDistance;
import Core.Tasks.CreateInputVectorTask;
import Core.Tasks.ExtractFacesFromImageTask;
import Presentation.Models.PersonModel;
import br.com.pict.dao.FacialDataDAO;
import br.com.pict.dao.PessoaDAO;
import br.com.pict.util.ConfigFactory;
import com.twelvemonkeys.io.FileUtil;
import javafx.beans.property.SimpleStringProperty;
import org.apache.commons.io.FileUtils;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.opencv.core.Size;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ControllerIA {
     
     private SimpleStringProperty message;
     private List<PersonModel> trainList;
     private CreateInputVectorTask createInputVector;
     private EuclideanDistance euclideanDistance;
     
     public int counterErrors = 0;
     public int counterHits = 0;
     public double counterMax = 0;
     public double precisionErrorsMax = 0;
     public double precisionErrorsMin = 100;
     public double precisionHitsMax = 0;
     public double precisionHitsMin = 100;
     public double precisionHitsAverage = 0;
     public double precisionErrorsAverage = 0;
     public String values[];
     
     private int absoluteFaceSize;
     
     public static ExecutorService executorServiceTrainig = Executors.newCachedThreadPool();
     public static ExecutorService executorServiceRecoganize = Executors.newCachedThreadPool();
     
     public static boolean tokenFileARFF = true;
     
     public ControllerIA() {
          createInputVector = new CreateInputVectorTask();
          trainList = new ArrayList<>();
          euclideanDistance = new EuclideanDistance();
          message = new SimpleStringProperty();
          loadOfFileARFF();
     }
     
     public void loadOfFileARFF() {
          System.out.println("----- Carregando Arquivo ------");
          if (new File(ConfigFactory.config_IA_paths.get("arquivoARFF")).length() == 0)
               return;
          try {
               BufferedReader bufferedReader = new BufferedReader(new FileReader(ConfigFactory.config_IA_paths.get("arquivoARFF")));
               while (bufferedReader.ready()) {
                    String linha = bufferedReader.readLine();
                    values = linha.split(",");
                    double[] faceFeatureArray = new double[values.length - 1];
                    for (int i = 0; i < values.length; i++) {
                         if (i < 4096) {
                              double array = Double.parseDouble(values[i]);
                              faceFeatureArray[i] = array;
                         } else {
                              PersonModel objPersonModel = new PersonModel(values[i], faceFeatureArray);
                              trainList.add(objPersonModel);
                         }
                    }
               }
               bufferedReader.close();
          } catch (IOException e) {
               e.printStackTrace();
          }
          System.out.println("----- Arquivo Carregado ------");
          
     }
     
     public static File arrayByteToFileForRecoganize(byte[] bytes) throws IOException {
          File file = new File(ConfigFactory.data_stored_path.get("facePathWithSpaceBar") + "face_on_recoganize.jpg");
          OutputStream os = new FileOutputStream(file);
          os.write(bytes);
          os.close();
          return file;
     }
     
     public double getCounterMax() {
          return counterMax;
     }
     
     public double getPrecisionHitsAverage() {
          return precisionHitsAverage;
     }
     
     public double getPrecisionErrorsAverage() {
          return precisionErrorsAverage;
     }
     
     public double getPrecisionErrorsMax() {
          return precisionErrorsMax;
     }
     
     public double getPrecisionErrorsMin() {
          return precisionErrorsMin;
     }
     
     public double getPrecisionHitsMax() {
          return precisionHitsMax;
     }
     
     public double getPrecisionHitsMin() {
          return precisionHitsMin;
     }
     
     public int getCounterErrors() {
          return counterErrors;
     }
     
     public int getCounterHits() {
          return counterHits;
     }
     
     public String getMessage() {
          return message.get();
     }
     
     public SimpleStringProperty messageProperty() {
          return message;
     }
     
     public void setMessage(String message) {
          this.message.set(message);
     }
     
     public List<PersonModel> getTrainList() {
          return trainList;
     }
     
     public void setTrainList(List<PersonModel> trainList) {
          this.trainList = trainList;
     }
     
     public CreateInputVectorTask getCreateInputVector() {
          return createInputVector;
     }
     
     public void setCreateInputVector(CreateInputVectorTask createInputVector) {
          this.createInputVector = createInputVector;
     }
     
     public EuclideanDistance getEuclideanDistance() {
          return euclideanDistance;
     }
     
     public void setEuclideanDistance(EuclideanDistance euclideanDistance) {
          this.euclideanDistance = euclideanDistance;
     }
}

