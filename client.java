/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

/**
 *
 * @author srinivastanneru
 */
public class client {
    public final static int FILE_SIZE = 6022386;
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException,NullPointerException{
	  
   // int bytesRead;
    //int fileposition = 0;
    FileOutputStream fos = null;
    BufferedOutputStream bos = null;
    //FileInputStream fis = null;
    BufferedInputStream bis = null;
    OutputStream os = null;
    Socket sock = null;
    
    String env=System.getenv("PA1_SERVER");
	  String[] parts=env.split(":");
	  String server=parts[0];
	  int port=Integer.parseInt(parts[1]);
          //String args[2]=null;
          String part1=args[0];
                  String part2=args[1];
                  /*if(!args[2]!=null)
                      String part3=args[2];*/
	  
	  try {
		   sock=new Socket(server,port);
		  
		  
		  
        switch(part1) {
	case "upload" :{
			     Upload_Files(args[1], sock, args[2]);
                            break;
			  	
		  }
        case "download" :{
                            Download_Files(args[1], sock, args[2]);
                            break;
                        }
        case "dir" :{
                        System.out.println("Directory function begins");
                            List_Files(part2,sock);
                            break;
                    }
        case "mkdir" :{
                        Make_Directory(part2,sock);
                            break;
                       }
        case "rmdir" :{
                        Remove_Directory(part2,sock);
                            break;
                      }
        case "rm" : {
                        Remove_Files(part2,sock);
                            break;
                    }
        case "shutdown" :{
                         Shut_Down(sock);
                            break;
                         }
        default:    {
                        System.out.println("Please start again");
                    }
        }
	  }
	  catch(IOException e) {
		  e.printStackTrace();
	  }
          finally{
                    if (fos != null) fos.close();
                    if (bos != null) bos.close();
                    if (bis != null) bis.close();
                    if (os != null) os.close();
                    if (sock != null) sock.close();
                    }
          
  }
    private static void Make_Directory(String string,Socket sock) throws IOException, ClassNotFoundException,NullPointerException,ArrayIndexOutOfBoundsException {
	String sendToServer="mkdir:";
	sendToServer=sendToServer.concat(string);
	
	//ObjectOutputStream outToServer=new ObjectOutputStream(outStream);
	//outToServer.writeObject(sendToServer);
        DataOutputStream outToServer=new DataOutputStream(sock.getOutputStream());
	outToServer.writeBytes(sendToServer+ '\n');
	
	//DataInputStream inFromServer=new DataInputStream(sock.getInputStream());
	//String input=inFromServer.readLine();
	//ObjectInputStream inFromServer=new ObjectInputStream(inStream);
       BufferedReader inFromServer=new BufferedReader(new InputStreamReader(sock.getInputStream()));
	String input=(String)inFromServer.readLine();
	System.out.println(string+":" +" "+ input);
	inFromServer.close();
	outToServer.close();// TODO Auto-generated method stub
	
}
private static void List_Files(String string, Socket sock) throws IOException,NullPointerException {
	
	String sendserver="dir:";
	sendserver=sendserver.concat(string);
	  DataOutputStream outToServer = new DataOutputStream(sock.getOutputStream());
	outToServer.writeBytes(sendserver + '\n');
        
	 BufferedReader inFromServer = new BufferedReader(new InputStreamReader(sock.getInputStream()));
	String listOfFiles = inFromServer.readLine();
        
	if(!listOfFiles.equals("No such Directory")){
	System.out.println("NO ERROR");
	File file = new File(string);
	String parentPath = file.getAbsolutePath();
	System.out.print("The Parent Path is "+parentPath+" "+'\n');
	System.out.println("Files "+":"+listOfFiles);}
        else
            System.out.println(listOfFiles);
	inFromServer.close();
	
		
	// TODO Auto-generated method stub
	
}
public static void Remove_Files(String string, Socket sock) throws IOException{
	String sendToServer="rm:";
	sendToServer=sendToServer.concat(string);
	
	DataOutputStream outToServer=new DataOutputStream(sock.getOutputStream());
	outToServer.writeBytes(sendToServer+'\n');
	
	BufferedReader inFromServer=new BufferedReader(new InputStreamReader(sock.getInputStream()));
	String input=inFromServer.readLine();
	System.out.println(string+":"+" " + input);
	outToServer.close();
	inFromServer.close();// TODO Auto-generated method stub
	
}
public static void Remove_Directory(String s,Socket sock) throws IOException{
	  String sendToServer="rmdir:";
	  sendToServer=sendToServer.concat(s);
	  DataOutputStream outToServer=new DataOutputStream(sock.getOutputStream());
	  outToServer.writeBytes(sendToServer+ '\n');
	  
	  BufferedReader inFromServer=new BufferedReader(new InputStreamReader(sock.getInputStream()));
	  String input=inFromServer.readLine();
	  System.out.println(s+":"+" "+ input);
	  
	  inFromServer.close();
	  outToServer.close();
	  
  }
public static void Shut_Down(Socket sock) throws IOException{
	 String sendToServer="shutdown:";
	  sendToServer=sendToServer.concat("shutdown");
	  //PrintWriter out=new PrintWriter()
	  System.out.println("Sevrer has been shut down...");
	  DataOutputStream outToServer=new DataOutputStream(sock.getOutputStream());
	  outToServer.writeBytes(sendToServer);
	  outToServer.close();
}
public static void Download_Files(String part2,Socket sock,String part3) throws IOException, InterruptedException{
                            String sendserver="download:";
                            sendserver=sendserver.concat(part2);
                            DataOutputStream outToServer = new DataOutputStream(sock.getOutputStream());
                                outToServer.writeBytes(sendserver + '\n');
                                
                                File f=new File(part3);
                                long fs=0;
                                if(f.exists()) fs=f.length();

                                String FILE_TO_RECEIVED =part3;	
                                System.out.println("Error-0- NO ERROR");
                              
                                byte [] mybytearray  = new byte [FILE_SIZE];
                                InputStream is = sock.getInputStream();
                                FileOutputStream fos = new FileOutputStream(FILE_TO_RECEIVED);
                                BufferedOutputStream bos = new BufferedOutputStream(fos);
                                int bytesRead = is.read(mybytearray,0,mybytearray.length);
                                int fileposition = bytesRead;

                                do {
                                   bytesRead =is.read(mybytearray, fileposition, (mybytearray.length-fileposition));
                                        if(bytesRead >= 0) 
                                            fileposition += bytesRead;
                                    } while(bytesRead > -1);

                                bos.write(mybytearray, 0 , fileposition);
                                bos.flush();
                                  System.out.print("      downloaded 	...");
                                  for (int i=0;i<=100;i++){ 
                                      System.out.print("\r"+i+"%");  
                                      Thread.sleep(30);
                                  }
                                  System.out.println();
                                System.out.println("File " + FILE_TO_RECEIVED
                                    + " downloaded (" + fileposition + " bytes read)");
}
public static void Upload_Files(String part2,Socket sock,String part3) throws IOException, InterruptedException{
                            String sendserver="upload:";
                            sendserver=sendserver.concat(part3);
                            DataOutputStream outToServer = new DataOutputStream(sock.getOutputStream());
                            outToServer.writeBytes(sendserver + '\n');
                            String FILE_TO_SEND=part2;
	
	
                            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                            String servermsg = inFromClient.readLine();
                            if(servermsg.equals("absolute_path")){

	
                            File myFile = new File (FILE_TO_SEND);
                            byte [] mybytearray  = new byte [(int)myFile.length()];
                                FileInputStream fis = new FileInputStream(myFile);
                                BufferedInputStream bis = new BufferedInputStream(fis);
                            bis.read(mybytearray,0,mybytearray.length);
                                OutputStream os = sock.getOutputStream();
                            System.out.println("Sending " + FILE_TO_SEND + "(" + mybytearray.length + " bytes)");
                            os.write(mybytearray,0,mybytearray.length);
                            os.flush();
                            System.out.print("     uploaded 	...");
                            for (int i=0;i<=100;i++){ 
                                System.out.print("\r"+i+"%");  
                                Thread.sleep(30);
                            }
                            System.out.println();
                            System.out.println("File " + FILE_TO_SEND+ " uploaded");
                                                         
                            System.out.println("Error 0- NO ERROR");
                            }
}
}
