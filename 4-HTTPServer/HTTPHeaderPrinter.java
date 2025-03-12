import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;

public class HTTPHeaderPrinter {
    public static void main(String args[]) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: java HTTPHeaderPrinter <port>");
            System.exit(1);
        }

        int serverPort = Integer.parseInt(args[0]);
        ServerSocket server = new ServerSocket(serverPort, 20);

        System.out.println("Server running on port " + serverPort);

        while (true) {
            Socket client = server.accept();
            System.out.println("Client connected ("
                + client.getInetAddress().getHostAddress()
                + ":" + client.getPort() + ")");

            InputStream in = client.getInputStream();
            OutputStream out = client.getOutputStream();
            byte[] buffer = new byte[1024];
            String request = "";
            int bytesRead;

            while (!request.endsWith("\r\n\r\n")) {
                bytesRead = in.read(buffer);
                request += new String(buffer, 0, bytesRead);
            }

            System.out.println("Request: " + request);

            out.write("HTTP/1.1 200 OK\r\n".getBytes());
            out.write("Content-Type: text/plain\r\n".getBytes());
            out.write("\r\n".getBytes());
            out.write("Hello, World!".getBytes());

            client.close();
        }
    }
}
