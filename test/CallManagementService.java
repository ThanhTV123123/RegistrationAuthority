
import java.net.URL;
import javax.xml.namespace.QName;
import vn.mobileid.esigncloud.management.Management;
import vn.mobileid.esigncloud.management.ManagementReq;
import vn.mobileid.esigncloud.management.ManagementResp;
import vn.mobileid.esigncloud.management.Management_Service;
import static vn.ra.process.RSSPProcessCommon.URL;
import vn.ra.utility.SSLUtilities;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author DELL
 */
public class CallManagementService {

    public static void main(String[] args) throws Exception {
        SSLUtilities.trustAllHostnames();
        SSLUtilities.trustAllHttpsCertificates();
        Management_Service service = new Management_Service(
                new URL("https://rssp.mobile-id.vn/eSignCloud/Management?wsdl"),
                new QName("http://management.esigncloud.mobileid.vn/", "Management"));
        Management management = service.getManagementPort();
        ManagementReq managementReq = new ManagementReq();
        managementReq.setEntityName("CORE_CLOUD_ENTITY");
        managementReq.setUuid("FBA58084-FAC6-4007-BE8B-557A08B064B4");

        management.getAuthModes(managementReq);

        ManagementResp managementResp = management.getAuthModes(managementReq);
        System.out.println(managementResp.getAuthModes());
        
    }
}
