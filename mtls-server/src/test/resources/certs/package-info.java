/**
 * Provides client and server certificates for mutual TLS (mTLS) testing.
 * <p>
 * These certificates are stored in PKCS12 keystores and truststores to facilitate mutual authentication
 * between a {@link org.springframework.test.web.reactive.server.WebTestClient} and the mtls-server
 * itself for HTTPS with client authentication.
 * <p>
 * <b>Keystore Files:</b>
 * <ul>
 *   <li>{@code mtls-client-keystore.p12}: Contains the WebTestClient's private key and certificate.</li>
 *   <li>{@code mtls-client-truststore.p12}: Holds trusted server certificates for the WebTestClient.</li>
 *   <li>{@code mtls-server-keystore.p12}: Contains the mtls-server's private key and certificate.</li>
 *   <li>{@code mtls-server-truststore.p12}: Stores trusted client certificates for the mtls-server.</li>
 * </ul>
 * <p>
 * <b>Certificate Generation:</b>
 * Certificates are generated using the {@code create-keystores-truststores.sh} script,
 * located in the root project directory.
 * <p>
 * <b>References:</b>
 * <p>
 * README.md: Provides an overview of how certificate verification works in mTLS.</li>
 */
package certs;