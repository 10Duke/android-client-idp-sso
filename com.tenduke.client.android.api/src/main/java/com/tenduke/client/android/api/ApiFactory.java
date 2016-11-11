package com.tenduke.client.android.api;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tenduke.client.ApiCredentials;


/** Factory, which creates properly configured REST-API instances.
 * 
 *  @param <T> API interface class.
 */
public interface ApiFactory<T> {


    /** Constructs an API instance.
     * 
     *  @return 
     */
    @NonNull T get ();


    /** Sets credentials to use for creating the API instances.
     * 
     *  @param credentials credentials to use.
     */
    void setCredentials (@Nullable ApiCredentials credentials);

}
