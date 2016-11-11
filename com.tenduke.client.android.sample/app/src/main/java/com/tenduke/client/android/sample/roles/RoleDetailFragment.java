package com.tenduke.client.android.sample.roles;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.tenduke.client.android.android_utils.BundleUtil;
import com.tenduke.client.android.sample.R;
import com.tenduke.client.android.sample.crud.AbstractDetailFragment;
import com.tenduke.client.android.sample.singletons.Apis;
import com.tenduke.client.api.idp.IdpApi;
import com.tenduke.client.api.idp.Role;

import java.util.UUID;

import retrofit2.Call;

public class RoleDetailFragment extends AbstractDetailFragment<Role> {

    public static final String KEY_ROLE_ID = "param.roleId";

    private final IdpApi _api = Apis.INSTANCE.idp();

    private EditText _description;
    private EditText _name;


    @Override
    protected @NonNull Role bindFromView() {
        //
        final Role entity = (getEntity() == null ? new Role() : getEntity());

        entity.setDescription(_description.getText().toString());
        entity.setName(_name.getText().toString());

        return entity;
    }


    @Override
    protected void bindToView(@Nullable final Role entity) {
        //
        if (entity != null) {
            _description.setText(entity.getDescription());
            _name.setText(entity.getName());
        }
    }


    @Override
    protected @NonNull Call<Role> create(@NonNull final Role entity) {
        return _api.createRole(entity);
    }


    @Override
    protected @NonNull Call<Void> delete(@NonNull final Role entity) {
        return _api.deleteRole(entity.getId());
    }


    @Override
    protected @Nullable Call<Role> read() {
        //
        final UUID roleId = BundleUtil.getUUID(KEY_ROLE_ID, getArguments());
        if (roleId != null) {
            return _api.findRole(roleId);
        }
        return null;
    }


    @Override
    protected @NonNull View createContentView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container, @Nullable Bundle savedInstanceState) {
        //
        final View root = inflater.inflate(R.layout.fragment_role_detail, container, false);

        _description = (EditText) root.findViewById(R.id.roleDescription);
        _name  = (EditText) root.findViewById(R.id.roleName);

        return root;
    }


    @Override
    protected @NonNull Call<Role> update(@NonNull Role entity) {
        return _api.updateRole(entity);
    }


    public RoleDetailFragment withArguments (@Nullable final UUID roleId) {
        //
        final Bundle arguments = new Bundle ();

        if (roleId != null) {
            arguments.putString(KEY_ROLE_ID, roleId.toString());
        }
        setArguments(arguments);

        return this;
    }


    public RoleDetailFragment withArguments (@Nullable final Role role) {
        //
        return withArguments((role == null ? null : role.getId()));
    }

}
