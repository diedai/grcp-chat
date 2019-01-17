package cn.com.hawkeye.grpcpushserver.device;

import java.util.concurrent.BlockingQueue;

import cn.com.hawkeye.device.ClientInfo;
import cn.com.hawkeye.device.Message;
import io.grpc.stub.StreamObserver;

public interface IClientCache<T>{
	
	public void registerToServer(ClientInfo clientInfo, StreamObserver<T> observer);
	
	public boolean removeErrorObserver(String clientId);
	
	public StreamObserver<T> getObserver(String clientId);
	
	public void logoutFromServer(ClientInfo clientInfo);
	
	public StreamObserver<T> getObserver(ClientInfo clientInfo);
	
	public void clearErrorObserver();

	/**
	 * 取阻塞队列
	 * @return 
	 */
	public BlockingQueue<Message> getBlockingQueue(String clientId);

}
