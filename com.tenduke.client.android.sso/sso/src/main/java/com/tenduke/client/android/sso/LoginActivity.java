package com.tenduke.client.android.sso;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tenduke.client.ApiCredentials;
import com.tenduke.client.android.sso.oauth2.OAuth2LoginFragment;


/** Default / example LoginActivity.
 *
 *  <p>
 *  This activity hosts {@link OAuth2LoginFragment}.
 *
 *  <p>
 *  To use this activity, declare the activity in AndroidManifest.xml:
 *      {@code <activity android:name="com.tenduke.client.android.sso.LoginActivity"/>}
 *
 *  <p>
 *  Parameters:
 *  <ul>
 *      <li>Use {@link #createIntent(Context, String, Uri, String, String)}  to create the intent
 *          for starting the activity. This method defines the parameters.
 *      </li>
 *  </ul>
 *
 *  <p>
 *  Starting:
 *  <ul>
 *    <li>Start the activity by calling {@link android.app.Activity#startActivityForResult(Intent, int)}</li>
 *    <li>Example:<br>
 *        {@code startActivityForResult (LoginActivity.createIntent (this, ..., ..., ...), 1);}
 *    </li>
 *  </ul>
 *
 *  <p>
 *  Return values:
 *  <ul>
 *    <li>Handle return values in {@link android.app.Activity#onActivityResult(int, int, Intent)}.</li>
 *    <li>{@code resultCode} is {@link #RESULT_CANCELED} if user canceled (eg. pressed back button)</li>
 *    <li>{@code resultCode} is {@link android.app.Activity#RESULT_OK} if login was successful. In this case, {@link android.content.Intent}
 *      contains instance of {@link ApiCredentials}, locatable with key {@code EXTRA_OUT_CREDENTIALS}, (e.g. {@link android.content.Intent#getSerializableExtra(String) getSerializableExtra (EXTRA_OUT_CREDENTIALS)}).
 *    </li>
 *    <li>{@code resultCode} is {@link #RESULT_ERROR} if login failed. In this case {@link android.content.Intent}
 *      contains instance of {@link SSOError}, locatable with key {@code EXTRA_OUT_ERROR} (e.g. {@link android.content.Intent#getSerializableExtra(String) getSerializableExtra (EXTRA_OUT_ERROR)}).
 *    </li>
 *    <li>Example:
 *      <pre>
 *          if (resultCode == RESULT_OK) {
 *              ApiCredentials session = (ApiCredentials) intent.getSerializableExtra (LoginActivity.EXTRA_OUT_CREDENTIALS);
 *          }
 *          else if (resultCode == LoginActivity.RESULT_ERROR) {
 *              SSOError error = (SSOError) intent.getSerializableExtra (LoginActivity.EXTRA_OUT_ERROR);
 *          }
 *       </pre>
 *    </li>
 *  </ul>
 *
 */
public class LoginActivity extends AbstractSingleFragmentActivity implements LoginStatusListener {

    /** Key for input parameter "CLIENT_ID".*/
    protected static final String EXTRA_IN_CLIENT_ID = "extra.in.clientId";

    /** Key for input parameter "NONCE".*/
    protected  static final String EXTRA_IN_NONCE = "extra.in.nonce";

    /** Key for output parameter "CREDENTIALS". */
    public static final String EXTRA_OUT_CREDENTIALS = "extra.out.credentials";


    /** Create the fragment, which handles the login.
     *
     * @return -
     */
    @Override
    protected @NonNull OAuth2LoginFragment createFragment() {
        //
        final Intent intent = getIntent ();

        return new OAuth2LoginFragment ().withArguments(
                intent.getData(),
                intent.getStringExtra (EXTRA_IN_CLIENT_ID),
                intent.getStringExtra(EXTRA_IN_NONCE)
        );
    }


    /** Callback for handling errors in the login-process.
     *
     *  The implementation sets the activity result value with {@link #setResult(int, Intent)} and
     *  ends the activity with {@link #finish()}.
     *
     *  The calling activity will receive the results in
     *  {@link android.app.Activity#onActivityResult(int, int, Intent)} callback.
     *
     *  @param error -
     */

    @Override
    public void onLoginError(@NonNull SSOError error) {
        final Intent intent = new Intent();
        //
        intent.putExtra(EXTRA_OUT_ERROR, error);
        super.setResult(RESULT_ERROR, intent);
        super.finish();
    }


    /** Callback for handling the login success.
     *
     *  The implementation sets the activity result value with {@link #setResult(int, Intent)} and
     *  ends the activity with {@link #finish()}.
     *
     *  The calling activity will receive the results in
     *  {@link android.app.Activity#onActivityResult(int, int, Intent)} callback.
     *
     *  @param session -
     */

    @Override
    public void onLoginSuccess(@NonNull final ApiCredentials session) {
        //
        final Intent intent = new Intent();
        //
        intent.putExtra(EXTRA_OUT_CREDENTIALS, session);
        super.setResult(RESULT_OK, intent);
        super.finish();
    }


    /** Utility to create an intent for starting this activity.
     *
     *  @param context context
     *  @param apiUri base URI of the API (eg. "https://demo-idp.10duke.com")
     *  @param clientId API Client id
     *  @param nonce (Optional). If {@code null} a new nonce is generated.
     *  @return an Intent, which can be used to start this activity.
     */

    public static Intent createIntent (
            @NonNull final Context context,
            @NonNull final String title,
            @NonNull final Uri apiUri,
            @NonNull final String clientId,
            @Nullable final String nonce) {
        //
        final Intent intent = new Intent (context, LoginActivity.class);
        intent.setData(apiUri);
        intent.putExtra(EXTRA_IN_TITLE, title);
        intent.putExtra(EXTRA_IN_CLIENT_ID, clientId);
        if (nonce != null) {
            intent.putExtra(EXTRA_IN_NONCE, nonce);
        }
        return intent;
    }

}
