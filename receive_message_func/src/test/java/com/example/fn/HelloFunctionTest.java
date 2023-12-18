package com.example.fn;

import com.fnproject.fn.testing.*;
import org.junit.*;

import static org.junit.Assert.*;

public class HelloFunctionTest {

    @Rule
    public final FnTestingRule testing = FnTestingRule.createDefault();

    @Test
    public void shouldReturnGreeting() {
        testing.givenEvent().withBody("[{\"stream\": \"mynewstream\", \"partition\": \"0\", \"key\": null, \"value\":\"eyJuYW1lIjoiaWdvciIsICJsb2NhdGlvbiI6ICJEdWJsaW4ifQ==\", \"offset\": 0, \"timestamp\": \"2018-04-26T01:03:06.051Z\"}]").enqueue();
        testing.thenRun(HelloFunction.class, "handleRequest");

        FnResult result = testing.getOnlyResult();
        assertEquals("OK", result.getBodyAsString());
    }

}
