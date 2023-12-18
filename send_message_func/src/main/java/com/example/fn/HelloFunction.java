package com.example.fn;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fnproject.fn.api.FnConfiguration;
import com.fnproject.fn.api.RuntimeContext;
import com.oracle.bmc.auth.ResourcePrincipalAuthenticationDetailsProvider;
import com.oracle.bmc.streaming.StreamClient;
import com.oracle.bmc.streaming.model.PutMessagesDetails;
import com.oracle.bmc.streaming.model.PutMessagesDetailsEntry;
import com.oracle.bmc.streaming.requests.PutMessagesRequest;
import com.oracle.bmc.streaming.responses.PutMessagesResponse;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloFunction {

    private static final Logger LOG = LoggerFactory.getLogger(HelloFunction.class);

    private String ociStreamOcid;
    private String ociMessageEndpoint;
    private StreamClient streamClient;

    @FnConfiguration
    public void config(RuntimeContext ctx) {
        final ResourcePrincipalAuthenticationDetailsProvider provider = ResourcePrincipalAuthenticationDetailsProvider
                .builder().build();
        ociStreamOcid = ctx.getConfigurationByKey("ociStreamOcid").orElse("");
        ociMessageEndpoint = ctx.getConfigurationByKey("ociMessageEndpoint").orElse("");
        streamClient = StreamClient.builder().endpoint(ociMessageEndpoint).build(provider);
    }

    public Response handleRequest(Input input) {
        Response response = publishMessage(input);
        return response;
    }

    private Response publishMessage(Input input) {
        List<PutMessagesDetailsEntry> sendMessage = new ArrayList<>();
        sendMessage.add(PutMessagesDetailsEntry.builder().value(javaToJson(input).getBytes()).build());
        PutMessagesDetails messageDetail = PutMessagesDetails.builder().messages(sendMessage).build();
        PutMessagesRequest putRequest = PutMessagesRequest.builder().streamId(ociStreamOcid)
                .putMessagesDetails(messageDetail).build();

        PutMessagesResponse putResponse = streamClient.putMessages(putRequest);
        if (putResponse == null) {
            throw new RuntimeException();
        } else {
            return new Response("ack");
        }

    }

    private String javaToJson(Input input) {
        try {
            LOG.info("start java2json");
            return new ObjectMapper().writeValueAsString(input);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
