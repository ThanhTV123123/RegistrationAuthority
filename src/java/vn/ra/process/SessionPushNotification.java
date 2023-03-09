/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.process;

import java.util.ArrayList;
import vn.ra.object.PUSH_TOKEN_ATTR;

/**
 *
 * @author THANH-PC
 */
public class SessionPushNotification {
    private final ArrayList<PUSH_TOKEN_ATTR> cart;

    public SessionPushNotification() {
        cart = new ArrayList<>();
    }

    public ArrayList<PUSH_TOKEN_ATTR> getGH() {
        return cart;
    }

    public String AddSessionPushNotification(PUSH_TOKEN_ATTR mh) {
        String sRes = "0";
        try {
            int intR = 100000;
            for (int i = 0; i < cart.size(); i++) {
                if (cart.get(i).name.equals(mh.name)) {
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

    public String UpdateSessionPushNotification(PUSH_TOKEN_ATTR mh) {
        String sResult = "0";
        int intR = 100000;
        for (int i = 0; i < cart.size(); i++) {
            if (cart.get(i).name.toUpperCase().equals(mh.name.toUpperCase())) {
                intR = i;
                break;
            }
        }
        if (intR != 100000) {
            PUSH_TOKEN_ATTR hang = cart.get(intR);
            hang.value = mh.value;
        }
        return sResult;
    }
}
