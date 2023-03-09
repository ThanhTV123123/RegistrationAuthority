/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.process;

import java.net.URL;
import javax.xml.namespace.QName;
import vn.ra.raconnector.RegistrationAuthorityWS;
import vn.ra.raconnector.RegistrationAuthorityWS_Service;
import vn.ra.utility.SSLUtilities;

/**
 *
 * @author THANH-PC
 */
public class ConnectorWS {

    private static ConnectorWS instance;
    private RegistrationAuthorityWS ws;

    public static ConnectorWS getInstance() {
        if (instance == null) {
            instance = new ConnectorWS();
        }
        return instance;
    }

    public RegistrationAuthorityWS getWS(String strURL) {
        if (ws == null) {
            try {
                if (strURL.contains("https")) {
                    SSLUtilities.trustAllHostnames();
                    SSLUtilities.trustAllHttpsCertificates();
                }
                RegistrationAuthorityWS_Service service = new RegistrationAuthorityWS_Service(new URL(strURL), new QName("http://raservice.mobileid.vn/", "RegistrationAuthorityWS"));
                ws = service.getRegistrationAuthorityWSPort();
            } catch (Exception e) {
                CommonFunction.LogExceptionJSP(null, "RegistrationAuthorityWS-GetWS: " + e.getMessage(), e);
            }
        }
        return ws;
    }
}
