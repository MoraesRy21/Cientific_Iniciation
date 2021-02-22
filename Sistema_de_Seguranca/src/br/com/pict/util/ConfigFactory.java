package br.com.pict.util;

import br.com.pict.database.DatabaseMySQL;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
    
    public static Map<String, String> extract_sslfb;
    public static Map<String, String> extract_sslfb_data;
    public static Map<String, String> data_stored_path;
    
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
        
        data_stored_path = new HashMap<>();
        String[] buildPath = buildDataLocalPath();
        data_stored_path.put("facePath",buildPath[0]);
    }
    
    public static String[] buildDataLocalPath(){
        String localPath = System.getProperty("user.dir");
        String[] splitedPath = localPath.split("\\Sistema_de_Seguranca");
        
        String[] buildPath = new String[2];
        buildPath[0] = splitedPath[0]+"NetBeansSuport\\Data\\Faces";
        
        File dir1 = new File(buildPath[0]);
        if(!dir1.exists()){
            System.out.println(dir1.mkdirs());
            System.out.println("Diretório Faces criado com sucesso");
        }else
            System.out.println("O caminho para armazenar as faces existe");
        
        return buildPath;
    }
}
