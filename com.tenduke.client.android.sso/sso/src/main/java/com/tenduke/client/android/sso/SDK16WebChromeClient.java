package com.tenduke.client.android.sso;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;

import java.util.ArrayList;
import java.util.List;


/** A guess-work based handler, which tries to detect HTTP-errors
 *  in SDK's older than 23.
 *
 *  Method {@link android.webkit.WebViewClient#onReceivedHttpError(WebView, WebResourceRequest, WebResourceResponse)} is available from SDK 23 onwards.
 *  Method {@link android.webkit.WebViewClient#onReceivedError(WebView, int, String, String)} does not report HTTP-errors.
 *
 *  Before SDK 23 there is no good way to detect HTTP-error, so this class implements a
 *  mother of all kludges: If the the TITLE-element of the HTML-document {@link #looksLikeError(String)},
 *  a HTTP-error is assumed.
 *
 *  This class is not use at SDK level 23 and above.
 */

public class SDK16WebChromeClient extends WebChromeClient{

    private final List<WebViewErrorListener> _listeners = new ArrayList<>(2);


    /** Adds an error listener, which will receive notification if (possible) HTTP-error occurred.
     *
     * @param listener -
     * @return the same SDK16WebChromeClient instance
     */
    public SDK16WebChromeClient withErrorListener (@NonNull final WebViewErrorListener listener) {
        _listeners.add(listener);
        return this;
    }


    /** Tries to detect from the title, if the response is an HTTP error.
     *
     *  If so, stops loading the view, and notifies all the error listeners of the error.
     *
     *  @param view the view
     *  @param title the title
     */
    @Override
    public void onReceivedTitle(final WebView view, final String title) {
        super.onReceivedTitle(view, title);

        if (title != null && looksLikeError (title)) {
            view.stopLoading();

            //
            for (final WebViewErrorListener listener : _listeners) {
                listener.onWebViewError(new SSOError(
                        guessHttpErrorCode(title),
                        title,
                        null
                ));
            }
        }
    }


    /** Tries to guess if the title string looks like error.
     *
     *  Currently returns true if the title contains (case does not matter):
     *    - error
     *    - not found
     *
     *  This method is obviously far from perfect...
     *
     *  @param title the title string
     *  @return true if this looks like error.
     */
    protected boolean looksLikeError (@NonNull final String title) {
        //
        return title.toLowerCase().contains("error")
            || title.toLowerCase().contains("not found")
        ;
    }


    /** Tries to guess the HTTP status code from the title.
     *
     * @param title The HTML-document TITLE-element contents.
     * @return The guessed HTTP status code or {@code null} if unable to deduce the status code.
     */
    protected @Nullable Integer guessHttpErrorCode (@NonNull final String title) {
        //
        // Note this is probably not the best way of doing this
        final String digits = title.replaceAll("[^0-9]", "");
        if (TextUtils.isEmpty(digits)) {
            return null;
        }
        return Integer.valueOf(digits);
    }

}
