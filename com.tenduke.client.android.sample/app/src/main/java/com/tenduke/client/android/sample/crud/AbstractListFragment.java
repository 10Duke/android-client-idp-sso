package com.tenduke.client.android.sample.crud;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tenduke.client.android.sample.ui.AbstractFragment;

/** Abstract base for "list-entities" -fragment.
 *
 * @param <T> Type of the entities to list
 * @param <VH> Type of the view holder
 */
public abstract class AbstractListFragment <T, VH extends RecyclerView.ViewHolder> extends AbstractFragment implements EntityModificationListener<T> {

    private CrudRecyclerViewAdapter<T, VH> _adapter;


    /** Creates the CrudRecyclerViewAdapter.
     *
     * @param savedInstanceState saved instance state
     * @return Suitable adapter
     */
    protected abstract CrudRecyclerViewAdapter<T, VH> createAdapter (final @Nullable Bundle savedInstanceState);


    /** Creates the fragment: Creates the adapter, adds the hosting activity to list of selection listeners.
     *
     * @param savedInstanceState {@inheritDoc}
     */
    @Override
    public void onCreate(final @Nullable Bundle savedInstanceState) {
        //
        super.onCreate(savedInstanceState);
        _adapter = createAdapter (savedInstanceState);
        if (getActivity() instanceof EntitySelectionListener) {
            _adapter.addSelectionListener((EntitySelectionListener) getActivity());
        }
        _adapter.populate ();
    }


    /** Sets up the base "list-entities" -view layout.
     *
     * @param inflater {@inheritDoc}
     * @param container {@inheritDoc}
     * @param savedInstanceState {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public @Nullable View onCreateView(
            final LayoutInflater inflater,
            final ViewGroup container,
            final Bundle savedInstanceState) {
        //
        // Setup the recycler view
        final RecyclerView recyclerView = new RecyclerView(getActivity());
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(_adapter);

        setProgressIndicatorVisible();

        return recyclerView;
    }


    /** Handles "entity-modified" callbacks.
     *
     *  <p>Current implementation just delegates them to the CrudRecyclerViewAdapter.</p>
     *
     * @param entity {@inheritDoc}
     * @param action {@inheritDoc}
     */
    @Override
    public void entityModified(@NonNull final T entity, @NonNull final EntityModificationAction action) {
        //
        if (_adapter != null) {
            _adapter.entityModified(entity, action);
        }
    }
}
