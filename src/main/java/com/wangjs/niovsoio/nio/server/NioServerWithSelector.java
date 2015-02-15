package com.wangjs.niovsoio.nio.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;

import com.wangjs.niovsoio.util.Constants;

/**
 * NIO Server main class
 */
public class NioServerWithSelector {
	
//	TODO 2015-02-15<br/>
//	 这段代码很行为很奇怪，只要有链接进来，就不断的发动readOps.
//	 连iterator.remove方法都不能移除，
//	监测连接的connected属性，明明连接已经断掉了，但是服务器还是socketChannle.isConnected = true
	
	public static void main(String[] args) {
		NioServerWithSelector server = new NioServerWithSelector();
		server.startServer();
	}
	
	
	public void startServer() {
		
		System.out.println("starting server ......");
		
		Selector sel = null;
		
		try (ServerSocketChannel serverChannel = ServerSocketChannel.open()){
			serverChannel.configureBlocking(false);
			serverChannel.socket().bind(new InetSocketAddress(Constants.LISTENER_PORT));
			
			System.out.println("server started.");
			
			sel = Selector.open();
			serverChannel.register(sel, SelectionKey.OP_ACCEPT);
			
			int stopCount = 50;
			
			while(true){
				int readyers = sel.select();
				
				if(readyers==0){
					continue;
				}
				
				Iterator<SelectionKey> iterator = sel.selectedKeys().iterator();

				while (iterator.hasNext()) {
					SelectionKey key = (SelectionKey) iterator.next();
					
					if(stopCount--==0){
						System.exit(0);
					}else{
						System.out.println("sel.selectedKeys() ================== ");
						for(SelectionKey selectionKey : sel.selectedKeys()){
							System.out.println("selectionKey = "+selectionKey.interestOps()+"_"+selectionKey.channel());
						}
						System.out.println("sel.selectedKeys() ================== ");
					}

					// remove current key
					iterator.remove();
					
					if (key.isAcceptable()) {
						System.out.println("new socket connecting....");
						doAcceptThing(key);
						continue;
					}

					if (key.isReadable()) {
						System.out.println("socket is sending data to server....");
						doReadThing(key);
						continue;
					}

					if (key.isWritable()) {
						System.out.println("socket is ready for receive data...");
						doWriteThing(key);
						continue;
					}
					

				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if(sel!=null){
				try {
					sel.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void doAcceptThing(SelectionKey key) throws IOException{
		ServerSocketChannel serverChannel = (ServerSocketChannel)key.channel();
		SocketChannel socketChannel = serverChannel.accept();
		socketChannel.configureBlocking(false);
		
		Selector sel = key.selector();
		socketChannel.register(sel,SelectionKey.OP_READ|SelectionKey.OP_WRITE);
	}
	
	private void doReadThing(SelectionKey key) throws IOException{
		SocketChannel socketChannel = (SocketChannel)key.channel();		
		
		System.out.println("socket is connected = "+socketChannel.isConnected());
		
		ByteBuffer buffer = ByteBuffer.allocate(Constants.READ_BUFFER);
		
		StringBuilder sb = new StringBuilder();
		
		int size = -1;
		Charset charset = Charset.forName(Constants.CHARSET);
		
		while(true){
			size = socketChannel.read(buffer);
			if(size != -1){
				buffer.flip();
				sb.append(charset.decode(buffer).toString());
				buffer.clear();
			}else{
				break;
			}
		}
		
		System.out.println(sb.toString());

	}
	
	private void doWriteThing(SelectionKey key){
		
	}
}
