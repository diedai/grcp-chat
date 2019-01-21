package cn.com.hawkeye.grpcpushserver.device;

import cn.com.hawkeye.device.Message;
import io.grpc.stub.StreamObserver;

/**
 * 推送消息工具类
 * 
 * @author gongzhihao
 *
 */
public class PushUtil {

	public volatile static CacheUtil<Message> cacheUtil = new CacheUtil<Message>();


	/**
	 * 推送数据给接收者
	 * 
	 * @param clientId 接收者-客户端id
	 * @param msg      消息
	 */
	public static void pushMsg(String clientId, Message msg) {
		StreamObserver<Message> respObserver = cacheUtil.getObserver(clientId); // 取接收者-客户端id的流
		if (null != respObserver) {
			System.out.println("server push msg：" + msg.getContent());
			respObserver.onNext(msg);
		}

	}

}
