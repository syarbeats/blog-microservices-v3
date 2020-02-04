/**
 * <h1>User Context</h1>
 * This class will be used to save token
 * for internal purpose
 *
 * @author Syarif Hidayat
 * @version 1.0
 * @since 2019-08-20
 * */

package com.mitrais.cdc.blogmicroservices.utility;

import org.springframework.stereotype.Component;

@Component
public class UserContext {

    private String authToken= new String();

    /**
     * This method will be used to get token
     *
     * @return will return Jwt Token
     */
    public String getAuthToken() {
        return authToken;
    }

    /**
     * This method will be used to set JwtToken.
     *
     * @param authToken
     */
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

}