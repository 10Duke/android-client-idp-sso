package com.tenduke.client.android.sample.groups;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tenduke.client.android.android_utils.BundleUtil;
import com.tenduke.client.android.sample.crud.AbstractDetailActivity;
import com.tenduke.client.api.idp.Group;

import java.util.UUID;

public class GroupDetailActivity extends AbstractDetailActivity<Group> {

    private static final String EXTRA_IN_GROUP_ID = "extra.in.groupId";

    private UUID _groupId;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //
        super.onCreate(savedInstanceState);

        _groupId = BundleUtil.getUUID(EXTRA_IN_GROUP_ID, getIntent());

        showDetailFragment(new GroupDetailFragment().withArguments(_groupId));
    }


    public static @NonNull Intent createIntent (
            @NonNull final Context context,
            @Nullable final Group entity) {
        //
        final Intent intent = new Intent (context, GroupDetailActivity.class);

        if (entity != null && entity.getId () != null) {
            intent.putExtra(EXTRA_IN_GROUP_ID, entity.getId().toString());
        }

        return intent;
    }



}
