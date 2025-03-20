import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Scanner;

public class EchoUDPBasicClient {
    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.err.println("Usage: java EchoUDPClient <host> <port>");
            System.exit(1);
        }

        String host = args[0];
        int port = Integer.parseInt(args[1]);

        Scanner scanner = new Scanner(System.in);
        DatagramSocket socket = new DatagramSocket();
        DatagramPacket in_packet = new DatagramPacket(new byte[255], 255);

        System.out.println("Enter lines of text to send to the server. Type 'END' to exit.");

        while (true) {
            System.out.print("> ");
            String userInput = scanner.nextLine();

            DatagramPacket out_packet = new DatagramPacket(
                userInput.getBytes("UTF-8"),
                userInput.length(),
                InetAddress.getByName(host), port
            );

            if ("END".equalsIgnoreCase(userInput)) {
                System.out.println("Exiting client...");
                break;
            }

            socket.send(out_packet);
            System.out.println("Sent: " + userInput);

            socket.receive(in_packet);
            System.out.println("Received: "
                + new String(in_packet.getData(), 0, in_packet.getLength(), "UTF-8"));

            socket.close();
        }
    }
}
