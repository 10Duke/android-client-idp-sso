package com.tenduke.client.android.security;

import java.security.SignatureException;

/** Exception thrown, when the signature verification fails.
 * 
 */
public class SignatureVerificationException extends SignatureException {
    
    /** Constructs new instance.
     */
    public SignatureVerificationException() {
        super ();
    }
}
