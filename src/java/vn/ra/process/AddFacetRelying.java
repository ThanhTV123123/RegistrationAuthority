/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.process;
import java.util.ArrayList;
import vn.ra.object.FACET_MANAGEMENT;
import vn.ra.utility.Definitions;
/**
 *
 * @author THANH-PC
 */
public class AddFacetRelying {
 private final ArrayList<FACET_MANAGEMENT> cart;

    public AddFacetRelying() {
        cart = new ArrayList<>();
    }

    public String AddFacetList(FACET_MANAGEMENT mh) {
        String sRes = "0";
        try {
            int intR = 100000;
            for (int i = 0; i < cart.size(); i++) {
                if (cart.get(i).NAME.equals(mh.NAME))
                {
                    intR = i;
                    break;
                }
            }
            if (intR != 100000) {
                sRes = "1";
            } else {
                for (int i = 0; i < cart.size(); i++) {
                    if (cart.get(i).NAME.equals(Definitions.CONFIG_RELYING_VALUE_ALL) && cart.get(i).ENABLED == true)
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

    public ArrayList<FACET_MANAGEMENT> getGH() {
        return cart;
    }

    public String DeleteFacetList(String sNAME) {
        String sRes = "0";
        try {
            int intR = 100000;
            for (int i = 0; i < cart.size(); i++) {
                if (cart.get(i).NAME.equals(sNAME)) {
                    intR = i;
                }
            }
            if (intR != 100000) {
                FACET_MANAGEMENT hang = cart.get(intR);
                cart.remove(hang);
            } else {
                sRes = "1";
            }
        } catch (Exception e) {
            sRes = e.getMessage();
        }
        return sRes;
    }
    public String UpdateFacetList(FACET_MANAGEMENT mh) {
        String sResult = "0";
        int intR = 100000;
        for (int i = 0; i < cart.size(); i++) {
            if (cart.get(i).NAME.toUpperCase().equals(mh.NAME.toUpperCase()))
            {
                intR = i;
            }
        }
        if (intR != 100000) {
            FACET_MANAGEMENT hang = cart.get(intR);
            hang.ENABLED = mh.ENABLED;
        }
        return sResult;
    }
}
