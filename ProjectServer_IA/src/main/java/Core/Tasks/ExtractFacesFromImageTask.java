package Core.Tasks;

import Core.BaseTask;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ExtractFacesFromImageTask extends BaseTask<List<File>> {
  private File _imageFilePath;
  private File _haarFile;
  private File _tempFolder;
  private double _scaleFactor;
  private int _minNeighbours;
  private Size _minFaceSize;
  private Size _maxFaceSize;

  private CascadeClassifier _classifier;

  private boolean _initialized=false;

  public ExtractFacesFromImageTask(String taskName, File imageFilePath, File haarFile, File tempFolder,
                                   double scaleFactor, int minNeighbours, Size minFaceSize, Size maxFaceSize) {
    super(taskName);

    _imageFilePath = imageFilePath;
    _haarFile = haarFile;
    _tempFolder = tempFolder;
    _scaleFactor = scaleFactor;
    _minNeighbours = minNeighbours;
    _minFaceSize = minFaceSize;
    _maxFaceSize = maxFaceSize;

    try {
      System.out.println("Loading OpenCV");
      nu.pattern.OpenCV.loadShared();
      System.out.println("Loaded OpenCV");

      _classifier = new CascadeClassifier(_haarFile.getAbsolutePath());
      _initialized= true;
    }
    catch (Exception err){
      System.out.println("Failed to load OpenCV");
    }
  }

  @Override
  protected List<File> runTask() {
    List<File> faceList = new ArrayList<>();

    MatOfRect matFaceList = new MatOfRect();

    Mat image = Imgcodecs.imread(_imageFilePath.getAbsolutePath());
    if (image.empty()) return faceList;

    _classifier.detectMultiScale(image,matFaceList,_scaleFactor,_minNeighbours,0,_minFaceSize,_maxFaceSize);

    if (!matFaceList.empty()) {
      int i=0;
      for (Rect faceRectangle : matFaceList.toArray()) {
        Mat faceImage = image.submat(faceRectangle);
        String fileName = _tempFolder + "\\Face_" + i + ".jpg";
        Imgcodecs.imwrite(fileName, faceImage);
        faceList.add(new File(fileName));

        updateMessage(fileName);
        i++;

        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }

    updateValue(faceList);
    return faceList;
  }
}
