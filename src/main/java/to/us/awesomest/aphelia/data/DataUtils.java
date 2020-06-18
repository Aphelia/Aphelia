package to.us.awesomest.aphelia.data;

import com.google.gson.Gson;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

@SuppressWarnings("unchecked")
public final class DataUtils {
    static Gson json = new Gson();

    @SuppressWarnings("ResultOfMethodCallIgnored")
    static void writeFile(String guildId, String module, HashMap<String, String> mappedData ) {
        try {
            File guildDataDir = new File("GuildData/" + guildId + "/");
            guildDataDir.mkdirs();
            FileWriter fileWriter;
            fileWriter = new FileWriter(new File("GuildData/" + guildId + "/" + module + ".json"));  //FileWriter throws IOException
            LoggerFactory.getLogger("DataUtils").debug("Attempting to write " + mappedData + " for " + module);
            fileWriter.write(json.toJson(mappedData));
            LoggerFactory.getLogger("DataUtils").debug("Wrote " + json.toJson(mappedData) + " for " + module);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static HashMap<String, String> readFile(String guildId, String module) {
        File dataFile = new File("GuildData/" + guildId + "/" + module + ".json");
        HashMap<String, String> output = new HashMap<>();
        if (dataFile.exists()) {
            try {
                Scanner dataScanner = new Scanner(dataFile);
                while (dataScanner.hasNextLine()) {
                    String data = dataScanner.nextLine();
                    output = json.fromJson(data, output.getClass());
                    LoggerFactory.getLogger("DataUtils").debug("Read " + data + " for " + module);
                }
                dataScanner.close();
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }
        return output;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    static void writeRaw(String guildId, String module, String data) {
        try {
            File guildDataDir = new File("GuildData/" + guildId + "/");
            guildDataDir.mkdirs();
            FileWriter fileWriter;
            fileWriter = new FileWriter(new File("GuildData/" + guildId + "/" + module + ".txt"));  //FileWriter throws IOException
            LoggerFactory.getLogger("DataUtils").debug("Attempting to write " + data + " for " + module);
            fileWriter.write(data);
            LoggerFactory.getLogger("DataUtils").debug("Wrote " + data + " for " + module);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static String readRaw(String guildId, String module) {
        File dataFile = new File("GuildData/" + guildId + "/" + module + ".txt");
        String data = null;
        if (dataFile.exists()) {
            try {
                Scanner dataScanner = new Scanner(dataFile);
                while (dataScanner.hasNextLine()) {
                    data = dataScanner.nextLine();
                    LoggerFactory.getLogger("DataUtils").debug("Read " + data + " for " + module);
                }
                dataScanner.close();
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }
        return data;

    }
}
