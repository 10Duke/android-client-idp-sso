package com.tenduke.client.android.sample.crud;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.tenduke.client.android.sample.BaseActivity;
import com.tenduke.client.android.sample.R;


/** Abstract base class for "list entities" -activity.
 *
 *  <p>This activity mostly hosts the list-fragment. On landscape, the detail fragment is directly
 *  hosted too.</p>
 *
 *  @param <T> Type of object to list
 */
public abstract class AbstractListActivity <T> extends BaseActivity implements EntitySelectionListener<T>, EntityModificationListener<T> {

    protected static final int REQUEST_CODE_DETAIL = 2;

    private View _detailContainer;
    private View _listContainer;
    private AbstractDetailFragment<T> _detailFragment;
    private AbstractListFragment<T, ?> _listFragment;

    public abstract @NonNull AbstractDetailFragment<T> createDetailFragment (@Nullable final T entity);
    public abstract @NonNull Intent createDetailIntent (@Nullable final T entity);


    /** Handles results from {@link com.tenduke.client.android.sample.crud.AbstractDetailActivity}.
     *
     *  @param requestCode {@inheritDoc}
     *  @param resultCode {@inheritDoc}
     *  @param data {@inheritDoc}
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //
        if (requestCode == REQUEST_CODE_DETAIL) {
            final EntityModificationAction action = EntityModificationAction.byResultCode(resultCode);
            final T entity = getReturnValue(data);

            if (action != null && entity != null) {
                entityModified(entity, action);
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    @SuppressWarnings("unchecked")
    protected @Nullable T getReturnValue (@Nullable final Intent data) {
        //
        if (data == null) {
            return (null);
        }
        return (T) data.getSerializableExtra(AbstractDetailActivity.EXTRA_OUT_ENTITY);
    }


    /** Creates the base layout.
     *
     * @param savedInstanceState {@inheritDoc}
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud_list);
        setListContainer(findViewById(R.id.crud_list_container));
        setDetailContainer(findViewById(R.id.crud_detail_container));
    }


    /** Adds the "list entities" -menu.
     *
     * @param menu {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.activity_list, menu);
        return true;
    }


    /** Callback, which handles "entity modified"-actions.
     *
     *  <p>
     *  This callback gets called directly, when the application is in landscape, and the activity
     *  shows both the list-fragment and the detail-fragment. In these cases, the detail fragment
     *  calls this callback directly, when entities are modified.
     *  </p>
     *
     *  <p>
     *  This callback gets called indirectly from {@link #onActivityResult(int, int, Intent)}, when
     *  detail-activity has been closed and the detail-activity modified an entity.
     *  </p>
     *
     * @param entity {@inheritDoc}
     * @param action {@inheritDoc}
     */
    @Override
    public void entityModified(@NonNull final T entity, @NonNull final EntityModificationAction action) {
        //
        // Call the list-fragments entityModified ()-callback
        if (_listFragment != null) {
            _listFragment.entityModified(entity, action);
        }

        // If the entity was deleted in landscape, pop the fragment back stack.
        if (action == EntityModificationAction.DELETE && _detailContainer != null) {
            getFragmentManager().popBackStack();
        }
    }


    /** Callback, which is called, when entity has been selected from the list: Shows the entity
     *  details.
     *
     * @param entity {@inheritDoc}
     */
    @Override
    public void onEntitySelected(@NonNull final T entity) {
        //
        showDetail(entity);
    }


    /** Handles "create-entity" -action, delegates all others.
     *
     * @param item {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        //
        switch (item.getItemId()) {
            case R.id.action_create_entity:
                showDetail(null);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /** Shows the entity details.
     *
     *  <p>
     *  Depending on the layout (landscape / portrait), either shows the entity details in a fragment
     *  or starts the detail activity.
     *  </p>
     *
     * @param entity the entity to show.
     */
    public void showDetail (@Nullable final T entity) {
        //
        if (_detailContainer != null) {
            _detailFragment = createDetailFragment(entity);
            showFragment (
                    _detailFragment,
                    true,
                    _detailContainer.getId()
            );
        }
        else {
            //
            startActivityForResult(
                    createDetailIntent (entity),
                    REQUEST_CODE_DETAIL
            );
        }
    }


    /** Sets the list-container view.
     *
     * @param listContainer list-container view
     */
    protected void setListContainer(@Nullable View listContainer) {
        this._listContainer = listContainer;
    }


    /** Sets the list-fragment view.
     *
     * @param listFragment list-fragment view
     */
    public void setListFragment(AbstractListFragment<T, ?>  listFragment) {
        //
        this._listFragment = listFragment;
    }


    /** Sets the detail-container view.
     *
     * @param detailContainer the detail-container view
     */
    protected void setDetailContainer(@Nullable View detailContainer) {
        this._detailContainer = detailContainer;
    }


    /** Shows the list fragment.
     *
     */
    protected void showListFragment () {
        //
        if (_listFragment != null && _listContainer != null) {
          showFragment(_listFragment, false, _listContainer.getId());
        }
    }

}
