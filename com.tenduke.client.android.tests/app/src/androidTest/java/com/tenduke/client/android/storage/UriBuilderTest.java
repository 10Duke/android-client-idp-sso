package com.tenduke.client.android.storage;

import android.net.Uri;

import org.junit.Test;

public class UriBuilderTest {

    @Test
    public void testUriBuildah ()
    {
        asok ("https://localhost/ploppo", "para/plyy");
        asok ("https://localhost/ploppo", "para/plyy/");
        asok ("https://localhost/ploppo", "/para/plyy");
        asok ("https://localhost/ploppo", "/para/plyy/");
        asok ("https://localhost/ploppo/", "para/plyy");
        asok ("https://localhost/ploppo/", "para/plyy/");
        asok ("https://localhost/ploppo/", "/para/plyy");
        asok ("https://localhost/ploppo/", "/para/plyy/");
    }

    public void asok (final String base, final String path) {

        System.out.println ("**********************************************************************");
        System.out.println ("\"" + base + "\".path (\"" + path + "\") =");
        System.out.println (Uri.parse(base).buildUpon().path(path).build().toString());
        System.out.println ("\"" + base + "\".appendPath (\"" + path + "\") =");
        System.out.println (Uri.parse(base).buildUpon().appendPath(path).build().toString());
        System.out.println ("\"" + base + "\".appendEncodedPath (\"" + path + "\") =");
        System.out.println (Uri.parse(base).buildUpon().appendEncodedPath(path).build().toString());
        System.out.println ("**********************************************************************");
    }

    @Test
    public void testUriBuildaaah ()
    {
        final Uri uri = Uri.parse ("https://vslidp.10duke.com");

        System.out.println ("**********************************************************************");
        System.out.println ("scheme = " + uri.getScheme());
        System.out.println ("schemeSpecificPart = " + uri.getSchemeSpecificPart());
        System.out.println ("encodedSchemeSpecificPart = " + uri.getEncodedSchemeSpecificPart());
        System.out.println ("");
        System.out.println (new Uri.Builder ().scheme(uri.getScheme()).path("//localhost/testos.html").build().toString());
        System.out.println ("**********************************************************************");
    }

    @Test
    public void testUriBuildaaahz ()
    {
        final Uri uri = Uri.parse ("https://vslidp.10duke.com");

        System.out.println ("**********************************************************************");
        System.out.println ("scheme = " + uri.getScheme());
        System.out.println ("schemeSpecificPart = " + uri.getSchemeSpecificPart());
        System.out.println ("encodedSchemeSpecificPart = " + uri.getEncodedSchemeSpecificPart());
        System.out.println ("");
        System.out.println (uri.buildUpon().scheme("http").toString());
        System.out.println ("**********************************************************************");
    }

    @Test
    public void testUriBuilder ()
    {
        //Uri uri = Uri.parse ("https://localhost/oauth2_login_success.html")
        Uri uri = Uri.parse ("https://localhost/aunariz")
                . buildUpon()
                . appendPath("/asokkos")
                . appendQueryParameter ("response_type", "token+id_token")
                . appendQueryParameter ("scope", "openid+email+profile")
                . appendQueryParameter ("client_id", "asdf")
                . appendQueryParameter ("redirect_uri", "https://localhost:4242/login-successful.html")
                . appendQueryParameter ("nonce", "zxcv")
                . build()
                ;
        System.out.println ("**********************************************************************");
        System.out.println (uri.toString());
        System.out.println ("**********************************************************************");
    }


}
