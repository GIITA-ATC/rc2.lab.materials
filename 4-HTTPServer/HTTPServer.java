import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.Socket;
import java.net.ServerSocket;


public class HTTPServer {
    private static String wwwroot;
    private static String okHeader = "HTTP/1.1 200 OK\n\n";
    private static String notFoundHeader = "HTTP/1.1 404 Not Found\n\n";
    private static String methodNotAllowedHeader = "HTTP/1.1 405 Method Not Allowed\n\n";
    private static String badRequestHeader = "HTTP/1.1 400 Bad Request\n\n";

    public static void main(String args[]) throws IOException {
        if (args.length != 2) {
            System.err.println("Usage: java HTTPServer <port> <web_root>");
            System.exit(1);
        }

        int serverPort = Integer.parseInt(args[0]);
        ServerSocket server = new ServerSocket(serverPort, 20);
        wwwroot = args[1];

        System.out.println("Server running on port " + serverPort);

        while (true) {
            Socket client = server.accept();
            System.out.println("Client connected ("
                + client.getInetAddress().getHostAddress()
                + ":" + client.getPort() + ")");

            new HTTPServer.ClientHandler(client).start();
        }
    }

    private static class ClientHandler extends Thread {
        private final Socket client;
        private byte[] buffer = new byte[1024];

        public ClientHandler(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {
            String url = "";

            try {
                InputStream in = client.getInputStream();
                OutputStream out = client.getOutputStream();
                String request = "";
                int bytesRead;

                while (!request.endsWith("\r\n\r\n")) {
                    bytesRead = in.read(buffer);
                    request += new String(buffer, 0, bytesRead);
                }

                String[] lines = request.split("\n");

                if (lines.length > 0) {
                    String[] tokens = lines[0].split(" ");
                    System.out.println("Request: " + lines[0]);

                    if (tokens[0].equals("GET")) {
                        url = tokens[1];
                        serveFile(out, url);
                    } else {
                        out.write(methodNotAllowedHeader.getBytes());
                        System.out.print("Response: " + methodNotAllowedHeader);
                    }

                } else {
                    out.write(badRequestHeader.getBytes());
                    System.out.print("Response: " + badRequestHeader);
                }

                out.close();
                in.close();
                client.close();

            } catch (IOException e) {
                System.err.println("ERROR with client: " + e.getMessage());
            }
        }

        private void serveFile(OutputStream out, String url) throws IOException {
            if (url.equals("/")) {
                url = HTTPServer.wwwroot + "/index.html";
            } else {
                url = HTTPServer.wwwroot + url;
            }

            try {
                FileInputStream file = new FileInputStream(url);
                int bytesRead;

                out.write(okHeader.getBytes());
                System.out.print("Response: " + okHeader);

                while ((bytesRead = file.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                    out.flush();
                }

                file.close();
            } catch (FileNotFoundException e) {
                out.write(notFoundHeader.getBytes());
                System.out.print("Response: " + notFoundHeader);
            }
        }
    }
}
