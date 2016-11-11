package com.tenduke.client.android.sso;

import android.support.annotation.Nullable;

import java.io.Serializable;

/** An SSO-error, gives details why SSO failed.
 *
 */
public class SSOError implements Serializable {

    private final Integer _numericCode;
    private final String _errorCode;
    private final String _description;
    private final String _url;

    /** Constructs new instance.
     *
     *  @param numericCode numeric code (eg. 404)
     *  @param errorCode error code
     *  @param description descriptive message
     *  @param url Url causing the error
     */
    public SSOError(
            @Nullable final Integer numericCode,
            @Nullable final String errorCode,
            @Nullable final String description,
            @Nullable final String url) {
        _description = description;
        _numericCode = numericCode;
        _errorCode = errorCode;
        _url = url;
    }


    /** Constructs new instance.
     *
     *  @param numericCode numeric code (eg. 404)
     *  @param description descriptive message
     *  @param url Url causing the error
     */

    public SSOError(
            @Nullable Integer numericCode,
            @Nullable final String description,
            @Nullable String url) {
        this (numericCode, null, description, url);
    }


    /** Constructs new instance.
     *
     *  @param errorCode error code
     *  @param description descriptive message
     */

    public SSOError(
            @Nullable final String errorCode,
            @Nullable final String description) {
        this (null, errorCode, description);
    }


    public String getDescription() {
        return _description;
    }

    public String getErrorCode() {
        return _errorCode;
    }

    public Integer getNumericCode() {
        return _numericCode;
    }

    public String getUrl() {
        return _url;
    }


    @Override
    public String toString() {
        return "SSOError{" +
                "_description='" + _description + '\'' +
                ", _numericCode=" + _numericCode +
                ", _errorCode='" + _errorCode + '\'' +
                ", _url='" + _url + '\'' +
                '}';
    }
}
