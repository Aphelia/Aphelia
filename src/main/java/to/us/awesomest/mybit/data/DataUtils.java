package to.us.awesomest.mybit.data;

import com.google.gson.Gson;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public final class DataUtils {
    static Gson json = new Gson();
    static void writeFile(String guildId, String module, HashMap<String, String> mappedData ) {
        try {
            File guildDataDir = new File("GuildData/" + guildId + "/");
            guildDataDir.mkdirs();
            FileWriter fileWriter;
            fileWriter = new FileWriter(new File("GuildData/" + guildId + "/" + module + ".json"));  //FileWriter throws IOException
            fileWriter.write(json.toJson(mappedData));
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
                    LoggerFactory.getLogger("DataUtils").debug("Wrote " + data + " for " + module);
                }
                dataScanner.close();
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }
        return output;
    }
}
