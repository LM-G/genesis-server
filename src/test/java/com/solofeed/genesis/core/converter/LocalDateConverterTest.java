package com.solofeed.genesis.core.converter;

import org.junit.Before;
import org.junit.Test;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Calendar;

import static org.junit.Assert.assertTrue;

/**
 * Test {@link LocalDateConverter}
 */
public class LocalDateConverterTest {
    private LocalDateConverter converter;

    @Before
    public void setUp() throws Exception {
        converter = new LocalDateConverter();
    }

    @Test
    public void shouldConvertToDatabaseColumn() throws Exception {
        // init
        LocalDate now = LocalDate.now();

        // excecution
        Date dateConverted = converter.convertToDatabaseColumn(now);

        // assertions
        assertTrue(dateConverted.equals(Date.valueOf(now)));
    }

    @Test
    public void shouldConvertToEntityAttribute() throws Exception {
        // init
        Date now = new Date(Calendar.getInstance().getTimeInMillis());

        // excecution
        LocalDate dateConverted = converter.convertToEntityAttribute(now);

        // assertions
        assertTrue(dateConverted.equals(LocalDate.now()));
    }

}