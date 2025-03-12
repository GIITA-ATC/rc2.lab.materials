import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class SimpleFileReader {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java SimpleFileReader <file_name>");
            System.exit(1);
        }

        String fileName = args[0];

        try (FileInputStream fileInput = new FileInputStream(fileName)) {
            int byteData;
            while ((byteData = fileInput.read()) != -1) {
                System.out.print((char) byteData);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
}
