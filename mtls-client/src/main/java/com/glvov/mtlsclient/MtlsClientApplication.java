package com.glvov.mtlsclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class MtlsClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(MtlsClientApplication.class, args);
    }
}
