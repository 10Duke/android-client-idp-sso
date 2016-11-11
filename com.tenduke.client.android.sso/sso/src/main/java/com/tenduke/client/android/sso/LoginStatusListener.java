package com.tenduke.client.android.sso;

import android.support.annotation.NonNull;

import com.tenduke.client.ApiCredentials;

/** Listener-interface for login status events.
 *
 */
public interface LoginStatusListener {

    /** Called when error in the login process occurred (could be network-error, HTTP-error, sign-on-error).
     *
     * @param error the error
     */
    void onLoginError (@NonNull SSOError error);


    /** Called when login is successful.
     *
     *  @param session the session.
     */
    void onLoginSuccess(@NonNull ApiCredentials session);

}
