package com.solofeed.genesis.core.exception.mapper;


import com.solofeed.genesis.core.exception.dto.ErrorPayload;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;

import javax.inject.Singleton;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@Singleton
abstract class AbstractExceptionMapper {
    /**
     * Construct an error response
     * @param status status of the response
     * @param payload payload of the response
     * @return error response
     */
    Response toErrorResponse(HttpStatus status, ErrorPayload payload) {
        return create(status, payload, null);
    }

    /**
     * Construct an error response
     * @param status status of the response
     * @param payload payload of the response
     * @param t cause of this error
     * @return error response
     */
    Response toErrorResponse(HttpStatus status, ErrorPayload payload, Throwable t) {
        return toErrorResponse(status, payload, t, new HashMap<>());
    }

    /**
     * Construct an error response
     * @param status status of the response
     * @param payload payload of the response
     * @param t cause of this error
     * @param headers possible headers
     * @return error response
     */
    Response toErrorResponse(HttpStatus status, ErrorPayload payload, Throwable t, Map<String, String> headers) {
        LOGGER.trace(t);
        return create(status, payload, headers);
    }

    /**
     * Returns a response error to client
     * @param status http status
     * @param payload error content
     * @param headers possible headers
     * @return http response
     */
    private Response create(HttpStatus status, ErrorPayload payload, Map<String, String> headers) {
        Response.ResponseBuilder responseBuilder = Response.status(status.value())
            .type(MediaType.APPLICATION_JSON)
            .entity(payload);
        if(!CollectionUtils.isEmpty(headers)){
            for(Map.Entry<String, String> header : headers.entrySet()){
                responseBuilder.header(header.getKey(), header.getValue());
            }
        }


        return responseBuilder.build();
    }
}
