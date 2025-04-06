package com.gofit.assistantservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

/**
 * Service class for JSON processing operations.
 */
@Service
public class JsonService {

    /**
     * Converts the given object to its JSON string representation.
     *
     * @param object The object to convert
     * @return JSON string representation of the object
     */
    public String toJson(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }
}
