package com.tenduke.client.android.sample.groups;

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
import com.tenduke.client.api.idp.Group;
import com.tenduke.client.api.idp.IdpApi;

import java.util.UUID;

import retrofit2.Call;

public class GroupDetailFragment extends AbstractDetailFragment<Group> {

    public static final String KEY_GROUP_ID = "param.groupId";

    private final IdpApi _api = Apis.INSTANCE.idp();

    private EditText _name;


    @Override
    protected @NonNull Group bindFromView() {
        //
        final Group entity = (getEntity() == null ? new Group() : getEntity());

        entity.setName(_name.getText().toString());

        return entity;
    }


    @Override
    protected void bindToView(@Nullable final Group entity) {
        //
        if (entity != null) {
            _name.setText(entity.getName());
        }
    }


    @Override
    protected @NonNull Call<Group> create(@NonNull final Group entity) {
        return _api.createGroup(entity);
    }


    @Override
    protected @NonNull Call<Void> delete(@NonNull final Group entity) {
        return _api.deleteGroup(entity.getId());
    }


    @Override
    protected @Nullable Call<Group> read() {
        //
        final UUID id = BundleUtil.getUUID(KEY_GROUP_ID, getArguments());
        if (id != null) {
            return _api.findGroup(id);
        }
        return null;
    }


    @Override
    protected @NonNull View createContentView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container, @Nullable Bundle savedInstanceState) {
        //
        final View root = inflater.inflate(R.layout.fragment_group_detail, container, false);

        _name  = (EditText) root.findViewById(R.id.groupName);

        return root;
    }


    @Override
    protected @NonNull Call<Group> update(@NonNull Group entity) {
        return _api.updateGroup(entity);
    }


    public GroupDetailFragment withArguments (@Nullable final UUID groupId) {
        //
        final Bundle arguments = new Bundle ();

        if (groupId != null) {
            arguments.putString(KEY_GROUP_ID, groupId.toString());
        }
        setArguments(arguments);

        return this;
    }


    public GroupDetailFragment withArguments (@Nullable final Group entity) {
        //
        return withArguments((entity == null ? null : entity.getId()));
    }

}
