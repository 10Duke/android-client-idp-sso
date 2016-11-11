package com.tenduke.client.android.android_utils;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.util.UUID;

/** Utilities for Bundle-manipulation.
 * 
 */
public class BundleUtil {


    /** Returns value by given {@code key} from the {@code bundle}, converting the value to UUID.
     * 
     *  @param key key to lookup
     *  @param bundle the bundle
     *  @return the value as UUID. If {@code bundle} is {@code null}, returns {@code null}. If value is not 
     *          found, returns {@code null}. If the value is NOT an UUID, undefined behavior occurs (i.e.
     *          usually IllegalArgumentException is thrown, but some invalid strings may be parsed into UUIDs,
     *          but the result maybe total garbage).
     *  @throws IllegalArgumentException if the value is not an UUID
     */
    public static @Nullable UUID getUUID (@NonNull final String key, final @Nullable Bundle bundle) {
        //
        if (bundle == null) {
            return null;
        }
        
        final String value = bundle.getString(key);
        
        return (value == null ? null : UUID.fromString(value));
    }


    /** Returns value by given {@code key} from the {@code intent}, converting the value to UUID.
     * 
     *  @param key key to lookup
     *  @param intent the intent
     *  @return the value as UUID. If {@code intent} is {@code null}, returns {@code null}. If value is not 
     *          found, returns {@code null}. If the value is NOT an UUID, undefined behavior occurs (i.e.
     *          usually IllegalArgumentException is thrown, but some invalid strings may be parsed into UUIDs,
     *          but the result maybe total garbage).
     *  @throws IllegalArgumentException if the value is not an UUID
     */
    public static @Nullable UUID getUUID (@NonNull final String key, final @Nullable Intent intent) {
        //
        if (intent == null) {
            return null;
        }
        
        return getUUID (key, intent.getExtras());
    }
    
}
