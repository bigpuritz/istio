package com.example.istio.service1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/callme")
public class CallmeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CallmeController.class);

    private final BuildProperties buildProperties;

    @Autowired
    public CallmeController(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @GetMapping(value = "/ping", produces = MediaType.TEXT_PLAIN_VALUE)
    public String ping() {
        LOGGER.info("Ping: name={}, version={}", buildProperties.getName(), buildProperties.getVersion());
        return String.format("[%s] I'm %s %s", LocalDateTime.now(), buildProperties.getName(), buildProperties.getVersion());
    }
}
