package com.tenduke.client.android.sample;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.tenduke.client.ApiCredentials;
import com.tenduke.client.android.sample.singletons.Apis;
import com.tenduke.client.android.sample.singletons.Session;
import com.tenduke.client.android.sample.singletons.Storage;
import com.tenduke.client.android.sample.ui.AbstractActivity;
import com.tenduke.client.android.sso.LoginActivity;
import com.tenduke.client.android.sso.LogoutActivity;
import com.tenduke.client.android.sso.SSOError;

import java.io.Serializable;


/** A base-activity for this sample, provides common logic and features.
 *
 */
public class BaseActivity extends AbstractActivity {

    /** Request code for logout. See {@link #logout()} and {@link #onActivityResult(int, int, Intent)}. */
    protected static final int REQUEST_CODE_LOGOUT = 1;
    /** Request code for login. See {@link #login()} and {@link #onActivityResult(int, int, Intent)}. */
    protected static final int REQUEST_CODE_LOGIN = 2;

    private static final String TAG = BaseActivity.class.getSimpleName();


    /** This gets executed after successful login.
     *
     *  This implementation stores the credentials to survive restarts and sets up the APIs to use
     *  the credentials.
     *
     *  @param credentials the credentials
     *  @param store determines whether the credentials should be stored
     */
    protected void afterSuccessfulLogin (@Nullable final ApiCredentials credentials, boolean store) {
        //
        if (store) {
            Session.INSTANCE.logIn(credentials);
            Apis.INSTANCE.setCredentials(credentials);
            if (credentials != null) {
                try {
                    Storage.Key.CREDENTIALS.store(credentials);
                } catch (final Throwable t) {
                    //TODO
                }
            }
        }

        invalidateOptionsMenu();
    }


    /** Returns a serializable extra by given key, casting it to given class.
     *
     *  @param intent from where the extra is looked up
     *  @param key key to lookup with
     *  @param clazz class to which the extra value is to be cast to
     *  @param <T> type of the class to which the extra value is to be cast to
     *  @return The extra by given {@code key}, cast to correct class. If {@code intent} is {@code null},
     *  returns {@code null}. If no extra with given {@code key} is found, returns {@code null}.
     */
    protected @Nullable <T extends Serializable> T getExtra (@Nullable final Intent intent, @NonNull final String key, @NonNull final Class<T> clazz) {
        //
        if (intent == null) {
            return null;
        }
        final Serializable extra = intent.getSerializableExtra(key);
        return (extra == null ? null : clazz.cast(extra));
    }


    /** Handles login result.
     *
     *  @param resultCode result code from the onActivityResult()
     *  @param data the intent from onActivityResult()
     */
    protected void handleLoginResult (int resultCode, @Nullable Intent data) {

        switch (resultCode) {

            case RESULT_OK: {
                final ApiCredentials session = getExtra (data, LoginActivity.EXTRA_OUT_CREDENTIALS, ApiCredentials.class);
                if (session != null) {
                    afterSuccessfulLogin(session, true);
                }
                else {
                    Log.e (TAG, "handleLoginResult(), RESULT_OK, but no session!");
                    // TODO: Handle error here!
                }
                break;
            }

            case LoginActivity.RESULT_ERROR:
                final SSOError error = getExtra (data, LoginActivity.EXTRA_OUT_ERROR, SSOError.class);
                Log.e (TAG, "handleLoginResult() SSOError occurred: " + (error == null ? "<null>" : error.toString()));
                // TODO: Handle error here!
        }

    }


    /** Handles logout result.
     *
     *  <ul>
     *      <li>Marks the session logged out.</li>
     *      <li>Removes the stored api credentials</li>
     *      <li>Removes the credentials from APIs</li>
     *      <li>Goes to main activity</li>
     *  </ul>
     *
     *  @param resultCode result code from the onActivityResult()
     *  @param data the intent from onActivityResult()
     */
    protected void handleLogoutResult (int resultCode, @Nullable Intent data) {
        //
        switch (resultCode) {
            case LogoutActivity.RESULT_ERROR: {
                final SSOError error = getExtra(data, LoginActivity.EXTRA_OUT_ERROR, SSOError.class);
                Log.e(TAG, "handleLogoutResult(): RESULT_ERROR: " + (error == null ? "<null>" : error.toString()));
                break;
            }
        }

        Session.INSTANCE.logout ();
        Storage.Key.CREDENTIALS.delete();
        Apis.INSTANCE.setCredentials(null);

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }


    /** Starts login activity.
     *
     */
    protected void login () {
        //
        startActivityForResult (
                LoginActivity.createIntent(
                        this,
                        getString(R.string.action_login),
                        Apis.getSsoUrl(),
                        Apis.getSsoClientId(),
                        null // No nonce in this example
                ),
                REQUEST_CODE_LOGIN
        );
    }


    /** Starts logout activity.
     *
     */
    protected void logout () {
        //
        startActivityForResult (

                LogoutActivity.createIntent(
                        this,
                        getString(R.string.action_login),
                        Apis.getSsoUrl(),
                        Apis.getCallbackUrl(),
                        Session.INSTANCE.get()),
                REQUEST_CODE_LOGOUT
        );
    }


    /** Handler for activities, which return a result.
     *
     *  <p>This implementation handles REQUEST_CODE_LOGIN and REQUEST_CODE_LOGOUT.</p>
     *
     *  @param requestCode {@inheritDoc}
     *  @param resultCode {@inheritDoc}
     *  @param data {@inheritDoc}
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //
        super.onActivityResult(requestCode, resultCode, data);
        //
        switch (requestCode) {

            case REQUEST_CODE_LOGIN:
                handleLoginResult (resultCode, data);
                break;

            case REQUEST_CODE_LOGOUT:
                handleLogoutResult (resultCode, data);
                break;

        }
    }


    /** {@inheritDoc}
     *
     * @param savedInstanceState {@inheritDoc}
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
    }


    /** Inflates the base menu (currently only logout)
     *
     *  @param menu {@inheritDoc}
     *  @return {@inheritDoc}
     */
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        //
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.activity_base, menu);
        return true;
    }


    /** Handles selections of base menu (currently only logout).
     *
     *  @param item {@inheritDoc}
     *  @return {@inheritDoc}
     */
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_logout:
                logout ();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /** Toggles the visibility of the logout-item based on login-status.
     *
     *  @param menu {@inheritDoc}
     *  @return {@inheritDoc}
     */
    @Override
    public boolean onPrepareOptionsMenu(final Menu menu) {
        //
        visibleIfLogged (menu.findItem(R.id.action_logout));

        return true;
    }


    /** Displays a fragment.
     *
     * @param fragment fragment to display
     * @param addToBackStack should the fragment be added to back-stack
     * @param containerId ID of the container, where the fragment should be plugged in
     * @param <T> type of the fragment
     */
    protected<T extends Fragment> void showFragment (
            @NonNull final T fragment,
            final boolean addToBackStack,
            final int containerId) {
        //
        final FragmentManager fragmentManager = getFragmentManager();
        final FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(containerId, fragment);
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }


    /** Toggles menu item visibility based on login status.
     *
     *  @param item menu item to toggle
     */
    protected void visibleIfLogged (final MenuItem item) {
        if (item != null) {
            item.setVisible (Session.INSTANCE.isLoggedOn());
        }
    }





}
