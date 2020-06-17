package to.us.awesomest.mybit.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MCData implements GuildDataHandler {

    private HashMap<String, String> tokenChannelIdMap = new HashMap<>();
    static MCData instance;
    public static MCData getInstance() {
        if(instance == null) {
            instance = new MCData();
            instance.tokenChannelIdMap = DataUtils.readFile("misc", "mcTokens");
        }
        return instance;
    }

    @Override
    public void setEntry(String token, String channelId) {
        tokenChannelIdMap.put(token, channelId);
        DataUtils.writeFile("misc", "mcTokens", tokenChannelIdMap);
    }

    @Override
    public void deleteEntry(String token) {
        tokenChannelIdMap.remove(token);
    }

    @Override
    public String getEntry(String token) {
        return tokenChannelIdMap.get(token);
    }

    @Override
    public Set<Map.Entry<String, String>> getAllEntries() {
        throw new UnsupportedOperationException("Cannot print private tokens!");
    }

    @Override
    public boolean hasEntry(String token) {
        return tokenChannelIdMap.containsKey(token);
    }
}
