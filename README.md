# MTLS Client and Server with Spring Boot

This project implements MTLS (Mutual TLS) connection between 2 Spring Boot applications. It contains two main modules:

- mtls-client: The client module that calls the server over a secure MTLS connection

- mtls-server: The server module that responds to requests from the client with MTLS authentication

---

### How Certificate Verification Works in mTLS:

mTLS (mutual TLS) or two-way TLS is an extension of TLS (Transport Layer Security) where both the server and the client 
present their certificates to each other during the TLS handshake, and both validate the certificates of the other party.

How it works:

1. Server Certificate Verification:
   - During the handshake, the **client** receives the server's certificate.
   - The client checks the server's certificate against a trusted certificate authority (CA) in its **truststore**,
     ensuring that the server is legitimate and not impersonating someone else.
   - The client will also check if the certificate is valid (i.e., not expired) and that the certificate matches the expected domain or IP.

2. Client Certificate Verification:
   - Once the server's certificate is verified, the **server** requests a certificate from the client.
   - The server checks the client's certificate against a trusted certificate authority (CA) in its **truststore**, 
     ensuring that the client is authorized to access the server.
   - Similarly, the server checks if the certificate is valid and whether it matches the expected client identity.

**Note**:
- The server's certificate must be present in the client's truststore to ensure the client trusts the server.
- The client's certificate must be present in the server's truststore to ensure the server trusts the client.

---

## Requirements

Before getting started, ensure that you have the following installed:

- Java 21 or higher
- Gradle for building the project
- OpenSSL (for key and certificate generation)
- Unix-based OS (Linux, macOS, etc.): The `create-keystores-truststores.sh` script is intended to be executed on Unix-based systems

### Alternatives for Non-Unix Environments
If you do not have a Unix-based system or prefer not to run the script, there are a couple of alternatives:

 - Use the `certs-generated` branch:
   In the `certs-generated` branch of the project repository, the keystores and truststores are already pre-generated. 
   You can simply check out this branch and use the certificates that are already created and placed in the appropriate directories. This eliminates the need to run the script yourself.
 - Manually generate keystores and truststores:
   Use the provided `keytool` commands directly from the script.
   Once generated, manually place the keystores and truststores in the `resources/certs` directory for both the `mtls-client` and `mtls-server` modules.

---

## Setup

### Keystore and Truststore Setup

Before running the client or server application, you must execute the script `create-keystores-truststores.sh` 
to generate the required keystores and truststores for both the client and the server.

1. Navigate to the project root directory in terminal

2. Run the `create-keystores-truststores.sh` script, e.g.: `bash create-keystores-truststores.sh`


This script does the following:

- Generates a keystore for the server
- Generates a keystore for the client
- Creates a truststore that includes the server's certificate, so the client can trust it
- Creates a truststore that includes the client's certificate, so the server can trust it

After running the script, the keystores and truststores will be placed in the `resources/certs` directory of each application.

---

## How to Test

1. Start the Applications:
   - Make sure both the `mtls-client` and `mtls-server` applications are running and configured with the correct keystores and truststores.

2. Make a Test Request to `mtls-client`:

     ```
     http://localhost:8080/mtls-client
     ```

3. Expected Response:
   - The successful response should be:

     ```
     Mtls-server ping successful! Response: 200 OK, Server is up and running!
     ```

   This means the `mtls-client` successfully communicated with the `mtls-server`.

