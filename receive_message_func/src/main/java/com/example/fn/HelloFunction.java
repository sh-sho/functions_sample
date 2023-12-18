package com.example.fn;

import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class HelloFunction {

    private static final Logger LOG = LoggerFactory.getLogger(HelloFunction.class);

    public String handleRequest(List<Input> input) {
        input.forEach(s -> LOG.info(new String(Base64.getDecoder().decode(s.value))));
        return "OK";
    }

}
