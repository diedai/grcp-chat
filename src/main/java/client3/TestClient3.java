package client3;

import cn.com.hawkeye.device.LoginGrpc;
import cn.com.hawkeye.device.ErrorInfo;
import cn.com.hawkeye.device.LoginRequest;
import cn.com.hawkeye.device.Message;
import cn.com.hawkeye.device.SendMsgGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * 客户端
 * @author gongzhihao
 *
 */
public class TestClient3 {
    private final LoginGrpc.LoginStub asyncStub;
    private final SendMsgGrpc.SendMsgBlockingStub blockingStub;
    private final ManagedChannel channel;
    private final String clientId;
    private final LoginRequest loginRequest;

    public TestClient3(String host, int port, String clientId) {
        this(ManagedChannelBuilder.forAddress(host, port).usePlaintext(true), clientId);
    }

    /**
     * Construct client for accessing RouteGuide server using the existing
     * channel.W
     */
    public TestClient3(ManagedChannelBuilder<?> channelBuilder, String clientId) {
        this.clientId = clientId;
        this.loginRequest = LoginRequest.newBuilder().setClientId(clientId).build();
        this.channel = channelBuilder.build();
        this.asyncStub = LoginGrpc.newStub(channel);
        this.blockingStub = SendMsgGrpc.newBlockingStub(channel);
        System.out.println("init client "+clientId);
    }

    public void shutdown() throws InterruptedException {
        this.channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
        System.out.println("shut down "+clientId);
    }

    /**
     * 发送信息
     * @param content 
     */
    public void sendMsg(String content){
        Message msg = Message.newBuilder().setMsgId(0).setSender("client3").setReceiver("client2").setContent(content).build();
        ErrorInfo result =  blockingStub.sendMsg(msg);
        if(result.getCode() == 0){
            System.out.println("send："+content );
        }
    }
    
    public void saveClientIdAndStream(){
        this.asyncStub.saveClientIdAndStream(this.loginRequest, new DefaultObserver(this.clientId, this));
    }
    
    public void sub(){
        this.asyncStub.sub(this.loginRequest, new DefaultObserver(this.clientId, this));
    }

    /**
     * 入口
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
    	//建立连接
        TestClient3 client = new TestClient3("127.0.0.1", 8980,"client3");
        
        //登陆
        client.login();
        
        //保存客户端id和流
        client.saveClientIdAndStream();
        
        //订阅主题
        client.sub();
        
        //发送信息
        Scanner sc=new Scanner(System.in);
        while (true){
            String str = sc.nextLine();
            client.sendMsg(str);
        }

        
    }

    /**
     * 登陆
     */
	private void login() {
		this.asyncStub.login(this.loginRequest, new DefaultObserver(this.clientId, this));
	}

}
