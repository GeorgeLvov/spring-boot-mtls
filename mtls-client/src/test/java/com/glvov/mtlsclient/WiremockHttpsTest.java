package com.glvov.mtlsclient;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.extension.RegisterExtension;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

/**
 * An abstract base class for configuring and using an HTTPS-enabled WireMock server with mutual TLS (mTLS) support
 * in tests. The WireMock server is set up to require also the client authentication.
 *
 * <p>The configuration ensures the server:
 * <ul>
 *   <li>Runs on HTTPS port 8443</li>
 *   <li>Mandates mutual TLS (client authentication)</li>
 *   <li>Uses PKCS12 keystores and truststores for server certificates and trusted client certificates</li>
 * </ul>
 *
 * <p>Test certificates are stored in the project's test resources directory:
 * <ul>
 *   <li>{@code STORAGE_TYPE}: Defines the keystore and truststore type (PKCS12).</li>
 *   <li>{@code KEY_STORE_PATH}: Path to the server's keystore file.</li>
 *   <li>{@code TRUST_STORE_PATH}: Path to the server's truststore file.</li>
 * </ul>
 */
public abstract class WiremockHttpsTest {

    private static final String STORAGE_TYPE = "PKCS12";
    private static final String KEY_STORE_PATH = "src/test/resources/certs/mtls-server-keystore.p12";
    private static final String TRUST_STORE_PATH = "src/test/resources/certs/mtls-server-truststore.p12";

    @RegisterExtension
    protected static final WireMockExtension wireMock = configureForHttps();


    private static WireMockExtension configureForHttps() {
        return WireMockExtension.newInstance()
                        .options(wireMockConfig()
                                .httpDisabled(true)
                                .httpsPort(8443)
                                .needClientAuth(true)
                                .keystoreType(STORAGE_TYPE)
                                .keystorePath(KEY_STORE_PATH)
                               // .keystorePassword("password") // "password" is default value, can be omitted
                                .trustStoreType(STORAGE_TYPE)
                                .trustStorePath(TRUST_STORE_PATH)
                               // .trustStorePassword("password") // "password" is default value, can be omitted
                                )
                        .build();
    }
}
