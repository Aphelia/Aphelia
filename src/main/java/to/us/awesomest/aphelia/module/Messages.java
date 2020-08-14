package to.us.awesomest.aphelia.module;

import net.dv8tion.jda.api.entities.Guild;
import org.yaml.snakeyaml.Yaml;

import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class Messages {
    String[] languages = {"en_US", "zh_CN", "fr_FR", "nb_NO"};
    private static final Map<String, Locale> guildLanguage = new TreeMap<>();
    public String getMessage(String guildId) {
        return "ignored temp";
    }
}
