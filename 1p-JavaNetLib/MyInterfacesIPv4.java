import java.net.*;
import java.util.Enumeration;

public class MyInterfacesIPv4 {

	public static void main(String[] args) {
		try {
            Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces();
			System.out.println("Network interfaces list:");

			while (ifaces.hasMoreElements()) {
				NetworkInterface iface = ifaces.nextElement();

				if(iface.isUp()) {
					System.out.println("    " + iface.getDisplayName());
                    Enumeration<InetAddress> iface_addresses = iface.getInetAddresses();

					while (iface_addresses.hasMoreElements()) {
						InetAddress address = iface_addresses.nextElement();
						if (address instanceof Inet4Address) {
							System.out.println("        " + address.getHostAddress());
						}
					}
				}
			}
		} catch (SocketException e) {
			System.out.println("Error: " + e.getMessage());
		}

	}
}
