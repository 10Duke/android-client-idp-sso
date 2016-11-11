package com.tenduke.client.android.sso.oauth2;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.webkit.WebView;

import com.tenduke.client.android.sso.AbstractWebViewClient;
import com.tenduke.client.android.sso.SSOError;

import java.util.HashMap;
import java.util.Map;

/** Abstract base WebViewClient for OAuth2 operations.
 *
 */
public abstract class AbstractOAuth2WebViewClient extends AbstractWebViewClient {

    private final String _callbackUrl;

    /**
     * Constructs a new instance
     *
     * @param callbackUri -
     */
    public AbstractOAuth2WebViewClient(@NonNull final Uri callbackUri) {
        //
        super();
        _callbackUrl = callbackUri.toString();
    }


    /** Returns the configured callback URL.
     *
     *  @return the callback URL
     */
    public String getCallbackUrl() {
        return _callbackUrl;
    }


    /** Checks, if the callback URL is called successfully. If so, intercepts the URL-loading and
     *  handles the OAuth2 response
     *
     *  @param view -
     *  @param url  -
     *  @return -
     */
    @Override
    public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
        //
        if (view instanceof OAuth2WebView && url.startsWith(_callbackUrl)) {
            handleOAuth2Response(extractParameters(url.substring(_callbackUrl.length())));
            return true;
        }
        return false;
    }


    /** Handles an OAuth2 response by determining if the response is error or success and
     *  delegating the call to {@link #handleOAuth2Error(Map)} or {@link #handleOAuth2Success(Map)},
     *  respectively.
     *
     * @param parameters -
     */
    protected void handleOAuth2Response(@NonNull final Map<String, String> parameters) {
        //
        if (parameters.containsKey("error")) {
            handleOAuth2Error(parameters);
        }
        else {
            handleOAuth2Success(parameters);
        }
    }


    /** Handles an OAuth2 error response.
     *
     * @param parameters -
     */

    protected void handleOAuth2Error(@NonNull final Map<String, String> parameters) {
        //
        onWebViewError (
                new SSOError(
                        parameters.get("error"),
                        parameters.get("error_description")
                )
        );
    }


    /** Handles an OAuth2 success response.
     *
     * @param parameters -
     */

    protected abstract void handleOAuth2Success(@NonNull final Map<String, String> parameters);


    /** Extracts parameters from the callback-URL
     *
     *  @param parameterString OAuth2-parameter string, starting with #
     *  @return -
     */

    private @NonNull Map<String, String> extractParameters (final String parameterString) {
        //
        final Map<String,String> parameters = new HashMap<>();
        //
        final TextUtils.StringSplitter splitter = new TextUtils.SimpleStringSplitter('&');

        splitter.setString(parameterString.substring (parameterString.indexOf('#') + 1));
        for (final String string : splitter) {
            final int idx = string.indexOf('=');
            if (idx > 0) {
                parameters.put (string.substring(0, idx), string.substring (idx + 1));
            }
            if (idx == -1) {
                parameters.put (string, null);
            }
        }
        return parameters;
    }



}

