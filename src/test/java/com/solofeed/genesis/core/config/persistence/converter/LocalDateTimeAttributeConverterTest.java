package com.solofeed.genesis.core.config.persistence.converter;

import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Calendar;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test {@link LocalDateTimeAttributeConverter}
 */
public class LocalDateTimeAttributeConverterTest {
    private LocalDateTimeAttributeConverter converter;

    @Before
    public void setUp() throws Exception {
        converter = new LocalDateTimeAttributeConverter();
    }

    @Test
    public void shouldConvertToDatabaseColumn() throws Exception {
        // init
        LocalDateTime now = LocalDateTime.now();

        // excecution
        Timestamp dateConverted = converter.convertToDatabaseColumn(now);

        // assertions
        assertThat(dateConverted).isEqualTo(Timestamp.valueOf(now));
    }

    @Test
    public void shouldConvertToEntityAttribute() throws Exception {
        // init
        Timestamp now = new Timestamp(Calendar.getInstance().getTimeInMillis());
        LocalDateTime nowLocalDate = now.toLocalDateTime();

        // excecution
        LocalDateTime dateConverted = converter.convertToEntityAttribute(now);

        // assertions
        assertThat(dateConverted).isEqualTo(nowLocalDate);
    }

}