package cn.com.hawkeye.grpcpushserver.device;

import cn.com.hawkeye.device.Message;
import io.grpc.stub.StreamObserver;
import redis.clients.jedis.JedisPubSub;

/**
 * 订阅者
 * @author gongzhihao
 *
 */
public class Subscriber extends JedisPubSub{
	private StreamObserver<Message> respObserver;
	
//	public Subscriber(StreamObserver<Message> stream) {
//		this.stream=stream;
//	}
	
	public Subscriber(StreamObserver<Message> responseObserver) {
		this.respObserver=responseObserver;
	}

	@Override
	public void onMessage(String channel, String message) { //回调函数 //主题有数据，就消费
		// 推送数据给接收者
		Message msg = Message.newBuilder().setMsgId(0).setReceiver(channel).setContent(message).build();
		//PushUtil.pushMsg(channel, msg);
		
		if (null != respObserver) {
			System.out.println("server push msg：" + msg.getContent());
			respObserver.onNext(msg);
		}
		
	}
}
