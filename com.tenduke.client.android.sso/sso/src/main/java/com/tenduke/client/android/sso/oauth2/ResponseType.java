package com.tenduke.client.android.sso.oauth2;

import android.support.annotation.NonNull;

/** Supported OAuth2 response types.
 *
 */
public enum ResponseType {

    TOKEN ("token"),
    ID_TOKEN ("id_token")
    ;

    private final String _parameterValue;

    ResponseType(@NonNull final String parameterValue) {
        _parameterValue = parameterValue;
    }

    @Override
    public String toString() {
        return getParameterValue();
    }

    public String getParameterValue() {
        return _parameterValue;
    }
}
