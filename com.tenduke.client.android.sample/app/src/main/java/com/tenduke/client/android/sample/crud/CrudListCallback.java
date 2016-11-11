package com.tenduke.client.android.sample.crud;

import android.support.v7.widget.RecyclerView;

import com.tenduke.client.android.sample.ui.ProgressIndicator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;


/** A Retrofit-callback specializing for the CRUD list functionality.
 *
 *  @param <T> Type of the entity to handle
 */
public class CrudListCallback<T> extends AbstractCrudCallback<List<T>> {

    private final CrudRecyclerViewAdapter<T, ? extends RecyclerView.ViewHolder> _adapter;

    public CrudListCallback(CrudRecyclerViewAdapter<T, ? extends RecyclerView.ViewHolder> adapter, ProgressIndicator progressIndicator) {
        super (progressIndicator);
        _adapter = adapter;
    }

    @Override
    public void onResponse(final Call<List<T>> call, final Response<List<T>> response) {
        //
        _adapter.setEntities(response.body());
        hideProgressIndicator ();
    }

    @Override
    public void onFailure(final Call<List<T>> call, final Throwable t) {
        hideProgressIndicator ();
    }

}
