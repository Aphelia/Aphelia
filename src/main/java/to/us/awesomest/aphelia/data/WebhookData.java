package to.us.awesomest.aphelia.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class WebhookData implements GuildDataHandler {
    private static final HashMap<String, WebhookData> guildInstanceMap = new HashMap<>();
    private HashMap<String, String> webhookMap = new HashMap<>();
    private String guildId;

    private WebhookData() {
    }

    public static WebhookData getInstanceByGuildId(String guildIdInstance) {
        if (!guildInstanceMap.containsKey(guildIdInstance)) {
            guildInstanceMap.put(guildIdInstance, new WebhookData());
            guildInstanceMap.get(guildIdInstance).webhookMap = DataUtils.readFile(guildIdInstance, "webhookData");
        }
        guildInstanceMap.get(guildIdInstance).guildId = guildIdInstance;
        return guildInstanceMap.get(guildIdInstance);
    }

    @Override
    public void setEntry(String channelId, String webhookURL) {
        webhookMap.put(channelId, webhookURL);
        DataUtils.writeFile(guildId, "webhookData", webhookMap);
    }

    @Override
    public void deleteEntry(String channelId) {
        webhookMap.remove(channelId);
        DataUtils.writeFile(guildId, "webhookData", webhookMap);
    }

    @Override
    public String getEntry(String channelId) {
        return webhookMap.get(channelId);
    }

    @Override
    public Set<Map.Entry<String, String>> getAllEntries() {
        return webhookMap.entrySet();
    }

    @Override
    public boolean hasEntry(String key) {
        return webhookMap.containsKey(key);
    }

    public boolean hasValue(String value) {
        return webhookMap.containsValue(value);
    }
}
