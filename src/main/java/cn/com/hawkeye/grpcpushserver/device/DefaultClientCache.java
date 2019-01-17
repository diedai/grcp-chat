package cn.com.hawkeye.grpcpushserver.device;

import cn.com.hawkeye.device.ClientInfo;
import cn.com.hawkeye.device.Message;
import io.grpc.stub.StreamObserver;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;


public class DefaultClientCache<T> implements IClientCache<T> {
	
	private volatile ConcurrentHashMap<String, StreamObserver<T>> cache = new ConcurrentHashMap<String, StreamObserver<T>>();
	private volatile ConcurrentHashMap<String, BlockingQueue<Message>> cache2 = new ConcurrentHashMap<String, BlockingQueue<Message>>();
	public void registerToServer(ClientInfo clientInfo, StreamObserver<T> observer) {
		BlockingQueue<Message> blockingQueue = new LinkedBlockingQueue<>(); //每个连接 独立的阻塞队列
		cache.put(clientInfo.getClientId(), observer);
//		cache2.put(clientInfo.getClientId(), blockingQueue);
		System.out.println("clientId="+clientInfo.getClientId()+" Already registered.");

	}

	public void clearErrorObserver() {

	}

	@Override
	public StreamObserver<T> getObserver(ClientInfo clientInfo) {
		return getObserver(clientInfo.getClientId());
	}

	@Override
	public boolean removeErrorObserver(String clientId) {
		cache.remove(clientId);
		return true;
	}

	@Override
	public StreamObserver<T> getObserver(String clientId) {
		return cache.get(clientId);
	}

	@Override
	public void logoutFromServer(ClientInfo clientInfo) {

	}

	@Override
	public BlockingQueue<Message> getBlockingQueue(String clientId) {
		return cache2.get(clientId);
	}


}
