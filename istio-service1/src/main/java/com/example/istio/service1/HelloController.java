package com.example.istio.service1;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
public class HelloController {

    private final DiscoveryClient discoveryClient;

    private final MyConfig myConfig;

    public HelloController(DiscoveryClient discoveryClient, MyConfig myConfig) {
        this.discoveryClient = discoveryClient;
        this.myConfig = myConfig;
    }

    @RequestMapping("/")
    public String hello() {
        return "Hello World";
    }

    @RequestMapping("/services")
    public List<Object> services() {

        try {
            final List<String> serviceIds = this.discoveryClient.getServices();
            return serviceIds.stream().map(this.discoveryClient::getInstances).filter(Objects::nonNull).collect(Collectors.toList());

        } catch (Exception e) {
            return List.of(ExceptionUtils.getFullStackTrace(e));
        }
    }


    @RequestMapping("/conf")
    public MyConfig config() {
        return myConfig;
    }

}
