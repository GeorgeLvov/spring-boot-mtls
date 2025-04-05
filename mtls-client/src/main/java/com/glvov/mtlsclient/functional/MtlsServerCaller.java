package com.glvov.mtlsclient.functional;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@Log4j2
@RequiredArgsConstructor
public class MtlsServerCaller {

    private final WebClient webClient;

    @Value("${mtls-server.url}")
    private String url;


    public String call() {
        String result;

        try {
            var response = webClient
                    .get()
                    .uri(url)
                    .retrieve()
                    .toEntity(String.class)
                    .block();

            log.info("Response: " + response);

            String responseInfo = response != null ?
                    String.format(" Response: %s, %s", response.getStatusCode(), response.getBody())
                    : "";

            result = "Mtls-server ping successful!" + responseInfo;

        } catch (Exception e) {
            log.error(e, e);

            result = "Failed to ping mtls-server! Reason: " + e.getMessage();
        }

        return result;
    }
}
