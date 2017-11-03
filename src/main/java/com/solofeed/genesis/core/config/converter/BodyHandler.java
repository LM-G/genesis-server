package com.solofeed.genesis.core.config.converter;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.solofeed.genesis.core.exception.MarshallerException;
import org.springframework.context.annotation.Bean;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

/**
 * Marshal and Unmarshal handler between POJO and JSON using Gson
 * @param <T> generic type
 */
@Provider
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BodyHandler<T> implements MessageBodyReader<T>, MessageBodyWriter<T> {
    /** option pretty print dans les query params */
    private static final String PRETTY_PRINT = "pretty-print";
    /** Normal gson instance */
    private final Gson gson;
    /** gson instance with pretty print enabled */
    private final Gson prettyGson;

    @Context
    private UriInfo ui;

    public BodyHandler() {
        // Configuration de l'instance gson
        GsonBuilder builder = new GsonBuilder()
            // example : access_token
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            // example : { "(5,6)": "a" , ...
            .enableComplexMapKeySerialization();

        this.gson = builder.create();
        this.prettyGson = builder.setPrettyPrinting().create();
    }

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    @Override
    public T readFrom(Class<T> type, Type genericType, Annotation[] annotations,
                      MediaType mediaType, MultivaluedMap<String, String> httpHeaders,
                      InputStream entityStream) throws IOException {
        try (InputStreamReader reader = new InputStreamReader(entityStream, StandardCharsets.UTF_8)) {
            return gson.fromJson(reader, type);
        } catch (JsonParseException e) {
            throw MarshallerException.ofUnmarshalling(e);
        }
    }

    @Override
    public void writeTo(T t, Class<?> type, Type genericType, Annotation[] annotations,
                        MediaType mediaType, MultivaluedMap<String, Object> httpHeaders,
                        OutputStream entityStream) throws IOException {

        try (PrintWriter printWriter = new PrintWriter(entityStream)) {
            String json;
            // enables the pretty print mode if required by the request
            if (ui.getQueryParameters().containsKey(PRETTY_PRINT)) {
                json = prettyGson.toJson(t);
            } else {
                json = gson.toJson(t);
            }
            printWriter.write(json);
            printWriter.flush();
        } catch (JsonParseException e) {
            throw MarshallerException.ofMarshalling(e);
        }
    }

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    @Override
    public long getSize(T t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    @Bean
    public Gson gson(){
        return gson;
    }
}
