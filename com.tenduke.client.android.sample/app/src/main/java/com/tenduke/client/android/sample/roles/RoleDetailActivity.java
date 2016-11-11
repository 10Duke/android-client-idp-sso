package com.tenduke.client.android.sample.roles;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tenduke.client.android.android_utils.BundleUtil;
import com.tenduke.client.api.idp.Role;
import com.tenduke.client.android.sample.crud.AbstractDetailActivity;

import java.util.UUID;

public class RoleDetailActivity extends AbstractDetailActivity<Role> {

    private static final String EXTRA_IN_ROLE_ID = "extra.in.roleId";

    private UUID _roleId;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //
        super.onCreate(savedInstanceState);

        _roleId = BundleUtil.getUUID(EXTRA_IN_ROLE_ID, getIntent());

        showDetailFragment(new RoleDetailFragment().withArguments(_roleId));
    }


    public static @NonNull Intent createIntent (
            @NonNull final Context context,
            @Nullable final Role role) {
        //
        final Intent intent = new Intent (context, RoleDetailActivity.class);

        if (role != null && role.getId () != null) {
            intent.putExtra(EXTRA_IN_ROLE_ID, role.getId().toString());
        }

        return intent;
    }



}
