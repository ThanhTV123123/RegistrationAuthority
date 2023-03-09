/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.process;
import java.util.ArrayList;
import vn.ra.object.METADATA;
import vn.ra.utility.Definitions;
/**
 *
 * @author THANH-PC
 */
public class AddMetaDataRelying {
   private final ArrayList<METADATA> cart;

    public AddMetaDataRelying() {
        cart = new ArrayList<>();
    }

    public String AddMetaDataList(METADATA mh) {
        String sRes = "0";
        try {
            int intR = 100000;
            for (int i = 0; i < cart.size(); i++) {
                if (cart.get(i).AAID.equals(mh.AAID))
                {
                    intR = i;
                    break;
                }
            }
            if (intR != 100000) {
                sRes = "1";
            } else {
                for (int i = 0; i < cart.size(); i++) {
                    if (cart.get(i).AAID.equals(Definitions.CONFIG_RELYING_VALUE_ALL) && cart.get(i).ENABLED == true)
                    {
                        intR = i;
                        break;
                    }
                }
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

    public ArrayList<METADATA> getGH() {
        return cart;
    }

    public String DeleteMetaDataList(String sAAID) {
        String sRes = "0";
        try {
            int intR = 100000;
            for (int i = 0; i < cart.size(); i++) {
                if (cart.get(i).AAID.equals(sAAID)) {
                    intR = i;
                }
            }
            if (intR != 100000) {
                METADATA hang = cart.get(intR);
                cart.remove(hang);
            } else {
                sRes = "1";
            }
        } catch (Exception e) {
            sRes = e.getMessage();
        }
        return sRes;
    }
    public String UpdateMetaDataList(METADATA mh) {
        String sResult = "0";
        int intR = 100000;
        for (int i = 0; i < cart.size(); i++) {
            if (cart.get(i).AAID.toUpperCase().equals(mh.AAID.toUpperCase()))
            {
                intR = i;
            }
        }
        if (intR != 100000) {
            METADATA hang = cart.get(intR);
            hang.ENABLED = mh.ENABLED;
        }
        return sResult;
    }
}
