package com.tenduke.client.android.sample.ui;


/** Interface for simple progress-indicator functionality.
 *
 */
public interface ProgressIndicator {

    /** Checks if the progress indicator is visible.
     *
     *  @return true if the progress indicator is visible.
     */
    boolean isProgressIndicatorVisible ();


    /** Shows or hides the progress indicator.
     *
     * @param visible if true, shows the progress indicator. Hides it otherwise.
     */
    void setProgressIndicatorVisible (boolean visible);


    /** Shows or hides the progress indicator based on the current status (ensures the
     *  indicator is visible or hidden).
     *
     *  <p>
     *  Sometimes the progress indicator status is set, but the layout has not been
     *  created yet. This allows you to enforce the status after the layout has been created.
     *  </p>
     */
    void setProgressIndicatorVisible ();

}
