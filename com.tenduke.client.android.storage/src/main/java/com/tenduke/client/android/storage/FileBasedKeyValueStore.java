package com.tenduke.client.android.storage;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import com.tenduke.client.android.security.EncryptionContext;
import com.tenduke.client.android.security.SigningContext;
import com.tenduke.client.io.IOUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.SignatureException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;


/** A key-value store, which uses file system.
 * 
 */
public class FileBasedKeyValueStore extends AbstractEncryptingStore implements KeyValueStore {

    private final Context _context;
    private final Object _lock = new Object ();
    private final File _directory;


    /** Constructs a new instance.
     *
     * @param context -
     * @param encryptionContext Encryption to use. Optional, if {@code null}, the data is not encrypted.
     * @param signingContext Signing to use. Optional, if {@code null}, the data is not signed.
     * @param directory Directory to store the files in. Optional. If not given, the default directory is returned by {@link Context#getFilesDir() } is used.
     * @throws java.io.IOException -
     */
    public FileBasedKeyValueStore(
            @NonNull final Context context,
            @Nullable final EncryptionContext encryptionContext,
            @Nullable final SigningContext signingContext,
            @Nullable final String directory) throws IOException {
        //
        super (encryptionContext, signingContext);
        _context = context;
        if (directory == null) {
            _directory = context.getFilesDir();
        }
        else {
            _directory = new File (context.getFilesDir (), directory);
        }
        if (_directory.exists() && ! _directory.isDirectory()) {
            throw new IOException ("File " + _directory.getAbsolutePath() + " exists and is not a directory!");
        }
        if (! _directory.exists ()) {
            if (! _directory.mkdirs()) {
                throw new IOException ("Unable to created directory " + _directory.getAbsolutePath());
            }
        }
    }


    /** Constructs a new instance, using default directory.
     *
     * @param context -
     * @param encryptionContext Encryption to use. Optional, if {@code null}, the data is not encrypted.
     * @param signingContext Signing to use. Optional, if {@code null}, the data is not signed.
     * @throws java.io.IOException -
     */
    public FileBasedKeyValueStore(
            @NonNull final Context context,
            @Nullable final EncryptionContext encryptionContext,
            @Nullable final SigningContext signingContext
    ) throws IOException {
        //
        this (context, encryptionContext, signingContext, FileBasedKeyValueStore.class.getName());
    }


    /** Constructs a new instance without encryption or signature, using default directory.
     *
     * @param context -
     * @throws java.io.IOException -
     */
    public FileBasedKeyValueStore(@NonNull final Context context) throws IOException {
        this (context, null, null);
    }


    /** {@inheritDoc}
     * 
     *  @param key {@inheritDoc }
     */
    @Override
    public void delete (@NonNull final String key) {

        synchronized (_lock) {

            final boolean deleted = new File (_directory, key).delete();

            Log.d (TAG, "delete(), deleted = " + deleted);

        }

    }


    /** {@inheritDoc}
     *
     *  @param <T> {@inheritDoc }
     *  @param key {@inheritDoc }
     *  @param objectClass {@inheritDoc }
     *  @return {@inheritDoc }
     *  @throws BadPaddingException -
     *  @throws ClassCastException -
     *  @throws ClassNotFoundException -
     *  @throws IllegalBlockSizeException -
     *  @throws InvalidKeyException -
     *  @throws IOException -
     *  @throws SignatureException -
     */
    @Override
    public <T extends Serializable> T read (@NonNull final String key, @NonNull final Class<T> objectClass) throws BadPaddingException, ClassCastException, ClassNotFoundException, IllegalBlockSizeException, InvalidKeyException, IOException, SignatureException {

        synchronized (_lock) {

            try {
                Object object = readObject(key);
                object = retrieveSignedObject(object);
                object = decryptSealedObject(object);

                return objectClass.cast(object);
            }
            catch (final FileNotFoundException e) {
                return (null);
            }
        }
    }


    /** {@inheritDoc}
     *
     *  @param <T> {@inheritDoc }
     *  @param key {@inheritDoc }
     *  @param object {@inheritDoc }
     *  @throws IllegalBlockSizeException -
     *  @throws InvalidKeyException -
     *  @throws IOException -
     *  @throws SignatureException -
     */
    @Override
    public <T extends Serializable> void store (@NonNull final String key, @NonNull final T object) throws IllegalBlockSizeException, InvalidKeyException, IOException, SignatureException {

        synchronized (_lock) {

            final Serializable objectToWrite = signObject(sealObject(object));

            Throwable mainException = null;
            final ObjectOutputStream stream = openOutputStream(key);
            try {
                stream.writeObject(objectToWrite);
            }
            catch (final Throwable t) {
                mainException = t;
                throw t;
            }
            finally {
                IOUtil.close (stream, mainException);
            }
        }
    }


    /** Reads the object from a file.
     * 
     *  @param key key
     *  @return the read object
     *  @throws ClassNotFoundException -
     *  @throws IOException -
     */
    protected Object readObject (@NonNull final String key) throws ClassNotFoundException, IOException {
        //
        Throwable mainException = null;
        final ObjectInputStream stream = openInputStream (key);
        //
        try {
            return stream.readObject();
        }
        catch (final ClassNotFoundException e) {
            mainException = e;
            throw e;
        }
        catch (final IOException e) {
            mainException = e;
            throw e;
        }
        finally {
            IOUtil.close (stream, mainException);
        }
    }

    
    /** Opens the input stream for reading the object.
     * 
     * @param filename Filename to open
     * @return opened ObjectInputStream
     * @throws IOException -
     */
    protected @NonNull ObjectInputStream openInputStream (@NonNull final String filename) throws IOException {
        //
        final FileInputStream fileStream = new FileInputStream(new File (_directory, filename));

        try {
            return new ObjectInputStream(fileStream);
        }
        catch (final Throwable t) {
            IOUtil.close (fileStream, t);
            throw t;
        }
    }


    /** Opens the input stream for writing the object.
     * 
     *  @param filename Filename to open
     *  @return opened ObjectOutputStream
     *  @throws IOException -
     */
    protected @NonNull ObjectOutputStream openOutputStream(@NonNull final String filename) throws IOException {
        //
        //final FileOutputStream fileStream = _context.openFileOutput(filename, Context.MODE_PRIVATE);
        final FileOutputStream fileStream = new FileOutputStream(new File (_directory, filename));

        try {
            return new ObjectOutputStream(fileStream);
        }
        catch (final Throwable t) {
            IOUtil.close (fileStream, t);
            throw t;
        }
    }


    private static final String TAG = FileBasedKeyValueStore.class.getSimpleName();

}
