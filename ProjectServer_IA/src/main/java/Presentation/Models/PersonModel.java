package Presentation.Models;

import br.com.pict.model.Pessoa;

public class PersonModel {
  private double[] _faceFeatureArray;
  private String _personName;
  private Pessoa pessoa;

  public PersonModel(){

  }

  public PersonModel(String personName, double[] faceFeatureArray){
    _personName = personName;
    _faceFeatureArray = faceFeatureArray;
  }

  public PersonModel(String personName, double[] faceFeatureArray, Pessoa pessoa){
    _personName = personName;
    _faceFeatureArray = faceFeatureArray;
    this.pessoa = pessoa;
  }

  public double[] get_faceFeatureArray(){
    return _faceFeatureArray;
  }

  public void set_faceFeatureArray(double[] faceFeatureArray){
    _faceFeatureArray = faceFeatureArray;
  }

  public String get_personName() {
    return _personName;
  }

  public void set_personName(String _personName) {
    this._personName = _personName;
  }
}
