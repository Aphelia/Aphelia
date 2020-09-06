package to.us.awesomest.aphelia.globalutils;

import net.dv8tion.jda.api.entities.Guild;
import to.us.awesomest.aphelia.data.LangData;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

public final class LanguageUtils {
    private static final Map<String, Properties> languages = new HashMap<>();
    private static final AtomicBoolean isInitialized = new AtomicBoolean(false);
    static final String[] langList = {
            "en_US",
            "fr_FR",
            "es_ES",
            "zh_CN"
    };

    private static void initialize() {
        try {
            for(String langCode : langList) {
                Properties propertyFile = new Properties();
                propertyFile.load(new FileReader(LanguageUtils.class.getClassLoader().getResource("lang/" + langCode + ".lang").getFile()));
                languages.put(langCode, propertyFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        isInitialized.set(true);
    }
    public static boolean isValidLang(String langCode) {
        if(!isInitialized.get()) initialize();
        return languages.containsKey(langCode);
    }

    public static String getMessage(String langCode, String message) {
        if(!isInitialized.get()) initialize();
        return languages.get(langCode).getProperty(message);
    }

    public static String getMessage(Guild guild, String message) {
        return getMessage(LangData.getInstanceByGuildId(guild.getId()).getEntry(null), message);
    }
}
