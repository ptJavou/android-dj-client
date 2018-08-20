package com.example.ramoncosta.android_dj_beta;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ResourceBundle;

public class SocketClient {

	private final int SERVER_PORT = 3330;
	private String serverIp;
	//private ResourceBundle bundle = ResourceBundle.getBundle("config");

	public SocketClient(String serverIp){
		this.serverIp = serverIp;
	}

	public Socket getSocket() throws IOException{
		InetAddress inetAddress = InetAddress.getByName(serverIp);
		Socket socket = new Socket(inetAddress, SERVER_PORT);
		
		return socket;
	}
	
}
