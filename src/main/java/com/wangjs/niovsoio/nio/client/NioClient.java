package com.wangjs.niovsoio.nio.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;

import com.wangjs.niovsoio.util.Constants;

public class NioClient {
    public static void main(String[] args) {
        try {
            new NioClient().connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connect() throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.connect(new InetSocketAddress(Constants.SERVER_HOST, Constants.LISTENER_PORT));

        Selector sel = Selector.open();

        socketChannel.register(sel, SelectionKey.OP_CONNECT);

        while (true) {
            sel.select();

            Iterator<SelectionKey> iterator = sel.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();

                if (key.isConnectable()) {
                    ByteBuffer buffer = ByteBuffer.allocate(Constants.WRITE_BUFFER);

                    SocketChannel client = (SocketChannel) key.channel();
                    buffer.put("Hello, this is from NIO client!".getBytes());
                    client.write(buffer);

                    buffer.clear();
                    client.read(buffer);
                    buffer.flip();

                    Charset charset = Charset.forName(Constants.CHARSET);
                    System.out.println(charset.decode(buffer).toString());

                }

            }

        }

    }
}
