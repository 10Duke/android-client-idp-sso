package com.tenduke.client.android.sample.users;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.tenduke.client.android.android_utils.BundleUtil;
import com.tenduke.client.api.idp.User;
import com.tenduke.client.api.idp.IdpApi;
import com.tenduke.client.android.sample.R;
import com.tenduke.client.android.sample.crud.AbstractDetailFragment;
import com.tenduke.client.android.sample.singletons.Apis;

import java.util.UUID;

import retrofit2.Call;

public class UserDetailFragment extends AbstractDetailFragment<User> {

    public static final String KEY_USER_ID = "param.userId";

    private final IdpApi _api = Apis.INSTANCE.idp();

    private EditText _description;
    private EditText _email;
    private EditText _firstName;
    private EditText _lastName;

    @Override
    protected @NonNull User bindFromView() {
        //
        final User user = (getEntity() == null ? new User() : getEntity());

        user.setDescription(_description.getText().toString());
        user.setEmail(_email.getText().toString());
        user.setFirstName(_firstName.getText().toString());
        user.setLastName(_lastName.getText().toString());

        return user;
    }

    @Override
    protected void bindToView(@Nullable final User entity) {
        //
        if (entity != null) {
            _description.setText(entity.getDescription());
            _email.setText(entity.getEmail());
            _firstName.setText(entity.getFirstName());
            _lastName.setText(entity.getLastName());
        }
    }


    @Override
    protected @NonNull Call<User> create(@NonNull final User entity) {
        return _api.createUser(entity);
    }


    @Override
    protected @NonNull Call<Void> delete(@NonNull final User entity) {
        return _api.deleteUser(entity.getId());
    }


    @Override
    protected @Nullable Call<User> read() {
        //
        final UUID userId = BundleUtil.getUUID(KEY_USER_ID, getArguments());
        if (userId != null) {
            return _api.findUser(userId);
        }
        return null;
    }



    @Override
    protected @NonNull View createContentView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container, @Nullable Bundle savedInstanceState) {
        //
        final View root = inflater.inflate(R.layout.fragment_user_detail, container, false);

        _description = (EditText) root.findViewById(R.id.user_description);
        _email = (EditText) root.findViewById(R.id.user_email);
        _firstName = (EditText) root.findViewById(R.id.user_firstName);
        _lastName = (EditText) root.findViewById(R.id.user_lastName);

        return root;
    }

    @Override
    protected @NonNull Call<User> update(@NonNull User entity) {
        return _api.updateUser(entity);
    }

    public UserDetailFragment withArguments (@Nullable final UUID userId) {
        //
        final Bundle arguments = new Bundle ();

        if (userId != null) {
            arguments.putString(KEY_USER_ID, userId.toString());
        }
        setArguments(arguments);

        return this;
    }


    public UserDetailFragment withArguments (@Nullable final User user) {
        //
        return withArguments((user == null ? null : user.getId()));
    }

}
