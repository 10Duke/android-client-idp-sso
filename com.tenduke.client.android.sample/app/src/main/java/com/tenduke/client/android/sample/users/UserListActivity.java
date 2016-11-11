package com.tenduke.client.android.sample.users;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tenduke.client.api.idp.User;
import com.tenduke.client.android.sample.R;
import com.tenduke.client.android.sample.crud.AbstractDetailFragment;
import com.tenduke.client.android.sample.crud.AbstractListActivity;

public class UserListActivity extends AbstractListActivity <User> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //
        super.onCreate(savedInstanceState);

        setToolbarTitle(R.string.action_users);
        setListFragment (new UserListFragment());
        showListFragment ();
    }

    @Override
    public @NonNull AbstractDetailFragment<User> createDetailFragment(@Nullable final User entity) {
        return new UserDetailFragment().withArguments(entity);
    }


    @Override
    public @NonNull Intent createDetailIntent(@Nullable User entity) {
        return UserDetailActivity.createIntent(this, entity);
    }


}
