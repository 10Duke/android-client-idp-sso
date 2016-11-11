package com.tenduke.client.android.sample.ui;

import android.app.Activity;
import android.app.Fragment;
import android.support.annotation.Nullable;


/** Abstract fragment with support for the progress indicator (delegates the progress indicator
 *  stuff to the hosting activity).
 *
 *  <p>If the hosting activity is not instance of {@link ProgressIndicator}, the progress-indicator
 *  methods do nothing.</p>
 */
public abstract class AbstractFragment extends Fragment implements ProgressIndicator {


    /** Retrieves the progress indicator from the hosting activity.
     *
     *  @return progress indicator or {@code null} if hosting activity is not instance of {@link ProgressIndicator}.
     */
    protected @Nullable ProgressIndicator getProgressIndicator () {
        //
        final Activity activity = getActivity();
        if (activity != null && activity instanceof ProgressIndicator) {
            return (ProgressIndicator) activity;
        }
        return null;
    }


    /** {@inheritDoc}
     *
     *  @return {@inheritDoc}
     */
    @Override
    public boolean isProgressIndicatorVisible() {
        //
        final ProgressIndicator indicator = getProgressIndicator();

        return indicator != null && indicator.isProgressIndicatorVisible();
    }


    /** {@inheritDoc}
     *
     *  @param visible {@inheritDoc}
     */
    @Override
    public void setProgressIndicatorVisible(boolean visible) {
        //
        final ProgressIndicator indicator = getProgressIndicator();
        if (indicator != null) {
            indicator.setProgressIndicatorVisible(visible);
        }
    }


    /** {@inheritDoc}
     */
    @Override
    public void setProgressIndicatorVisible() {
        //
        setProgressIndicatorVisible(isProgressIndicatorVisible());
    }



}
