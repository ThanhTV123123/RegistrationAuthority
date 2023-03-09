/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fms;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Properties;
import javax.jcr.Repository;
import org.apache.commons.io.IOUtils;
import vn.mobileid.fms.client.JCRConfig;
import vn.mobileid.fms.client.JCRFile;
import vn.mobileid.fms.client.JCRManager;
import vn.mobileid.fms.client.loadbalancing.HealcheckConfig;

/**
 *
 * @author TRUONGNNT
 */
public class TestDownloadMultiFMS {

    static final String FMS_MID_CFG = "fms_mobileid.properties";

    static final String FMS_245_CFG = "fms_245.properties";

    public static void main(String[] args) throws Exception {
        try {
            //PropertyConfigurator.configure(Config.getResourceAsStream(LOG_CONFIG));
            //Gson gson = new GsonBuilder().setPrettyPrinting().create();

            //JCRManager fmsMid = JCRManager.getInstance(getJCRConfig(FMS_MID_CFG), new HealcheckConfig());
            //JCRFile file = fmsMid.downloadJCR("64e7b36f-68e1-44d3-bb99-bd1c088fc7cd");
            //System.out.println("vn.mobileid.fms.client.test.TestDownloadMultiFMS.main()" + gson.toJson(file));
            JCRManager fmsMid = JCRManager.getInstance(getJCRConfig(FMS_MID_CFG), new HealcheckConfig());
            downloadTofile(fmsMid, "64e7b36f-68e1-44d3-bb99-bd1c088fc7cd");

            JCRManager fms245 = JCRManager.getInstance(getJCRConfig(FMS_245_CFG), new HealcheckConfig());
            downloadTofile(fms245, "f226b868-aebb-41dd-a74c-51d3e94846ff");
        } finally {
            JCRManager.destroyAllJCR();
        }
    }

    static void downloadTofile(JCRManager fms, String uuid) {
        try {
            //System.out.println("Start download : " + fms.getSession().getRepository().getDescriptor(Repository.REP_VENDOR_URL_DESC)+ ", UUID: " + uuid);
            JCRFile file = fms.downloadJCR(uuid);
            System.out.println("Download file successful: " + file.getFileName());
            try (OutputStream fOut = new FileOutputStream("E:/Download/" + file.getFileName())) {
                IOUtils.write(IOUtils.toByteArray(file.getStream()), fOut);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    static final String JCR_HOST = "jcr.config.host";
    static final String JCR_USER_ID = "jcr.config.userid";
    static final String JCR_PWD = "jcr.config.password";
    static final String JCR_MAX_SESSION = "jcr.config.maxSession";
    static final String JCR_MAX_FILE_IN_FOLDER = "jcr.config.maxFileInFolder";
    static final String JCR_MAX_PREFIX_FOLDER = "jcr.config.prefixFolder";
    static final String JCR_WORKSPACE = "jcr.config.workspace";

    static JCRConfig getJCRConfig(String cfgFile) throws Exception {
        Properties cfg = new Properties();
        cfg.load(TestDownloadMultiFMS.class.getResourceAsStream(cfgFile));

        JCRConfig jcrConfig = new JCRConfig();
        jcrConfig.setHost(cfg.getProperty(JCR_HOST));
        jcrConfig.setUserID(cfg.getProperty(JCR_USER_ID));
        jcrConfig.setPassword(cfg.getProperty(JCR_PWD));
        jcrConfig.setMaxSession(Integer.parseInt(cfg.getProperty(JCR_MAX_SESSION, "50")));
        jcrConfig.setMaxFileInFolder(Integer.parseInt(cfg.getProperty(JCR_MAX_FILE_IN_FOLDER, "5000")));
        jcrConfig.setWorkSpaceName(cfg.getProperty(JCR_WORKSPACE, "default"));

        String prefix = cfg.getProperty(JCR_MAX_PREFIX_FOLDER, "247_client_");
        jcrConfig.setFolderPrefix(prefix);

        return jcrConfig;
    }
}
