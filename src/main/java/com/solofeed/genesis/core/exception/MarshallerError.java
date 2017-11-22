package com.solofeed.genesis.core.exception;

import com.google.gson.JsonParseException;
import com.solofeed.genesis.core.exception.model.TechnicalException;
import lombok.experimental.UtilityClass;

/**
 * Serialization exception builder
 */
@UtilityClass
public final class MarshallerError {
    /** Marshalling error */
    public static final String E_COULDNOT_MARSHAL = "E_COULDNOT_MARSHAL";
    /** Unmarshalling error */
    public static final String E_COULDNOT_UNMARSHAL = "E_COULDNOT_UNMARSHAL";

    /**
     * When a DTO couldn't be marshalled into JSON
     * @param e initial exception
     * @return technical exception of marshalling error with http status 500
     */
    public static TechnicalException ofMarshalling(JsonParseException e){
        return new TechnicalException(E_COULDNOT_MARSHAL, "Erreur lors de la sérialisation", e);
    }

    /**
     * When a JSON couldn't be unmarshalled into DTO
     * @param e initial exception
     * @return technical exception of unmarshalling error with http status 500
     */
    public static TechnicalException ofUnmarshalling(JsonParseException e){
        return new TechnicalException(E_COULDNOT_UNMARSHAL, "Erreur lors de la désérialisation", e);
    }
}
