/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.utility;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author USER
 */
public class LoadParamSystem {

    private static final Map<String, String> mapStart = new HashMap<>();
//    private static final Map<PREFIX_UUID, String> mapPrefix = new HashMap<PREFIX_UUID, String>();

    public static String getParamStart(String value) {
        String result = mapStart.get(value.toUpperCase().trim());
        if (result == null) {
            result = "";
        }
        return result;
    }

    public static void updateParamSystem(String sKey, String sValue) {
        mapStart.put(sKey, sValue);
    }
    
//    public static PREFIX_UUID getParamPrefixStart(PREFIX_UUID prefix) {
//        PREFIX_UUID result = mapPrefix.get(prefix);
//        if (result == null) {
//            result = "";
//        }
//        return result;
//    }
//
//    public static void updateParamPrefixSystem(PREFIX_UUID sKey, String sValue) {
//        mapPrefix.put(sKey, sValue);
//    }
}
