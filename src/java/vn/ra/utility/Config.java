/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vn.ra.utility;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import vn.ra.process.CommonFunction;


/**
 *
 * @author HaiSonVH
 */
public class Config {
    private static Properties property;
    
    public Config()
    {
        try {
//            File file = new File("../config.properties");
//            FileReader stream = new FileReader(file);
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            InputStream stream = loader.getResourceAsStream("config.properties");
            property = new Properties();
            property.load(stream);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public String GetPropertybyCode(String name)
    {
       return property.getProperty(name).trim();       
    }
    
    public String GetTryPropertybyCode(String name)
    {
        String sValue = "";
        try {
            sValue = property.getProperty(name).trim();
        } catch(Exception e) {
            
        }
        return sValue;
    }
    
    public String GetPrintPropertybyCode(String name, String isLanguage)
    {
        try {
            if("0".equals(isLanguage)) {
                Properties propertyLanguage = new Properties();
                ClassLoader loaderLanguage = Thread.currentThread().getContextClassLoader();
                InputStream streamLanguage = loaderLanguage.getResourceAsStream("configlanguage.properties");
                propertyLanguage.load(streamLanguage);
                return property.getProperty(propertyLanguage.getProperty(Definitions.CONFIG_LANGUAGE_TAG_EN).trim().toUpperCase().replace(".", "_") + name).trim();
            } else {
                return property.getProperty(name).trim();
            }
        } catch(IOException e){
            CommonFunction.LogExceptionServlet(null, "GetPrintPropertybyCode", e);
            return null;
        }
//       return property.getProperty(name).trim();
    }
    
    public boolean isDebugMode() {
	return GetPropertybyCode("Debug").equals("true");
    }
}
