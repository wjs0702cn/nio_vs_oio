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

//	TODO 2015-02-15
//	 这段代码很行为很奇怪，只要有链接进来，就不断的发动readOps.
//	 连iterator.remove方法都不能移除，
//	监测连接的connected属性，明明连接已经断掉了，但是服务器还是socketChannel.isConnected = true
//  问题解决了，原因在与服务器端的read 循环读 阻塞了线程？

    // todo 2015-02-15
    // 服务器在接受第一个请求的时候表现正常
    // 但是，在第二个和第三个请求接入的时候表现为阻塞状态。
    // 经过查看，是int available = sel.select();方法一直在阻塞
	
	//以上两个问题，在于：1. 不用在方法外面调用key.cancel()
	// 2. 在read操作读到-1 的时候，应该把channel.close();
	// 3. IO操作最好在ThreadGroup 或者  try...catch中执行，原因在于客户端不受控制，随时会断掉。


    public static void main(String[] args) {
        NioServerWithSelector server = new NioServerWithSelector();
        server.startServer();
    }


    public void startServer() {

        System.out.println("starting server ......");

        Selector sel = null;

        try (ServerSocketChannel serverChannel = ServerSocketChannel.open()) {
            serverChannel.configureBlocking(false);
            serverChannel.socket().bind(new InetSocketAddress(Constants.LISTENER_PORT));

            System.out.println("server started.");

            sel = Selector.open();
            serverChannel.register(sel, SelectionKey.OP_ACCEPT);

            while (true) {
                int available = sel.select();
                System.out.println("available = " + available);

                if (available == 0) {
                    continue;
                }

                Iterator<SelectionKey> iterator = sel.selectedKeys().iterator();

                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();

                    // remove current key
                    iterator.remove();

                    if (key.isValid() && key.isAcceptable()) {
                        System.out.println("new socket connecting....");
                        doAcceptThing(key);
                    }

                    if (key.isValid() && key.isReadable()) {
                        System.out.println("socket is sending data to server....");
//                        doReadThing(key);
							doEchoThing(key);

                    }

                    if (key.isValid() && key.isWritable()) {
                        System.out.println("socket is ready for receive data...");
                        doWriteThing(key);
                    }


                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (sel != null) {
                try {
                    sel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void doAcceptThing(SelectionKey key) throws IOException {
        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
        SocketChannel socketChannel = serverChannel.accept();
        socketChannel.configureBlocking(false);

        Selector sel = key.selector();
//        socketChannel.register(sel, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        socketChannel.register(sel, SelectionKey.OP_READ);
    }

    private void doReadThing(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(Constants.READ_BUFFER);

        StringBuilder sb = new StringBuilder();

        int size = -1;
        Charset charset = Charset.forName(Constants.CHARSET);

        while (true) {
            size = socketChannel.read(buffer);
            if (size != -1) {
                buffer.flip();
                sb.append(charset.decode(buffer).toString());
                buffer.clear();
            } else {
                break;
            }
        }

        System.out.println(sb.toString());

    }

    private void doEchoThing(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(Constants.READ_BUFFER);

        int size = -1;

        try {
			while (true) {
			    buffer.clear();
			    size = socketChannel.read(buffer);
			    if (size == -1) {
			        break;
			    }
			    buffer.flip();
			    socketChannel.write(buffer);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        socketChannel.close();
    }

    private void doWriteThing(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(Constants.WRITE_BUFFER);
        buffer.clear();
        buffer.put("Hello, response from server".getBytes());
        buffer.flip();
        socketChannel.write(buffer);

    }
}
