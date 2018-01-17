import java.util.Set;
import java.util.List;
import redis.clients.jedis.Jedis;

public class MyRedis {
    private Jedis jedis;
    
	MyRedis() {
		//Jedis jedis = new Jedis(Config.getRedisServer(), 6379);
		jedis = new Jedis("localhost", 6379);
		
		for(String key : jedis.keys("*")) {
			System.out.println(key);
		}
	}

    public void close() {
	jedis.close();
	jedis = null;
    }

    public Set<String> keys() {
	return jedis.keys("*");
    }

    public List<String> values(String key) {
	return jedis.lrange(key, 0, -1);
    }
}
