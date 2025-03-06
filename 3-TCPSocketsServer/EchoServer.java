import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;

public class EchoServer {
    public static void main(String args[]) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: java EchoServer <port>");
            System.exit(1);
        }

        int serverPort = Integer.parseInt(args[0]);
        ServerSocket server = new ServerSocket(serverPort);

        System.out.println("Server running on port " + serverPort);

        int msgSize;
        byte[] buffer = new byte[1024];

        while (true) {
            Socket client = server.accept();
            System.out.println("Client connected ("
                + client.getInetAddress().getHostAddress()
                + ":" + client.getPort() + ")");

            InputStream in = client.getInputStream();
            OutputStream out = client.getOutputStream();
            while ((msgSize = in.read(buffer)) != -1) {
                out.write(buffer, 0, msgSize);
            }

            client.close();
            System.out.println("Client disconnected");
        }
    }
}
