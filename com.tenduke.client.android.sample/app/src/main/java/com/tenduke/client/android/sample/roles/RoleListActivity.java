package com.tenduke.client.android.sample.roles;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tenduke.client.api.idp.Role;
import com.tenduke.client.android.sample.R;
import com.tenduke.client.android.sample.crud.AbstractDetailFragment;
import com.tenduke.client.android.sample.crud.AbstractListActivity;

public class RoleListActivity extends AbstractListActivity <Role> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //
        super.onCreate(savedInstanceState);

        setToolbarTitle(R.string.action_roles);
        setListFragment (new RoleListFragment());
        showListFragment ();
    }

    @Override
    public @NonNull AbstractDetailFragment<Role> createDetailFragment(@Nullable final Role entity) {
        return new RoleDetailFragment().withArguments(entity);
    }


    @Override
    public @NonNull Intent createDetailIntent(@Nullable Role entity) {
        return RoleDetailActivity.createIntent(this, entity);
    }


}
