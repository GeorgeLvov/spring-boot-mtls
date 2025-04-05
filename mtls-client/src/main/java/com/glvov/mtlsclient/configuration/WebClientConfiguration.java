package com.glvov.mtlsclient.configuration;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.security.KeyStore;

@Configuration
@Log4j2
@RequiredArgsConstructor
public class WebClientConfiguration {

    private final WebClientSslProperties sslProperties;


    @Bean
    @SneakyThrows
    public WebClient webClient() {
        KeyManagerFactory keyManagerFactory = initKeyManagerFactory();
        TrustManagerFactory trustManagerFactory = initTrustManagerFactory();

        SslContext sslContext = SslContextBuilder.forClient()
                .keyManager(keyManagerFactory)
                .trustManager(trustManagerFactory)
                .build();

        HttpClient httpClient = HttpClient.create()
                .secure(spec -> spec.sslContext(sslContext));

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .filter(logRequest())
                .build();
    }

    @SneakyThrows
    private KeyManagerFactory initKeyManagerFactory() {
        char[] password = sslProperties.getKeyStorePassword().toCharArray();

        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(new FileInputStream(sslProperties.getKeyStorePath()), password);

        var keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, password);

        return keyManagerFactory;
    }

    @SneakyThrows
    private TrustManagerFactory initTrustManagerFactory() {
        char[] password = sslProperties.getTrustStorePassword().toCharArray();

        KeyStore trustStore = KeyStore.getInstance("PKCS12");
        trustStore.load(new FileInputStream(sslProperties.getTrustStorePath()), password);

        var trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(trustStore);

        return trustManagerFactory;
    }

    private static ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(request -> {
            log.info("Executing request: {} {}", request.method(), request.url());
            log.debug("Request headers: {}", request.headers());
            return Mono.just(request);
        });
    }
}
