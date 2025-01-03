package com.example.connectMates.config;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.StreamWriteConstraints;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        // Create a custom JsonFactory
        JsonFactory jsonFactory = new JsonFactory();
        // Set the max nesting depth
        jsonFactory.setStreamWriteConstraints(StreamWriteConstraints.builder()
                .maxNestingDepth(2000)  // Set the max nesting depth
                .build());

        // Create an ObjectMapper with the custom JsonFactory
        ObjectMapper objectMapper = new ObjectMapper(jsonFactory);
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT); // Optional: for pretty printing
        return objectMapper;
    }
}
