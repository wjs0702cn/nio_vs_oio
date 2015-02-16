package com.wangjs.niovsoio.oio.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.wangjs.niovsoio.util.Constants;

public class OioClient {
    public static void main(String[] args) {
//        OioClient client = new OioClient();
//        client.sendData();
    	
    	ExecutorService pools = Executors.newFixedThreadPool(10);
    	while(true){
    		pools.execute(new Runnable() {
				
				@Override
				public void run() {
					new OioClient().sendData();
				}
			});
    	}
    	
    	
    }

    private List<String> greetingWords = new ArrayList<String>();

    {
        greetingWords.add("Hello, Server!");
        greetingWords.add("Hello, Server! again!");
        greetingWords.add("bye");
    }

    public void sendData() {
        Socket socket = null;
        PrintWriter toServer = null;
        BufferedReader fromServer = null;
        try {
            socket = new Socket(Constants.SERVER_HOST, Constants.LISTENER_PORT);
            toServer = new PrintWriter(socket.getOutputStream(), true);
            fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;

            for (String greeting : greetingWords) {
                toServer.println(greeting);
                line = fromServer.readLine();
                System.out.println("from server = " + line);
            }


        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    if (toServer != null) {
                        try {
                            toServer.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                    if (fromServer != null) {
                        try {
                            fromServer.close();
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
