package client2;

import java.util.List;

import redis.clients.jedis.Jedis;

public class ListenMsg1 implements Runnable {

	@Override
	public void run() {
		while (true) {
			// 监听阻塞队列：消费client1的数据
			Jedis jedis = new Jedis("47.107.81.60", 6379);
//			System.out.println(jedis.llen("client2")); //元素数量
//			for (String string : jedis.lrange("client2", 0, jedis.llen("client2")-1)) {
//				System.out.print(string + ",");
//			}
//			System.out.println();
			
			//监听客户端1的阻塞队列
			String key = "client1client2";
			List<String> lists = jedis.brpop(key,"0"); 
			for (String string : lists) {
				System.out.print(string + ",");
			}
			System.out.println();
			
//			System.out.println(jedis.llen("client2")); //元素数量
//			for (String string : jedis.lrange("client2", 0, jedis.llen("client2")-1)) {
//				System.out.print(string + ",");
//			}
//			System.out.println();
		}
	}

}
