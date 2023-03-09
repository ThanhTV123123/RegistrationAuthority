
package vn.ra.raconnector;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the vn.ra.raconnector package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _EnrollCertificateDSResponse_QNAME = new QName("http://raservice.mobileid.vn/", "enrollCertificateDSResponse");
    private final static QName _GenerateKeystoreResponse_QNAME = new QName("http://raservice.mobileid.vn/", "generateKeystoreResponse");
    private final static QName _ReloadSystemConfiguarion_QNAME = new QName("http://raservice.mobileid.vn/", "reloadSystemConfiguarion");
    private final static QName _ReloadSystemConfiguarionResponse_QNAME = new QName("http://raservice.mobileid.vn/", "reloadSystemConfiguarionResponse");
    private final static QName _EnrollCertificateResponse_QNAME = new QName("http://raservice.mobileid.vn/", "enrollCertificateResponse");
    private final static QName _SendMailResponse_QNAME = new QName("http://raservice.mobileid.vn/", "sendMailResponse");
    private final static QName _GenerateDigitalCertificationResponse_QNAME = new QName("http://raservice.mobileid.vn/", "generateDigitalCertificationResponse");
    private final static QName _RevokeResponse_QNAME = new QName("http://raservice.mobileid.vn/", "revokeResponse");
    private final static QName _GenerateDigitalCertification_QNAME = new QName("http://raservice.mobileid.vn/", "generateDigitalCertification");
    private final static QName _UatResponse_QNAME = new QName("http://raservice.mobileid.vn/", "uatResponse");
    private final static QName _UpdateTokenSOPIN_QNAME = new QName("http://raservice.mobileid.vn/", "updateTokenSOPIN");
    private final static QName _EnrollCertificateMLSignerResponse_QNAME = new QName("http://raservice.mobileid.vn/", "enrollCertificateMLSignerResponse");
    private final static QName _Uat_QNAME = new QName("http://raservice.mobileid.vn/", "uat");
    private final static QName _NotifyForgottenPasswordToUserResponse_QNAME = new QName("http://raservice.mobileid.vn/", "notifyForgottenPasswordToUserResponse");
    private final static QName _IOException_QNAME = new QName("http://raservice.mobileid.vn/", "IOException");
    private final static QName _GenerateKeystore_QNAME = new QName("http://raservice.mobileid.vn/", "generateKeystore");
    private final static QName _CertificateEncodingException_QNAME = new QName("http://raservice.mobileid.vn/", "CertificateEncodingException");
    private final static QName _UpdateTokenSOPINResponse_QNAME = new QName("http://raservice.mobileid.vn/", "updateTokenSOPINResponse");
    private final static QName _GetTokenInfoResponse_QNAME = new QName("http://raservice.mobileid.vn/", "getTokenInfoResponse");
    private final static QName _NotifyForgottenPasswordToUser_QNAME = new QName("http://raservice.mobileid.vn/", "notifyForgottenPasswordToUser");
    private final static QName _SendAuthenticationCodeResponse_QNAME = new QName("http://raservice.mobileid.vn/", "sendAuthenticationCodeResponse");
    private final static QName _SignFile_QNAME = new QName("http://raservice.mobileid.vn/", "signFile");
    private final static QName _GetEnterpriseInfoResponse_QNAME = new QName("http://raservice.mobileid.vn/", "getEnterpriseInfoResponse");
    private final static QName _EnrollCertificateIS_QNAME = new QName("http://raservice.mobileid.vn/", "enrollCertificateIS");
    private final static QName _EnrollCertificateDLSignerResponse_QNAME = new QName("http://raservice.mobileid.vn/", "enrollCertificateDLSignerResponse");
    private final static QName _EnrollCertificateMLSigner_QNAME = new QName("http://raservice.mobileid.vn/", "enrollCertificateMLSigner");
    private final static QName _EnrollCertificateDLSigner_QNAME = new QName("http://raservice.mobileid.vn/", "enrollCertificateDLSigner");
    private final static QName _SignFileResponse_QNAME = new QName("http://raservice.mobileid.vn/", "signFileResponse");
    private final static QName _EnrollCertificateISResponse_QNAME = new QName("http://raservice.mobileid.vn/", "enrollCertificateISResponse");
    private final static QName _EnrollCertificate_QNAME = new QName("http://raservice.mobileid.vn/", "enrollCertificate");
    private final static QName _EnrollCertificateDS_QNAME = new QName("http://raservice.mobileid.vn/", "enrollCertificateDS");
    private final static QName _GetEnterpriseInfo_QNAME = new QName("http://raservice.mobileid.vn/", "getEnterpriseInfo");
    private final static QName _SendAuthenticationCode_QNAME = new QName("http://raservice.mobileid.vn/", "sendAuthenticationCode");
    private final static QName _GetTokenInfo_QNAME = new QName("http://raservice.mobileid.vn/", "getTokenInfo");
    private final static QName _Revoke_QNAME = new QName("http://raservice.mobileid.vn/", "revoke");
    private final static QName _SendMail_QNAME = new QName("http://raservice.mobileid.vn/", "sendMail");
    private final static QName _SetCustomerInfoResponse_QNAME = new QName("http://raservice.mobileid.vn/", "setCustomerInfoResponse");
    private final static QName _NoSuchAlgorithmException_QNAME = new QName("http://raservice.mobileid.vn/", "NoSuchAlgorithmException");
    private final static QName _NotifyPasswordToNewUser_QNAME = new QName("http://raservice.mobileid.vn/", "notifyPasswordToNewUser");
    private final static QName _NotifyPasswordToNewUserResponse_QNAME = new QName("http://raservice.mobileid.vn/", "notifyPasswordToNewUserResponse");
    private final static QName _SetCustomerInfo_QNAME = new QName("http://raservice.mobileid.vn/", "setCustomerInfo");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: vn.ra.raconnector
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SignFileResponse }
     * 
     */
    public SignFileResponse createSignFileResponse() {
        return new SignFileResponse();
    }

    /**
     * Create an instance of {@link EnrollCertificate }
     * 
     */
    public EnrollCertificate createEnrollCertificate() {
        return new EnrollCertificate();
    }

    /**
     * Create an instance of {@link EnrollCertificateDS }
     * 
     */
    public EnrollCertificateDS createEnrollCertificateDS() {
        return new EnrollCertificateDS();
    }

    /**
     * Create an instance of {@link GetEnterpriseInfo }
     * 
     */
    public GetEnterpriseInfo createGetEnterpriseInfo() {
        return new GetEnterpriseInfo();
    }

    /**
     * Create an instance of {@link EnrollCertificateISResponse }
     * 
     */
    public EnrollCertificateISResponse createEnrollCertificateISResponse() {
        return new EnrollCertificateISResponse();
    }

    /**
     * Create an instance of {@link SendAuthenticationCode }
     * 
     */
    public SendAuthenticationCode createSendAuthenticationCode() {
        return new SendAuthenticationCode();
    }

    /**
     * Create an instance of {@link GetTokenInfo }
     * 
     */
    public GetTokenInfo createGetTokenInfo() {
        return new GetTokenInfo();
    }

    /**
     * Create an instance of {@link SendMail }
     * 
     */
    public SendMail createSendMail() {
        return new SendMail();
    }

    /**
     * Create an instance of {@link SetCustomerInfoResponse }
     * 
     */
    public SetCustomerInfoResponse createSetCustomerInfoResponse() {
        return new SetCustomerInfoResponse();
    }

    /**
     * Create an instance of {@link Revoke }
     * 
     */
    public Revoke createRevoke() {
        return new Revoke();
    }

    /**
     * Create an instance of {@link NoSuchAlgorithmException }
     * 
     */
    public NoSuchAlgorithmException createNoSuchAlgorithmException() {
        return new NoSuchAlgorithmException();
    }

    /**
     * Create an instance of {@link NotifyPasswordToNewUser }
     * 
     */
    public NotifyPasswordToNewUser createNotifyPasswordToNewUser() {
        return new NotifyPasswordToNewUser();
    }

    /**
     * Create an instance of {@link NotifyPasswordToNewUserResponse }
     * 
     */
    public NotifyPasswordToNewUserResponse createNotifyPasswordToNewUserResponse() {
        return new NotifyPasswordToNewUserResponse();
    }

    /**
     * Create an instance of {@link SetCustomerInfo }
     * 
     */
    public SetCustomerInfo createSetCustomerInfo() {
        return new SetCustomerInfo();
    }

    /**
     * Create an instance of {@link SignFile }
     * 
     */
    public SignFile createSignFile() {
        return new SignFile();
    }

    /**
     * Create an instance of {@link GetEnterpriseInfoResponse }
     * 
     */
    public GetEnterpriseInfoResponse createGetEnterpriseInfoResponse() {
        return new GetEnterpriseInfoResponse();
    }

    /**
     * Create an instance of {@link EnrollCertificateDLSignerResponse }
     * 
     */
    public EnrollCertificateDLSignerResponse createEnrollCertificateDLSignerResponse() {
        return new EnrollCertificateDLSignerResponse();
    }

    /**
     * Create an instance of {@link EnrollCertificateIS }
     * 
     */
    public EnrollCertificateIS createEnrollCertificateIS() {
        return new EnrollCertificateIS();
    }

    /**
     * Create an instance of {@link EnrollCertificateMLSigner }
     * 
     */
    public EnrollCertificateMLSigner createEnrollCertificateMLSigner() {
        return new EnrollCertificateMLSigner();
    }

    /**
     * Create an instance of {@link EnrollCertificateDLSigner }
     * 
     */
    public EnrollCertificateDLSigner createEnrollCertificateDLSigner() {
        return new EnrollCertificateDLSigner();
    }

    /**
     * Create an instance of {@link RevokeResponse }
     * 
     */
    public RevokeResponse createRevokeResponse() {
        return new RevokeResponse();
    }

    /**
     * Create an instance of {@link UatResponse }
     * 
     */
    public UatResponse createUatResponse() {
        return new UatResponse();
    }

    /**
     * Create an instance of {@link GenerateDigitalCertification }
     * 
     */
    public GenerateDigitalCertification createGenerateDigitalCertification() {
        return new GenerateDigitalCertification();
    }

    /**
     * Create an instance of {@link EnrollCertificateMLSignerResponse }
     * 
     */
    public EnrollCertificateMLSignerResponse createEnrollCertificateMLSignerResponse() {
        return new EnrollCertificateMLSignerResponse();
    }

    /**
     * Create an instance of {@link UpdateTokenSOPIN }
     * 
     */
    public UpdateTokenSOPIN createUpdateTokenSOPIN() {
        return new UpdateTokenSOPIN();
    }

    /**
     * Create an instance of {@link NotifyForgottenPasswordToUserResponse }
     * 
     */
    public NotifyForgottenPasswordToUserResponse createNotifyForgottenPasswordToUserResponse() {
        return new NotifyForgottenPasswordToUserResponse();
    }

    /**
     * Create an instance of {@link Uat }
     * 
     */
    public Uat createUat() {
        return new Uat();
    }

    /**
     * Create an instance of {@link CertificateEncodingException }
     * 
     */
    public CertificateEncodingException createCertificateEncodingException() {
        return new CertificateEncodingException();
    }

    /**
     * Create an instance of {@link IOException }
     * 
     */
    public IOException createIOException() {
        return new IOException();
    }

    /**
     * Create an instance of {@link GenerateKeystore }
     * 
     */
    public GenerateKeystore createGenerateKeystore() {
        return new GenerateKeystore();
    }

    /**
     * Create an instance of {@link GetTokenInfoResponse }
     * 
     */
    public GetTokenInfoResponse createGetTokenInfoResponse() {
        return new GetTokenInfoResponse();
    }

    /**
     * Create an instance of {@link NotifyForgottenPasswordToUser }
     * 
     */
    public NotifyForgottenPasswordToUser createNotifyForgottenPasswordToUser() {
        return new NotifyForgottenPasswordToUser();
    }

    /**
     * Create an instance of {@link SendAuthenticationCodeResponse }
     * 
     */
    public SendAuthenticationCodeResponse createSendAuthenticationCodeResponse() {
        return new SendAuthenticationCodeResponse();
    }

    /**
     * Create an instance of {@link UpdateTokenSOPINResponse }
     * 
     */
    public UpdateTokenSOPINResponse createUpdateTokenSOPINResponse() {
        return new UpdateTokenSOPINResponse();
    }

    /**
     * Create an instance of {@link EnrollCertificateDSResponse }
     * 
     */
    public EnrollCertificateDSResponse createEnrollCertificateDSResponse() {
        return new EnrollCertificateDSResponse();
    }

    /**
     * Create an instance of {@link GenerateKeystoreResponse }
     * 
     */
    public GenerateKeystoreResponse createGenerateKeystoreResponse() {
        return new GenerateKeystoreResponse();
    }

    /**
     * Create an instance of {@link ReloadSystemConfiguarion }
     * 
     */
    public ReloadSystemConfiguarion createReloadSystemConfiguarion() {
        return new ReloadSystemConfiguarion();
    }

    /**
     * Create an instance of {@link ReloadSystemConfiguarionResponse }
     * 
     */
    public ReloadSystemConfiguarionResponse createReloadSystemConfiguarionResponse() {
        return new ReloadSystemConfiguarionResponse();
    }

    /**
     * Create an instance of {@link EnrollCertificateResponse }
     * 
     */
    public EnrollCertificateResponse createEnrollCertificateResponse() {
        return new EnrollCertificateResponse();
    }

    /**
     * Create an instance of {@link SendMailResponse }
     * 
     */
    public SendMailResponse createSendMailResponse() {
        return new SendMailResponse();
    }

    /**
     * Create an instance of {@link GenerateDigitalCertificationResponse }
     * 
     */
    public GenerateDigitalCertificationResponse createGenerateDigitalCertificationResponse() {
        return new GenerateDigitalCertificationResponse();
    }

    /**
     * Create an instance of {@link Attachment }
     * 
     */
    public Attachment createAttachment() {
        return new Attachment();
    }

    /**
     * Create an instance of {@link EnterpriseInfo }
     * 
     */
    public EnterpriseInfo createEnterpriseInfo() {
        return new EnterpriseInfo();
    }

    /**
     * Create an instance of {@link MailComponent }
     * 
     */
    public MailComponent createMailComponent() {
        return new MailComponent();
    }

    /**
     * Create an instance of {@link RegistrationAuthorityWSRequest }
     * 
     */
    public RegistrationAuthorityWSRequest createRegistrationAuthorityWSRequest() {
        return new RegistrationAuthorityWSRequest();
    }

    /**
     * Create an instance of {@link Certification }
     * 
     */
    public Certification createCertification() {
        return new Certification();
    }

    /**
     * Create an instance of {@link RegistrationAuthorityWSResponse }
     * 
     */
    public RegistrationAuthorityWSResponse createRegistrationAuthorityWSResponse() {
        return new RegistrationAuthorityWSResponse();
    }

    /**
     * Create an instance of {@link RaPortalUser }
     * 
     */
    public RaPortalUser createRaPortalUser() {
        return new RaPortalUser();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EnrollCertificateDSResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://raservice.mobileid.vn/", name = "enrollCertificateDSResponse")
    public JAXBElement<EnrollCertificateDSResponse> createEnrollCertificateDSResponse(EnrollCertificateDSResponse value) {
        return new JAXBElement<EnrollCertificateDSResponse>(_EnrollCertificateDSResponse_QNAME, EnrollCertificateDSResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GenerateKeystoreResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://raservice.mobileid.vn/", name = "generateKeystoreResponse")
    public JAXBElement<GenerateKeystoreResponse> createGenerateKeystoreResponse(GenerateKeystoreResponse value) {
        return new JAXBElement<GenerateKeystoreResponse>(_GenerateKeystoreResponse_QNAME, GenerateKeystoreResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReloadSystemConfiguarion }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://raservice.mobileid.vn/", name = "reloadSystemConfiguarion")
    public JAXBElement<ReloadSystemConfiguarion> createReloadSystemConfiguarion(ReloadSystemConfiguarion value) {
        return new JAXBElement<ReloadSystemConfiguarion>(_ReloadSystemConfiguarion_QNAME, ReloadSystemConfiguarion.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReloadSystemConfiguarionResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://raservice.mobileid.vn/", name = "reloadSystemConfiguarionResponse")
    public JAXBElement<ReloadSystemConfiguarionResponse> createReloadSystemConfiguarionResponse(ReloadSystemConfiguarionResponse value) {
        return new JAXBElement<ReloadSystemConfiguarionResponse>(_ReloadSystemConfiguarionResponse_QNAME, ReloadSystemConfiguarionResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EnrollCertificateResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://raservice.mobileid.vn/", name = "enrollCertificateResponse")
    public JAXBElement<EnrollCertificateResponse> createEnrollCertificateResponse(EnrollCertificateResponse value) {
        return new JAXBElement<EnrollCertificateResponse>(_EnrollCertificateResponse_QNAME, EnrollCertificateResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SendMailResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://raservice.mobileid.vn/", name = "sendMailResponse")
    public JAXBElement<SendMailResponse> createSendMailResponse(SendMailResponse value) {
        return new JAXBElement<SendMailResponse>(_SendMailResponse_QNAME, SendMailResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GenerateDigitalCertificationResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://raservice.mobileid.vn/", name = "generateDigitalCertificationResponse")
    public JAXBElement<GenerateDigitalCertificationResponse> createGenerateDigitalCertificationResponse(GenerateDigitalCertificationResponse value) {
        return new JAXBElement<GenerateDigitalCertificationResponse>(_GenerateDigitalCertificationResponse_QNAME, GenerateDigitalCertificationResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RevokeResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://raservice.mobileid.vn/", name = "revokeResponse")
    public JAXBElement<RevokeResponse> createRevokeResponse(RevokeResponse value) {
        return new JAXBElement<RevokeResponse>(_RevokeResponse_QNAME, RevokeResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GenerateDigitalCertification }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://raservice.mobileid.vn/", name = "generateDigitalCertification")
    public JAXBElement<GenerateDigitalCertification> createGenerateDigitalCertification(GenerateDigitalCertification value) {
        return new JAXBElement<GenerateDigitalCertification>(_GenerateDigitalCertification_QNAME, GenerateDigitalCertification.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UatResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://raservice.mobileid.vn/", name = "uatResponse")
    public JAXBElement<UatResponse> createUatResponse(UatResponse value) {
        return new JAXBElement<UatResponse>(_UatResponse_QNAME, UatResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateTokenSOPIN }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://raservice.mobileid.vn/", name = "updateTokenSOPIN")
    public JAXBElement<UpdateTokenSOPIN> createUpdateTokenSOPIN(UpdateTokenSOPIN value) {
        return new JAXBElement<UpdateTokenSOPIN>(_UpdateTokenSOPIN_QNAME, UpdateTokenSOPIN.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EnrollCertificateMLSignerResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://raservice.mobileid.vn/", name = "enrollCertificateMLSignerResponse")
    public JAXBElement<EnrollCertificateMLSignerResponse> createEnrollCertificateMLSignerResponse(EnrollCertificateMLSignerResponse value) {
        return new JAXBElement<EnrollCertificateMLSignerResponse>(_EnrollCertificateMLSignerResponse_QNAME, EnrollCertificateMLSignerResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Uat }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://raservice.mobileid.vn/", name = "uat")
    public JAXBElement<Uat> createUat(Uat value) {
        return new JAXBElement<Uat>(_Uat_QNAME, Uat.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NotifyForgottenPasswordToUserResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://raservice.mobileid.vn/", name = "notifyForgottenPasswordToUserResponse")
    public JAXBElement<NotifyForgottenPasswordToUserResponse> createNotifyForgottenPasswordToUserResponse(NotifyForgottenPasswordToUserResponse value) {
        return new JAXBElement<NotifyForgottenPasswordToUserResponse>(_NotifyForgottenPasswordToUserResponse_QNAME, NotifyForgottenPasswordToUserResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IOException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://raservice.mobileid.vn/", name = "IOException")
    public JAXBElement<IOException> createIOException(IOException value) {
        return new JAXBElement<IOException>(_IOException_QNAME, IOException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GenerateKeystore }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://raservice.mobileid.vn/", name = "generateKeystore")
    public JAXBElement<GenerateKeystore> createGenerateKeystore(GenerateKeystore value) {
        return new JAXBElement<GenerateKeystore>(_GenerateKeystore_QNAME, GenerateKeystore.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CertificateEncodingException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://raservice.mobileid.vn/", name = "CertificateEncodingException")
    public JAXBElement<CertificateEncodingException> createCertificateEncodingException(CertificateEncodingException value) {
        return new JAXBElement<CertificateEncodingException>(_CertificateEncodingException_QNAME, CertificateEncodingException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateTokenSOPINResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://raservice.mobileid.vn/", name = "updateTokenSOPINResponse")
    public JAXBElement<UpdateTokenSOPINResponse> createUpdateTokenSOPINResponse(UpdateTokenSOPINResponse value) {
        return new JAXBElement<UpdateTokenSOPINResponse>(_UpdateTokenSOPINResponse_QNAME, UpdateTokenSOPINResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetTokenInfoResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://raservice.mobileid.vn/", name = "getTokenInfoResponse")
    public JAXBElement<GetTokenInfoResponse> createGetTokenInfoResponse(GetTokenInfoResponse value) {
        return new JAXBElement<GetTokenInfoResponse>(_GetTokenInfoResponse_QNAME, GetTokenInfoResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NotifyForgottenPasswordToUser }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://raservice.mobileid.vn/", name = "notifyForgottenPasswordToUser")
    public JAXBElement<NotifyForgottenPasswordToUser> createNotifyForgottenPasswordToUser(NotifyForgottenPasswordToUser value) {
        return new JAXBElement<NotifyForgottenPasswordToUser>(_NotifyForgottenPasswordToUser_QNAME, NotifyForgottenPasswordToUser.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SendAuthenticationCodeResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://raservice.mobileid.vn/", name = "sendAuthenticationCodeResponse")
    public JAXBElement<SendAuthenticationCodeResponse> createSendAuthenticationCodeResponse(SendAuthenticationCodeResponse value) {
        return new JAXBElement<SendAuthenticationCodeResponse>(_SendAuthenticationCodeResponse_QNAME, SendAuthenticationCodeResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SignFile }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://raservice.mobileid.vn/", name = "signFile")
    public JAXBElement<SignFile> createSignFile(SignFile value) {
        return new JAXBElement<SignFile>(_SignFile_QNAME, SignFile.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetEnterpriseInfoResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://raservice.mobileid.vn/", name = "getEnterpriseInfoResponse")
    public JAXBElement<GetEnterpriseInfoResponse> createGetEnterpriseInfoResponse(GetEnterpriseInfoResponse value) {
        return new JAXBElement<GetEnterpriseInfoResponse>(_GetEnterpriseInfoResponse_QNAME, GetEnterpriseInfoResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EnrollCertificateIS }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://raservice.mobileid.vn/", name = "enrollCertificateIS")
    public JAXBElement<EnrollCertificateIS> createEnrollCertificateIS(EnrollCertificateIS value) {
        return new JAXBElement<EnrollCertificateIS>(_EnrollCertificateIS_QNAME, EnrollCertificateIS.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EnrollCertificateDLSignerResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://raservice.mobileid.vn/", name = "enrollCertificateDLSignerResponse")
    public JAXBElement<EnrollCertificateDLSignerResponse> createEnrollCertificateDLSignerResponse(EnrollCertificateDLSignerResponse value) {
        return new JAXBElement<EnrollCertificateDLSignerResponse>(_EnrollCertificateDLSignerResponse_QNAME, EnrollCertificateDLSignerResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EnrollCertificateMLSigner }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://raservice.mobileid.vn/", name = "enrollCertificateMLSigner")
    public JAXBElement<EnrollCertificateMLSigner> createEnrollCertificateMLSigner(EnrollCertificateMLSigner value) {
        return new JAXBElement<EnrollCertificateMLSigner>(_EnrollCertificateMLSigner_QNAME, EnrollCertificateMLSigner.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EnrollCertificateDLSigner }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://raservice.mobileid.vn/", name = "enrollCertificateDLSigner")
    public JAXBElement<EnrollCertificateDLSigner> createEnrollCertificateDLSigner(EnrollCertificateDLSigner value) {
        return new JAXBElement<EnrollCertificateDLSigner>(_EnrollCertificateDLSigner_QNAME, EnrollCertificateDLSigner.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SignFileResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://raservice.mobileid.vn/", name = "signFileResponse")
    public JAXBElement<SignFileResponse> createSignFileResponse(SignFileResponse value) {
        return new JAXBElement<SignFileResponse>(_SignFileResponse_QNAME, SignFileResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EnrollCertificateISResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://raservice.mobileid.vn/", name = "enrollCertificateISResponse")
    public JAXBElement<EnrollCertificateISResponse> createEnrollCertificateISResponse(EnrollCertificateISResponse value) {
        return new JAXBElement<EnrollCertificateISResponse>(_EnrollCertificateISResponse_QNAME, EnrollCertificateISResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EnrollCertificate }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://raservice.mobileid.vn/", name = "enrollCertificate")
    public JAXBElement<EnrollCertificate> createEnrollCertificate(EnrollCertificate value) {
        return new JAXBElement<EnrollCertificate>(_EnrollCertificate_QNAME, EnrollCertificate.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EnrollCertificateDS }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://raservice.mobileid.vn/", name = "enrollCertificateDS")
    public JAXBElement<EnrollCertificateDS> createEnrollCertificateDS(EnrollCertificateDS value) {
        return new JAXBElement<EnrollCertificateDS>(_EnrollCertificateDS_QNAME, EnrollCertificateDS.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetEnterpriseInfo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://raservice.mobileid.vn/", name = "getEnterpriseInfo")
    public JAXBElement<GetEnterpriseInfo> createGetEnterpriseInfo(GetEnterpriseInfo value) {
        return new JAXBElement<GetEnterpriseInfo>(_GetEnterpriseInfo_QNAME, GetEnterpriseInfo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SendAuthenticationCode }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://raservice.mobileid.vn/", name = "sendAuthenticationCode")
    public JAXBElement<SendAuthenticationCode> createSendAuthenticationCode(SendAuthenticationCode value) {
        return new JAXBElement<SendAuthenticationCode>(_SendAuthenticationCode_QNAME, SendAuthenticationCode.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetTokenInfo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://raservice.mobileid.vn/", name = "getTokenInfo")
    public JAXBElement<GetTokenInfo> createGetTokenInfo(GetTokenInfo value) {
        return new JAXBElement<GetTokenInfo>(_GetTokenInfo_QNAME, GetTokenInfo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Revoke }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://raservice.mobileid.vn/", name = "revoke")
    public JAXBElement<Revoke> createRevoke(Revoke value) {
        return new JAXBElement<Revoke>(_Revoke_QNAME, Revoke.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SendMail }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://raservice.mobileid.vn/", name = "sendMail")
    public JAXBElement<SendMail> createSendMail(SendMail value) {
        return new JAXBElement<SendMail>(_SendMail_QNAME, SendMail.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SetCustomerInfoResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://raservice.mobileid.vn/", name = "setCustomerInfoResponse")
    public JAXBElement<SetCustomerInfoResponse> createSetCustomerInfoResponse(SetCustomerInfoResponse value) {
        return new JAXBElement<SetCustomerInfoResponse>(_SetCustomerInfoResponse_QNAME, SetCustomerInfoResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NoSuchAlgorithmException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://raservice.mobileid.vn/", name = "NoSuchAlgorithmException")
    public JAXBElement<NoSuchAlgorithmException> createNoSuchAlgorithmException(NoSuchAlgorithmException value) {
        return new JAXBElement<NoSuchAlgorithmException>(_NoSuchAlgorithmException_QNAME, NoSuchAlgorithmException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NotifyPasswordToNewUser }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://raservice.mobileid.vn/", name = "notifyPasswordToNewUser")
    public JAXBElement<NotifyPasswordToNewUser> createNotifyPasswordToNewUser(NotifyPasswordToNewUser value) {
        return new JAXBElement<NotifyPasswordToNewUser>(_NotifyPasswordToNewUser_QNAME, NotifyPasswordToNewUser.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NotifyPasswordToNewUserResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://raservice.mobileid.vn/", name = "notifyPasswordToNewUserResponse")
    public JAXBElement<NotifyPasswordToNewUserResponse> createNotifyPasswordToNewUserResponse(NotifyPasswordToNewUserResponse value) {
        return new JAXBElement<NotifyPasswordToNewUserResponse>(_NotifyPasswordToNewUserResponse_QNAME, NotifyPasswordToNewUserResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SetCustomerInfo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://raservice.mobileid.vn/", name = "setCustomerInfo")
    public JAXBElement<SetCustomerInfo> createSetCustomerInfo(SetCustomerInfo value) {
        return new JAXBElement<SetCustomerInfo>(_SetCustomerInfo_QNAME, SetCustomerInfo.class, null, value);
    }

}
