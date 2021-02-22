package Presentation.MainView;

import Presentation.Models.PersonModel;
import br.com.pict.dao.FacialDataDAO;
import br.com.pict.model.FacialData;
import br.com.pict.util.ConfigFactory;

import java.io.*;
import java.util.List;

/**
 * @author Iarley
 */
public class Training implements Runnable {

     private ControllerIA controllerIA;
     private FacialDataDAO facialDataDAO = new FacialDataDAO();

     public Training(ControllerIA controllerIA) {
          this.controllerIA = controllerIA;
     }

     @Override
     public void run() {
          try {
               List<FacialData> facialList = facialDataDAO.findAllLastImages();
               for (int i = 0; i < facialList.size(); i++) {
                    FacialData f = facialList.get(i);
                    String personName = f.getPessoa().getId().toString();
                    File faceImageFile = new File(ConfigFactory.data_stored_path.get("facePathWithSpaceBar") + personName + "_" + i + ".jpg");
                    OutputStream os = new FileOutputStream(faceImageFile);
                    os.write(f.getArquivo());
                    os.close();
                    onTrain(personName, faceImageFile);
                    faceImageFile.delete();
               }
          } catch (IOException e) {
               e.printStackTrace();
          }
     }

     public void onTrain(String personName, File faceImageFile) {
          double[] faceFeatureArray = null;
          System.out.println("ControllerIA null");
          faceFeatureArray = controllerIA.getCreateInputVector().runTask(faceImageFile);
          PersonModel objPersonModel = new PersonModel(personName, faceFeatureArray);
          controllerIA.getTrainList().add(objPersonModel);
          saveOnFileARFF(personName, faceFeatureArray);
     }

     public void saveOnFileARFF(String personName, double[] faceFeatureArray) {
          try {
               FileWriter dataFile = new FileWriter(ConfigFactory.config_IA_paths.get("arquivoARFF"), true);
               PrintWriter save = new PrintWriter(dataFile);
               for (int i = 0; i < faceFeatureArray.length; i++) {
                    save.printf(faceFeatureArray[i] + ",");
               }
               save.printf(personName + "\n");
               dataFile.close();
          } catch (IOException e) {
               e.printStackTrace();
          }
     }
}
