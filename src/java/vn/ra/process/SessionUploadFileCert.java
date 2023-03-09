/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.process;

import java.util.ArrayList;
import vn.ra.object.FILE_PROFILE_DATA;

/**
 *
 * @author thanh
 */
public class SessionUploadFileCert {

    private final ArrayList<FILE_PROFILE_DATA> cart;

    public SessionUploadFileCert() {
        cart = new ArrayList<>();
    }

    public ArrayList<FILE_PROFILE_DATA> getGH() {
        return cart;
    }

    public String AddRoleFunctionsList(FILE_PROFILE_DATA mh) {
        String sRes = "0";
        try {
            int intR = 100000;
            for (int i = 0; i < cart.size(); i++) {
                if (cart.get(i).FILE_NAME.equals(mh.FILE_NAME)) {
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

    public String DeleteFunctionList(String sFILE_PROFILE, String sFILE_NAME) {
        String sRes = "0";
        try {
            int intR = 100000;
            for (int i = 0; i < cart.size(); i++) {
                if (cart.get(i).FILE_PROFILE.equals(sFILE_PROFILE) && cart.get(i).FILE_NAME.equals(sFILE_NAME)) {
                    intR = i;
                }
            }
            if (intR != 100000) {
                FILE_PROFILE_DATA hang = cart.get(intR);
                cart.remove(hang);
            } else {
                sRes = "1";
            }
        } catch (Exception e) {
            sRes = e.getMessage();
        }
        return sRes;
    }

    public String UpdateRoleFunctionsList(FILE_PROFILE_DATA mh) {
        String sResult = "0";
        int intR = 100000;
        for (int i = 0; i < cart.size(); i++) {
            if (cart.get(i).FILE_NAME.toUpperCase().equals(mh.FILE_NAME.toUpperCase())) {
                intR = i;
                break;
            }
        }
        if (intR != 100000) {
            FILE_PROFILE_DATA hang = cart.get(intR);
            hang.FILE_MIMETYPE = mh.FILE_MIMETYPE;
        }
        return sResult;
    }
}
