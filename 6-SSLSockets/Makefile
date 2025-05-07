SERVER_CERT_DIR = server_certs
CLIENT_CERT_DIR = client_certs

KEYSTORE = $(SERVER_CERT_DIR)/keystore.jks
TRUSTSTORE = $(CLIENT_CERT_DIR)/truststore.jks
CERT_FILE = $(SERVER_CERT_DIR)/server-cert.pem

CERT_ALIAS = servercert
KEY_ALIAS = serverkey
STOREPASS = changeit
KEYPASS = changeit
DNAME = CN=localhost, OU=Test, O=Example, L=City, ST=Region, C=ES

all: $(KEYSTORE) $(CERT_FILE) $(TRUSTSTORE)

$(KEYSTORE):
	mkdir -p $(SERVER_CERT_DIR)
	keytool -genkeypair \
	 -alias $(KEY_ALIAS) \
	 -keyalg RSA \
	 -keystore $(KEYSTORE) \
	 -storepass $(STOREPASS) \
	 -dname "$(DNAME)"


$(CERT_FILE): $(KEYSTORE)
	keytool -exportcert \
	 -alias $(KEY_ALIAS) \
	 -keystore $(KEYSTORE) \
	 -storepass $(STOREPASS) \
	 -file $(CERT_FILE)

$(TRUSTSTORE): $(CERT_FILE)
	mkdir -p $(CLIENT_CERT_DIR)
	keytool -importcert \
	 -alias $(CERT_ALIAS) \
	 -file $(CERT_FILE) \
	 -keystore $(TRUSTSTORE) \
	 -storepass $(STOREPASS) \
	 -noprompt


run-server:
	javac SSLServer.java && java SSLServer

run-client:
	javac SSLClient.java && java SSLClient

clean:
	rm -rf $(SERVER_CERT_DIR) $(CLIENT_CERT_DIR) *.class
