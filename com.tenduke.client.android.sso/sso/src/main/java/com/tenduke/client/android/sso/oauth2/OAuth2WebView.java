package com.tenduke.client.android.sso.oauth2;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.webkit.WebView;

import com.tenduke.client.android.sso.SDK16WebChromeClient;

/** WebView for handling OAuth2 functionality.
 *
 *  On Android-platforms less than SDK 23, this sets {@link SDK16WebChromeClient} to
 *  detect HTTP-errors.
 */
public class OAuth2WebView extends WebView {

    protected OAuth2WebView (
            @NonNull final Context context,
            @NonNull final AbstractOAuth2WebViewClient viewClient) {
        //
        super (context);
        //
        super.getSettings().setJavaScriptEnabled(true);
        super.setWebViewClient(viewClient);

        // Hack-warning:
        // Before SDK 23 there is no sensible way of detecting HTTP-errors.
        // This kludge tries to detect HTTP-errors.
        // (At SDK 23 and later, WebViewClient.onReceivedHttpError() is available)
        if (Build.VERSION.SDK_INT < 23) {
            super.setWebChromeClient(new SDK16WebChromeClient().withErrorListener(viewClient));
        }
    }

}
