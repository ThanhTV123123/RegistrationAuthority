/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.utility;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author THANH-PC
 */
public class JackRabbitConfig {
    private static ConcurrentHashMap<String, JackRabbitConfig> instanceList = new ConcurrentHashMap<>();
    private Properties prop = new Properties();
    private JackRabbitConfig(String fileName) {
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            InputStream stream = loader
                    .getResourceAsStream(fileName);
            if (stream == null) {
                stream = Config.class.getResourceAsStream(fileName);
            }
            if (stream == null) {
                stream = new FileInputStream(fileName);
            }
            prop.load(stream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static InputStream getResourceAsStream(String fileBase64)
            throws FileNotFoundException {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream stream = loader
                .getResourceAsStream(fileBase64);
        if (stream == null) {
            stream = Config.class.getResourceAsStream(fileBase64);
        }
        if (stream == null) {
            stream = new ByteArrayInputStream(Base64.getDecoder().decode(fileBase64));
        }
        return stream;
    }

    public static JackRabbitConfig getInstance(String fileName) {
        if (instanceList.containsKey(fileName)) {
            return instanceList.get(fileName);
        }
        JackRabbitConfig instance = new JackRabbitConfig(fileName);
        instanceList.put(fileName, instance);
        return instance;
    }

    public String getProperty(String name) {
        return prop.getProperty(name);
    }

    public void println(String mess) {
        System.out.println(mess);
    }

    public int getIntProperty(String name, int defaultValue) {
        String value = prop.getProperty(name);
        if (value != null) {
            try {
                int valueInt = new Integer(value);
                return valueInt;
            } catch (NumberFormatException e) {

            }
        }
        return defaultValue;
    }

    public String getStringProperty(String name, String defaultValue) {
        String value = prop.getProperty(name);
        if (value != null) {
            return value;
        }
        return defaultValue;
    }
}
