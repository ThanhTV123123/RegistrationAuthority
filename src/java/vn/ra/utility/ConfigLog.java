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
public class ConfigLog {

    private static Properties property;

    public ConfigLog() {
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            InputStream stream = loader.getResourceAsStream("log4j.properties");
            property = new Properties();
            property.load(stream);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public String GetPropertybyCode(String name) {
        return property.getProperty(name).trim();
    }

    public boolean isDebugMode() {
        return GetPropertybyCode("Debug").equals("true");
    }
}
