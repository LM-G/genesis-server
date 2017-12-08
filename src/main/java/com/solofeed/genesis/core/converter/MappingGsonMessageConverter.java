package com.solofeed.genesis.core.converter;

import org.springframework.http.MediaType;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.converter.AbstractMessageConverter;
import org.springframework.messaging.converter.MessageConversionException;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * A Gson based {@link org.springframework.messaging.converter.MessageConverter} implementation
 */
@Component
public class MappingGsonMessageConverter extends AbstractMessageConverter {

    private final JSONConverter jsonConverter;

    private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    public MappingGsonMessageConverter(JSONConverter jsonConverter) {
        super(Arrays.asList(
                new MediaType("application", "json", DEFAULT_CHARSET),
                new MediaType("application", "*+json", DEFAULT_CHARSET)
        ));
        this.jsonConverter = jsonConverter;
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return true;
    }

    @Override
    public Object convertFromInternal(Message<?> message, Class<?> targetClass, Object conversionHint) {
        Object payload = message.getPayload();
        InputStream inputStream = new ByteArrayInputStream((byte[]) payload);
        try {
            return jsonConverter.fromJson(inputStream, targetClass);
        } catch (IOException e) {
            throw new MessageConversionException(message, "Could not read JSON: " + e.getMessage(), e);
        }
    }


    @Override
    public Object convertToInternal(Object payload, MessageHeaders headers, Object conversionHint) {
        ByteArrayOutputStream entityStream = new ByteArrayOutputStream(1024);
        this.jsonConverter.toJson(payload, entityStream);
        return entityStream.toByteArray();
    }
}
