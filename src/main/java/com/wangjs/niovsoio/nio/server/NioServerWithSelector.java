package com.wangjs.niovsoio.nio.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * NIO Server main class
 */
public class NioServerWithSelector {
	
	
	public void startServer() {
		
		Selector selector = null;
		
		try (ServerSocketChannel server = ServerSocketChannel.open()){
			server.socket().bind(new InetSocketAddress("localhost", 10000));
			
			selector = Selector.open();

			while(true){
				try(SocketChannel socketChannel = server.accept()){
					socketChannel.configureBlocking(false);
					
					socketChannel.register(selector, SelectionKey.OP_CONNECT);
				}
				
				
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if(selector!=null){
				try {
					selector.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
