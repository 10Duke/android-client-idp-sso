package com.tenduke.client.android.sample.singletons;

import android.net.Uri;
import android.support.annotation.Nullable;

import com.tenduke.client.ApiConfiguration;
import com.tenduke.client.ApiCredentials;
import com.tenduke.client.android.api.ApiProviders;
import com.tenduke.client.api.idp.IdpApi;
import com.tenduke.client.gson.GsonUtil;
import com.tenduke.client.retrofit.RetrofitUtil;

import retrofit2.converter.gson.GsonConverterFactory;


/** A singleton, which hosts the configured APIs.
 *
 */

public enum Apis {

    /** The singleton instance.
     */
    INSTANCE;


    /** The base-URL for the APIs.
     *
     *  <p>
     *  This sample hard codes the base-URL, but the base-URL could be configured e.g. in resources.
     *  </p>
     */
    private static final String BASE_URL = "http://10.0.2.2:30262/foo/";
    //private static final String BASE_URL = "https://vslidp.10duke.com/";

    /** URL for SSO. Again, hard coded in this example. */
    private static final Uri SSO_URL = Uri.parse ("https://vslidp.10duke.com");

    /** Client Id for the SSO. Again, hard coded in this example. */
    private static final String SSO_CLIENT_ID = "android_test";

    /** Call-back URL. Called by the backend to indicate result for OAuth2-operations.
     *  NOTE: This URL is used for logout only and HAS TO MATCH THE VALUE CONFIGURED IN THE BACKEND. */
    private static final Uri CALLBACK_URL= Uri.parse ("https://localhost/oauth2_callback.html");

    /** Configured API-providers. */
    private final ApiProviders _providers;



    /** Constructs the instance: Configures the APIs in use.
     *
     */
    Apis() {

        // The API configuration:
        //  - Base URL
        //  - Converter.Factory for JSON serialization/de-serialization
        //  - Retrofit client
        final ApiConfiguration configuration = new ApiConfiguration(
                BASE_URL,
                GsonConverterFactory.create(GsonUtil.createDefaultGson()),
                RetrofitUtil.buildInitialClient()
        );

        // Following APIs are configured:
        _providers = new ApiProviders(configuration)
                .withProviderFor(IdpApi.class)
        ;
    }


    /** Sets the current credentials. All configured APIs automatically use these credentials.
     *
     * @param credentials the credentials
     */
    public void setCredentials(@Nullable final ApiCredentials credentials) {
        _providers.setCredentials(credentials);
    }


    /** A syntactic sugar for retrieving the IdpApi:
     *
     * @return configured IDP-API.
     */
    public IdpApi idp() {
        return _providers.provide(IdpApi.class);
    }


    /** Returns the callback URL for OAuth2 operations.
     *
     *  @return configured callback URL.
     */

    public static Uri getCallbackUrl() {
        return CALLBACK_URL;
    }


    /** Returns the configured SSO client id.
     *
     *  @return configured SSO client id.
     */
    public static String getSsoClientId() {
        return SSO_CLIENT_ID;
    }


    /** Returns the configured SSO API URL.
     *
     *  @return the configured SSO API URL.
     */
    public static Uri getSsoUrl() {
        return SSO_URL;
    }

}
