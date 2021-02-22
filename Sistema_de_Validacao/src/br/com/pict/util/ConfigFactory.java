package br.com.pict.util;

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
    
    public static Map<String, String> extract_sslfb;
    public static Map<String, String> extract_sslfb_data;
    public static Map<String, String> data_stored_path;
    
    public static Map<String, String> connectionVPS;
    
    public static void getConfig(){
        connectionVPS = new HashMap<>();
        
        connectionVPS.put("vps_directAccses_ip", "35.215.211.143");
        connectionVPS.put("vps_2_directAccses_ip", "35.199.98.243");
        connectionVPS.put("vps_vpnAccses_ip", "10.10.10.1");
    }
    
    public static void getProp() {
        Properties props = new Properties();
        try {
            FileInputStream file;
            file = new FileInputStream(System.getProperty("user.dir")+"\\aplication.properties");
            props.load(file);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ConfigFactory.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ConfigFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        properties = props;
        extractFromProp();
    }
    
    private static void extractFromProp(){
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
    }
}
