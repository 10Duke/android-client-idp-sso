package com.tenduke.client.android.sample;

import com.tenduke.client.ApiCredentials;
import com.tenduke.client.android.storage.FileBasedKeyValueStore;
import com.tenduke.client.android.sample.singletons.Session;
import com.tenduke.client.android.sample.singletons.Storage;

/** An application instance.
 *
 */

public class Application extends android.app.Application {

    /** {@inheritDoc}
     *
     */
    @Override
    public void onCreate() {
        //
        super.onCreate();
        //
        try {
            // Configure storage to use FileBasedKeyValueStore.
            Storage.INSTANCE.configure (new FileBasedKeyValueStore(this));

            // Read stored credentials and if found, "log user in".
            Session.INSTANCE.logIn(Storage.Key.CREDENTIALS.read (ApiCredentials.class));
        }
        catch (final Throwable t) {
            throw new RuntimeException("Initialization error:", t);
        }

    }


}
