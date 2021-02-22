package Core.Math;

import org.nd4j.linalg.api.ndarray.INDArray;

public class EuclideanDistance {
  public double run(INDArray array1, INDArray array2) {
    return array1.distance2(array2);
  }
}
