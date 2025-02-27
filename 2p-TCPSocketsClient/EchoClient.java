import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class EchoClient {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java EchoClient <host> <text>");
            return;
        }

        String host = args[0];
        String text = args[1];

        try (Socket socket = new Socket(host, 7);
             OutputStream out = socket.getOutputStream();
             InputStream in = socket.getInputStream()) {

            byte[] dataToSend = text.getBytes("UTF-8");
            int dataLength = dataToSend.length;

            out.write(dataToSend);
            out.flush();
            System.out.println("Sent: " + text);

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

        } catch (UnknownHostException e) {
            System.err.println("Unknown Host: " + host);
        } catch (IOException e) {
            System.err.println("I/O Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
