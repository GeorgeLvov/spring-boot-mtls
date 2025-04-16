package com.glvov.mtlsserver;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("integration-test")
@Import(HttpsWebTestClientConfig.class)
class MtlsServerTests {

    private static final String MTLS_SERVER_PATH = "/ping-server";

    @Autowired
    private WebTestClient webTestClient;


    /**
     * Tests the server endpoint to ensure it is running and accessible via HTTPS.
     */
    @Test
    void testVerifyServerIsUpAndRunning() {
        webTestClient.get()
                .uri(MTLS_SERVER_PATH)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(String.class)
                .isEqualTo("Server is up and running!");
    }
}
