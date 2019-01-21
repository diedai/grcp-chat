package cn.com.hawkeye.grpcpushserver.device;

import cn.com.hawkeye.device.ErrorInfo;
import cn.com.hawkeye.device.LoginRequest;
import cn.com.hawkeye.device.Message;
import io.grpc.stub.StreamObserver;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import cn.com.hawkeye.device.LoginGrpc;

/**
 * 登陆服务
 * 
 * @author gongzhihao
 *
 */
public class LoginService extends LoginGrpc.LoginImplBase {
	/**
	 * 登陆
	 */
	public void login(LoginRequest request, StreamObserver<Message> responseObserver) {
		// 登陆成功
		System.out.println("检验用户名和密码:" +  request.getClientId() + " 登陆成功");
	}

	/**
	 *
	 * 保存客户端id和流
	 */
	public void saveClientIdAndStream(LoginRequest request, StreamObserver<Message> responseObserver) {
		PushUtil.cacheUtil.saveClientIdAndStream(request.getClientId(), responseObserver);
		System.out.println("保存客户端id和流:" + request.getClientId() + " 保存成功");
	}

	/**
	 * 订阅
	 */
	public void sub(LoginRequest request, StreamObserver<Message> responseObserver) {
		//开启一个线程 用于订阅主题
//		Runnable runnable = new SubscriberThread(request);
//		Thread thread = new Thread(runnable);
//		thread.start();
		
		Jedis jedis = new Jedis("47.107.81.60", 6379);
		
		//订阅主题
		String key = request.getClientId();
		System.out.println("订阅主题:" + key);
		jedis.subscribe(new Subscriber(responseObserver), key); // 订阅

		System.out.println("该行代码是执行不到的");
	}

}
