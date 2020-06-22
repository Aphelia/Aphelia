package to.us.awesomest.aphelia.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ShortcutData implements GuildDataHandler {
    private static final HashMap<String, ShortcutData>  guildInstanceMap = new HashMap<>();
    private HashMap<String, String> commandOutputMap = new HashMap<>();
    private String guildId;
    private ShortcutData(){}

    public static ShortcutData getInstanceByGuildId(String guildIdInstance) {
        if(!guildInstanceMap.containsKey(guildIdInstance)) {
            guildInstanceMap.put(guildIdInstance, new ShortcutData());
            guildInstanceMap.get(guildIdInstance).commandOutputMap = DataUtils.readFile(guildIdInstance, "shortcutData");
        }
        guildInstanceMap.get(guildIdInstance).guildId = guildIdInstance;
        return guildInstanceMap.get(guildIdInstance);
    }

    @Override
    public void setEntry(String command, String output) {
        commandOutputMap.put(command, output);
        DataUtils.writeFile(guildId, "shortcutData", commandOutputMap);
    }

    @Override
    public void deleteEntry(String command) {
        commandOutputMap.remove(command);
        DataUtils.writeFile(guildId, "shortcutData", commandOutputMap);
    }

    @Override
    public String getEntry(String command) {
        return commandOutputMap.get(command);
    }

    @Override
    public Set<Map.Entry<String, String>> getAllEntries() {
        return commandOutputMap.entrySet();
    }
    @Override
    public boolean hasEntry(String shortcut) {
        return commandOutputMap.containsKey(shortcut);
    }
}
