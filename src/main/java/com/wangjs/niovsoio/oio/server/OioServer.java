package com.wangjs.niovsoio.oio.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import com.wangjs.niovsoio.util.Constants;

public class OioServer {
	public static void main(String[] args) {
		try {
			new OioServer().startServer();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void startServer() throws IOException{
		ServerSocket serverSocket = new ServerSocket();
		serverSocket.bind(new InetSocketAddress(Constants.LISTENER_PORT));
		
		while(true){
			Socket socket = serverSocket.accept();
			dealSocket(socket);
		}

	}
	
	private void dealSocket(Socket socket){
		new SocketDeal(socket).start();
	}
	
	private class SocketDeal extends Thread{
		private Socket socket;
		
		public SocketDeal(Socket socket){
			this.socket = socket;
		}

		@Override
		public void run() {
			BufferedReader fromClient = null;
			PrintWriter toClient = null;
			try {
				fromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				toClient = new PrintWriter(socket.getOutputStream(),true);
				
				String line ;
				while((line = fromClient.readLine())!=null){
					System.out.println("from Client = "+line);
					toClient.print("Thanks");
					if("Bye".equalsIgnoreCase(line)){
						break;
					}
				}
					
				
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				if (fromClient!=null) {
					try {
						fromClient.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
				if (toClient!=null) {
					try {
						toClient.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				if (socket!=null) {
					try {
						socket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		
		
	}
}
