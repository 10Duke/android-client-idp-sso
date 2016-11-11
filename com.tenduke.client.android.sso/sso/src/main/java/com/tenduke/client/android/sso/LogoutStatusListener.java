package com.tenduke.client.android.sso;

import android.support.annotation.NonNull;

/** Listener-interface for logout status events.
 *
 */

public interface LogoutStatusListener {


    /** Called when error in the logout process occurred (e.g. network-error, HTTP-error etc...)
     *
     * @param error the error
     */
    void onLogoutError (@NonNull SSOError error);


    /** Called when logout is successful.
     *
     */
    void onLogoutSuccess ();

}
