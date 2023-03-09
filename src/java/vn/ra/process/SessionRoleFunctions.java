/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.process;

import java.util.ArrayList;
import vn.ra.object.ROLE_DATA;

/**
 *
 * @author THANH-PC
 */
public class SessionRoleFunctions {

    private final ArrayList<ROLE_DATA> cart;

    public SessionRoleFunctions() {
        cart = new ArrayList<>();
    }

    public ArrayList<ROLE_DATA> getGH() {
        return cart;
    }

    public String AddRoleFunctionsList(ROLE_DATA mh) {
        String sRes = "0";
        try {
            int intR = 100000;
            for (int i = 0; i < cart.size(); i++) {
                if (cart.get(i).name.equals(mh.name) && cart.get(i).attributeType.equals(mh.attributeType)) {
                    intR = i;
                    break;
                }
            }
            if (intR != 100000) {
                sRes = "1";
            } else {
                if (intR != 100000) {
                    sRes = "2";
                } else {
                    cart.add(mh);
                }
            }
        } catch (Exception e) {
            sRes = e.getMessage();
        }
        return sRes;
    }

    public String UpdateRoleFunctionsList(ROLE_DATA mh) {
        String sResult = "0";
        int intR = 100000;
        for (int i = 0; i < cart.size(); i++) {
            if (cart.get(i).name.toUpperCase().equals(mh.name.toUpperCase())
                && cart.get(i).attributeType.toUpperCase().equals(mh.attributeType.toUpperCase())) {
                intR = i;
                break;
            }
        }
        if (intR != 100000) {
            ROLE_DATA hang = cart.get(intR);
            hang.enabled = mh.enabled;
//            hang.attributeType = mh.attributeType;
        }
        return sResult;
    }
}
