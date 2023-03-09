/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.rssp.restful;

import com.google.gson.GsonBuilder;
import java.io.InputStream;
import vn.ra.process.CommonFunction;
//import static vn.ra.process.RSSPProcessCommon.URL;
import vn.ra.utility.Config;
import vn.ra.utility.Definitions;
import vn.ra.utility.EscapeUtils;

/**
 *
 * @author USER
 */
public class RSSPRestCommon {
    String language = "VN";
    String profile = "";
    String URL = "";
    String relyingParty = "";
    String relyingPartyUser = "";
    String relyingPartyPassword = "";
    String relyingPartySignature = "";
//    String relyingPartyKeyStore = "";
    String relyingPartyKeyStorePassword = "";
    String userType = "";
    InputStream relyingPartyKeyStore = null;
    static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(RSSPRestCommon.class.getName());
    
    public RSSPRestCommon() {
        Config conf = new Config();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL = conf.GetPropertybyCode(Definitions.CONFIG_RSSP_REST_URL);
        profile = conf.GetPropertybyCode(Definitions.CONFIG_RSSP_REST_PROFILE);
        relyingParty = conf.GetPropertybyCode(Definitions.CONFIG_RSSP_REST_RP_NAME);
        relyingPartyUser = conf.GetPropertybyCode(Definitions.CONFIG_RSSP_REST_RP_USER);
        relyingPartyPassword = conf.GetPropertybyCode(Definitions.CONFIG_RSSP_REST_RP_PWD);
        relyingPartySignature = conf.GetPropertybyCode(Definitions.CONFIG_RSSP_REST_RP_SIGNATURE);
//        relyingPartyKeyStore = System.getProperty("user.home") + conf.GetPropertybyCode(Definitions.CONFIG_RSSP_REST_P12_URL);
        relyingPartyKeyStore = loader.getResourceAsStream(conf.GetPropertybyCode(Definitions.CONFIG_RSSP_REST_P12_NAME));
        relyingPartyKeyStorePassword = conf.GetPropertybyCode(Definitions.CONFIG_RSSP_REST_P12_PWD);
        userType = conf.GetPropertybyCode(Definitions.CONFIG_RSSP_REST_USER_TYPE);
    }
    
    public void authLoginRSSPRest(int[] sResCode, String[] sResMess) {
        try{
            RsspRequest request = new RsspRequest(URL, relyingPartyUser, relyingPartyPassword, relyingPartySignature, relyingPartyKeyStore, relyingPartyKeyStorePassword, profile);
            request.rememberMe = true;
            request.relyingParty = relyingParty;
            request.lang = language;
            request.userType = userType;
            CommonFunction.LogDebugString(log, "authLoginRSSPRest", request.toString());
            RsspResponse response = request.sendPost(RsspFuntion.auth_login);
            CommonFunction.LogDebugString(log, "authLoginRSSPRest", new GsonBuilder().setPrettyPrinting().create().toJson(response));
            if(response.error == 0) {
                sResCode[0] = 0;
                sResMess[0] = EscapeUtils.CheckTextNull(response.accessToken);
            } else {
                sResCode[0] = response.error;
                sResMess[0] = response.errorDescription;
            }
        } catch(Exception e){
            sResCode[0] = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            sResMess[0] = "";
            CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
        }
    }
    
    public void createOwnerRSSPRest(String accessToken, String username, String fullname, String email, String phone,
        String idenType, String identification, int[] sResCode, String[] sResMess) {
        try{
            RsspRequest request = new RsspRequest(URL, relyingPartyUser, relyingPartyPassword, relyingPartySignature, relyingPartyKeyStore, relyingPartyKeyStorePassword, profile);
            request.bearer = accessToken;
            request.username = username;
            if(!"".equals(fullname)) {
                request.fullname = fullname;
            }
            request.email = email;
            if(!"".equals(phone)) {
                request.phone = phone;
            }
            if(!"".equals(idenType)) {
                request.identificationType = idenType;
            }
            if(!"".equals(identification)) {
                request.identification = identification;
            }
            request.registerTSEEnabled = true;
            request.lang = language;
            CommonFunction.LogDebugString(log, "createOwnerRSSPRest", request.toString());
            RsspResponse response = request.sendPost(RsspFuntion.owner_create);
            CommonFunction.LogDebugString(log, "createOwnerRSSPRest", new GsonBuilder().setPrettyPrinting().create().toJson(response));
            if(response.error == 0) {
                sResCode[0] = 0;
            } else {
                sResCode[0] = response.error;
                sResMess[0] = response.errorDescription;
            }
        } catch(Exception e){
            sResCode[0] = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            sResMess[0] = "";
            CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
        }
    }
    
    public void assignAgreementRSSPRest(String accessToken, String username, String agreementUUID, int[] sResCode, String[] sResMess)
    {
        try {
            RsspRequest request = new RsspRequest(URL, relyingPartyUser, relyingPartyPassword, relyingPartySignature, relyingPartyKeyStore, relyingPartyKeyStorePassword, profile);
            request.bearer = accessToken;
            request.user = username;
            request.agreementUUID = agreementUUID;
            request.userType = userType;
            request.lang = language;
            CommonFunction.LogDebugString(log, "assignAgreementRSSPRest", request.toString());
            RsspResponse response = request.sendPost(RsspFuntion.agreement_assign);
            CommonFunction.LogDebugString(log, "assignAgreementRSSPRest", new GsonBuilder().setPrettyPrinting().create().toJson(response));
            if(response.error == 0) {
                sResCode[0] = 0;
            } else {
                sResCode[0] = response.error;
                sResMess[0] = response.errorDescription;
            }
        } catch(Exception e){
            sResCode[0] = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            sResMess[0] = "";
            CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
        }
    }
}
