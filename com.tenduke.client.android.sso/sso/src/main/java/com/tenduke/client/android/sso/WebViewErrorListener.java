package com.tenduke.client.android.sso;

import android.support.annotation.NonNull;

/** Listener-interface for web-view-errors.
 *
 *  Mostly used internally by the components.
  */

public interface WebViewErrorListener {


    /** Called when error happened.
     *
     * @param error the error
     */
    void onWebViewError(@NonNull SSOError error);

}
