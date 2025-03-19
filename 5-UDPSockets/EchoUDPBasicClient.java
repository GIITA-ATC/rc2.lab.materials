import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;

public class EchoUDPBasicClient {
    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.err.println("Usage: java EchoUDPClient <text> <host> [<port>]");
            System.exit(1);
        }

        String text = args[0];
        String host = args[1];
        int port = args.length > 2 ? Integer.parseInt(args[2]) : 10000;

        DatagramSocket socket = new DatagramSocket();
        DatagramPacket in_packet = new DatagramPacket(new byte[255], 255);
        DatagramPacket out_packet = new DatagramPacket(
            text.getBytes("UTF-8"),
            text.length(),
            InetAddress.getByName(host), port
        );

        socket.send(out_packet);
        System.out.println("Sent: " + text);

        socket.receive(in_packet);
        System.out.println("Received: "
            + new String(in_packet.getData(), 0, in_packet.getLength(), "UTF-8"));

        socket.close();
    }
}
