import javax.net.ssl.*;
import java.io.*;
import java.security.KeyStore;

public class SSLServer {
    public static void main(String[] args) throws Exception {
        int port = 1721;
        String keystorePath = "server_certs/keystore.jks";

        KeyStore keyStore = KeyStore.getInstance("JKS");
        FileInputStream keyStoreStream = new FileInputStream(keystorePath);
        keyStore.load(keyStoreStream, "changeit".toCharArray());

        KeyManagerFactory kmf = KeyManagerFactory.getInstance(
            TrustManagerFactory.getDefaultAlgorithm()
        );
        kmf.init(keyStore, "changeit".toCharArray());
        KeyManager[] keyManagers = kmf.getKeyManagers();

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagers, null, null);

        SSLServerSocketFactory serverSocketFactory = sslContext.getServerSocketFactory();
        SSLServerSocket serverSocket = (SSLServerSocket) serverSocketFactory.createServerSocket(port);

        System.out.println("Server listening on port " + port + "...");

        SSLSocket clientSocket = (SSLSocket) serverSocket.accept();
        System.out.println("Client connected");

        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

        String msg = in.readLine();
        System.out.println("Received: " + msg);
        out.println("Hi, SSL client!");
    }
}
