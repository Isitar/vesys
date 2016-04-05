package messageboard.server;

import java.util.concurrent.ConcurrentHashMap;

// In-memory data source simulating a persistent data store.
public class DataStore {
	private static final ConcurrentHashMap<String, String> map;

	static {
		map = new ConcurrentHashMap<>();
		map.put("gruntz", "1234");
		map.put("dominik", "5678");
	}

	public static String get(String key) {
		return map.get(key);
	}
}