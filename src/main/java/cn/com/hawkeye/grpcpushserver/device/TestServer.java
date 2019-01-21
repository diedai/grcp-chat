package cn.com.hawkeye.grpcpushserver.device;

import java.io.IOException;
import java.util.Scanner;

import cn.com.hawkeye.device.Message;
import io.grpc.Server;
import io.grpc.ServerBuilder;

/**
 * 服务器启动入口类
 * 
 * @author gongzhihao
 *
 */
public class TestServer {

	private int port;
	private Server server;

	public TestServer(int port) throws IOException {
		this(ServerBuilder.forPort(port), port);
	}

	public TestServer(ServerBuilder<?> serverBuilder, int port) {
		this.port = port;
		this.server = serverBuilder.addService(new LoginService()).addService(new SendMsgService()).build(); // 添加服务
	}

	/** Start serving requests. */
	public void start() throws IOException {
		server.start(); // 启动服务器 开始监听
		System.out.println("Server started, listening on " + port);
		Runtime.getRuntime().addShutdownHook(new Thread() {

			public void run() {
				System.err.println("*** shutting down gRPC server since JVM is shutting down");
				TestServer.this.stop();
				System.err.println("*** server shut down");
			}
		});
	}

	/** Stop serving requests and shutdown resources. */
	public void stop() {
		if (server != null) {
			server.shutdown();
		}
	}

	/**
	 * Await termination on the main thread since the grpc library uses daemon
	 * threads.
	 */
	public void blockUntilShutdown() throws InterruptedException {
		if (server != null) {
			server.awaitTermination();
		}
	}

	/**
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		//启动服务器
		TestServer server = new TestServer(8980);
		
		server.start();
		
		//让服务器一直运行
		Scanner sc = new Scanner(System.in);
		// server.blockUntilShutdown();
		while (true) {
			String str = sc.nextLine();
			Message msg = Message.newBuilder().setMsgId(0).setReceiver("1").setContent(str).build();
			PushUtil.pushMsg(msg.getReceiver(), msg);
		}
	}

}
