package com.tenduke.client.android.security;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;


/** Container for secret key and cipher -pair.
 * 
 *  WARNING: This class is not thread safe.
 */
public class EncryptionContext {

    private final SecretKey _secretKey;
    private final Cipher _cipher;


    /** Constructs a new instance with given secret key and cipher.
     * 
     * @param secretKey the secret key
     * @param cipher the cipher
     */
    public EncryptionContext (@NonNull final SecretKey secretKey, @NonNull final Cipher cipher) {
        //
        _secretKey = secretKey;
        _cipher = cipher;
    }


    /** Constructs a new instance by generating a new secret key with given parameters and constructing a
     *  corresponding cipher.
     * 
     *  For key types, see {@link javax.crypto.KeyGenerator}.
     * 
     *  For cipher types, see {@link javax.crypto.Cipher}.
     * 
     *  @param keyType Type of key
     *  @param keyBits Bits in key
     *  @param cipherType Cipher type to generate.
     *  @param random Random generator to be used with key generation. If {@code null}, a new SecureRandom is constructed.
     *  @throws java.security.NoSuchAlgorithmException -
     *  @throws javax.crypto.NoSuchPaddingException -
     */
    public EncryptionContext (
            @NonNull final String keyType,
            final int keyBits,
            @NonNull final String cipherType,
            @Nullable final SecureRandom random) throws NoSuchAlgorithmException, NoSuchPaddingException {
        //
        this (
                generateSecretKey(keyType, keyBits, random),
                Cipher.getInstance(cipherType)
        );
    }


    /** Constructs a new instance by generating a new secret key and constructing a corresponding cipher.
     * 
     *  @param random Random generator to be used with key generation. If {@code null}, a new SecureRandom is constructed.
     *  @throws NoSuchAlgorithmException -
     *  @throws NoSuchPaddingException  -
     */
    public EncryptionContext (@Nullable final SecureRandom random) throws NoSuchAlgorithmException, NoSuchPaddingException {
        this ("AES", 256, "AES/ECB/PKCS5Padding", random);
    }


    /** Constructs a new instance by generating a new secret key and constructing a corresponding cipher.
     * 
     *  @throws NoSuchAlgorithmException -
     *  @throws NoSuchPaddingException  -
     */
    public EncryptionContext () throws NoSuchAlgorithmException, NoSuchPaddingException {
            this (null);
    }

    
    /** Generates a new {@link SecretKey} with given parameters.
     * 
     *  For key types, see {@link javax.crypto.KeyGenerator}.
     * 
     * 
     *  @param keyType Type of key
     *  @param keyBits Bits in key
     *  @param random Random generator to be used with key generation. If {@code null}, a new SecureRandom is constructed.
     *  @return a new SecretKey.
     *  @throws NoSuchAlgorithmException -
     */
    public static @NonNull SecretKey generateSecretKey (
            @NonNull final String keyType,
            final int keyBits,
            @Nullable final SecureRandom random) throws NoSuchAlgorithmException {
        //
        final KeyGenerator secretKeyGenerator = KeyGenerator.getInstance(keyType);

        secretKeyGenerator.init(keyBits, (random == null ? new SecureRandom() : random));
        return secretKeyGenerator.generateKey();
    }


    /** Initializes the context for encryption.
     * 
     *  @throws InvalidKeyException -
     */
    public void initForEncryption () throws InvalidKeyException {
        _cipher.init (Cipher.ENCRYPT_MODE, _secretKey);
    }


    /** Initializes the context for decryption.
     * 
     *  @throws InvalidKeyException -
     */
    public void initForDecryption () throws InvalidKeyException {
        _cipher.init (Cipher.DECRYPT_MODE, _secretKey);
    }


    /** Returns the contained cipher.
     * 
     * @return the contained cipher
     */
    public Cipher getCipher() {
        return _cipher;
    }


    /** Returns the contained secret key.
     * 
     * @return the contained secret key
     */
    public SecretKey getSecretKey() {
        return _secretKey;
    }
}
