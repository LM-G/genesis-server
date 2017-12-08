package com.solofeed.genesis.core.converter;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.solofeed.genesis.core.exception.MarshallerError;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Created by LM-G on 08/12/2017.
 */
@Component
@RequiredArgsConstructor
public class JSONConverter {

    /** gson object mapper defined in {@link com.solofeed.genesis.core.config.GsonConfig} */
    private final Gson gson;

    /**
     * Unmarshal an {@link InputStream} containing bytes reprensentating a json to the wanted DTO
     *
     * @param entityStream entity stream containing the json representation
     * @param targetClass DTO to create
     * @param <T> class of wanted DTO
     * @return DTO representation of a json object
     * @throws IOException if the input stream can't be used by {@link InputStreamReader}
     */
    public <T> T fromJson(InputStream entityStream, Class<T> targetClass) throws IOException {
        try (InputStreamReader reader = new InputStreamReader(entityStream, StandardCharsets.UTF_8)) {
            return gson.fromJson(reader, targetClass);
        } catch (JsonParseException e) {
            throw MarshallerError.ofUnmarshalling(e);
        }
    }

    /**
     * Marshal a DTO into a json object included in the provided {@link OutputStream}
     *
     * @param sourceObject DTO to marshal into json object
     * @param entityStream entity stream which will contain the json representation
     * @param <T> class of the DTO
     */
    public <T> void toJson(T sourceObject, OutputStream entityStream) {
        try (PrintWriter printWriter = new PrintWriter(entityStream)) {
            String json = gson.toJson(sourceObject);
            printWriter.write(json);
            printWriter.flush();
        } catch (JsonParseException e) {
            throw MarshallerError.ofMarshalling(e);
        }
    }
}
