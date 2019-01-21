package cn.com.hawkeye.grpcpushserver.device;

import cn.com.hawkeye.device.LoginRequest;
import cn.com.hawkeye.device.Message;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

/**
 * 订阅者线程
 * @author gongzhihao
 *
 */
public class SubscriberThread implements Runnable {
	private LoginRequest loginRequest;

	public SubscriberThread(LoginRequest loginRequest) {
		this.loginRequest = loginRequest;
	}

	@Override
	public void run() {
		Jedis jedis = new Jedis("47.107.81.60", 6379);
	
		//订阅主题
		String key = loginRequest.getClientId();
		System.out.println("订阅主题:" + key);
		jedis.subscribe(new Subscriber(null), key); // 订阅

		System.out.println("该行代码是执行不到的");
	}

}
