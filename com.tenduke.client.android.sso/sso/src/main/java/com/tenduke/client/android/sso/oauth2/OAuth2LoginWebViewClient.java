package com.tenduke.client.android.sso.oauth2;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.tenduke.client.ApiCredentials;
import com.tenduke.client.android.sso.LoginStatusListener;
import com.tenduke.client.android.sso.SSOError;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** WebView-client, which implements logic for OAuth2-implicit-flow-processing.
 *
 */
public class OAuth2LoginWebViewClient extends AbstractOAuth2WebViewClient {

    private static final String TAG = AbstractOAuth2WebViewClient.class.getSimpleName();

    private static final String PARAMETER_NAME_ACCESS_TOKEN = "access_token";
    private static final String PARAMETER_NAME_EXPIRES_IN = "expires_in";
    private static final String PARAMETER_NAME_ID_TOKEN = "id_token";
    private static final String PARAMETER_NAME_STATE = "state";
    private static final String PARAMETER_NAME_TOKEN_TYPE = "token_type";

    private final String _parameterNameAccessToken;
    private final String _parameterNameExpiresIn;
    private final String _parameterNameIdToken;
    private final String _parameterNameState;
    private final String _parameterNameTokenType;

    private final List<LoginStatusListener> _listeners = new ArrayList<>(2);


    /**
     * Constructs a new instance
     *
     * @param callbackUri -
     * @param parameterNameAccessToken -
     * @param parameterNameExpiresIn   -
     * @param parameterNameIdToken     -
     * @param parameterNameState       -
     * @param parameterNameTokenType   -
     */
    public OAuth2LoginWebViewClient(
            @NonNull final Uri callbackUri,
            @NonNull final String parameterNameAccessToken,
            @NonNull final String parameterNameExpiresIn,
            @NonNull final String parameterNameIdToken,
            @NonNull final String parameterNameState,
            @NonNull final String parameterNameTokenType) {
        //
        super(callbackUri);
        _parameterNameAccessToken = parameterNameAccessToken;
        _parameterNameExpiresIn = parameterNameExpiresIn;
        _parameterNameIdToken = parameterNameIdToken;
        _parameterNameState = parameterNameState;
        _parameterNameTokenType = parameterNameTokenType;
    }


    /** Constructs a new instance with default values for parameter names.
     *
     *  @param callbackUri -
     */
    public OAuth2LoginWebViewClient(final Uri callbackUri) {
        //
        this(callbackUri, PARAMETER_NAME_ACCESS_TOKEN, PARAMETER_NAME_EXPIRES_IN, PARAMETER_NAME_ID_TOKEN, PARAMETER_NAME_STATE, PARAMETER_NAME_TOKEN_TYPE);
    }


    /** Handles an OAuth2 success response and signals the registered listeners by calling
     *  {@link LoginStatusListener#onLoginSuccess(ApiCredentials)}.
     *
     * @param parameters -
     */
    @Override
    protected void handleOAuth2Success(@NonNull final Map<String, String> parameters) {
        //
        final String expiresInString = parameters.get(_parameterNameExpiresIn);
        long expiresIn = 0;
        if (!TextUtils.isEmpty (expiresInString)) {
            try {
                expiresIn = Long.valueOf(expiresInString);
            }
            catch (final NumberFormatException e) {
                Log.e (TAG, "expiresIn is not a number, value is: \"" + expiresInString + "\"");
            }
        }

        final OAuth2Credentials session = new OAuth2Credentials(
                parameters.get(_parameterNameTokenType),
                parameters.get(_parameterNameAccessToken),
                expiresIn,
                parameters.get (_parameterNameIdToken),
                parameters.get (_parameterNameState)
        );

        for (final LoginStatusListener listener : _listeners) {
            listener.onLoginSuccess(session);
        }
    }


    /** Callback, which handles WebView-errors.
     *
     *  The implementation signals the registered listeners by calling {@link LoginStatusListener#onLoginError(SSOError)}.
     *
     *  @param error the error
     */
    @Override
    public void onWebViewError(@NonNull SSOError error) {
        //
        for (final LoginStatusListener listener : _listeners) {
            listener.onLoginError(error);
        }
    }

    /** Adds a {@link LoginStatusListener}.
     */
    public void addListener (@NonNull final LoginStatusListener listener) {
        _listeners.add (listener);
    }

}
