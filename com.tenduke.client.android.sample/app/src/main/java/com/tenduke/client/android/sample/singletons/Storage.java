package com.tenduke.client.android.sample.singletons;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tenduke.client.android.storage.KeyValueStore;

import java.io.Serializable;

/** Simple storage-singleton.
 *
 *  <p>Currently used for persisting the API-credentials</p>
  */
public enum Storage {

    /** The singleton instance.
     */
    INSTANCE;

    private KeyValueStore _store;


    /** Configures the KeyValue-store, should be called only once, during application initialization.
     *
     * @param store The KeyValue-store to use.
     */
    public void configure (@NonNull final KeyValueStore store) {
        _store = store;
    }


    /** Returns the configured KeyValue-store.
     *
     * @return the configured KeyValue-store
     */
    public KeyValueStore get () {
        return _store;
    }


    /** Pre-configured keys.
     *
     *  Example usage could be:
     *
     *  <pre>
     *    Storage.Key.CREDENTIALS.store (credentials);
     *    credentials = Storage.Key.CREDENTIALS.read (ApiCredentials.class);
     *    Storage.Key.CREDENTIALS.delete ();
     *  </pre>
     */
    public enum Key {

        /** 10Duke API-credentials are stored with this key. */
        CREDENTIALS("10duke.sso.credentials");

        private final String _key;


        /** Constructor.
         *
         * @param key Key to use.
         */
        Key(final String key) {
            _key = key;
        }


        /** Convenience method, which wraps to {@link com.tenduke.client.android.storage.KeyValueStore#delete(java.lang.String)}.
         *
         */
        public void delete() {
            Storage.INSTANCE.get ().delete(_key);
        }


        /** Convenience method, which wraps to {@link com.tenduke.client.android.storage.KeyValueStore#read(String, Class)}.
         *
         * @param objectClass Class to read
         * @param <T> Type of the class to read
         * @return The stored object, may return {@code null} if the object is not found by the key.
         * @throws Exception if an exception occurred
         */
        @Nullable public <T extends Serializable> T read(Class<T> objectClass) throws Exception {
            return Storage.INSTANCE.get ().read(_key, objectClass);
        }


        /** Convenience method, which wraps to {@link com.tenduke.client.android.storage.KeyValueStore#store(java.lang.String, java.io.Serializable)} .
         *
         * @param object Object to store
         * @param <T> Type of the object to store
         * @throws Exception if an exception occurred
         */
        public <T extends Serializable> void store(@NonNull T object) throws Exception {
            Storage.INSTANCE.get ().store(_key, object);
        }
    }



}
