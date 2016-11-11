package com.tenduke.client.android.sample.crud;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.tenduke.client.android.sample.BaseActivity;
import com.tenduke.client.android.sample.R;

import java.io.Serializable;

/** Abstract base class for a "edit details" -activity.
 *
 *  <p>
 *  The detail-activity only host the actual {@link AbstractDetailFragment}, which contains
 *  the "edit details" -logic.
 *  </p>
 *
 * @param <T> Type of the object to manipulate.
 */
public abstract class AbstractDetailActivity<T> extends BaseActivity implements EntityModificationListener<T> {

    /** Key for return value of "entity". */
    public static final String EXTRA_OUT_ENTITY="result.out.entity";

    private EntityModificationAction _actionToReport = null;
    private T _entity = null;


    /** Finishes the activity, returning a result.
     *
     *  <p>
     *  Calls internally {@link #setResult(int, Intent)} and {@link #finish()}.
     *  </p>
     */
    public void doFinish () {
        //
        int resultCode = (_actionToReport == null ? RESULT_CANCELED : _actionToReport.getActivityResultCode());

        final Intent resultingData = new Intent();
        if (_entity != null) {
            resultingData.putExtra (EXTRA_OUT_ENTITY, (Serializable) _entity);
        }
        setResult(resultCode, resultingData);
        finish();
    }


    /** Called when entity has been modified.
     *
     *  @param entity {@inheritDoc}
     *  @param action {@inheritDoc}
     */
    @Override
    public void entityModified(@NonNull final T entity, @NonNull final EntityModificationAction action) {
        //
        _entity = entity;
        _actionToReport = action.mapToResultingAction(_actionToReport);

        switch (action) {
            case CREATE:
                Toast.makeText(this, R.string.toast_created, Toast.LENGTH_SHORT).show();
            case UPDATE:
                Toast.makeText(this, R.string.toast_updated, Toast.LENGTH_SHORT).show();
                break;
            case DELETE:
                doFinish ();
        }
    }


    /** {@inheritDoc}
     *
     */
    @Override
    public void onBackPressed() {
        doFinish ();
    }


    /** {@inheritDoc}
     *
     * @param savedInstanceState {@inheritDoc}
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud_detail);
    }


    /** Shows the detail fragment.
     *
     * @param fragment fragment to plug in.
     */
    protected void showDetailFragment (@NonNull final AbstractDetailFragment<T> fragment) {
        //
        showFragment(
                fragment,
                false,
                R.id.crud_detail_container
        );
    }


}
