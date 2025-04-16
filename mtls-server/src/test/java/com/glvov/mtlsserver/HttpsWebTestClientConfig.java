package com.glvov.mtlsserver;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.security.KeyStore;

/**
 * Configuration class for setting up a WebTestClient with HTTPS and mutual TLS (mTLS).
 * It configures the client to use SSL/TLS with custom key and trust stores.
 */

@TestConfiguration
@ConfigurationProperties(prefix = "web-test-client.ssl")
@Setter
public class HttpsWebTestClientConfig {

    private String baseUrl;
    private String storageType;
    private String keyStorePath;
    private String keyStorePassword;
    private String trustStorePath;
    private String trustStorePassword;

    /**
     * Creates and configures a WebTestClient instance for making HTTPS requests.
     * It sets up the client with an SSL context using the configured key and trust stores.
     */
    @Bean
    public WebTestClient webTestClient() throws Exception {
        SslContext sslContext = SslContextBuilder
                .forClient()
                .keyManager(initKeyManagerFactory())
                .trustManager(initTrustManagerFactory())
                .build();

        HttpClient httpClient = HttpClient.create()
                .secure(ssl -> ssl.sslContext(sslContext));

        return WebTestClient.bindToServer(new ReactorClientHttpConnector(httpClient))
                .baseUrl(baseUrl)
                .build();
    }

    private KeyManagerFactory initKeyManagerFactory() throws Exception {
        char[] password = keyStorePassword.toCharArray();

        KeyStore keyStore = KeyStore.getInstance(storageType);
        keyStore.load(new FileInputStream(keyStorePath), password);

        var keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, password);

        return keyManagerFactory;
    }


    private TrustManagerFactory initTrustManagerFactory() throws Exception {
        char[] password = trustStorePassword.toCharArray();

        KeyStore trustStore = KeyStore.getInstance(storageType);
        trustStore.load(new FileInputStream(trustStorePath), password);

        var trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(trustStore);

        return trustManagerFactory;
    }
}
