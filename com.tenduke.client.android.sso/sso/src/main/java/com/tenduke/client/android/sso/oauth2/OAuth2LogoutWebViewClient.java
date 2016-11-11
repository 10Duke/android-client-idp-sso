package com.tenduke.client.android.sso.oauth2;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;

import com.tenduke.client.android.sso.LogoutStatusListener;
import com.tenduke.client.android.sso.SSOError;
import com.tenduke.client.io.Charsets;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


/** WebViewClient, which implements logic for logout-processing.
 */
public class OAuth2LogoutWebViewClient extends AbstractOAuth2WebViewClient {

    private static final String TEXT_LOGOUT_SUCCESSFUL = "Logout successful";

    private final List<LogoutStatusListener> _listeners = new ArrayList<>(2);
    private boolean _logoutFinished = false;
    private final String _logoutSuccessText;


    /** Constructs new instance.
     *
     *  @param callbackUri Callback URI. This *MUST* match the callback URI configured in the backend.
     *  @param logoutSuccessText Text to (temporarily) display on logout success.
     */
    public OAuth2LogoutWebViewClient(
            @NonNull final Uri callbackUri,
            @NonNull final String logoutSuccessText) {
        super(callbackUri);
        _logoutSuccessText = logoutSuccessText;
    }


    /** Constructs new instance.
     *
     *  @param callbackUri Callback URI. This *MUST* match the callback URI configured in the backend.
     */
    public OAuth2LogoutWebViewClient(@NonNull final Uri callbackUri) {
        this (callbackUri, TEXT_LOGOUT_SUCCESSFUL);
    }


    /** Handles logout-success-response.
     *
     *  <p>
     *  @param parameters -
     */
    @Override
    protected void handleOAuth2Success(@NonNull final Map<String, String> parameters) {
        //
        for (final LogoutStatusListener listener : _listeners) {
            listener.onLogoutSuccess();
        }
    }


    /** Handles logout-error-response.
     *
     * @param error -
     */
    @Override
    public void onWebViewError(@NonNull final SSOError error) {
        //
        for (final LogoutStatusListener listener : _listeners) {
            listener.onLogoutError(error);
        }
    }


    /** Adds a {@link LogoutStatusListener}.
     *
     * @param listener -
     */
    public void addListener (@NonNull final LogoutStatusListener listener) {
        _listeners.add (listener);
    }


    /** Intercepts the callback.
     *
     *  <p>
     *  The Logout-callback is executed differently, which means that unfortunately we cannot use
     *  standard method from the superclass. Instead, we need to use this method to intercept the
     *  request.
     *  </p>
     *  <p>
     *  If the request is detected to be the callback, a dummy content is returned and a flag is
     *  set to mark that logout is finished. Method {@link #onPageFinished(WebView, String)}
     *  completes the logout-handling.
     *  </p>
     *
     *  @param view {@inheritDoc}
     *  @param url {@inheritDoc}
     *  @return {@inheritDoc}
     */
    @Override
    public WebResourceResponse shouldInterceptRequest(final WebView view, final String url) {
        //
        if (url.startsWith(getCallbackUrl())) {
            _logoutFinished = true;
            return new WebResourceResponse("text/plain", Charsets.UTF8.name(), new ByteArrayInputStream(_logoutSuccessText.getBytes(Charsets.UTF8)));
        }
        else {
            return super.shouldInterceptRequest(view, url);
        }
    }


    /** Handles "onPageFinished"-functionality.
     *
     *  If a successful logout has been flagged, notifies the registered listeners of the logout.
     *
     * @param view {@inheritDoc}
     * @param url {@inheritDoc}
     */
    @Override
    public void onPageFinished(WebView view, String url) {
        //
        super.onPageFinished(view, url);

        if (_logoutFinished) {
            handleOAuth2Success(Collections.<String, String>emptyMap());
        }
    }
}
