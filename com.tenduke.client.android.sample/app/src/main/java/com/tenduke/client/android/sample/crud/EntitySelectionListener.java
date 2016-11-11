package com.tenduke.client.android.sample.crud;

import android.support.annotation.NonNull;


/** Interface for handling notifications of entity selections from a list.
 *
 * @param <T> Type of the entity
 */
public interface EntitySelectionListener<T> {

    /** Called when entity has been selected from a list.
     *
     *  @param entity entity, which was selected
     */
    void onEntitySelected (@NonNull T entity);

}
