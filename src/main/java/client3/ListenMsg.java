package client3;

import java.util.List;

import redis.clients.jedis.Jedis;

public class ListenMsg implements Runnable {

	@Override
	public void run() {
		while (true) {
			// 监听阻塞队列：消费client3的数据
			Jedis jedis = new Jedis("47.107.81.60", 6379);
			System.out.println(jedis.llen("client3")); //元素数量
			for (String string : jedis.lrange("client3", 0, jedis.llen("client3")-1)) {
				System.out.print(string + ",");
			}
			System.out.println();
			
			List<String> lists = jedis.brpop("client3","0"); 
			for (String string : lists) {
				System.out.print(string + ",");
			}
			System.out.println();
			
			System.out.println(jedis.llen("client3")); //元素数量
			for (String string : jedis.lrange("client3", 0, jedis.llen("client3")-1)) {
				System.out.print(string + ",");
			}
			System.out.println();
		}
	}

}