/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.uat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import org.json.simple.JSONObject;
import vn.ra.object.GENERAL_POLICY;
import vn.ra.uaf.MetadataStatement;
import vn.ra.object.METADATA_STATEMENT;
import vn.ra.object.NEAC_LOG;
import vn.ra.process.ConnectDatabase;
import vn.ra.process.SynchNEACFunction;
import vn.ra.uaf.AlgAndEncodingEnum;
import vn.ra.utility.Config;
import vn.ra.utility.DatabaseUAFConstants;
import vn.ra.utility.Definitions;
import vn.ra.utility.LoadParamSystem;

/**
 *
 * @author THANH-PC
 */
public class Test_getMetaData {

    public static void main(String[] args) throws Exception {
        // NEAC DONG BO
        Config conf = new Config();
        String sSourceNEAC = conf.GetPrintPropertybyCode(Definitions.CONFIG_SYNCH_NEAC_WS_SOURCE, "");
        String sUrlNEAC = conf.GetPrintPropertybyCode(Definitions.CONFIG_SYNCH_NEAC_WS_URL, "");
        String sUserIDNEAC = conf.GetPrintPropertybyCode(Definitions.CONFIG_SYNCH_NEAC_WS_USERID, "");
        String sUserKeyNEAC = conf.GetPrintPropertybyCode(Definitions.CONFIG_SYNCH_NEAC_WS_USERKEY, "");
        LoadParamSystem.updateParamSystem(Definitions.CONFIG_SYNCH_NEAC_WS_SOURCE, sSourceNEAC);
        LoadParamSystem.updateParamSystem(Definitions.CONFIG_SYNCH_NEAC_WS_URL, sUrlNEAC);
        LoadParamSystem.updateParamSystem(Definitions.CONFIG_SYNCH_NEAC_WS_USERID, sUserIDNEAC);
        LoadParamSystem.updateParamSystem(Definitions.CONFIG_SYNCH_NEAC_WS_USERKEY, sUserKeyNEAC);
        ConnectDatabase db = new ConnectDatabase();
        String neacLogID = "383";
        NEAC_LOG[][] rsLog = new NEAC_LOG[1][];
        db.S_BO_NEAC_LOG_DETAIL(neacLogID, "1", rsLog);
        if (rsLog[0].length > 0) {
            int intRemainingSystem = 0;
            GENERAL_POLICY[][] sessGeneralPolicy = new GENERAL_POLICY[1][];
            db.S_BO_GENERAL_POLICY_LIST("1", sessGeneralPolicy);
            if (sessGeneralPolicy[0].length > 0) {
                for (GENERAL_POLICY rsPolicy1 : sessGeneralPolicy[0]) {
                    if (rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_BO_MAX_RETRY_NEAC_SYNCHRONIZATION)) {
                        intRemainingSystem = Integer.parseInt(rsPolicy1.VALUE);
                        break;
                    }
                }
            }
            int[] intRes = new int[1];
            String[] strRes = new String[1];
            if (rsLog[0][0].NEAC_SYNC_STATE_ID == Definitions.CONFIG_SYNCH_NEAC_STATE_PROCESSING_MANUALLY
                    || rsLog[0][0].NEAC_SYNC_STATE_ID == Definitions.CONFIG_SYNCH_NEAC_STATE_PROCESSING
                    || rsLog[0][0].NEAC_SYNC_STATE_ID == Definitions.CONFIG_SYNCH_NEAC_STATE_SPECIAL_CASE
                    || rsLog[0][0].NEAC_SYNC_STATE_ID == Definitions.CONFIG_SYNCH_NEAC_STATE_ERROR_ASYNCHRONOUS)
            {
                SynchNEACFunction.synchNEACCertificate("admin", rsLog[0][0].ID, rsLog[0][0].CERTIFICATION_ID,
                        rsLog[0][0].CERTIFICATION_ATTR_TYPE_ID, rsLog[0][0].REMAINING_COUNTER, intRemainingSystem, intRes, strRes);
                if (intRes[0] == 0) {
                    System.out.println("OK");
                } else {
                    System.out.println(strRes[0]);
                }
            }
        }
    }
}
