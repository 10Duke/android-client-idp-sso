package com.tenduke.client.android.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.tenduke.client.ApiCredentials;
import com.tenduke.client.android.sample.groups.GroupListActivity;
import com.tenduke.client.android.sample.roles.RoleListActivity;
import com.tenduke.client.android.sample.singletons.Session;
import com.tenduke.client.android.sample.users.UserListActivity;


/** The main activity of the sample.
 *
 */
public class MainActivity extends BaseActivity {

    private Button _btnLogin;


    /** {@inheritDoc}
     *
     *  @param savedInstanceState {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Button
        _btnLogin = (Button) findViewById(R.id.main_login_button);


        if (Session.INSTANCE.isLoggedOn()) {
            afterSuccessfulLogin(Session.INSTANCE.get(), false);
            startActivity(new Intent (this, UserListActivity.class));
        }
        setProgressIndicatorVisible(false);
    }


    /** {@inheritDoc}
     *
     *  @param menu {@inheritDoc}
     *  @return {@inheritDoc}
     */
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    /** Handles following menu actions: Groups, Roles, Users.
     *
     *  @param item {@inheritDoc}
     *  @return {@inheritDoc}
     */
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_groups:
                startActivity(new Intent (this, GroupListActivity.class));
                return true;
            case R.id.action_roles:
                startActivity(new Intent (this, RoleListActivity.class));
                return true;
            case R.id.action_users:
                startActivity(new Intent (this, UserListActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /** Toggles visibility of following menu items based on login status: Groups, Roles, Users.
     *
     *  @param menu {@inheritDoc}
     *  @return {@inheritDoc}
     */
    @Override
    public boolean onPrepareOptionsMenu(final Menu menu) {
        //
        visibleIfLogged (menu.findItem(R.id.action_groups));
        visibleIfLogged (menu.findItem(R.id.action_roles));
        visibleIfLogged (menu.findItem(R.id.action_users));

        return super.onPrepareOptionsMenu(menu);
    }


    /** Custom actions to perform after successful login: Hides login-button.
     *
     * @param credentials {@inheritDoc}
     * @param store {@inheritDoc}
     */
    @Override
    protected void afterSuccessfulLogin(@Nullable final ApiCredentials credentials, boolean store) {
        //
        super.afterSuccessfulLogin (credentials, store);

        _btnLogin.setVisibility(View.GONE);
    }


    /** Custom actions to perform after successful login: Shows login-button
     *
     * @param resultCode {@inheritDoc}
     * @param data @{inheritDoc}
     */
    @Override
    protected void handleLogoutResult (int resultCode, @Nullable Intent data) {
        //
        super.handleLogoutResult(resultCode, data);
        _btnLogin.setVisibility(View.VISIBLE);
    }

    public void onLoginClicked (View view) {
        login ();
    }


}
