package client2;

import cn.com.hawkeye.device.Message;
import io.grpc.stub.StreamObserver;

/**
 * 流-回调函数
 * @author gongzhihao
 *
 */
public class DefaultObserver implements StreamObserver<Message> {

	private TestClient2 client;
	private String info;
	
	public DefaultObserver(String info, TestClient2 client){
		this.info = info;
		this.client = client;
	}
	
	@Override
	public void onCompleted() {
		System.out.println(info+"onCompleted");
	}

	@Override
	public void onError(Throwable arg0) {
		System.out.println(info+"onError");
	}

	/**
	 *
	 * Receive server push messages
	 */
	@Override
	public void onNext(Message message) {
		System.out.println("Receive："+message.getContent());
	}

}
