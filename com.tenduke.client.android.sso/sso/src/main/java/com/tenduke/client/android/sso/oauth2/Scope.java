package com.tenduke.client.android.sso.oauth2;

import android.support.annotation.NonNull;

/** Supported OAuth2 scopes.
 *
 */
public enum Scope {

    OPENID ("openid"),
    EMAIL ("email"),
    PROFILE ("profile")
    ;

    private final String _parameterValue;

    Scope (@NonNull final String parameterValue) {
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
