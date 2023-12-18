package com.example.fn;

public class JsonMap {
    public String message;
    public Input value;

    @Override
    public String toString() {
        return "{" + message + ": " + value + "}";
    }

}
