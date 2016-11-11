package com.tenduke.client.android.sso;

import android.os.Build;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public abstract class AbstractWebViewClient extends WebViewClient implements WebViewErrorListener {


    /** WebView error-handler, does not cover HTTP-errors.
     *
     *  @param view -
     *  @param errorCode -
     *  @param description -
     *  @param failingUrl -
     */
    @Override
    public void onReceivedError(
            final WebView view,
            final int errorCode,
            final String description,
            final String failingUrl) {
        //
        super.onReceivedError(view, errorCode, description, failingUrl);
        onWebViewError(new SSOError(errorCode, description, failingUrl));
    }


    /** Detects HTTP-errors.
     *
     *  NOTE: This method is available only beginning from SDK 23. Before that
     *  the HTTP-error detection is done using the mother of all kludges,
     *  {@link SDK16WebChromeClient}.
     *
     * @param view -
     * @param request -
     * @param response -
     */
    @Override
    public void onReceivedHttpError(
            final WebView view,
            final WebResourceRequest request,
            final WebResourceResponse response) {
        //
        super.onReceivedHttpError(view, request, response);
        SSOError error;
        if (Build.VERSION.SDK_INT >= 23) {
            error = new SSOError(
                    response.getStatusCode(),
                    response.getReasonPhrase(),
                    null,
                    request.getUrl().toString()
            );
        }
        else {
            error = new SSOError("Unknown HTTP error", "Unknown HTTP error occurred. The error is unknown as the code was called in incorrect SDK version.");
        }

        onWebViewError(error);
    }


}
