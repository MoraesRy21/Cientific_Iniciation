package Presentation.Estastistic;

import Presentation.MainView.ControllerIA;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Estatistic {

    public static void finalStatistic(ControllerIA main){
        double total = main.getCounterErrors()+main.getCounterHits();
        double correctPercentage = main.getCounterHits()*100/total;
        double wrongPercentage = main.getCounterErrors()*100/total;

        System.out.println("Relatory:");
        System.out.println("Total: "+(main.getCounterHits()+main.getCounterErrors()));
        System.out.println("Correct Answers: "+main.getCounterHits());
        System.out.println("Wrong Answers: "+main.getCounterErrors());
        System.out.println("Percentage correct Answers: "+correctPercentage);
        System.out.println("Percentage wrong Answers: "+wrongPercentage);
        System.out.println("Percentage average: "+main.getCounterMax()/(main.getCounterErrors()+main.getCounterHits()));
        System.out.println("Percentage errors MAX: "+main.getPrecisionErrorsMax());
        System.out.println("Percentage errors MIN: "+main.getPrecisionErrorsMin());
        System.out.println("Percentage correct MAX: "+main.getPrecisionHitsMax());
        System.out.println("Percentage correct MIN: "+main.getPrecisionHitsMin());
    }

    public static void teste(){
//        double max = 100;
//        double erros = minimalDistance*100 / MAXVALUE;
//        max = 100 - erros;
//        String label[] = personImageFile.getName().split("-");
//
//        if(result.equals(label[0])){
//            counterHits++;
//            precisionHitsAverage = max + precisionHitsAverage;
//            if(max>=precisionHitsMax){
//                precisionHitsMax = max;
//            }
//            if(max<=precisionHitsMin){
//                precisionHitsMin = max;
//            }
//        }else{
//            counterErrors++;
//            try {
//                FileWriter wrong = new FileWriter("C:\\Users\\projeto\\Documents\\NetBeansProjects\\FaceTest\\Face Detection\\wrong.txt", true);
//                PrintWriter saveWrong = new PrintWriter(wrong);
//                saveWrong.printf("File: "+label[0]+"%n");
//                saveWrong.printf("Predicted wrong: "+result+"%n");
//                saveWrong.printf("Trust level%n");
//                saveWrong.printf("Precision: "+max+"%"+"%n");
//                saveWrong.printf("Errors: "+erros+"%"+"%n");
//                saveWrong.printf("Euclidean distance: "+minimalDistance+"%n");
//                saveWrong.printf("MAX Value param: "+MAXVALUE+"%n");
//                saveWrong.printf("+-------------+%n");
//                saveWrong.close();
//            }catch(IOException e) {
//                e.printStackTrace();
//            }
//            precisionErrorsAverage = max + precisionErrorsAverage;
//            if(max>=precisionErrorsMax){
//                precisionErrorsMax = max;
//            }
//            if(max<=precisionErrorsMin){
//                precisionErrorsMin = max;
//            }
//        }
//        counterMax = counterMax+max;
//        try {
//            FileWriter statitics = new FileWriter("C:\\Users\\projeto\\Documents\\NetBeansProjects\\FaceTest\\Face Detection\\estatistics.txt", true);
//            PrintWriter saveStatistics = new PrintWriter(statitics);
//            saveStatistics.printf("File: "+label[0]+"%n");
//            saveStatistics.printf("Predict: "+result+"%n");
//            saveStatistics.printf("Trust level%n");
//            saveStatistics.printf("Precision: "+max+"%"+"%n");
//            saveStatistics.printf("Errors: "+erros+"%"+"%n");
//            saveStatistics.printf("Euclidean distance: "+minimalDistance+"%n");
//            saveStatistics.printf("MAX Value param: "+MAXVALUE+"%n");
//            saveStatistics.printf("+-------------+%n");
//            statitics.close();
//        }catch(IOException e) {
//            e.printStackTrace();
//        }
//        System.out.println("Predict: "+result);
//        System.out.println(" Trust level:");
//        System.out.println(" Precision: "+max+" %");
//        System.out.println(" Errors: "+erros+" %");
//        System.out.println(" Euclidean distance: "+minimalDistance);
//        System.out.println("MAX Value param: "+MAXVALUE);
    }
}
