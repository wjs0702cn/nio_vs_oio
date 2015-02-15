package com.wangjs.niovsoio.oio.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import com.wangjs.niovsoio.util.Constants;

public class Client {
	public static void main(String[] args) {
		Client client = new Client();
		client.sendData();
	}
	
	public void sendData(){
		Socket socket = null;
		PrintWriter pw = null;
		BufferedReader br = null;
		try{
			socket = new Socket(Constants.SERVER_HOST,Constants.LISTENER_PORT);
			pw = new PrintWriter(socket.getOutputStream());
			pw.append("Hello!");
			pw.flush();
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String line;
			while((line=br.readLine())!=null){
				System.out.println("from server = "+line);
			}
			pw.append("Hello! again");
			pw.flush();
//			while((line=br.readLine())!=null){
//				System.out.println("from server = "+line);
//			}

			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if (socket!=null) {
				try {
					if(pw!=null){
						try {
							pw.close();
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
					if(br!=null){
						try{
							br.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					socket.close();
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	
	}
}
