/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.process;

import java.util.ArrayList;
import vn.ra.object.DNS_NAME_DATA;

/**
 *
 * @author THANH-PC
 */
public class SessionDNSName {
    private final ArrayList<DNS_NAME_DATA> cart;

    public SessionDNSName() {
        cart = new ArrayList<>();
    }

    public ArrayList<DNS_NAME_DATA> getGH() {
        return cart;
    }

    public String AddRoleFunctionsList(DNS_NAME_DATA mh) {
        String sRes = "0";
        try {
            int intR = 100000;
            for (int i = 0; i < cart.size(); i++) {
                if (cart.get(i).DNS_NAME.equals(mh.DNS_NAME)) {
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

    public String DeleteFunctionList(String sDNS_NAME) {
        String sRes = "0";
        try {
            int intR = 100000;
            for (int i = 0; i < cart.size(); i++) {
                if (cart.get(i).DNS_NAME.equals(sDNS_NAME)) {
                    intR = i;
                }
            }
            if (intR != 100000) {
                DNS_NAME_DATA hang = cart.get(intR);
                cart.remove(hang);
            } else {
                sRes = "1";
            }
        } catch (Exception e) {
            sRes = e.getMessage();
        }
        return sRes;
    }
}
