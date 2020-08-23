package to.us.awesomest.aphelia.module.chathandlers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

public final class TLDUtils {
    private static final HashSet<String> TLDList = new HashSet<>();
    private static boolean isInitialized = false;
    public static boolean isTLD(String tld) {
        if(!isInitialized) {
            try {
                //noinspection ConstantConditions
                BufferedReader inputReader = new BufferedReader(new FileReader(TLDUtils.class.getClassLoader().getResource("tlds.txt").getFile()));
                while(true) {
                    String line = inputReader.readLine();
                    if(line == null) break;
                    TLDList.add(line);
                }
                isInitialized = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return TLDList.contains(tld.toUpperCase());
    }
}
