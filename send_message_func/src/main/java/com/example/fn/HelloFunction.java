package com.example.fn;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
// import com.oracle.bmc.ConfigFileReader;
// import com.oracle.bmc.auth.AuthenticationDetailsProvider;
// import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
// import com.oracle.bmc.auth.InstancePrincipalsAuthenticationDetailsProvider;
import com.oracle.bmc.auth.ResourcePrincipalAuthenticationDetailsProvider;
import com.oracle.bmc.streaming.StreamClient;
import com.oracle.bmc.streaming.model.PutMessagesDetails;
import com.oracle.bmc.streaming.model.PutMessagesDetailsEntry;
// import com.oracle.bmc.streaming.model.PutMessagesResultEntry;
import com.oracle.bmc.streaming.requests.PutMessagesRequest;
// import com.oracle.bmc.streaming.responses.PutMessagesResponse;

// import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

public class HelloFunction {

    public String handleRequest(Input input) {
        // final String configurationFilePath = "~/.oci/config";
        // final String profile = "DEFAULT";
        final String ociStreamOcid = "ocid1.stream.oc1.phx.amaaaaaassl65iqa3scwbrnz6imvqwxmr6vzbv3xpvi46bwt5qzdlsqkcmzq";
        final String ociMessageEndpoint = "https://cell-1.streaming.us-phoenix-1.oci.oraclecloud.com";
        
        
        // final ConfigFileReader.ConfigFile configFile = ConfigFileReader.parseDefault();
        // final ConfigFileReader.ConfigFile configFile = ConfigFileReader.parse(configurationFilePath, profile);
        // final AuthenticationDetailsProvider provider = new ConfigFileAuthenticationDetailsProvider(configFile);
        System.out.println("Here");
        final ResourcePrincipalAuthenticationDetailsProvider provider = ResourcePrincipalAuthenticationDetailsProvider.builder().build();
        // final InstancePrincipalsAuthenticationDetailsProvider provider = InstancePrincipalsAuthenticationDetailsProvider.builder().build();
        System.out.println("Here2");
        // final Logger log = LoggerFactory.getLogger(HelloFunction.class);
        
        // Create a stream client
        StreamClient streamClient = StreamClient.builder().endpoint(ociMessageEndpoint).build(provider);
        
        
        publishExampleMessages(streamClient, ociStreamOcid, input);
        // log.info("end pub");
        return "{\"status\": \"ack\"}";
    
        
        
        // try {
        //     // final ConfigFileReader.ConfigFile configFile = ConfigFileReader.parseDefault();
        //     final ConfigFileReader.ConfigFile configFile = ConfigFileReader.parse(configurationFilePath, profile);
        //     final AuthenticationDetailsProvider provider = new ConfigFileAuthenticationDetailsProvider(configFile);
        //     // final InstancePrincipalsAuthenticationDetailsProvider provider = InstancePrincipalsAuthenticationDetailsProvider.builder().build();
        //     // Create a stream client
        //     StreamClient streamClient = StreamClient.builder().endpoint(ociMessageEndpoint).build(provider);

        //     publishExampleMessages(streamClient, ociStreamOcid, input);

        //     return "{\"status\": \"ack\"}";
        // } catch (IOException e) {
        //     e.printStackTrace();
        //     throw new RuntimeException(e);
        // }
    }

    private void publishExampleMessages(StreamClient streamClient, String streamId, Input input) {
        List<PutMessagesDetailsEntry> sendMessage = new ArrayList<>();
        // sendMessage.add(PutMessagesDetailsEntry.builder().value(Base64.getEncoder().encode(javaToJson(input).getBytes())).build());
        sendMessage.add(PutMessagesDetailsEntry.builder().value(javaToJson(input).getBytes()).build());
        PutMessagesDetails messagesDetails = PutMessagesDetails.builder().messages(sendMessage).build();
        PutMessagesRequest putRequest = PutMessagesRequest.builder().streamId(streamId)
                .putMessagesDetails(messagesDetails).build();

        // PutMessagesResponse putResponse = streamClient.putMessages(putRequest);
        streamClient.putMessages(putRequest);
    }

    private String javaToJson(Input input) {
        try {
            System.out.println("start java2json");
            return new ObjectMapper().writeValueAsString(input);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}