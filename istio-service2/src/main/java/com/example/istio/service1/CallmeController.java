package com.example.istio.service1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
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
        LOGGER.info("Ping: name={}, version={}", buildProperties.getName(), buildProperties.getVersion());

        String srv3Endpoint = "http://istio-service3:8080/srv3/callme/ping";
        LOGGER.info("Calling Service 3: {}", srv3Endpoint);
        String response;
        try {
            response = restTemplate.getForObject(srv3Endpoint, String.class);
        } catch (RestClientException e) {
            response = e.getMessage();
        }
        return String.format("[%s] I'm %s %s\n\nSRV3-Response: %s", LocalDateTime.now(), buildProperties.getName(), buildProperties.getVersion(), response);
    }
}
