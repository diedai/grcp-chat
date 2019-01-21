package cn.com.hawkeye.grpcpushserver.device;

import io.grpc.stub.StreamObserver;

public interface CacheUtilI<T> {
	public void saveClientIdAndStream(String clientId, StreamObserver<T> responseObserver) ;
		



	public StreamObserver<T> getObserver(String clientId) ;
}
