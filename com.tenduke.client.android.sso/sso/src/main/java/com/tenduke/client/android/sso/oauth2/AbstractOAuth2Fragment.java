package com.tenduke.client.android.sso.oauth2;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/** Abstract base-fragment for OAuth2-operations.
 *
 */
public abstract class AbstractOAuth2Fragment extends Fragment {

    private static final Uri DEFAULT_CALLBACK_URI           = Uri.parse ("https://localhost/oauth2_callback.html");

    protected static final String KEY_API_URI               = "param.apiUri";
    protected static final String KEY_CALLBACK_URI          = "param.callbackUri";

    private Uri _apiUri;
    private Uri _callbackUri;
    private OAuth2WebView _view;


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
        final Bundle arguments = getArguments();
        //
        if (arguments != null) {
            _apiUri = Uri.parse(arguments.getString(KEY_API_URI));
        }
        else {
            throw new RuntimeException("Missing arguments");
        }

        // Determine callback URI:
        final String callbackUriParameter = arguments.getString (KEY_CALLBACK_URI);

        // If not given as parameter, build callback URI from default
        // Use the URI-scheme from API-URI to avoid problems, when e.g. https changes to http...
        if (callbackUriParameter == null) {
            _callbackUri = DEFAULT_CALLBACK_URI.buildUpon().scheme (_apiUri.getScheme()).build ();
        }
        // Otherwise use the parameter as-is:
        else {
            _callbackUri = Uri.parse (callbackUriParameter);
        }

        _view = createWebView ();

        return _view;
    }


    /** Implementation point for subclasses.
     *
     * @return an OAuth2WebView
     */
    protected abstract OAuth2WebView createWebView ();

    protected @NonNull Uri getApiUri () {
        return _apiUri;
    }

    protected @NonNull Uri getCallbackUri() {
        return _callbackUri;
    }

    protected @NonNull OAuth2WebView getWebView() {
        return _view;
    }


}
