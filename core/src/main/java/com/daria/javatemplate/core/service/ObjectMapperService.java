package com.daria.javatemplate.core.service;

import com.daria.javatemplate.core.common.exception.ApplicationErrorException;
import com.daria.javatemplate.core.common.exception.ApplicationErrorType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ObjectMapperService {

    private final ObjectMapper objectMapper;

    public <T> T getFromJson(String json) throws ApplicationErrorException {
        try {
            return objectMapper.readValue(json, new TypeReference<T>() {
            });
        } catch (IOException e) {
            throw new ApplicationErrorException(ApplicationErrorType.JSON_PARSING_ERROR);
        }
    }

    public <T> T getFromJson(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ApplicationErrorException(ApplicationErrorType.JSON_PARSING_ERROR);
        }
    }

    public <T> List<T> getListFromJson(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<List<T>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
            throw new ApplicationErrorException(ApplicationErrorType.JSON_PARSING_ERROR);
        }
    }

    public <T> List<T> getListFromJson(String json, Class<T> clazz) {
        CollectionType collectionType = TypeFactory.defaultInstance().constructCollectionType(List.class, clazz);
        try {
            return objectMapper.readValue(json, collectionType);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ApplicationErrorException(ApplicationErrorType.JSON_PARSING_ERROR);
        }
    }

    public String toJsonString(Object object) {
        try {
            objectMapper.disable(SerializationFeature.INDENT_OUTPUT);
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new ApplicationErrorException(ApplicationErrorType.JSON_PARSING_ERROR);
        }
    }

    public <T> T convertValue(Object object, Class<T> clazz) {
        return objectMapper.convertValue(object, clazz);
    }
}
