/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import java.net.*;
/**
 *
 * @author srinivastanneru
 */

public class server {
public static void main(String[] args ) throws IndexOutOfBoundsException,IOException {
	ServerSocket service=null;
	if(args[0].equals("start")){ 
		int PORT=Integer.parseInt(args[1]);
	
	try {
	 service=new ServerSocket(PORT);
	//boolean stop=false;
	while(!(service.isClosed())) {
		System.out.println("Server started......");
                System.out.println("Waiting for the client -......");
		Socket sock=service.accept();
		System.out.println("Connected to "+" "+sock.getInetAddress().getHostName());
		
		MultiClient clientThread=new MultiClient(sock,service);
		clientThread.start();
		
		//BufferedReader in=new BufferedReader(new InputStreamReader(sock.getInputStream()));
		//String s=in.readLine();
		//if(s.equals("shutdown")) {
		//	stop =true;
		//}
	}
	}
        catch(IOException e)
        {
            System.out.println("Port is already opened! Please use another port.");
        }
		 catch(Exception e)
	        {
	            System.out.println(e.toString());
	            //System.out.println("Socket in Use");
	        }
		
	}
	
	else 
	System.out.println("Commmand not found..Please enter start along with your portnumber to get going");
}
}