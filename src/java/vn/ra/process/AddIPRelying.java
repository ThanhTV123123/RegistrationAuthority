/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.process;

import java.util.ArrayList;
import vn.ra.object.CERTIFICATE_ATTRIBUTES_SESSION;
import vn.ra.utility.Definitions;

/**
 *
 * @author THANH-PC
 */
public class AddIPRelying {

    private final ArrayList<CERTIFICATE_ATTRIBUTES_SESSION> cart;

    public AddIPRelying() {
        cart = new ArrayList<>();
    }

    public String AddIPList(CERTIFICATE_ATTRIBUTES_SESSION mh) {
        String sRes = "0";
        try {
            int intR = 100000;
            for (int i = 0; i < cart.size(); i++) {
                if(mh.attributeType.equals(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_RADIOBUTTON))
                { }
                else
                {
                    if (cart.get(i).name.equals(mh.name) && cart.get(i).prefix.equals(mh.prefix)) {
                        intR = i;
                        break;
                    }
                }
            }
            if (intR != 100000) {
                sRes = "1";
            } else {
                cart.add(mh);
            }
        } catch (Exception e) {
            sRes = e.getMessage();
        }
        return sRes;
    }

    public ArrayList<CERTIFICATE_ATTRIBUTES_SESSION> getGH() {
        return cart;
    }

    public String DeleteIPList(String sName, String sPrefix, boolean isCheck) {
        String sRes = "0";
        try {
            int intR = 100000;
            for (int i = 0; i < cart.size(); i++) {
                if(!cart.get(i).attributeType.equals(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_RADIOBUTTON))
                {
                    // && cart.get(i).prefix.equals(sPrefix)
                    if (cart.get(i).name.equals(sName)) {
                        intR = i;
                    }
                }
            }
            if (intR != 100000) {
                CERTIFICATE_ATTRIBUTES_SESSION hang = cart.get(intR);
                hang.require = isCheck;
//                cart.remove(hang);
            } else {
                sRes = "1";
            }
        } catch (Exception e) {
            sRes = e.getMessage();
        }
        return sRes;
    }
    
//    public String UpdateIPList(CERTIFICATE_ATTRIBUTES mh)
//    {
//        String sResult = "0";
//        int intR = 100000;
//        for (int i = 0; i < cart.size(); i++) {
//            if (cart.get(i).IP.toUpperCase().equals(mh.IP))
//            {
//                intR = i;
//            }
//        }
//        if (intR != 100000) {
//            CERTIFICATE_ATTRIBUTES hang = cart.get(intR);
//            hang.ENABLED = mh.ENABLED;
//        }
//        return sResult;
//    }
}
