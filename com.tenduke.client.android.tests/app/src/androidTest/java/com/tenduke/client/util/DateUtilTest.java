package com.tenduke.client.util;

import org.junit.Test;

import java.text.DateFormat;
import java.util.Date;

import static junit.framework.Assert.assertEquals;

/** Test replicated here. Not all date-format flags are supported in 4.x Androids!.
 *
 */

public class DateUtilTest {

    private static final Date Y2K = new Date (946684800000L);
    private static final String Y2K_EET = "2000-01-01T02:00:00.000+0200";
    private static final String Y2K_UTC = "2000-01-01T00:00:00.000Z";

    @Test
    public void testDateUtil() throws Exception {
        //
        final DateFormat formatter = DateUtil.createDefaultDateFormatter();

        assertEquals (Y2K, formatter.parse (Y2K_UTC));
        assertEquals (Y2K, formatter.parse (Y2K_EET));
        assertEquals (Y2K_UTC, formatter.format (Y2K));
    }
}
