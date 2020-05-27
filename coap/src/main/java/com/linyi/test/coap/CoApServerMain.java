package com.linyi.test.coap;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.network.CoapEndpoint;
import org.eclipse.californium.core.network.config.NetworkConfig;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.eclipse.californium.elements.tcp.TcpServerConnector;

/**
 * Hello world!
 *
 */
public class CoApServerMain {


	public static void main(String[] args) {

		final CoapServer server = new CoapServer();

		// 默认就是ucp实现传输层
		server.addEndpoint(new CoapEndpoint(new InetSocketAddress("127.0.0.1", 5683)));

		NetworkConfig net = NetworkConfig.createStandardWithoutFile()
			      .setLong(NetworkConfig.Keys.MAX_MESSAGE_SIZE, 16 * 1024)
			      .setInt(NetworkConfig.Keys.PROTOCOL_STAGE_THREAD_COUNT, 2)
			      .setLong(NetworkConfig.Keys.EXCHANGE_LIFETIME, 10000);
		
		// 加入tcp实现传输层
//		server.addEndpoint(new CoapEndpoint(new TcpServerConnector(new InetSocketAddress("127.0.0.1", 5683), 4, 20000),
//				NetworkConfig.getStandard()));
		
		server.addEndpoint(new CoapEndpoint(new TcpServerConnector(new InetSocketAddress("127.0.0.1", 5683), 4, 20000),
				net));

		// 可以加入dtls支持，也就是coaps
		// server.addEndpoint(new CoapEndpoint(
		// new DTLSConnector(), //这里只是抛砖引玉，需要构建DtlsConnectorConfig
		// NetworkConfig.getStandard()));

		server.add(new CoapResource("xxx") {
			@Override
			public void handleGET(CoapExchange exchange) {
				handlePOST(exchange);
			};
			@Override
			public void handlePOST(CoapExchange exchange) { // 1
				System.out.println(exchange.getRequestOptions().getUriQueryString());
				System.out.println(exchange.getRequestText().length());
				exchange.respond("hello xxx");
				super.handlePOST(exchange);
			}
		}.add(new CoapResource("yyy") {
			@Override
			public void handleGET(CoapExchange exchange) {
				handlePOST(exchange);
			};
			@Override
			public void handlePOST(CoapExchange exchange) { // 2
				exchange.respond("hello yyy");
				super.handlePOST(exchange);
			}
		}));
		
		server.add((new CoapResource("zzz") {
			@Override
			public void handleGET(CoapExchange exchange) {
				handlePOST(exchange);
			};
			@Override
			public void handlePOST(CoapExchange exchange) { // 2
				exchange.respond("hello zzz");
				super.handlePOST(exchange);
			}
		}));
		
		Executors.newSingleThreadExecutor().execute(() -> {
			server.start();
		});
	}

}
