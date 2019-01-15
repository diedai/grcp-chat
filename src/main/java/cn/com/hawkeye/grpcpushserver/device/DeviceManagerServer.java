package cn.com.hawkeye.grpcpushserver.device;

import cn.com.hawkeye.device.ClientInfo;
import cn.com.hawkeye.device.DeviceManagerGrpc;
import cn.com.hawkeye.device.ErrorInfo;
import cn.com.hawkeye.device.Message;
import io.grpc.stub.StreamObserver;

/**
 * 服务
 * @author gongzhihao
 *
 */
public class DeviceManagerServer extends DeviceManagerGrpc.DeviceManagerImplBase{

    /**
     *
     * 客户端注册
     */
    @Override
    public void registerToServer(ClientInfo request, StreamObserver<Message> responseObserver) {

        PushUtils.clientCache.registerToServer(request,responseObserver); //io.grpc.stub.ServerCalls$ServerCallStreamObserverImpl@3e18f8d5
    }

    /**
     *
     * client call
     */
    @Override
    public void sendMsg(Message request, StreamObserver<ErrorInfo> responseObserver) { //io.grpc.stub.ServerCalls$ServerCallStreamObserverImpl

        System.out.println("receive："+request.getContent());
        //保存消息
        //PushUtils.queue.offer(request);
        String errorMsg="send msg success with msgId="+request.getMsgId();
        //响应
        ErrorInfo reply = ErrorInfo.newBuilder().setCode(0).setMsg(errorMsg).build();
        responseObserver.onNext(reply); //调用当前客户端的流 写数据到当前客户端的流 客户端流读这个数据
        responseObserver.onCompleted();
        //给其他人推送消息
        PushUtils.pushMsg(request);
    }
}
