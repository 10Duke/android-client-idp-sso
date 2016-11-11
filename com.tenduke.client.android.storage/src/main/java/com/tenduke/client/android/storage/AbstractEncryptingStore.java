package com.tenduke.client.android.storage;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import com.tenduke.client.android.security.SignatureVerificationException;
import com.tenduke.client.android.security.EncryptionContext;
import com.tenduke.client.android.security.SigningContext;
import java.io.IOException;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.SignatureException;
import java.security.SignedObject;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SealedObject;


/** Abstract base class for key value stores, providing some utilities for storage providers.
 */
public abstract class AbstractEncryptingStore {

    private final EncryptionContext _encryptionContext;
    private final SigningContext _signingContext;


    /** Protected constructor.
     * 
     * @param encryptionContext -
     * @param signingContext -
     */
    protected AbstractEncryptingStore(
            @Nullable final EncryptionContext encryptionContext,
            @Nullable final SigningContext signingContext) {
        //
        _encryptionContext = encryptionContext;
        _signingContext = signingContext;
    }


    /** If the object given as parameter is {@link SealedObject}, decrypts the object
     *  and returns the contained object.
     *
     *  If the object is {@link SealedObject}, but no encryption context is set, returns the object
     *  given as parameter.
     *
     *  If the object is NOT {@link SealedObject}, returns the object given as parameter.
     *
     *  @param object The object to decrypt. If the object is not {@link SealedObject}, returns the same object.
     *  @return Either the decrypted object (returned by {@link SealedObject#getObject(Cipher)}) or the object passed as parameter.
     *  @throws javax.crypto.BadPaddingException -
     *  @throws java.lang.ClassNotFoundException -
     *  @throws javax.crypto.IllegalBlockSizeException -
     *  @throws java.security.InvalidKeyException -
     *  @throws java.io.IOException -
     */
    protected Object decryptSealedObject (@NonNull final Object object) throws BadPaddingException, ClassNotFoundException, IllegalBlockSizeException, InvalidKeyException, IOException {
        //
        if (object instanceof SealedObject) {

            if (_encryptionContext == null) {
                return object;
            }

            _encryptionContext.initForDecryption();
            return ((SealedObject) object).getObject(_encryptionContext.getCipher());
        }

        // Object is not SealedObject, return it as it is.
        return object;
    }


    /** If the object given as parameter is {@link java.security.SignedObject}, verifies the
     *  signature of the object and returns the contained object (as returned by {@link SignedObject#getObject()}.
     *
     *  If no signing context is set, the implementation does NOT verify the signature, but returns
     *  the contained object and writes a warning to log.
     *
     *  If the object is not {@link SignedObject}, returns the object given as parameter as is.
     *
     *  If signature verification fails, a {@link SignatureVerificationException} is thrown.
     *
     *  @param object object to process.
     *  @return the object given as parameter, or, if the object given as parameter was a
     *  {@link SignedObject}, then the value returned by {@link SignedObject#getObject()}.
     *  @throws java.lang.ClassNotFoundException -
     *  @throws java.security.InvalidKeyException -
     *  @throws java.io.IOException -
     *  @throws SignatureVerificationException if the signature verification failed.
     */
    protected Object retrieveSignedObject (@NonNull final Object object) throws ClassNotFoundException, InvalidKeyException, IOException, SignatureException, SignatureVerificationException {
        //
        if (object instanceof SignedObject) {
            final SignedObject signedObject = (SignedObject) object;

            // Object is SignedObject, but we don't have the signature settings configured.
            // Issue warning and return the object.
            if (_signingContext == null) {
                Log.w (TAG, "retrieveSignedObject(): Object is SignedObject, but no signature settings configured. Returning the object without verifying the signature!");
                return signedObject.getObject();
            }

            // Signature verification succeeded:
            if (signedObject.verify(_signingContext.getKeyPair().getPublic(), _signingContext.getSignature())) {
                return signedObject.getObject();
            }
            else {
                throw new SignatureVerificationException();
            }
        }

        // Object is not SignedObject, return it as it is.
        return object;
    }


    /** If encryption context is set, encrypts the object into a {@link javax.crypto.SealedObject}
     *  and returns the sealed object. Otherwise returns the object given as parameter.
     *
     *  @param object the object to encrypt
     *  @return if encryption context is set, a new {@link SealedObject} containing the object.
     *  Otherwise the object given as parameter is returned as it is.
     *  @throws IllegalBlockSizeException -
     *  @throws IOException -
     *  @throws InvalidKeyException -
     */
    protected @NonNull Serializable sealObject (@NonNull Serializable object) throws IllegalBlockSizeException, IOException, InvalidKeyException {
        //
        if (_encryptionContext == null) {
            return object;
        }

        _encryptionContext.initForEncryption();

        return new SealedObject(object, _encryptionContext.getCipher());
    }


    /** If signing context is set, envelopes the object inside {@link java.security.SignedObject} and
     *  returns the signed object. Otherwise returns the object given as parameter.
     *
     *  @param object the object to sign
     *  @return if signing context is set, a new {@link SignedObject} containing the object.
     *  Otherwise the object given parameter is returned as it is.
     *  @throws IOException -
     *  @throws InvalidKeyException -
     *  @throws SignatureException -
     */
    protected @NonNull Serializable signObject (@NonNull Serializable object) throws IOException, InvalidKeyException, SignatureException {
        //
        if (_signingContext == null) {
            return object;
        }

        return new SignedObject(object, _signingContext.getKeyPair().getPrivate(), _signingContext.getSignature());
    }


    /** Returns the encryption context.
     * 
     * @return the encryption context
     */
    protected @Nullable EncryptionContext getEncryptionContext() {
        return _encryptionContext;
    }

    
    /** Returns the signing context.
     * 
     * @return the signing context
     */
    protected @Nullable SigningContext getSigningContext() {
        return _signingContext;
    }

    
    private static final String TAG = AbstractEncryptingStore.class.getSimpleName();

}
