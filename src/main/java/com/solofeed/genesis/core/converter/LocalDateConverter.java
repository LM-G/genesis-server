package com.solofeed.genesis.core.converter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Handles conversion between a {@link LocalDate} and a representing {@link String}
 */
@Component
@ConfigurationPropertiesBinding
public class LocalDateConverter extends TypeAdapter<LocalDate> implements Converter<String, LocalDate> {
    @Override
    public LocalDate convert(String source) {
        if (source == null) {
            return null;
        }
        return LocalDate.parse(source, DateTimeFormatter.ISO_LOCAL_DATE);
    }

    @Override
    public void write(JsonWriter writer, LocalDate date) throws IOException {
        if (date == null) {
            writer.nullValue();
        } else {
            writer.value(DateTimeFormatter.ISO_LOCAL_DATE.format(date));
        }

    }

    @Override
    public LocalDate read(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return null;
        }
        String date = reader.nextString();
        return StringUtils.isNotBlank(date) ? LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE) : null;
    }
}
