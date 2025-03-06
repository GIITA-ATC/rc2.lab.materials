import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;

public class EchoServerThread {
    public static void main(String args[]) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: java EchoServerThread <port>");
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

            new EchoServerThread.ClientHandler(client).start();
        }
    }

	private static class ClientHandler extends Thread {
		private final Socket client;

		public ClientHandler(Socket client) {
			this.client = client;
		}

		@Override
		public void run() {
			try {
				InputStream in = client.getInputStream();
				OutputStream out = client.getOutputStream();

				byte[] buffer = new byte[1024];
				int msgSize;

				while ((msgSize = in.read(buffer)) != -1) {
					out.write(buffer, 0, msgSize);
				}

				System.out.println("Client finished ("
					+ client.getInetAddress().getHostAddress()
					+ ":" + client.getPort() + ")");

				client.close();

			} catch (IOException e) {
				System.err.println("Error with client: " + e.getMessage());
			}
		}
	}
}
