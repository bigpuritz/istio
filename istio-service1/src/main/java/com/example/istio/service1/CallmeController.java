package com.example.istio.service1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/callme")
public class CallmeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CallmeController.class);

    private final BuildProperties buildProperties;

    private final RestTemplate restTemplate;

    @Autowired
    public CallmeController(BuildProperties buildProperties, RestTemplate restTemplate) {
        this.buildProperties = buildProperties;
        this.restTemplate = restTemplate;
    }

    @GetMapping(value = "/ping", produces = MediaType.TEXT_PLAIN_VALUE)
    public String ping() {
        LOGGER.info("Ping: name={}, version={}, time={}", buildProperties.getName(), buildProperties.getVersion(), buildProperties.getTime());

        String srv2Endpoint = "http://istio-service2:8080/srv2/callme/ping";
        LOGGER.info("Calling Service 2: {}", srv2Endpoint);

        String response;
        try {
            response = restTemplate.getForObject(srv2Endpoint, String.class);
        } catch (RestClientException e) {
            LOGGER.error("Calling Service 2 failed.. Returning error message instead.");
            response = e.getMessage();
        }

        return String.format("SRV1-Response: [%s] I'm %s %s\n\nSRV2-Response: %s", LocalDateTime.now(), buildProperties.getName(), buildProperties.getVersion(), response);
    }
}
