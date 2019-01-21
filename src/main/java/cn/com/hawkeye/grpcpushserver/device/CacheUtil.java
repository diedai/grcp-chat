package cn.com.hawkeye.grpcpushserver.device;

import java.util.concurrent.ConcurrentHashMap;

import io.grpc.stub.StreamObserver;


public class CacheUtil<T> implements CacheUtilI<T>{
	
	private volatile ConcurrentHashMap<String, StreamObserver<T>> cache = new ConcurrentHashMap<String, StreamObserver<T>>();
	
	
	public void saveClientIdAndStream(String clientId, StreamObserver<T> responseObserver) {
		cache.put(clientId, responseObserver);
	}



	public StreamObserver<T> getObserver(String clientId) {
		return cache.get(clientId);
	}

	
	



}
