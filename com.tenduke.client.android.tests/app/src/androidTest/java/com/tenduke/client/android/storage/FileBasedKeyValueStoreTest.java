package com.tenduke.client.android.storage;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import com.tenduke.client.android.security.EncryptionContext;
import com.tenduke.client.android.security.SigningContext;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.Serializable;
import java.security.SignatureException;
import java.util.Date;

import javax.crypto.BadPaddingException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class FileBasedKeyValueStoreTest {

    private FileBasedKeyValueStore _provider;
    private TestData _testData;
    private Context _context;
    private String _key = "StoredData.key";

    @Before
    public void beforeTest () throws Exception {

        _context = InstrumentationRegistry.getTargetContext();
        _provider = new FileBasedKeyValueStore(_context);
        _testData = new TestData (new Date (), 42, "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
    }

    @After
    public void afterTest () throws Exception {

        _provider.delete(_key);

    }

    @Test
    @SmallTest
    public void testReadAndStore() throws Exception {
        //
        _provider.store(_key, _testData);
        assertEquals (_testData, _provider.read (_key, TestData.class));
    }

    @Test
    @SmallTest
    public void testReadAndStoreEncrypted() throws Exception {
        //
        _provider = new FileBasedKeyValueStore(_context, new EncryptionContext(), null);
        _provider.store(_key, _testData);
        assertEquals (_testData, _provider.read (_key, TestData.class));
    }

    @Test
    @SmallTest
    public void testReadAndStoreEncryptedAndSigned() throws Exception {
        //
        _provider = new FileBasedKeyValueStore(_context, new EncryptionContext(), new SigningContext());
        _provider.store(_key, _testData);
        assertEquals (_testData, _provider.read (_key, TestData.class));
    }

    @Test
    @SmallTest
    public void testReadMissingFile() throws Exception {
        //
        assertNull (_provider.read(_key, TestData.class));
    }

    @Test
    @SmallTest
    public void testReadAndStoreEncryptedFailure() throws Exception {
        //
        _provider = new FileBasedKeyValueStore(_context, new EncryptionContext(), null);
        _provider.store(_key, _testData);

        _provider = new FileBasedKeyValueStore(_context, new EncryptionContext(), null);
        try {
            _provider.read(_key, TestData.class);
            fail ("Should have thrown exception");
        }
        catch (final BadPaddingException e) {
            // Ignored intentionally: Expected exception.
        }
    }

    @Test
    @SmallTest
    public void testReadAndStoreEncryptedSignedSignatureFailure() throws Exception {
        //
        final EncryptionContext encryptionContext = new EncryptionContext();
        _provider = new FileBasedKeyValueStore(_context, encryptionContext, new SigningContext());
        _provider.store(_key, _testData);

        _provider = new FileBasedKeyValueStore(_context, encryptionContext, new SigningContext());

        try {
            _provider.read(_key, TestData.class);
            fail ("Should have thrown exception");
        }
        catch (final SignatureException e) {
            // Ignored intentionally: Expected exception.
        }
    }


    @Test
    @SmallTest
    public void testReadAndStoreEncryptedSignedDecryptionFailure() throws Exception {
        //
        final SigningContext signingContext = new SigningContext();
        _provider = new FileBasedKeyValueStore(_context, new EncryptionContext (), signingContext);
        _provider.store(_key, _testData);

        _provider = new FileBasedKeyValueStore(_context, new EncryptionContext(), signingContext);

        try {
            _provider.read(_key, TestData.class);
            fail ("Should have thrown exception");
        }
        catch (final BadPaddingException e) {
            // Ignored intentionally: Expected exception.
        }
    }


    private <T extends Throwable> void verifyException (final Class<?> expectedClass, final T actual) {
        //
        assertNotNull (
                "Expecting exception of \"" + expectedClass + "\", but was null",
                actual
        );
        assertTrue (
                "Expecting exception of \"" + expectedClass.getCanonicalName() + "\", but was \"" + actual.getClass().getCanonicalName() + "\"",
                actual.getClass ().isAssignableFrom(expectedClass)
        );
    }

    private static class TestData implements Serializable {
        private String _stringValue;
        private int _intValue;
        private Date _dateValue;

        public TestData(final Date _dateValue, final int _intValue, final String _stringValue) {
            this._dateValue = _dateValue;
            this._intValue = _intValue;
            this._stringValue = _stringValue;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            TestData testData = (TestData) o;

            if (_intValue != testData._intValue) return false;
            if (!_stringValue.equals(testData._stringValue)) return false;
            return _dateValue.equals(testData._dateValue);

        }

        @Override
        public int hashCode() {
            int result = _stringValue.hashCode();
            result = 31 * result + _intValue;
            result = 31 * result + _dateValue.hashCode();
            return result;
        }
    }
}
