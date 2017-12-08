package com.solofeed.genesis.core.config.persistence.converter;

import org.junit.Before;
import org.junit.Test;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Calendar;

import static org.junit.Assert.assertTrue;

/**
 * Test {@link LocalDateAttributeConverter}
 */
public class LocalDateAttributeConverterTest {
    private LocalDateAttributeConverter converter;

    @Before
    public void setUp() throws Exception {
        converter = new LocalDateAttributeConverter();
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