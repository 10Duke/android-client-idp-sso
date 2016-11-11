package com.tenduke.client.android.sso;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tenduke.client.ApiCredentials;
import com.tenduke.client.android.sso.oauth2.OAuth2LogoutFragment;


/** Default example LogoutActivity.
 *
 *  <p>
 *  This activity hosts {@link OAuth2LogoutFragment}.
 *
 *  <p>
 *  To use this activity, declare the activity in AndroidManifest.xml:
 *      {@code <activity android:name="com.tenduke.client.android.sso.LogoutActivity"/>}
 *
 *  <p>
 *  Parameters:
 *  <ul>
 *      <li>Use {@link #createIntent(Context, String, Uri, Uri, ApiCredentials)}  to create the intent
 *          for starting the activity. This method defines the parameters.</li>
 *  </ul>
 *
 *  <p>
 *  Starting:
 *  <ul>
 *    <li>Start the activity by calling {@link android.app.Activity#startActivityForResult(Intent, int)}</li>
 *    <li>Example:<br>
 *        {@code startActivityForResult (LogoutActivity.createIntent (this, ..., ..., ...), 1);}
 *    </li>
 *  </ul>
 *
 *  <p>
 *  Return values:
 *  <ul>
 *    <li>Handle return values in {@link android.app.Activity#onActivityResult(int, int, Intent)}.</li>
 *    <li>{@code resultCode} is {@link android.app.Activity#RESULT_OK} if logout was successful. In this case
 *      no Intent is returned.
 *    </li>
 *    <li>{@code resultCode} is {@link #RESULT_ERROR} if logout failed. In this case {@link android.content.Intent}
 *      contains instance of {@link SSOError}, locatable with key {@code EXTRA_OUT_ERROR} (e.g. {@link android.content.Intent#getSerializableExtra(String) getSerializableExtra (EXTRA_OUT_ERROR)}).
 *    </li>
 *    <li>Example:
 *        <pre>
 *        if (resultCode == RESULT_OK) {
 *            // Logout was successful
 *        }
 *        else if (resultCode == LogoutActivity.RESULT_ERROR) {
 *            // Logout had an error:
 *            SSOError error = (SSOError) intent.getSerializableExtra (LogoutActivity.EXTRA_OUT_ERROR);
 *        }
 *     </li>
 *  </ul>
 */

public class LogoutActivity extends AbstractSingleFragmentActivity implements LogoutStatusListener {

    /** Key for input parameter "CALLBACK_URI". This is the URI, which the backend calls, when the
     *  logout is complete.
     */
    protected static final String EXTRA_IN_CALLBACK_URI = "extra.in.callbackUri";


    /** Creates the fragment, which handles the logout
     *
     *  @return the fragment.
     */
    @Override
    protected @NonNull OAuth2LogoutFragment createFragment() {
        //
        final Intent intent = getIntent();

        return new OAuth2LogoutFragment().withArguments(intent.getData(), (Uri) intent.getParcelableExtra(EXTRA_IN_CALLBACK_URI));
    }


    /** Callback for handling errors in the logout-process.
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
    public void onLogoutError(@NonNull SSOError error) {
        //
        final Intent intent = new Intent();

        intent.putExtra(EXTRA_OUT_ERROR, error);
        super.setResult(RESULT_ERROR, intent);
        super.finish();
    }


    /** Callback for handling the logout success.
     *
     *  The implementation sets the activity result value with {@link #setResult(int)} and
     *  ends the activity with {@link #finish()}.
     *
     *  The calling activity will receive the results in
     *  {@link android.app.Activity#onActivityResult(int, int, Intent)} callback.
     *
     */
    @Override
    public void onLogoutSuccess() {
        //
        super.setResult(RESULT_OK);
        super.finish();
    }


    /** Utility to create an intent for starting this activity.
     *
     *  @param context -
     *  @param baseApiUri base URI of the API (eg. "https://demo-idp.10duke.com")
     *  @param callbackUri callback-URI, which indicates successful logout. NOTE: This URI *MUST* be
     *                     configured to the 10Duke IdP backend.
     *  @param credentials credentials to logout
     *  @return an Intent, which can be used to start this activity.
     */
    public static Intent createIntent (
            @NonNull final Context context,
            @Nullable final String title,
            @NonNull final Uri baseApiUri,
            @NonNull final Uri callbackUri,
            @Nullable final ApiCredentials credentials) {
        //
        final Intent intent = new Intent (context, LogoutActivity.class);

        intent.setData(baseApiUri);
        if (title != null) {
            intent.putExtra(EXTRA_IN_TITLE, title);
        }
        intent.putExtra (EXTRA_IN_CALLBACK_URI, callbackUri);

        return intent;
    }

}
