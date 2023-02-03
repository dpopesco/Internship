package org.example.exceptions;

public class ConversionJsonToModelException extends RuntimeException {

    public <T> ConversionJsonToModelException(Class<T> gClass, Exception exception) {
        super(String.format("Unable to parse from Json to Model " + gClass + " error: " + exception.getMessage()));
    }
}
