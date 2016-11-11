package com.tenduke.client.android.sso.oauth2;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tenduke.client.android.sso.LogoutStatusListener;

/** Android UI-fragment which handles logout-functionality.
 *
 *  <p>
 *  If you want to implement the logout-activity instead of using {@link com.tenduke.client.android.sso.LogoutActivity},
 *  you can use this fragment to do the necessary processing.
 *
 *  <p>
 *  Set fragment parameters using {@link #withArguments(Uri, Uri)}.
 *
 *  <p>
 *  Example instantiation:
 *    <pre>
 *    final OAuth2LogoutFragment fragment = new OAuth2LogoutFragment().withArguments(Uri.parse("https://vslidp.10duke.com"));
 *    </pre>
 */
public class OAuth2LogoutFragment extends AbstractOAuth2Fragment {

    /** {@inheritDoc}
     *
     * @param inflater {@inheritDoc}
     * @param container {@inheritDoc}
     * @param savedInstanceState {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public @Nullable View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //
        final View view = super.onCreateView(inflater, container, savedInstanceState);

        logout ();

        return view;
    }


    /** Sets given arguments to the fragment.
     *
     *  @param apiUri The logout-API URI.
     *  @param callbackUri Uri, which is the backend calls to indicate logout success. This URI has
     *                     to be configured to the 10Duke IDP.
     *  @return the same fragment instance, configured with the given arguments.
     */
    public OAuth2LogoutFragment withArguments (
            @NonNull final Uri apiUri,
            @NonNull final Uri callbackUri) {
        //
        final Bundle bundle = new Bundle ();

        bundle.putString(KEY_API_URI, apiUri.toString());
        bundle.putString(KEY_CALLBACK_URI, callbackUri.toString());
        setArguments(bundle);

        return this;
    }


    /** Creates the WebView for handling the logout.
     *
     * @return a WebView rigged to handle logout
     */
    @Override
    protected OAuth2WebView createWebView() {
        //
        final OAuth2LogoutWebViewClient client = new OAuth2LogoutWebViewClient(getCallbackUri());

        if (getActivity() instanceof LogoutStatusListener) {
            client.addListener((LogoutStatusListener)getActivity());
        }

        return new OAuth2WebView(this.getActivity(), client);
    }


    /** Logs out of the service.
     *
     */
    public void logout () {
        //
        final Uri.Builder uri = getApiUri()
                . buildUpon()
                . appendEncodedPath("logout")
        ;
        getWebView().loadUrl (uri.build ().toString ());
    }

}
