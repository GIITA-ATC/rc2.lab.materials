import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;

public class EchoUDPServer {
    public static void main(String args[]) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: java EchoUDPServer <port>");
            System.exit(1);
        }

        int serverPort = Integer.parseInt(args[0]);
        DatagramSocket server = new DatagramSocket(serverPort);
        DatagramPacket packet = new DatagramPacket(new byte[255], 255);

        System.out.println("Server running on port " + serverPort);

        while (true) {
            server.receive(packet);

            System.out.println("Packet received from "
                + packet.getAddress().getHostAddress()
                + ":" + packet.getPort());

            System.out.println("Echo: "
                + new String(packet.getData(), 0, packet.getLength(), "UTF-8"));

            server.send(packet);
        }
    }
}
