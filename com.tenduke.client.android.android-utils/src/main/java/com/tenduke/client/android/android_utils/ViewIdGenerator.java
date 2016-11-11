package com.tenduke.client.android.android_utils;

import android.os.Build;
import android.view.View;
import java.util.concurrent.atomic.AtomicInteger;

/** Utility for dynamically generating ids for Views.
 * 
 */
public class ViewIdGenerator {

    private static final AtomicInteger NEXT_GENERATED_ID = new AtomicInteger(1);
    

    /** A facade for dynamically generating ids for Views.
     *  On versions older than JELLY_BEAN_MR1, uses {@link #generateUnofficialViewId() }, otherwise
     *  uses the official {@link View#generateViewId() }.
     * 
     *  @return a dynamically generated id
     */
    public static int generateViewId () {
        //
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return ViewIdGenerator.generateUnofficialViewId();
        }
        else {
            return View.generateViewId();
        }
    }
    

    /** Generate a value suitable for use in {@link #setId(int)}.
     *  This value will not collide with ID values generated at build time by aapt for R.id.
     *
     *  @return a generated ID value
     */
    protected static int generateUnofficialViewId() {
        for (;;) {
            final int result = NEXT_GENERATED_ID.get();
            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
            if (NEXT_GENERATED_ID.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }
}
