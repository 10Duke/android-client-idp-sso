package com.tenduke.client.android.sample.crud;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/** Abstract base class for adapters, which are used by the RecyclerView in {@link com.tenduke.client.android.sample.crud.AbstractListFragment}.
 *
 * @param <T> Type of the entities to list
 * @param <VH> Type of the view holder
 */

public abstract class CrudRecyclerViewAdapter<T, VH extends RecyclerView.ViewHolder>
extends RecyclerView.Adapter<VH>
implements EntityModificationListener<T>, View.OnClickListener
{
    private final List<T> _entities = new ArrayList<>();
    private final List<EntitySelectionListener<T>> _listeners = new ArrayList<> (2);


    /** Adds an {@link com.tenduke.client.android.sample.crud.EntitySelectionListener} to the array
     *  of listeners.
     *
     *  @param listener listener to add.
     */
    @SuppressWarnings("unchecked")
    public void addSelectionListener (@NonNull final EntitySelectionListener listener) {
        _listeners.add (listener);
    }


    /** Implementation point: Binds an entity to the the view.
     *
     * @param entity entity to bind
     * @param holder the view to bind to
     */
    protected abstract void bindEntityToView (@NonNull T entity, @NonNull VH holder);


    /** Handles "entity-modified"-action.
     *
     *  <p>
     *  This modifies the list properly, based on the action, eg. inserts new item, modifies an
     *  existing one or removes the entity from the list.
     *  </p>
     *
     *  @param entity {@inheritDoc}
     *  @param action {@inheritDoc}
     */
    @Override
    public void entityModified(@NonNull final T entity, @NonNull final EntityModificationAction action) {
        //
        if (action == EntityModificationAction.CREATE) {
            _entities.add(entity);
            notifyItemInserted(_entities.size () - 1);
        }
        else {
            int i = findIndexOfEntity(entity);
            if (i >= 0) {
                switch (action) {

                    case DELETE:
                        _entities.remove (i);
                        notifyItemRemoved (i);
                        break;

                    case UPDATE:
                        _entities.set(i, entity);
                        notifyItemChanged(i);
                }
            }
        }
    }


    /** Implementation point: Given entity, find it's index in the list.
     *
     *  @param entity The entity to find
     *  @return index of the entity in the list. If negative, entity was not found in the list.
     */
    protected abstract int findIndexOfEntity(@NonNull T entity);


    /** {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public int getItemCount() {
        //
        return _entities.size ();
    }


    /** Returns the contained entities.
     *
     * @return the contained entities.
     */
    protected @NonNull List<T> getEntities () {
        return _entities;
    }


    /** Notifies registered listeners of entity selection.
     *
     * @param entity Entity, which has been selected.
     */
    protected void notifySelection (@NonNull final T entity) {
        //
        for (final EntitySelectionListener<T> listener : _listeners) {
            listener.onEntitySelected(entity);
        }
    }


    /** Binds entity by given position to the view holder.
     *
     *  <p>This sets up the view holder and calls
     *  {@link #bindEntityToView(Object, RecyclerView.ViewHolder)} to do the actual binding.</p>
     *
     *  @param holder Holder to bind to
     *  @param position Position of the entity in the list
     */
    @Override
    public void onBindViewHolder(final VH holder, int position) {
        //
        final T entity = getEntities().get (position);
        bindEntityToView (entity, holder);
        holder.itemView.setOnClickListener(this);
        holder.itemView.setTag (entity);
    }


    /** Called when a list item has been clicked: Notifies listeners of entity selection.
     *
      * @param view view
     */
    @Override
    public void onClick(final View view) {
        //
        if (view != null) {
            notifySelection (getTag (view));
        }
    }

    @SuppressWarnings("unchecked")
    private T getTag (@NonNull final View view) {
        //
        return (T) view.getTag();
    }

    /** Implementation point: Populate the list of entities.
     *
     */
    public abstract void populate ();


    /** Sets (replaces) the current list of entities.
     *
     * @param entities List of entities to set.
     */
    protected void setEntities (@Nullable final List<T> entities) {
        //
        _entities.clear();
        if (entities != null) {
            _entities.addAll (entities);
        }
        notifyDataSetChanged();
    }


    /** The view holder-class.
     *
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        /** Constructs new instance.
         *
         * @param itemView -
         */
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

}
