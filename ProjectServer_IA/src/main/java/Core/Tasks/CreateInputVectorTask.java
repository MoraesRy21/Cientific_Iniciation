package Core.Tasks;

import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.transferlearning.TransferLearningHelper;
import org.deeplearning4j.zoo.PretrainedType;
import org.deeplearning4j.zoo.ZooModel;
import org.deeplearning4j.zoo.model.VGG16;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.VGG16ImagePreProcessor;
import org.nd4j.linalg.factory.Nd4j;

import java.io.File;
import java.io.IOException;

public class CreateInputVectorTask{
  private File _faceimageFile;

  private double[] _result;
  public double[] get_result(){return _result;}

  private TransferLearningHelper _transferLearningHelper;
  private NativeImageLoader _nativeImageLoader;
  private DataNormalization _scaler;

  public Boolean _isInitialized=false;

  public CreateInputVectorTask() {
    try {
      System.out.println("Loading DL4J");
      ZooModel objZooModel = new VGG16();
      ComputationGraph objComputationGraph = null;
      objComputationGraph = (ComputationGraph)objZooModel.initPretrained(PretrainedType.VGGFACE);
      System.out.println("Loaded DL4J");
      _transferLearningHelper = new TransferLearningHelper(objComputationGraph,"fc7");
      _nativeImageLoader = new NativeImageLoader(224, 224, 3);
      _scaler = new VGG16ImagePreProcessor();
      _isInitialized=true;
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public double[] runTask(File faceImageFile) {
    if (!_isInitialized) return null;

    _faceimageFile = faceImageFile;

    try {
      INDArray imageMatrix = _nativeImageLoader.asMatrix(_faceimageFile);
      _scaler.transform(imageMatrix);

      DataSet objDataSet = new DataSet(imageMatrix, Nd4j.create(new float[]{0,0}));

      DataSet objFeaturized = _transferLearningHelper.featurize(objDataSet);
      INDArray featuresArray = objFeaturized.getFeatures();

      int reshapeDimension=1;
      for (int dimension : featuresArray.shape()) {
        reshapeDimension *= dimension;
        //System.out.println("Dimension: "+dimension+"Reshape: "+reshapeDimension);
      }

      featuresArray = featuresArray.reshape(1,reshapeDimension);

      _result = featuresArray.data().asDouble();
      return _result;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }
}
