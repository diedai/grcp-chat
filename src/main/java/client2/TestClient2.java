package client2;

import cn.com.hawkeye.device.ClientInfo;
import cn.com.hawkeye.device.DeviceManagerGrpc;
import cn.com.hawkeye.device.ErrorInfo;
import cn.com.hawkeye.device.Message;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class TestClient2 {
    private final DeviceManagerGrpc.DeviceManagerStub asyncStub;
    private final DeviceManagerGrpc.DeviceManagerBlockingStub blockingStub;
    private final ManagedChannel channel;
    private final String clientId;
    private final ClientInfo clientInfo;

    public TestClient2(String host, int port, String clientId) {
        this(ManagedChannelBuilder.forAddress(host, port).usePlaintext(true), clientId);
    }

    /**
     * Construct client for accessing RouteGuide server using the existing
     * channel.W
     */
    public TestClient2(ManagedChannelBuilder<?> channelBuilder, String clientId) {
        this.clientId = clientId;
        this.clientInfo = ClientInfo.newBuilder().setClientId(clientId).build();
        this.channel = channelBuilder.build();
        this.asyncStub = DeviceManagerGrpc.newStub(channel);
        this.blockingStub = DeviceManagerGrpc.newBlockingStub(channel);
        System.out.println("init client "+clientId);
    }

    public void shutdown() throws InterruptedException {
        this.channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
        System.out.println("shut down "+clientId);
    }

    public void registerToServer(){
        this.asyncStub.registerToServer(this.clientInfo, new DefaultObserver(this.clientId, this));
    }

    public void sendMsg(String content){
        Message msg = Message.newBuilder().setMsgId(0).setReceiver("client1").setContent(content).build();
        ErrorInfo result =  blockingStub.sendMsg(msg);
        if(result.getCode() == 0){
            System.out.println("send："+content );
        }
    }

    public static void main(String[] args) throws InterruptedException {
    	//建立连接
        TestClient2 client = new TestClient2("127.0.0.1", 8980,"client2");
        
        //注册到服务器
        client.registerToServer();
        
        //启动一个线程 接受信息：具体是监听redis 阻塞队列
        Runnable runnable = new ListenMsg1(); //客户端1
        Thread thread = new Thread(runnable);
        thread.start();
        
        Runnable runnable3 = new ListenMsg3(); //客户端3
        Thread thread3 = new Thread(runnable3);
        thread3.start();
        
        //发送信息
        Scanner sc=new Scanner(System.in);
        while (true){
            String str = sc.nextLine();
            client.sendMsg(str);
        }

        
    }

}
