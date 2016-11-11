package com.tenduke.client.android.sample.groups;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tenduke.client.android.sample.R;
import com.tenduke.client.android.sample.crud.AbstractListFragment;
import com.tenduke.client.android.sample.crud.CrudListCallback;
import com.tenduke.client.android.sample.crud.CrudRecyclerViewAdapter;
import com.tenduke.client.android.sample.singletons.Apis;
import com.tenduke.client.api.idp.Group;
import com.tenduke.client.api.idp.IdpApi;

import java.util.List;
import java.util.UUID;

public class GroupListFragment extends AbstractListFragment<Group, CrudRecyclerViewAdapter.ViewHolder> {

    @Override
    protected CrudRecyclerViewAdapter<Group, CrudRecyclerViewAdapter.ViewHolder> createAdapter(@Nullable Bundle savedInstanceState) {
        return new Adapter();
    }

    private class Adapter extends CrudRecyclerViewAdapter<Group, CrudRecyclerViewAdapter.ViewHolder> {

        private final IdpApi _api = Apis.INSTANCE.idp();

        @Override
        protected void bindEntityToView(@NonNull Group entity, @NonNull CrudRecyclerViewAdapter.ViewHolder holder) {
            //
            ((TextView)holder.itemView.findViewById(R.id.groupName)).setText(entity.getName());
        }

        @Override
        protected int findIndexOfEntity(@NonNull final Group entity) {
            //
            final UUID id = entity.getId();
            //
            if (id != null) {
                final List<Group> entities = getEntities();
                for (int i = 0, len = entities.size(); i < len; i++) {
                    if (id.equals(entities.get(i).getId())) {
                        return i;
                    }
                }
            }
            return -1;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final CardView view = (CardView) LayoutInflater.from (parent.getContext()).inflate(R.layout.card_group_list_item, parent, false);
            return new ViewHolder(view);
        }


        @Override
        public void populate() {
            //
            _api.findGroups(null, null, null, null, null).enqueue(new CrudListCallback<>(this, GroupListFragment.this));
        }
    }


}
