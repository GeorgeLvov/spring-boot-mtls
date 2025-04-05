#!/bin/bash

# Get the directory of this script
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

echo "SCRIPT_DIR: $SCRIPT_DIR"

# Build relative paths
CLIENT_CERTS_DIR="$SCRIPT_DIR/mtls-client/src/main/resources/certs"
SERVER_CERTS_DIR="$SCRIPT_DIR/mtls-server/src/main/resources/certs"

KEYSTORE_CLIENT="$CLIENT_CERTS_DIR/mtls-client-keystore.p12"
TRUSTSTORE_CLIENT="$CLIENT_CERTS_DIR/mtls-client-truststore.p12"

KEYSTORE_SERVER="$SERVER_CERTS_DIR/mtls-server-keystore.p12"
TRUSTSTORE_SERVER="$SERVER_CERTS_DIR/mtls-server-truststore.p12"

# Create certs directories if they don't exist
mkdir -p "$CLIENT_CERTS_DIR"
mkdir -p "$SERVER_CERTS_DIR"

# Generate keystores
keytool -genkeypair -v -keystore "$KEYSTORE_CLIENT" -storetype PKCS12 \
  -alias mtls-client -keyalg RSA -keysize 2048 -validity 365 \
  -storepass password -keypass password \
  -dname "CN=localhost, OU=dev, O=example, L=city, ST=state, C=US"

keytool -genkeypair -v -keystore "$KEYSTORE_SERVER" -storetype PKCS12 \
  -alias mtls-server -keyalg RSA -keysize 2048 -validity 365 \
  -storepass password -keypass password \
  -dname "CN=localhost, OU=dev, O=example, L=city, ST=state, C=US"

# Export public certificates
CERTIFICATE_CLIENT="$SCRIPT_DIR/certificate-client.crt"
CERTIFICATE_SERVER="$SCRIPT_DIR/certificate-server.crt"

keytool -exportcert -keystore "$KEYSTORE_CLIENT" -alias mtls-client \
  -file "$CERTIFICATE_CLIENT" -storepass password

keytool -exportcert -keystore "$KEYSTORE_SERVER" -alias mtls-server \
  -file "$CERTIFICATE_SERVER" -storepass password

# Import public certs into truststores
keytool -import -noprompt -alias mtls-server-cert -file "$CERTIFICATE_SERVER" \
  -keystore "$TRUSTSTORE_CLIENT" -storetype PKCS12 -storepass password

keytool -import -noprompt -alias mtls-client-cert -file "$CERTIFICATE_CLIENT" \
  -keystore "$TRUSTSTORE_SERVER" -storetype PKCS12 -storepass password

# Clean up temporary cert files
rm -f "$CERTIFICATE_CLIENT" "$CERTIFICATE_SERVER"
