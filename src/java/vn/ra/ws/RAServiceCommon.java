/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.ws;

import vn.ra.utility.Config;
import vn.ra.utility.Definitions;
import vn.ra.utility.EscapeUtils;

/**
 *
 * @author THANH-PC
 */
public class RAServiceCommon {

    public boolean checkCredentialData(CredentialData credentialData) {
//        Config conf = new Config();
        boolean checkData = false;
//        String sUsername = conf.GetPropertybyCode(Definitions.CONFIG_WS_AUTHEN_USERNAME);
//        String sPassword = conf.GetPropertybyCode(Definitions.CONFIG_WS_AUTHEN_PASSWORD);
//        if (EscapeUtils.CheckTextNull(credentialData.username).equals(sUsername) && EscapeUtils.CheckTextNull(credentialData.password).equals(sPassword)) {
//            checkData = true;
//        }
        return checkData;
    }
}
