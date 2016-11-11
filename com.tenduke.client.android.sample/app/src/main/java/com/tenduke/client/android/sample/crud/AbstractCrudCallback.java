package com.tenduke.client.android.sample.crud;

import android.support.annotation.Nullable;

import com.tenduke.client.android.sample.ui.ProgressIndicator;

import retrofit2.Callback;

/** Abstract base Retrofit-callback, which provides progress indicator-manipulation.
 *
 * @param <T> Type of the object to be manipulated by the API.
 */
public abstract class AbstractCrudCallback <T> implements Callback<T> {

    private final ProgressIndicator _progressIndicator;


    /** Constructs new instance.
     *
     * @param _progressIndicator Progress indicator -view
     */
    public AbstractCrudCallback(@Nullable final ProgressIndicator _progressIndicator) {
        this._progressIndicator = _progressIndicator;
        showProgressIndicator();
    }


    /** Hides the progress indicator
     *
     */
    protected void hideProgressIndicator () {
        //
        if (_progressIndicator != null) {
            _progressIndicator.setProgressIndicatorVisible(false);
        }
    }


    /** Shows the progress indicator.
     *
     */
    protected void showProgressIndicator () {
        //
        if (_progressIndicator != null) {
            _progressIndicator.setProgressIndicatorVisible(true);
        }
    }

}
