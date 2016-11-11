package com.tenduke.client.android.sso.oauth2;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tenduke.client.android.sso.LoginStatusListener;

import java.util.UUID;


/** Android UI-fragment, which implements SSO-login functionality.
 *
 *  <p>
 *  If you want to implement the login-activity instead of using {@link com.tenduke.client.android.sso.LoginActivity},
 *  you can use this fragment to do the OAuth2 implicit-flow processing.
 *
 *  <p>
 *  Set fragment parameters using {@link #withArguments(Uri, String, String)}.
 *
 *  <p>
 *  Results are communicated to containing Activity via a {@link LoginStatusListener} callbacks.
 *
 *  <p>
 *  NOTE: The containing activity *MUST* implement {@link LoginStatusListener}.
 *
 *  <p>
 *  Example instantiation:
 *  <pre>
 *    final OAuth2LoginFragment fragment = new OAuth2LoginFragment().withArguments (Uri.parse("https://vslidp.10duke.com", "android_test", null);
 *  </pre>
 */

public class OAuth2LoginFragment extends AbstractOAuth2Fragment {

    private static final ResponseType[] DEFAULT_RESPONSE_TYPES = {ResponseType.TOKEN, ResponseType.ID_TOKEN};
    private static final Scope[] DEFAULT_SCOPES = {Scope.OPENID, Scope.EMAIL, Scope.PROFILE};

    private static final String KEY_CLIENT_ID = "param.clientId";
    private static final String KEY_NONCE = "param.nonce";

    private String _clientId;
    private String _nonce;


    /** {@inheritDoc}
     *
     * @param inflater {@inheritDoc}
     * @param container {@inheritDoc}
     * @param savedInstanceState {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    @Nullable public View onCreateView(
            final LayoutInflater inflater,
            final ViewGroup container,
            final Bundle savedInstanceState) {
        //
        final View view = super.onCreateView (inflater, container, savedInstanceState);

        final Bundle arguments = getArguments();
        _clientId = arguments.getString(KEY_CLIENT_ID);
        _nonce = arguments.getString(KEY_NONCE);

        login ();

        return view;
    }


    /** Creates the WebView for handling the OAuth2 login process.
     *
     * @return -
     */
    @Override
    protected OAuth2WebView createWebView() {
        //
        final OAuth2LoginWebViewClient client = new OAuth2LoginWebViewClient(getCallbackUri());

        if (getActivity() instanceof LoginStatusListener) {
            client.addListener((LoginStatusListener)getActivity());
        }

        return new OAuth2WebView(this.getActivity(), client);
    }


    /** Sets given arguments to the fragment.
     *
     *  @param apiUri base URI of the API (eg. "https://demo-idp.10duke.com")
     *  @param clientId client Id
     *  @param nonce nonce to use. Optional, if {@code null}, a new nonce is generated
     *  @return the same fragment instance, configured with given arguments.
     */
    public OAuth2LoginFragment withArguments (
            @NonNull final Uri apiUri,
            @NonNull final String clientId,
            @Nullable final String nonce) {
        //
        final Bundle bundle = new Bundle ();

        bundle.putString(KEY_API_URI, apiUri.toString());
        bundle.putString(KEY_CLIENT_ID, clientId);
        bundle.putString(KEY_NONCE, nonce);
        setArguments(bundle);

        return this;
    }


    /** Does the login with given parameters.
     *
     * @param apiUri -
     * @param callbackUri -
     * @param responseTypes -
     * @param scopes -
     * @param client_id -
     * @param nonce -
     */
    protected void login (
            @NonNull final Uri apiUri,
            @NonNull final Uri callbackUri,
            @NonNull final ResponseType [] responseTypes,
            @NonNull final Scope [] scopes,
            @NonNull final String client_id,
            @Nullable final String nonce) {
        //
        final Uri uri = apiUri.buildUpon()
                . appendEncodedPath("oauth2/authz")
                . appendQueryParameter ("redirect_uri", callbackUri.toString())
                . appendQueryParameter ("response_type", TextUtils.join(" ", responseTypes))
                . appendQueryParameter ("scope", TextUtils.join(" ", scopes))
                . appendQueryParameter ("client_id", client_id)
                . appendQueryParameter ("nonce", (nonce == null ? generateNonce() : nonce))
                . build ()
        ;

        getWebView().loadUrl (uri.toString ());
    }


    /** Logs in.
     */
    public void login () {
        login (getApiUri(), getCallbackUri(), DEFAULT_RESPONSE_TYPES, DEFAULT_SCOPES, _clientId, _nonce);
    }


    /** Generates a random nonce.
     *
     * @return -
     */

    protected String generateNonce () {
        //
        return UUID.randomUUID().toString();
    }

}
