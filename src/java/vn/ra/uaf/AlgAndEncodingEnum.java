/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.uaf;

/**
 *
 * @author THANH-PC
 */
public enum AlgAndEncodingEnum {

    UAF_ALG_SIGN_SECP256R1_ECDSA_SHA256_RAW(0x01),
    UAF_ALG_SIGN_SECP256R1_ECDSA_SHA256_DER(0x02),
    UAF_ALG_SIGN_RSASSA_PSS_SHA256_RAW(0x03),
    UAF_ALG_SIGN_RSASSA_PSS_SHA256_DER(0x04),
    UAF_ALG_KEY_ECC_X962_RAW(0x100),
    UAF_ALG_KEY_ECC_X962_DER(0x101),
    UAF_ALG_KEY_RSA_2048_PSS_RAW(0x102),
    UAF_ALG_KEY_RSA_2048_PSS_DER(0x103),
    UAF_ALG_SIGN_SECP256K1_ECDSA_SHA256_RAW(0x05),
    UAF_ALG_SIGN_SECP256K1_ECDSA_SHA256_DER(0x06);

    public final int id;

    AlgAndEncodingEnum(int id) {
        this.id = id;
    }

    public static AlgAndEncodingEnum get(int id) {
        for (AlgAndEncodingEnum tag : AlgAndEncodingEnum.values()) {
            if (tag.id == id) {
                return tag;
            }
        }
        return null;
    }
//    public static AlgAndEncodingEnum get(Tag info) {
//        int id = (int) info.value[3] + (int) info.value[4] * 256;
//        return get(id);
//    }
}
