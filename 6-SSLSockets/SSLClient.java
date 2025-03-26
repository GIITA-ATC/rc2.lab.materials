import javax.net.ssl.*;
import java.io.*;
import java.security.KeyStore;

public class SSLClient {
    public static void main(String[] args) throws Exception {
        String host = "localhost";
        int port = 1721;
        String truststorePath = "client_certs/truststore.jks";

        KeyStore trustStore = KeyStore.getInstance("JKS");
        FileInputStream trustStream = new FileInputStream(truststorePath);
        trustStore.load(trustStream, "changeit".toCharArray());

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(
            TrustManagerFactory.getDefaultAlgorithm()
        );
        tmf.init(trustStore);
        TrustManager[] trustManagers = tmf.getTrustManagers();

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustManagers, null);

        SSLSocketFactory socketFactory = sslContext.getSocketFactory();
        SSLSocket socket = (SSLSocket) socketFactory.createSocket(host, port);

        socket.startHandshake();

        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        out.println("Hi, server!");
        String response = in.readLine();
        System.out.println("Server response: " + response);

    }
}
