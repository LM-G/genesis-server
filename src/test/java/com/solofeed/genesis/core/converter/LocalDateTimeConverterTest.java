package com.solofeed.genesis.core.converter;

import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Calendar;

import static org.junit.Assert.assertTrue;

/**
 * Test {@link LocalDateTimeConverter}
 */
public class LocalDateTimeConverterTest {
    private LocalDateTimeConverter converter;

    @Before
    public void setUp() throws Exception {
        converter = new LocalDateTimeConverter();
    }

    @Test
    public void shouldConvertToDatabaseColumn() throws Exception {
        // init
        LocalDateTime now = LocalDateTime.now();

        // excecution
        Timestamp dateConverted = converter.convertToDatabaseColumn(now);

        // assertions
        assertTrue(dateConverted.equals(Timestamp.valueOf(now)));
    }

    @Test
    public void shouldConvertToEntityAttribute() throws Exception {
        // init
        Timestamp now = new Timestamp(Calendar.getInstance().getTimeInMillis());

        // excecution
        LocalDateTime dateConverted = converter.convertToEntityAttribute(now);

        // assertions
        assertTrue(dateConverted.equals(LocalDateTime.now()));
    }

}