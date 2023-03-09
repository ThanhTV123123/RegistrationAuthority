/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.utility;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author USER
 */
public class ConfigLanguage {

    private static Properties property;

    public ConfigLanguage() {
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            InputStream stream = loader.getResourceAsStream("configlanguage.properties");
            property = new Properties();
            property.load(stream);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public String GetPropertybyCode(String name, String isLanguage) {
        if("0".equals(isLanguage)) {
            return property.getProperty(property.getProperty(Definitions.CONFIG_LANGUAGE_TAG_EN).trim() + name).trim();
        } else {
            return property.getProperty(property.getProperty(Definitions.CONFIG_LANGUAGE_TAG_VN).trim() + name).trim();
        }
    }

//    public boolean isDebugMode() {
//        return GetPropertybyCode("Debug").equals("true");
//    }
}
