package to.us.awesomest.mybit.data;

import java.util.Map;
import java.util.Set;

public interface GuildDataHandler {
    void setEntry(String key, String value);
    void deleteEntry(String key);
    Object getEntry(String key);
    Set<Map.Entry<String,String>> getAllEntries();
    boolean hasEntry(String key);
}
