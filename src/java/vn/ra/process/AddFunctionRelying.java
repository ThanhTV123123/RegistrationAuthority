/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.process;

import java.util.ArrayList;
import vn.ra.object.REPLYING_FUNCTIONALITY;
import vn.ra.utility.Definitions;

/**
 *
 * @author THANH-PC
 */
public class AddFunctionRelying {

    private final ArrayList<REPLYING_FUNCTIONALITY> cart;

    public AddFunctionRelying() {
        cart = new ArrayList<>();
    }

    public String AddFunctionList(REPLYING_FUNCTIONALITY mh) {
        String sRes = "0";
        try {
            int intR = 100000;
            for (int i = 0; i < cart.size(); i++) {
                if (cart.get(i).FUNCTION_NAME.equals(mh.FUNCTION_NAME))
                {
                    intR = i;
                    break;
                }
            }
            if (intR != 100000) {
                sRes = "1";
            } else {
//                for (int i = 0; i < cart.size(); i++) {
//                    if (cart.get(i).FUNCTION_NAME.equals(Definitions.CONFIG_RELYING_VALUE_ALL) && cart.get(i).ENABLED == true)
//                    {
//                        intR = i;
//                        break;
//                    }
//                }
//                if (intR != 100000) {
//                    sRes = "2";
//                } else {
                    cart.add(mh);
//                }
            }
        } catch (Exception e) {
            sRes = e.getMessage();
        }
        return sRes;
    }

    public ArrayList<REPLYING_FUNCTIONALITY> getGH() {
        return cart;
    }

    public String DeleteFunctionList(String sIP) {
        String sRes = "0";
        try {
            int intR = 100000;
            for (int i = 0; i < cart.size(); i++) {
                if (cart.get(i).FUNCTION_NAME.equals(sIP)) {
                    intR = i;
                }
            }
            if (intR != 100000) {
                REPLYING_FUNCTIONALITY hang = cart.get(intR);
                cart.remove(hang);
            } else {
                sRes = "1";
            }
        } catch (Exception e) {
            sRes = e.getMessage();
        }
        return sRes;
    }
    
    public String UpdateFunctionList(REPLYING_FUNCTIONALITY mh)
    {
        String sResult = "0";
        int intR = 100000;
        for (int i = 0; i < cart.size(); i++) {
            if (cart.get(i).FUNCTION_NAME.toUpperCase().equals(mh.FUNCTION_NAME.toUpperCase()))
            {
                intR = i;
            }
        }
        if (intR != 100000) {
            REPLYING_FUNCTIONALITY hang = cart.get(intR);
            hang.ENABLED = mh.ENABLED;
        }
        return sResult;
    }
}
