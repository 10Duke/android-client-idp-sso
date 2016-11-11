package com.tenduke.client.android.security;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Signature;


/** Container for key-pair and signature -pair.
 * 
 */
public class SigningContext {

    private final KeyPair _keyPair;
    private final Signature _signature;

    
    /** Constructs a new instance with given key-pair and signature engine.
     * 
     *  @param keyPair -
     *  @param signature - 
     */

    public SigningContext(/*@NonNull*/ final KeyPair keyPair, /*@NonNull*/ final Signature signature) {
        this._keyPair = keyPair;
        this._signature = signature;
    }


    /** Constructs a new instance by generating a new signing key-pair.
     * 
     *  @param signingKeyType -
     *  @param signingKeyBits -
     *  @param signatureType -
     *  @param random Random generator to be used with key generation. If {@code null}, a new SecureRandom is constructed.
     *  @throws NoSuchAlgorithmException -
     */

    public SigningContext (
            /*@NonNull*/ final String signingKeyType,
            final int signingKeyBits,
            /*@NonNull*/ final String signatureType,
            /*@Nullable*/ final SecureRandom random) throws NoSuchAlgorithmException {
        this (
                generateKeyPair (signingKeyType, signingKeyBits, random),
                Signature.getInstance(signatureType)
        );
    }


    /** Constructs a new instance by generating a new secret key and using suitable signature engine.
     * 
     *  @param random Random generator to be used with key generation. If {@code null}, a new SecureRandom is constructed.
     *  @throws NoSuchAlgorithmException  -
     */
    public SigningContext (/*@Nullable*/ final SecureRandom random) throws NoSuchAlgorithmException {
        this ("RSA", 1024, "SHA256withRSA", random);
    }


    /** Constructs a new instance by generating a new secret key and using suitable signature engine.
     * 
     *  @throws NoSuchAlgorithmException  -
     */
    public SigningContext () throws NoSuchAlgorithmException {
        this (null);
    }

    
    /** Generates a new key-pair with given characteristics.
     * 
     *  @param keyType Type of key
     *  @param keyBits Bits in key
     *  @param random Random generator to be used with key generation. If {@code null}, a new SecureRandom is constructed.
     *  @return the generated key pair
     *  @throws NoSuchAlgorithmException -
     */
    public static /*@NonNull*/ KeyPair generateKeyPair (
            /*@NonNull*/ final String keyType,
            final int keyBits,
            /*@Nullable*/ final SecureRandom random) throws NoSuchAlgorithmException {
        //
        final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(keyType);

        keyPairGenerator.initialize(keyBits, (random == null ? new SecureRandom() : random));

        return keyPairGenerator.genKeyPair();
    }

    
    /** Returns the contained key pair.
     * 
     *  @return  -
     */
    public KeyPair getKeyPair() {
        return _keyPair;
    }


    /** Returns the contained signature engine.
     * 
     *  @return  -
     */
    public Signature getSignature() {
        return _signature;
    }
}
