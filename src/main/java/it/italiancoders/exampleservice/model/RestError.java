package it.italiancoders.exampleservice.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RestError {
    public String message;
}
