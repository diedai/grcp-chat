package cn.com.hawkeye.grpcpushserver.device;

import cn.com.hawkeye.device.ErrorInfo;
import cn.com.hawkeye.device.Message;
import cn.com.hawkeye.device.SendMsgGrpc;
import io.grpc.stub.StreamObserver;
import redis.clients.jedis.Jedis;

/**
 * 发送信息服务
 * 
 * @author gongzhihao
 *
 */
public class SendMsgService extends SendMsgGrpc.SendMsgImplBase {
	@Override
	public void sendMsg(Message request, StreamObserver<ErrorInfo> responseObserver) { // io.grpc.stub.ServerCalls$ServerCallStreamObserverImpl
		System.out.println("发送者:" + request.getSender() + ",内容:" + request.getContent());

		//发布主题
		Jedis jedis = new Jedis("47.107.81.60", 6379);
		String key = request.getReceiver();
		jedis.publish(key, request.getContent());
		System.out.println("key的名字:" + key); // key的名字
		System.out.println("元素数量:" + jedis.llen(key)); // 元素数量
		for (String string : jedis.lrange(key, 0, jedis.llen(key) - 1)) {
			System.out.print(string + ",");
		}
		System.out.println();
		
		System.out.println("发布信息到中间件:" + "key=" + key + ",content=" + request.getContent());
		
		//响应
		String errorMsg="send msg success with msgId="+request.getMsgId();
        ErrorInfo reply = ErrorInfo.newBuilder().setCode(0).setMsg(errorMsg).build();
        responseObserver.onNext(reply); //调用当前客户端的流 写数据到当前客户端的流 客户端流读这个数据
        responseObserver.onCompleted();
	}
}
