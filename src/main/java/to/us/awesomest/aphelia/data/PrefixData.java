package to.us.awesomest.aphelia.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PrefixData implements GuildDataHandler {
    private static final HashMap<String, PrefixData> guildInstanceMap = new HashMap<>();
    private String guildId;
    private String prefix;

    private PrefixData() {
    }


    public static PrefixData getInstanceByGuildId(String guildIdInstance) {
        if (!guildInstanceMap.containsKey(guildIdInstance)) {
            guildInstanceMap.put(guildIdInstance, new PrefixData());
            guildInstanceMap.get(guildIdInstance).prefix = DataUtils.readFile(guildIdInstance, "prefixData").get("prefix");
        }
        guildInstanceMap.get(guildIdInstance).guildId = guildIdInstance;
        return guildInstanceMap.get(guildIdInstance);
    }

    @Override
    public void setEntry(String key, String value) {
        prefix = value;
        DataUtils.writeFile(guildId, "prefixData", new HashMap<String, String>() {{
            put("prefix", value);
        }});
    }

    @Override
    public void deleteEntry(String key) {
        throw new UnsupportedOperationException("You can't delete a prefix, silly!");
    }

    @Override
    public String getEntry(String key) {
        return prefix;
    }

    @Override
    public Set<Map.Entry<String, String>> getAllEntries() {
        throw new UnsupportedOperationException("There can only be one prefix, silly!");
    }

    @Override
    public boolean hasEntry(String key) {
        throw new UnsupportedOperationException("Of course there's an entry. It's a guild prefix!");
    }
}
