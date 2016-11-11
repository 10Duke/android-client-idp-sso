package com.tenduke.client.android.storage;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Serializable;

/** Interface for key-value stores.
 * 
 */
public interface KeyValueStore {


    /** Deletes the stored object by key
     *
     *  Does not throw exceptions. If object was not found, does nothing.
     *
     *  @param key key
     */
    void delete(@NonNull String key);


    /** Reads stored object by key.
     *
     * @param <T> Type of the object
     * @param key key
     * @param objectClass class of stored object
     * @return the data. Returns {@code null} in case the object is not found (e.g. have not been stored).
     * @throws Exception -
     */
    @Nullable <T extends Serializable> T read (@NonNull String key, @NonNull Class<T> objectClass) throws Exception;


    /** Stores object by key.
     *
     * @param <T> Type of the object
     * @param key key
     * @param object object to store
     * @throws Exception -
     */
    <T extends Serializable> void store (@NonNull String key, @NonNull T object) throws Exception;

}
