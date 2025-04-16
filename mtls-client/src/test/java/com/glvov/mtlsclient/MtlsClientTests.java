package com.glvov.mtlsclient;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;

/**
 * Integration tests for the mTLS client application using WireMock as a mock HTTPS server.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock
@ActiveProfiles("integration-test")
class MtlsClientTests extends WiremockHttpsTest {

    private static final int AWAIT_TIMEOUT_SECONDS = 5;

    private static final String MTLS_CLIENT_PATH = "/mtls-client";
    private static final String MTLS_SERVER_PATH = "/ping-server";

    @Autowired
    private WebTestClient webTestClient;


    /**
     * Tests the mTLS client's ability to make authenticated HTTPS (mTLS) requests to a mock server.
     * The mTLS server is mocked using WireMock.
     *
     * <p>This test performs the following steps:
     * <ol>
     *   <li>Configures WireMock to respond to GET requests on {@value #MTLS_SERVER_PATH}.</li>
     *   <li>Initiates a request to the client endpoint {@value #MTLS_CLIENT_PATH}, which triggers a call to the mock server.</li>
     *   <li>Verifies that:
     *     <ul>
     *       <li>The client successfully receives and processes the server response.</li>
     *       <li>The mock server receives the expected request.</li>
     *     </ul>
     *   </li>
     * </ol>
     */
    @Test
    void testWhenClientSendsHttpsRequestToServerThenResponseIsSuccessful() {
        wireMock.stubFor(get(urlEqualTo(MTLS_SERVER_PATH))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("Some server response")
                        .withHeader("Content-Type", "text/plain")));

        webTestClient.get()
                .uri(MTLS_CLIENT_PATH)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(String.class)
                .isEqualTo("Mtls-server ping successful! Response: 200 OK, Some server response");

        await().atMost(AWAIT_TIMEOUT_SECONDS, SECONDS)
                .untilAsserted(() ->
                        wireMock.verify(
                                getRequestedFor(urlPathEqualTo(MTLS_SERVER_PATH))
                        )
                );
    }
}
