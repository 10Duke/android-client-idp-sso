package com.tenduke.client.android.sample.groups;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tenduke.client.android.sample.R;
import com.tenduke.client.android.sample.crud.AbstractDetailFragment;
import com.tenduke.client.android.sample.crud.AbstractListActivity;
import com.tenduke.client.api.idp.Group;

public class GroupListActivity extends AbstractListActivity <Group> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //
        super.onCreate(savedInstanceState);

        setToolbarTitle(R.string.action_groups);
        setListFragment (new GroupListFragment());
        showListFragment ();
    }


    @Override
    public @NonNull AbstractDetailFragment<Group> createDetailFragment(@Nullable final Group entity) {
        return new GroupDetailFragment().withArguments(entity);
    }


    @Override
    public @NonNull Intent createDetailIntent(@Nullable Group entity) {
        return GroupDetailActivity.createIntent(this, entity);
    }


}
