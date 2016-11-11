package com.tenduke.client.android.sample.singletons;

import android.support.annotation.Nullable;

import com.tenduke.client.ApiCredentials;

/** Session singleton.
 */
public enum Session {

    /** Singleton instance.
     */
    INSTANCE;

    private static final Object _lock = new Object ();

    private ApiCredentials _credentials;


    /** Logs out the session: Simply clears the login-state.
     *
     */
    public void logout () {
        _credentials = null;
    }


    /** Marks that the user has a valid session (does NOT actually log the user in).
     *
     *  @param credentials the credentials
     */
    public void logIn(@Nullable final ApiCredentials credentials) {
        //
        if (credentials != null) {
            synchronized (_lock) {
                logout();
                _credentials = credentials;
            }
        }
    }


    /** Retrieves current API-credentials.
     *
     *  @return Current API-credentials. Returns {@code null} if the user is not logged in.
     */
    public @Nullable ApiCredentials get () {
        return _credentials;
    }



    /** Checks if the user is logged in.
     *
     *  @return {@code true} if user is logged in. Returns {@code false} otherwise.
     */
    public boolean isLoggedOn () {
        return _credentials != null;
    }

}
