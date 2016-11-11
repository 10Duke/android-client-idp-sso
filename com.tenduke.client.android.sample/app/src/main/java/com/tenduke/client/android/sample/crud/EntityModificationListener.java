package com.tenduke.client.android.sample.crud;

import android.support.annotation.NonNull;


/** Listener interface for handling entity modification notifications.
 *
 * @param <T> Type of the object to handle
 */
public interface EntityModificationListener<T> {

    /** Called when entity has been successfully modified.
     *
     * @param entity entity, which was modified
     * @param action action, which was done on the entity
     */
    void entityModified (@NonNull T entity, @NonNull EntityModificationAction action);

}
