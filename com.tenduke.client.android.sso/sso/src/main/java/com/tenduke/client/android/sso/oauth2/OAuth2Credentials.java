package com.tenduke.client.android.sso.oauth2;

import android.support.annotation.NonNull;

import com.tenduke.client.ApiCredentials;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/** Simple implementation of ApiCredentials for OAuth2.
 *
 */
public class OAuth2Credentials implements ApiCredentials {
    //
    private String _customState;
    private long _expiresInSeconds;
    private String _idToken;
    private String _token;
    private Date _tokenGranted;
    private String _tokenType;

    private final Map<String, String> _headers = new HashMap <> ();

    /** Constructs new instance
     *
     * @param tokenType token type
     * @param token token
     * @param expiresInSeconds seconds this expires
     * @param idToken id-token
     * @param customState custom state
     */
    public OAuth2Credentials(
            final String tokenType,
            final String token,
            final long expiresInSeconds,
            final String idToken,
            final String customState) {
        //
        _customState = customState;
        _expiresInSeconds = expiresInSeconds;
        _idToken = idToken;
        _token = token;
        _tokenType = tokenType;
        _tokenGranted = new Date ();

        if (_token != null) {
            final StringBuilder header = new StringBuilder (token.length() + 10);
            if (_tokenType == null) {
                header.append ("Bearer ");
            }
            else {
                header.append (_tokenType);
                header.append (' ');
            }
            header.append (_token);
            _headers.put ("Authorization", header.toString());
        }
    }

    @Override
    public @NonNull Map<String, String> getHeaders() {
        //
        return _headers;
    }

}
