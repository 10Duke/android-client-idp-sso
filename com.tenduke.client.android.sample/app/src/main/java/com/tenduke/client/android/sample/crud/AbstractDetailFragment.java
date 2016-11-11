package com.tenduke.client.android.sample.crud;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.tenduke.client.android.sample.R;
import com.tenduke.client.android.sample.ui.AbstractFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;


/** Abstract base class for "edit details"-fragment.
 *
 *  <p>
 *  Base logic for "edit details" -functionality.
 *  </p>
 *
 * @param <T> Type of object to manipulate
 */
public abstract class AbstractDetailFragment<T> extends AbstractFragment implements View.OnClickListener {

    private final List<EntityModificationListener<T>> _listeners = new ArrayList<>(2);

    private FloatingActionButton _fab;
    private T _entity = null;


    /** Adds a {@link com.tenduke.client.android.sample.crud.EntityModificationListener}, which
     *  will receive notification, when the entity manipulation has successfully completed.
     *
     * @param listener listener to add.
     */
    public void addModificationListener (@NonNull final EntityModificationListener<T> listener) {
        //
        _listeners.add(listener);
    }


    /** Implementation point: Create the content view.
     *
     * @param inflater inflater to use
     * @param container container to plug to
     * @param savedInstanceState saved instance state
     * @return created content view.
     */
    protected abstract @NonNull View createContentView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container, @Nullable Bundle savedInstanceState);


    /** Implementation point: Bind data from the view to an entity.
     *
     *  @return object with data bound from the view.
     */
    protected abstract @NonNull T bindFromView ();


    /** Implementation point: Bind data from an entity to the view.
     *
     * @param entity entity to bind
     */
    protected abstract void bindToView (@Nullable final T entity);


    /** Implementation point: API-call to create the entity.
     *
     *  @param entity entity to create
     *  @return the Retrofit Call returned by the API.
     */
    protected abstract @NonNull Call<T> create (@NonNull final T entity);


    /** Implementation point:  API-call to delete the entity.
     *
     *  @param entity entity to delete
     *  @return the Retrofit Call returned by the API.
     */
    protected abstract @NonNull Call<Void> delete(@NonNull final T entity);


    /** Implementation point: API-call to read the entity.
     *
     *  @return the Retrofit Call returned by the API.
     */
    protected abstract @Nullable Call<T> read ();


    /** Implementation point:  API-call to update the entity.
     *
     *  @param entity the entity to update
     *  @return the Retrofit Call returned by the API.
     */
    protected abstract @NonNull Call<T> update (@NonNull final T entity);


    /** Deletes the current entity.
     *
     */
    protected void deleteEntity () {
        //
        final T entity = _entity;
        delete (_entity).enqueue(new AbstractCrudCallback<Void>(this) {

            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                hideProgressIndicator();
                notifyEntityModification(entity, EntityModificationAction.DELETE);
                _entity = null;
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                hideProgressIndicator();
            }

        });
    }


    /** Returns the current entity.
     *
     *  <p>AbstractDetailFragment holds the current entity.</p>
     *
     *  @return the current entity.
     */
    public T getEntity () {
        return _entity;
    }


    /** Notifies all registered listeners of a successful entity modification.
     *
     * @param entity the entity which has been successfully modified.
     * @param action the action which was done on the entity.
     */
    public void notifyEntityModification (@NonNull final T entity, @NonNull final EntityModificationAction action) {
        //
        for (final EntityModificationListener<T> listener : _listeners) {
            listener.entityModified(entity, action);
        }
    }


    /** On-click handler, handles only the fab-button.
     *
     * @param view the view
     */
    @Override
    public void onClick(View view) {
        //
        if (view == _fab) {
           save ();
        }
    }


    /** Creates the fragment.
     *
     *  <p>If the hosting activity is an {@link EntityModificationListener}, adds the activity
     *  to the list of registered listeners.</p>
     *
     *  @param savedInstanceState {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        //
        super.onCreate(savedInstanceState);

        super.setHasOptionsMenu(true);
        if (getActivity() instanceof EntityModificationListener) {
            addModificationListener((EntityModificationListener<T>) getActivity());
        }
    }


    /** Adds the crud-menu.
     *
     * @param menu menu
     * @param inflater inflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crud, menu);
    }


    /** Creates the basic "edit details"-layout:
     *
     *  <ul>
     *      <li>Sets base layout</li>
     *      <li>Calls the subclass to create the content view</li>
     *      <li>Adds the FAB-button</li>
     *  </ul>
     * @param inflater {@inheritDoc}
     * @param container {@inheritDoc}
     * @param savedInstanceState {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public @Nullable View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //
        // Content view
        final FrameLayout _layout = new FrameLayout(getActivity());
        _layout.setLayoutParams(new FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));

        _layout.addView (createContentView (inflater, _layout, savedInstanceState));

        // Fab button
        _fab = new FloatingActionButton(getActivity());
        final FrameLayout.LayoutParams fabLayout = new FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        fabLayout.gravity = Gravity.TOP + Gravity.END;
        _fab.setLayoutParams(fabLayout);
        _fab.setImageResource(R.drawable.ic_save_white_48dp);
        _fab.setOnClickListener(this);
        _layout.addView(_fab);

        return _layout;
    }


    /** Handles "delete-entity" -action, delegates all other actions.
     *
     * @param item {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //
        switch (item.getItemId()) {
            case R.id.action_delete_entity:
                deleteEntity();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /** {@inheritDoc}
     *
     * @param menu {@inheritDoc}
     */
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        //
        super.onPrepareOptionsMenu(menu);
        final MenuItem item = menu.findItem(R.id.action_delete_entity);
        if (item != null) {
            item.setVisible(_entity != null);
        }
    }


    /** Reads the entity into "current entity".
     *
     */
    @Override
    public void onStart() {
        //
        super.onStart();
        //
        final Call<T> call = read ();
        if (call != null) {

            call.enqueue(new AbstractCrudCallback<T>(this) {

                @Override
                public void onResponse(Call<T> call, Response<T> response) {
                    _entity = response.body();
                    bindToView(_entity);
                    hideProgressIndicator();
                    getActivity().invalidateOptionsMenu();
                }

                @Override
                public void onFailure(Call<T> call, Throwable t) {
                    hideProgressIndicator();
                }
            });
        }
        else {
            setProgressIndicatorVisible(false);
        }
    }


    /** Saves (creates or updates) the current entity.
     *
     */
    public void save () {
        //
        final boolean creating = (_entity == null);
        final T entity = bindFromView();

        if (creating) {

            create (entity).enqueue(new AbstractCrudCallback<T>(this) {

                @Override
                public void onResponse(Call<T> call, Response<T> response) {
                    hideProgressIndicator();
                    final T responseEntity = response.body();
                    notifyEntityModification(responseEntity, EntityModificationAction.CREATE);
                    _entity = responseEntity;
                    getActivity().invalidateOptionsMenu();
                }

                @Override
                public void onFailure(Call<T> call, Throwable t) {
                    hideProgressIndicator();
                }

            });
        }
        else {
            update (entity).enqueue(new AbstractCrudCallback<T>(this) {

                @Override
                public void onResponse(Call<T> call, Response<T> response) {
                    hideProgressIndicator();
                    final T responseEntity = response.body();
                    notifyEntityModification(responseEntity, EntityModificationAction.UPDATE);
                    _entity = responseEntity;
                }

                @Override
                public void onFailure(Call<T> call, Throwable t) {
                    hideProgressIndicator();
                }

            });
        }
    }

}
