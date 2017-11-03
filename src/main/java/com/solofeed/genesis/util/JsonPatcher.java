package com.solofeed.genesis.util;

import com.google.gson.Gson;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Component
public class JsonPatcher {
    @Inject
    private Gson gson;

    public <T> Optional patch(String json, @NotNull @Valid T target) {
        /*
        JsonNode patchedNode = null;
        try {
            final JsonPatch patch = gson.fromJson(json, JsonPatch.class);
            patchedNode = patch.apply(gson.toJsonTree(target, JsonNode.class).);
        } catch (IOException | JsonPatchException e) {
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(mapper.convertValue(patchedNode, target.getClass()));
        */
        return Optional.empty();
    }
}
