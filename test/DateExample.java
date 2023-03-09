
import java.sql.Timestamp;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import vn.ra.process.CommonFunction;
import vn.ra.process.DESEncryption;
//import vn.ra.utility.Definitions;
import vn.ra.utility.PropertiesContent;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author vanth
 */
public class DateExample {

    public static void main(String[] args) throws Exception {
        String sPro = "FO_EMAIL_TEMPLATE_AUTHENTICATION_CODE_SUBJECT=[MOBILEID-CA] Xác nhận yêu cầu cấp phát chứng thư số HSM\n" +
"FO_EMAIL_TEMPLATE_AUTHENTICATION_CODE_CONTENT=Kính gửi Quý khách !{BR}MobileID xin cảm ơn Quý khách đã tin tưởng và sử dụng dịch vụ của chúng tôi.{BR}{BR}{BR}Yêu cầu cấp phát chứng thư số của Quý khách đã được tạo mới thành công. {BR}Thông tin chứng thư của Quý khách: {B}{SUBJECT_DN}{/B}{BR}{BR}Vui lòng xác nhận cấp phát chứng thư số tại đường dẫn sau: {B}{ACTIVE_URL}{/B}{BR}Vì lý do bảo mật, mã kích hoạt sẽ hết hạn trong {EXPIRATION_DURATION} ngày.{BR}{BR}{B}Trường hợp có thắc mắc hoặc yêu cầu hỗ trợ Quý khách vui lòng liên hệ số Hotline {font color=\"red\"}1900 6884{/font} hoặc gửi Email vào hòm thư : info@mobile-id.vn{BR}{BR}Quý khách lưu ý, đây là thư tự động, vui lòng không trả lời thư này !{/B}{BR}{BR}Trân trọng cảm ơn Quý khách !{BR}{BR}------------------------------------------------------------------------------------------------------------------------{BR}{BR}{B}Trung tâm chăm sóc khách hàng MobileID.{BR}{BR}19 Đặng Tiến Đông, Phường An Phú, Quận 2, TP. Hồ Chí Minh{BR}{BR}Hotline : {font color=\"red\"}1900 6884{/font}{BR}{BR}Email CSKH : info@mobile-id.vn{/B}";
        String sKey = "FO_EMAIL_TEMPLATE_AUTHENTICATION_CODE_CONTENT";
        String sValue = PropertiesContent.getPropertiesContentKey(sPro, sKey);
        System.out.println(sValue);
        
//        Date currentDate = new Date();
//        Calendar c = Calendar.getInstance();
//        c.setTime(currentDate);
//        c.add(Calendar.DATE, 10);
//        Date dateOne = c.getTime();
//        String sTime = String.valueOf(dateOne.getTime());
//        
//        String sKey = "28317#" + sTime;
//        DESEncryption clsEnrypt=new DESEncryption();
//        System.out.println("Key: " + clsEnrypt.encrypt(sKey));
//        Date date = new Date();  
//        System.out.println("stringTimeStamp: " + date.getTime());
        
        
        /*Date currentDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.add(Calendar.DATE, 0);
        Date dateOne = c.getTime();
        System.out.println(dateOne.getTime());
        String sTime = "1641293045013";
        Date date = new Date(Long.parseLong(sTime));
        Format format = new SimpleDateFormat(Definitions.CONFIG_DATE_PATTERN_DATE_TIME_DDMMYYYY);
        System.out.println(format.format(date));
        boolean sbc = CommonFunction.checkDateBiggerCurrent(format.format(date), Definitions.CONFIG_DATE_PATTERN_DATE_TIME_DDMMYYYY);
        System.out.println(sbc);*/
    }
}
