package com.solofeed.genesis.core.exception;

import com.google.gson.JsonParseException;

public final class MarshallerException {
    private static final String E_COULDNOT_MARSHAL = "E_COULDNOT_MARSHAL";
    private static final String E_COULDNOT_UNMARSHAL = "E_COULDNOT_UNMARSHAL";


    private MarshallerException(){
        // no-op
    }

    public static TechnicalException ofMarshalling(JsonParseException e){
        return new TechnicalException(E_COULDNOT_MARSHAL, "Erreur lors de la sérialisation", e);
    }

    public static TechnicalException ofUnmarshalling(JsonParseException e){
        return new TechnicalException(E_COULDNOT_UNMARSHAL, "Erreur lors de la désérialisation", e);
    }
}
