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
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * Handles conversion between a {@link LocalDateTime} and a representing {@link String}
 */
@Component
@ConfigurationPropertiesBinding
public class LocalDateTimeConverter extends TypeAdapter<LocalDateTime> implements Converter<String, LocalDateTime> {
    @Override
    public LocalDateTime convert(String source) {
        if (source == null) {
            return null;
        }
        return LocalDateTime.parse(source, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    @Override
    public void write(JsonWriter writer, LocalDateTime date) throws IOException {
        if (date == null) {
            writer.nullValue();
        } else {
            writer.value(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(date.atOffset(ZoneOffset.UTC)));
        }
    }

    @Override
    public LocalDateTime read(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return null;
        }
        String date = reader.nextString();
        return StringUtils.isNotBlank(date) ? LocalDateTime.parse(date, DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null;
    }
}
