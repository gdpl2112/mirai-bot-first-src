package Project;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PublicAddressor {

    public static final Map<String, Broadcast> Broadcasts = new ConcurrentHashMap<>();

    public static class Broadcast {
        public String id;
        
    }
}
