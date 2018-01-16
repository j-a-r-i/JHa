import redis.clients.jedis.Jedis;

public class MyRedis {
	MyRedis() {
		//Jedis jedis = new Jedis(Config.getRedisServer(), 6379);
		Jedis jedis = new Jedis("localhost", 6379);
		
		for(String key : jedis.keys("*")) {
			System.out.println(key);
		}
		jedis.close();
	}
}
