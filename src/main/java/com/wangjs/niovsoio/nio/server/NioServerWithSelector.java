package com.wangjs.niovsoio.nio.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * NIO Server main class
 */
public class NioServerWithSelector {
	/**
	 * 服务器监听端口
	 */
	public static final Integer LISTENER_PORT = 10000;
	
	
	public void startServer() {
		
		Selector sel = null;
		
		try (ServerSocketChannel serverChannel = ServerSocketChannel.open()){
			serverChannel.configureBlocking(false);
			serverChannel.socket().bind(new InetSocketAddress(LISTENER_PORT));
			
			sel = Selector.open();
			serverChannel.register(sel, SelectionKey.OP_ACCEPT);

			while(true){
				int readyers = sel.select();
				if(readyers>0){
					Set<SelectionKey> selectedKeys = sel.selectedKeys();
					Iterator<SelectionKey> iterator = selectedKeys.iterator();
					
					while(iterator.hasNext()){
						SelectionKey key = (SelectionKey)iterator.next();
						
						//remove current key
						iterator.remove();
						
						if(key.isAcceptable()){
							doAcceptThing(key);
							continue;
						}
						
						if(key.isReadable()){
							doReadThing(key);
							continue;
						}
						
						if(key.isWritable()){
							doWriteThing(key);
							continue;
						}
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
		socketChannel.register(sel, SelectionKey.OP_READ|SelectionKey.OP_WRITE);
	}
	
	private void doReadThing(SelectionKey key){
		SocketChannel socketChannel = (SocketChannel)key.channel();
		
	}
	
	private void doWriteThing(SelectionKey key){
		
	}
}
