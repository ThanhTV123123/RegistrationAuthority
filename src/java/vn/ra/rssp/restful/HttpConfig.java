/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.rssp.restful;

import java.net.HttpURLConnection;

/**
 *
 * @author USER
 */
public interface HttpConfig {
    
    public HttpURLConnection getHttpConnection(String endpointUrl);
    
}
