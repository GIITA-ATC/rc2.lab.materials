import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ChangerClient {
    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.err.println("Usage: java ChangerClient <host> <port>");
            System.exit(1);
        }

        String host = args[0];
        int port = Integer.parseInt(args[1]);

        try (Socket socket = new Socket(host, port);
             OutputStream out = socket.getOutputStream();
             InputStream in = socket.getInputStream();
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Enter lines of text to send to the server. Type 'END' to exit.");

            while (true) {
                System.out.print("> ");
                String userInput = scanner.nextLine();

                if ("END".equalsIgnoreCase(userInput)) {
                    System.out.println("Exiting client...");
                    break;
                }

                byte[] dataToSend = userInput.getBytes("UTF-8");
                int dataLength = dataToSend.length;

                out.write(dataToSend);
                out.flush();

                byte[] buffer = new byte[dataLength];
                int totalBytesReceived = 0;
                int bytesRead;

                while (totalBytesReceived < dataLength) {
                    bytesRead = in.read(buffer, totalBytesReceived, dataLength - totalBytesReceived);
                    if (bytesRead == -1) {
                        throw new SocketException("Connection closed prematurely");
                    }
                    totalBytesReceived += bytesRead;
                }

                System.out.println("Received: " + new String(buffer, "UTF-8"));
            }

        } catch (UnknownHostException e) {
            System.err.println("Unknown Host: " + host);
        }
    }
}
