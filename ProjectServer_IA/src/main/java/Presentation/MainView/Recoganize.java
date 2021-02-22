package Presentation.MainView;

import Presentation.Models.PersonModel;
import br.com.pict.dao.LogsDAO;
import br.com.pict.model.Logs;
import br.com.pict.model.Pessoa;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.io.File;
import java.util.Date;
import java.util.concurrent.Callable;

/**
 * @author Iarley
 */
public class Recoganize implements Callable<Integer> {
     
     private ControllerIA controllerIA;
     
     private byte[] image;
     
     private LogsDAO logsDAO = new LogsDAO();
     
     private int idReturn = -1;
     
     public Recoganize(ControllerIA controllerIA, byte[] imageArrayByte) {
          this.controllerIA = controllerIA;
          this.image = imageArrayByte;
     }
     
     @Override
     public Integer call() throws Exception {
          File file = ControllerIA.arrayByteToFileForRecoganize(image);
          onRecognize(file);
          if (idReturn != -1) {
               Logs logs = new Logs();
               logs.setDataEntrada(new Date());
               logs.setDataSaida(null);
               logs.setPessoa(new Pessoa(idReturn));
//               logsDAO.create(logs);
          }
          return idReturn;
     }
     
     public void onRecognize(File personImageFile) {
          System.out.println("Recognize: " + personImageFile.getName());
          double[] faceFeatureArray = controllerIA.getCreateInputVector().runTask(personImageFile);
          INDArray array1 = Nd4j.create(faceFeatureArray);
          
          double MAXVALUE = 1000000;
          double minimalDistance = MAXVALUE;
          String result = "";
          for (PersonModel personModel : controllerIA.getTrainList()) {
               INDArray array2 = Nd4j.create(personModel.get_faceFeatureArray());
               double distance = controllerIA.getEuclideanDistance().run(array1, array2);
               if (distance < minimalDistance) {
                    minimalDistance = distance;
                    result = personModel.get_personName();
                    System.out.println("Person Name: " + result);
                    idReturn = Integer.parseInt(result);
               }
          }
     }
}
