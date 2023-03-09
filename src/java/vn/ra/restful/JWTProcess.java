/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.restful;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import java.security.SecureRandom;
import java.util.Base64;
import org.apache.log4j.Logger;
import vn.ra.process.CommonFunction;

/**
 *
 * @author USER
 */
public class JWTProcess {

    private static final Logger log = Logger.getLogger(JWTProcess.class);
    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;

    private JWTProcess() {
        super();
    }

    public static String generalSecretToken(String sSECRET_KEY, String username,
            long ttlMillis) {
        //// String issuer, long ttlMillis

        //The JWT signature algorithm we will be using to sign the token
//        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //We will sign our JWT with our ApiKey secret
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(sSECRET_KEY);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, SIGNATURE_ALGORITHM.getJcaName());

        //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder().setIssuedAt(now)
                .setSubject(username)
                .signWith(SIGNATURE_ALGORITHM, signingKey);

        //if it has been specified, let's add the expiration
        if (ttlMillis > 0) {
//            long expMillis = nowMillis + EXPIRATION_LIMIT_IN_MINUTES;
//            Date exp = new Date(expMillis);
            long expirationTimeInMilliSeconds = TimeUnit.MINUTES.toMillis(ttlMillis);
            Date exp = new Date(nowMillis + expirationTimeInMilliSeconds);
            builder.setExpiration(exp);
        }
        //Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }

    public static Claims parseSecretToken(String jwt, String sSECRET_KEY, int[] intRes) {
        Claims sClaims = null;
        try {
        // This line will throw an exception if it is not a signed JWS (as expected)
            sClaims = Jwts.parser() // Configured and then used to parse JWT strings
                    .setSigningKey(DatatypeConverter.parseBase64Binary(sSECRET_KEY)) // Sets the signing key used to verify
                    // any discovered JWS digital signature
                    .parseClaimsJws(jwt) // Parses the specified compact serialized JWS string based
                    .getBody();
            intRes[0] = 0;
        } catch(ExpiredJwtException e) {
            intRes[0] = 1;
            CommonFunction.LogExceptionServlet(log, "Expired Time: " + e.getMessage(), e);
        } catch(SignatureException e) {
            intRes[0] = 2;
            CommonFunction.LogExceptionServlet(log, "Signature Invalid: " + e.getMessage(), e);
        } catch(Exception e) {
            intRes[0] = 3;
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        }
        return sClaims;
    }

    public static boolean checkTimeTokenValid(String jwt, String sSECRET_KEY) {
//        boolean validation = false;
        return Jwts.parser().setSigningKey(sSECRET_KEY).isSigned(jwt);// .parseClaimsJws(jwt).getBody().getSubject();
//        try {
//            return Jwts.parser().setSigningKey(sSECRET_KEY).isSigned(jwt);// .parseClaimsJws(jwt).getBody().getSubject();
////            validation = true;
//        } catch (ExpiredJwtException e) {
//            CommonFunction.LogExceptionServlet(log, "checkTimeTokenValid: " + e.getMessage(), e);
//        } catch (SignatureException e) {
//            CommonFunction.LogExceptionServlet(log, "checkTimeTokenValid: " + e.getMessage(), e);
//        } catch (Exception e) {
//            CommonFunction.LogExceptionServlet(log, "checkTimeTokenValid: " + e.getMessage(), e);
//        }
//        return validation;
    }

    public static String generateSecretKeyJWT(int intByte) {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[intByte];
        random.nextBytes(bytes);
        Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
        String token = encoder.encodeToString(bytes);
        return token;
    }
}
