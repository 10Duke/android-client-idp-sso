package com.tenduke.client.android.sample.users;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tenduke.client.android.android_utils.BundleUtil;
import com.tenduke.client.api.idp.User;
import com.tenduke.client.android.sample.crud.AbstractDetailActivity;
import com.tenduke.client.android.sample.singletons.Apis;

import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserDetailActivity extends AbstractDetailActivity<User> {

    private static final String EXTRA_IN_USER_ID = "extra.in.userId";

    private UUID _userId;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //
        super.onCreate(savedInstanceState);

        _userId = BundleUtil.getUUID(EXTRA_IN_USER_ID, getIntent());

        showDetailFragment(new UserDetailFragment().withArguments(_userId));

        Apis.INSTANCE.idp().findUsers(null, null, null, null, null).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {

                }
                else {

                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

            }
        });
    }


    public static @NonNull Intent createIntent (
            @NonNull final Context context,
            @Nullable final User user) {
        //
        final Intent intent = new Intent (context, UserDetailActivity.class);

        if (user != null && user.getId () != null) {
            intent.putExtra(EXTRA_IN_USER_ID, user.getId().toString());
        }

        return intent;
    }



}
