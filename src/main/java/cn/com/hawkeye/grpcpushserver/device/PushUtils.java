package cn.com.hawkeye.grpcpushserver.device;

import cn.com.hawkeye.device.Message;
import io.grpc.stub.StreamObserver;

public class PushUtils {

    public volatile static IClientCache<Message> clientCache = new DefaultClientCache<Message>();

    //public volatile static Queue<Message> queue = new LinkedList<Message>();

    public static void pushMsg(Message msg){

            String clientId = msg.getReceiver();
            StreamObserver<Message> respObserver = clientCache.getObserver(clientId); //客户端2的流
            if(null != respObserver){
                System.out.println("server push msg："+msg.getContent());
                respObserver.onNext(msg); //{1=io.grpc.stub.ServerCalls$ServerCallStreamObserverImpl@39aaf1c0, 2=io.grpc.stub.ServerCalls$ServerCallStreamObserverImpl@7bd1d387}
            }

    }

}
