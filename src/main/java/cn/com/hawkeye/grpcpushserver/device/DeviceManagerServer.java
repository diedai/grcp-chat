package cn.com.hawkeye.grpcpushserver.device;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import cn.com.hawkeye.device.ClientInfo;
import cn.com.hawkeye.device.DeviceManagerGrpc;
import cn.com.hawkeye.device.ErrorInfo;
import cn.com.hawkeye.device.LoginRequest;
import cn.com.hawkeye.device.Message;
import cn.com.hawkeye.device.SubRequest;
import io.grpc.stub.StreamObserver;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

/**
 * 服务
 * 
 * @author gongzhihao
 *
 */
public class DeviceManagerServer extends DeviceManagerGrpc.DeviceManagerImplBase {

	/**
	 *
	 * 客户端注册
	 */
	@Override
	public void registerToServer(ClientInfo request, StreamObserver<Message> responseObserver) {

		PushUtils.clientCache.registerToServer(request, responseObserver); // io.grpc.stub.ServerCalls$ServerCallStreamObserverImpl@3e18f8d5
	}
	
	/**
	 * 登陆
	 */
	public void login(LoginRequest request,StreamObserver<Message> responseObserver) {
		//登陆成功
		
		
		//订阅sub
		sub(request, responseObserver);
	}
	
	/**
	 * 订阅
	 */
	public void sub(SubRequest request,StreamObserver<Message> responseObserver) {
		Jedis jedis = new Jedis("47.107.81.60", 6379);
		 JedisPubSub jedisPubSub=new JedisPubSub() {  
             
	            /** 
	             * 处理普通订阅的频道的消息。 
	             */  
	            @Override  
	            public void onMessage(String channel, String message) {  
	                
	            }  
	        };  
	          
	        //执行完subscribe，本线程会阻塞。  
	        //订阅两个频道：china.beijing和china.shanghai  
	        //当订阅的频道有消息时，会执行jedisPubSub的onMessage方法。 
	        String key = request.getClientId();
	        jedis.subscribe(jedisPubSub,key);  //订阅
	          
	        System.out.println("该行代码是执行不到的");  
	}

	/**
	 *
	 * client call
	 */
	@Override
	public void sendMsg(Message request, StreamObserver<ErrorInfo> responseObserver) { // io.grpc.stub.ServerCalls$ServerCallStreamObserverImpl

		System.out.println("receive：" + request.getContent());
		// 保存消息
		// PushUtils.queue.offer(request);
		String errorMsg = "send msg success with msgId=" + request.getMsgId();
		// 响应
		ErrorInfo reply = ErrorInfo.newBuilder().setCode(0).setMsg(errorMsg).build();
		responseObserver.onNext(reply); // 调用当前客户端的流 写数据到当前客户端的流 客户端流读这个数据
		responseObserver.onCompleted();

		//写数据到缓存
		Jedis jedis = new Jedis("47.107.81.60", 6379);
//		jedis.lpush(request.getReceiver(), request.getContent());
		String key = request.getSender() + request.getReceiver();
		jedis.lpush(key, request.getContent());
		System.out.println("key的名字:" + key); //key的名字
		System.out.println("元素数量:" + jedis.llen(key)); //元素数量
		for (String string : jedis.lrange(key, 0, jedis.llen(key)-1)) {
			System.out.print(string + ",");
		}
	}
}
