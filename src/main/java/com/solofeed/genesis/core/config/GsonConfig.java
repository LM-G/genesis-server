package com.solofeed.genesis.core.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import net.jodah.typetools.TypeResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Created by LM-G on 08/12/2017.
 */
@Configuration
public class GsonConfig {

    /**
     * Normal gson instance
     */
    private final Gson gson;

    /**
     * Instanciate the gson object mapper and register all genesis adapters in it
     *
     * @param adapters instances of all type adapters
     */
    public GsonConfig(List<TypeAdapter> adapters) {
        // Configuration de l'instance gson
        GsonBuilder builder = new GsonBuilder()
                // example : { "(5,6)": "a" , ...
                .enableComplexMapKeySerialization();

        // register all types adapters in the gson builder
        for (TypeAdapter adapter : adapters) {
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
    public Gson gson() {
        return gson;
    }
}
