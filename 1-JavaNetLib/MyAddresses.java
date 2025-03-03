import java.net.InetAddress;
import java.net.UnknownHostException;

public class MyAddresses {
	public static void main(String[] args) {
		try {
			InetAddress localhost = InetAddress.getLocalHost();
			String host_name = localhost.getHostName();
			String canonical_host_name = localhost.getCanonicalHostName();

			System.out.println("getHostName(): " + host_name);
            System.out.println("getCanonicalHostName(): " + canonical_host_name);

			InetAddress[] my_addresses = InetAddress.getAllByName(canonical_host_name);

			if (my_addresses != null && my_addresses.length >= 1) {
				System.out.println("\nMy IP list:");

                for (InetAddress my_address : my_addresses) {
                    System.out.println("    " + my_address);
                }
			} else {
				System.out.println("No IP addresses found.");
			}
		}
		catch(UnknownHostException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
}
