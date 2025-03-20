import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.Scanner;

public class EchoUDPClient {
    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.err.println("Usage: java EchoUDPClient <host> <port>");
            System.exit(1);
        }

        String host = args[0];
        int port = Integer.parseInt(args[1]);

        Scanner scanner = new Scanner(System.in);
        DatagramSocket socket = new DatagramSocket();
        socket.setSoTimeout(3000);

        System.out.println("Enter lines of text to send to the server. Type 'END' to exit.");

        while (true) {
            System.out.print("> ");
            String userInput = scanner.nextLine();

            if ("END".equalsIgnoreCase(userInput)) {
                System.out.println("Exiting client...");
                break;
            }

            byte[] dataToSend = userInput.getBytes("UTF-8");
            DatagramPacket out_packet = new DatagramPacket(
                dataToSend,
                dataToSend.length,
                InetAddress.getByName(host), port
            );
            DatagramPacket in_packet = new DatagramPacket(
                new byte[dataToSend.length],
                dataToSend.length
            );

            Boolean reply_ok = false;
            for (int i = 0; i < 5; i++) {
                socket.send(out_packet);
                try {
                    socket.receive(in_packet);
                    System.out.println("Received: "
                        + new String(in_packet.getData(), 0, in_packet.getLength(), "UTF-8"));
                    reply_ok = true;
                    break;
                } catch (SocketTimeoutException e) {
                    System.err.println("Timeout: no response from server");
                }
            }

            if (!reply_ok) {
                System.err.println("No response from server after 5 attempts. Exiting client...");
                break;
            }
        }
        socket.close();
    }
}
