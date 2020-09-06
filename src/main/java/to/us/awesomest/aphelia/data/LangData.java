package to.us.awesomest.aphelia.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LangData implements GuildDataHandler {
    private static final HashMap<String, LangData> guildInstanceMap = new HashMap<>();
    private String guildId;
    private String lang;

    private LangData() {}


    public static LangData getInstanceByGuildId(String guildIdInstance) {
        if (!guildInstanceMap.containsKey(guildIdInstance)) {
            guildInstanceMap.put(guildIdInstance, new LangData());
            String lang = DataUtils.readRaw(guildIdInstance, "langData");
            if (lang != null)
                guildInstanceMap.get(guildIdInstance).lang = lang;
            else guildInstanceMap.get(guildIdInstance).setEntry("lang", "en_US");
        }
        guildInstanceMap.get(guildIdInstance).guildId = guildIdInstance;
        return guildInstanceMap.get(guildIdInstance);
    }

    @Override
    public void setEntry(String key, String value) {
        lang = value;
        DataUtils.writeRaw(guildId, "langData", value);
    }

    @Override
    public void deleteEntry(String key) {
        throw new UnsupportedOperationException("You can't delete a language, silly!");
    }

    @Override
    public String getEntry(String key) {
        return lang;
    }

    @Override
    public Set<Map.Entry<String, String>> getAllEntries() {
        throw new UnsupportedOperationException("There can only be one language, silly!");
    }

    @Override
    public boolean hasEntry(String key) {
        throw new UnsupportedOperationException("Of course there's an entry. It's a guild language!");
    }
}