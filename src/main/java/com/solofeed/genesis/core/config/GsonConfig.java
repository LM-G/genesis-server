package com.solofeed.genesis.core.config;

import com.google.gson.*;
import com.solofeed.genesis.core.exception.MarshallerError;
import net.jodah.typetools.TypeResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Created by LM-G on 08/12/2017.
 */
@Configuration
public class GsonConfig {

    /** Normal gson instance */
    private final Gson gson;

    /**
     * Instanciate the gson object mapper and register all genesis adapters in it
     * @param adapters instances of all type adapters
     */
    public GsonConfig(List<TypeAdapter> adapters){
        // Configuration de l'instance gson
        GsonBuilder builder = new GsonBuilder()
                // example : accessToken
                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                // example : { "(5,6)": "a" , ...
                .enableComplexMapKeySerialization();

        // register all types adapters in the gson builder
        for(TypeAdapter adapter : adapters) {
            Class<?> type = TypeResolver.resolveRawArguments(TypeAdapter.class, adapter.getClass())[0];
            builder.registerTypeAdapter(type, adapter);
        }

        this.gson = builder.create();
    }

    /**
     * Gson singleton object mapper
     *
     * @return gson object mapper
     */
    @Bean
    public Gson gson(){
        return gson;
    }
}
