package com.example.fn;

import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class HelloFunction {

    private final Logger log = LoggerFactory.getLogger(HelloFunction.class);

    public String handleRequest(List<Input> input) {
        
        for (Input putValue : input){
            log.info(new String(Base64.getDecoder().decode(putValue.value)));
        } 
        String decodeStr = new String(Base64.getDecoder().decode(input.get(0).value));
        return decodeStr;
    }

    
}