package to.us.awesomest.aphelia.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TicketData implements GuildDataHandler {
    private static final HashMap<String, TicketData> guildInstanceMap = new HashMap<>();
    private HashMap<String, String> ticketMap = new HashMap<>();
    private String guildId;

    private TicketData() {
    }

    public static TicketData getInstanceByGuildId(String guildIdInstance) {
        if (!guildInstanceMap.containsKey(guildIdInstance)) {
            guildInstanceMap.put(guildIdInstance, new TicketData());
            guildInstanceMap.get(guildIdInstance).ticketMap = DataUtils.readFile(guildIdInstance, "ticketData");
        }
        guildInstanceMap.get(guildIdInstance).guildId = guildIdInstance;
        return guildInstanceMap.get(guildIdInstance);
    }

    @Override
    public void setEntry(String channelId, String ownerId) {
        ticketMap.put(channelId, ownerId);
        DataUtils.writeFile(guildId, "ticketData", ticketMap);
    }

    @Override
    public void deleteEntry(String channelId) {
        ticketMap.remove(channelId);
        DataUtils.writeFile(guildId, "ticketData", ticketMap);
    }

    @Override
    public String getEntry(String channelId) {
        return ticketMap.get(channelId);
    }

    @Override
    public Set<Map.Entry<String, String>> getAllEntries() {
        return ticketMap.entrySet();
    }

    @Override
    public boolean hasEntry(String key) {
        return ticketMap.containsKey(key);
    }

    public boolean hasValue(String value) {
        return ticketMap.containsValue(value);
    }
}
