package com.solofeed.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Optional;

@Component
public class JsonPatcher {
    @Inject
    private ObjectMapper mapper;

    public <T> Optional patch(String json, @NotNull @Valid T target) {
        JsonNode patchedNode = null;
        try {
            final JsonPatch patch = mapper.readValue(json, JsonPatch.class);
            patchedNode = patch.apply(mapper.convertValue(target, JsonNode.class));
        } catch (IOException | JsonPatchException e) {
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(mapper.convertValue(patchedNode, target.getClass()));
    }
}
