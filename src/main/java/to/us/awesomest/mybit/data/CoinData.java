package to.us.awesomest.mybit.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CoinData implements GuildDataHandler {
    private static final HashMap<String, CoinData> guildInstanceMap = new HashMap<>();
    private HashMap<String, String> userCoinMap = new HashMap<>();
    private String guildId;
    private CoinData(){}


    public static CoinData getInstanceByGuildId(String guildIdInstance) {
        if(!guildInstanceMap.containsKey(guildIdInstance)) {
            guildInstanceMap.put(guildIdInstance, new CoinData());
            guildInstanceMap.get(guildIdInstance).userCoinMap = DataUtils.readFile(guildIdInstance, "coinData");
        }
        guildInstanceMap.get(guildIdInstance).guildId = guildIdInstance;
        return guildInstanceMap.get(guildIdInstance);
    }

    @Override
    public void setEntry(String userId, String coins) {
        userCoinMap.put(userId, coins);
        DataUtils.writeFile(guildId, "coinData", userCoinMap);
    }

    @Override
    public void deleteEntry(String key) {
        throw new UnsupportedOperationException("You can't yet delete a user's coins!");
    }

    @Override
    public String getEntry(String userId) {
        if(!userCoinMap.containsKey(userId)) setEntry(userId, "0");
        return userCoinMap.get(userId);
    }

    @Override
    public Set<Map.Entry<String, String>> getAllEntries() {
        return null;
    }

    @Override
    public boolean hasEntry(String key) {
        return false;
    }
}
