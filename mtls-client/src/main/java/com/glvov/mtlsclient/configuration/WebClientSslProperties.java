package com.glvov.mtlsclient.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("client.ssl")
@Getter
@Setter
public class WebClientSslProperties {

    private String keyStorePath;
    private String keyStorePassword;
    private String trustStorePath;
    private String trustStorePassword;
}
