package br.com.pict.util;

import br.com.pict.database.DatabaseMySQL;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Iarley
 */
public class ConfigFactory {
    
    public static Properties properties;
    public static String location;
    private static final String rootPath = System.getProperty("user.dir");
    
    public static Map<String, String> extract_sslfb;
    public static Map<String, String> extract_sslfb_data;
    public static Map<String, String> data_stored_path;
    public static Map<String, String> config_IA_paths;
    
    public static void getProp() {
        Properties props = new Properties();
        try {
            FileInputStream file;
            file = new FileInputStream(System.getProperty("user.dir")+"\\aplication.properties");
            props.load(file);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DatabaseMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DatabaseMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        properties = props;
        extractFromProp();
        extractFromSuport();
    }
    
    public static void extractFromProp(){
        extract_sslfb = new HashMap<>();
        extract_sslfb.put("driver",properties.getProperty(location+".database_sslfb.driver"));
        extract_sslfb.put("url",properties.getProperty(location+".database_sslfb.url"));
        extract_sslfb.put("username",properties.getProperty(location+".database_sslfb.username"));
        extract_sslfb.put("password",properties.getProperty(location+".database_sslfb.password"));
        
        extract_sslfb_data = new HashMap<>();
        extract_sslfb_data.put("driver",properties.getProperty(location+".database_sslfb_data.driver"));
        extract_sslfb_data.put("url",properties.getProperty(location+".database_sslfb_data.url"));
        extract_sslfb_data.put("username",properties.getProperty(location+".database_sslfb_data.username"));
        extract_sslfb_data.put("password",properties.getProperty(location+".database_sslfb_data.password"));
        
    }

    public static void extractFromSuport(){
        String[] suportPath = buildSuportLocalPath();
        data_stored_path = new HashMap<>();
        data_stored_path.put("facePath",suportPath[0]);
        data_stored_path.put("facePathWithSpaceBar",suportPath[0]+"\\");
        
        config_IA_paths = new HashMap<>();
        config_IA_paths.put("arquivoARFF", suportPath[1]+"\\dataset.arff");
    }
    
    public static String[] buildSuportLocalPath(){
        String[] splitedPath = rootPath.split("[\\\\/]",-1);
        String[] path = rootPath.split("FaceDetection"); // Path da pasta que se encontra o projeto
        
        String pastName = splitedPath[splitedPath.length - 1]+"Suport"; //Montando o nome da pasta
        
        String fullDataFacePath = path[0]+pastName+"\\Data\\Faces";
        
        File dataFaceDir = new File(fullDataFacePath);
        if(!dataFaceDir.exists()){
            System.out.println(dataFaceDir.mkdirs());
            System.out.println("Diretório Faces criado com sucesso");
        }else
            System.out.println("O caminho para armazenar as faces já existe");
        
        String fullConfig_IA_Path = path[0]+pastName+"\\Config_IA";
    
        File config_IA_dir = new File(fullConfig_IA_Path);
        if(!config_IA_dir.exists()){
            System.out.println(config_IA_dir.mkdirs());
            System.out.println("Diretório config_IA criado com sucesso");
        }else
            System.out.println("O Diretório config_IA já existe");
    
        try {
            File arffFile = new File(fullConfig_IA_Path+"\\dataset.arff");
            if(!arffFile.exists()){
                if(arffFile.createNewFile())
                    System.out.println("Arquivo dataset.arff criado com sucesso!");
                else
                    System.err.println("Erro ao criar arquivo dataset.arff");
            }else
                System.out.println("Arquvio dataset.arff já existe!");
        
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        String[] suportPath = {fullDataFacePath, fullConfig_IA_Path};
        return suportPath;
    }
    
    //Teste
    public static void main(String[] args) {
        Properties properties = System.getProperties();
        Enumeration<Object> enumeration = properties.keys();
        for (int i = 0; i < properties.size(); i++) {
            Object obj = enumeration.nextElement();
            System.out.println("Key: "+obj+"\tOutPut= "+System.getProperty(obj.toString()));
        }
        String teste = "Teste";
        if(teste.getClass().equals(String.class))
            System.out.println("Consegue comparar");
        else System.out.println("Não consegue comparar");

        String fullPath = properties.getProperty("user.dir")+"NetBeansSuport\\Data\\Faces"+"\\"+"03244304508\\";
        fullPath = fullPath+"proc_"+0;
        fullPath = fullPath+".jpg";
        System.out.println(fullPath);
        
        String[] array = rootPath.split("[\\\\/]",-1);
        for (int i=0; i<array.length; i++){
            System.out.println(array[i]);
        }
        System.out.println(array.length);

        String[] realPath = rootPath.split("FaceDetection");
        System.out.println(realPath[0]);
    
        String[] teste2;
        teste2 = buildSuportLocalPath();
        System.out.println(teste2[0]);
        System.out.println(teste2[1]);
    }
}
