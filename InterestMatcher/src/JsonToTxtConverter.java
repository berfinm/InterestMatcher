import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

public class JsonToTxtConverter {

    public static void main(String[] args) {
        String jsonFilePath = "C:\\Users\\Hp\\OneDrive\\Masaüstü\\twitter_data.json";
        String txtFilePath = "C:\\Users\\Hp\\OneDrive\\Masaüstü\\keepcoding in java\\ProjeDeneme\\data.txt";

        try {
            convertJsonToTxt(jsonFilePath, txtFilePath);

            System.out.println("JSON icerigi " + txtFilePath + " dosyasina yazildi.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void convertJsonToTxt(String jsonFilePath, String txtFilePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(jsonFilePath));
             BufferedWriter writer = new BufferedWriter(new FileWriter(txtFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.newLine();
            }
        }
    }
}
