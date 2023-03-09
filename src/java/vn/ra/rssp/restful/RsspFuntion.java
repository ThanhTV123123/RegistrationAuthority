/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.rssp.restful;

/**
 *
 * @author USER
 */
public enum RsspFuntion {
    info("info"),
    auth_login("auth/login"),
    auth_login_ssl_only("auth/login"),
    auth_revoke("auth/revoke"),
    credential_list("credentials/list"),
    credential_info("credentials/info"),
    credential_sendOTP("credentials/sendOTP"),
    credential_authorize("credentials/authorize"),
    credential_extendTransaction("credentials/extendTransaction"),
    signatures_signHash("signatures/signHash"),
    owner_list("owner/list"),
    owner_assign("owner/assign"),
    owner_changePassword("owner/changePassword"),
    owner_resetPassword("owner/resetPassword"),
    owner_changeEmail("owner/changeEmail"),
    credentials_changePassphrase("credentials/changePassphrase"),
    credentials_resetPassphrase("credentials/resetPassphrase"),
    credentials_changeEmail("credentials/changeEmail"),
    credentials_changePhone("credentials/changePhone"),
    auth_changePassWord("auth/changePassWord"),
    owner_create("owner/create"),
    agreement_assign("agreements/assign");

    private final String method;

    private RsspFuntion(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }
}
