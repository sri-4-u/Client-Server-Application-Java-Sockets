/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
//import java.util.ArrayList;
//import java.util.List;
/**
 *
 * @author srinivastanneru
 */


public class MultiClient extends Thread{
private Socket s=null;
private ServerSocket sock1=null;
//int numBytes=0;
InputStream input=null;
public final static int FILE_SIZE = 6022386;
	public MultiClient(Socket socket,ServerSocket sock1) {
		
		this.s=socket;
		this.sock1=sock1;
	}
	public void run() {
		FileInputStream fis = null;
	    BufferedInputStream bis = null;
	    OutputStream os = null;
	    //ServerSocket servsock = null;
	    //Socket sock = null;
	    int bytesRead;
	    int numBytes = 0;
	    FileOutputStream fos = null;
	    BufferedOutputStream bos = null;
		try {
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(s.getInputStream()));
			String clientinfo = inFromClient.readLine();
			String[] commands = clientinfo.split(":");
			String part1 = commands[0];
			String part2 = commands[1];
			
			switch(part1) {
			case "rmdir" :{
				File file=new File(part2);
				PrintStream out= new PrintStream(s.getOutputStream(),true);
				if(file.isDirectory())
					if(file.delete()) {
						out.println("Directory is removed");
					}
					else out.println("Directory cannot be removed");
				else out.println("Requested Directory is not in the Server");
				out.close();
				break;
				}
			
			case "rm" :{
				File file=new File(part2);
				PrintStream out=new PrintStream(s.getOutputStream(),true);
				if(file.isFile())
					if(file.delete()) {
						out.println("File is removed");
					}
						else {
							out.println("File cannot be removed");
							}
				else {
					out.println("File doesn't exist in Server");
				}
				out.close();
				break;
					}
			case "dir" :{
				
				File file1=new File(part2);
				if(file1.exists()) {
				File[] files=file1.listFiles();
				String list="";
				for(int i=0;i<files.length;i++) {
					if(files[i].isFile()) {
						list +=files[i].getName()+" ";
						}
					else if(files[i].isDirectory()) {
						list+=files[i].getName()+" ";
						}
					}
				
				    DataOutputStream outToClient=new DataOutputStream(s.getOutputStream());
				    outToClient.writeBytes(list+'\t');
				    outToClient.close();
				}
					else {
				   
				    	DataOutputStream outToClient1=new DataOutputStream(s.getOutputStream());
				    	outToClient1.writeBytes("No such Directory");
				    	outToClient1.close();
					}
				
				break;
			}
			
			case "mkdir" :{
				File file=new File(part2);
				if(!(file.exists()) ){
					if(file.mkdir()) {
						DataOutputStream outToClient=new  DataOutputStream(s.getOutputStream());
						outToClient.writeBytes("Directory is created");
					}
					else {
					PrintStream outToClient=new PrintStream(s.getOutputStream());
					outToClient.println("Directory is not created");
					}
				}
				else {
					PrintStream outToClient=new PrintStream(s.getOutputStream());
					outToClient.println("The name already exists");
				}
					
						break;
				}
			case "shutdown" :{
				sock1.close();
                                System.out.println("Server is shutting down ......");
                                break;
			}
			
			case "download" :{
				//Download_Files(part2);
                                String FILE_TO_SEND =part2;
          
                                File myFile = new File (FILE_TO_SEND);
                              if(myFile.exists()){
                                byte [] mybytearray  = new byte [(int)myFile.length()];
                                fis = new FileInputStream(myFile);
                                bis = new BufferedInputStream(fis);
                                bis.read(mybytearray,0,mybytearray.length);
                                os = s.getOutputStream();
                                System.out.println("Sending " + FILE_TO_SEND + "(" + mybytearray.length + " bytes)");
                                os.write(mybytearray,0,mybytearray.length);
                                os.flush();
                                System.out.println("Done");
                                
                              }
                              else {
                              DataOutputStream outToServer = new DataOutputStream(s.getOutputStream());
                              outToServer.writeBytes("Requested file does not exist");
                              }
			}
			
			case "upload" :{
				//Upload_Files(part2);
				
				DataOutputStream outToServer = new DataOutputStream(s.getOutputStream());
				String s1="absolute_path";
				outToServer.writeBytes(s1 + '\n');
				String FILE_TO_RECEIVED=part2;
				//resume upload
				File f=new File(part2);
				long fs=0;
				if(f.exists()) fs=f.length();
				
			      byte [] mybytearray  = new byte [FILE_SIZE];
			      InputStream is = s.getInputStream();
			      fos = new FileOutputStream(FILE_TO_RECEIVED);
			      bos = new BufferedOutputStream(fos);
			      bytesRead = is.read(mybytearray,0,mybytearray.length);
			      numBytes = bytesRead;

			      do {
			         bytesRead =
			            is.read(mybytearray, numBytes, (mybytearray.length-numBytes));
			         if(bytesRead >= 0) numBytes += bytesRead;
			      } while(bytesRead > -1);

			      bos.write(mybytearray, 0 , numBytes);
			      bos.flush();
			     System.out.println("File " + FILE_TO_RECEIVED + " uploaded (" + numBytes + " bytes read)");
				//String msg="filereceived";
				//outToServer.writeBytes(msg + '\n');
				System.out.println("FILE SUCCESSFULLY UPLOADED");
                                
			}
			
			}
				
			
			
		}
					
						
				
			
			
		
		 catch(IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();}
			finally {
				 if (bis != null) try {
                                     bis.close();
                                 } catch (IOException ex) {
                                     Logger.getLogger(MultiClient.class.getName()).log(Level.SEVERE, null, ex);
                                 }
		          if (os != null) try {
                              os.close();
                                 } catch (IOException ex) {
                                     Logger.getLogger(MultiClient.class.getName()).log(Level.SEVERE, null, ex);
                                 }
		      if (fos != null) try {
                          fos.close();
                                 } catch (IOException ex) {
                                     Logger.getLogger(MultiClient.class.getName()).log(Level.SEVERE, null, ex);
                                 }
		      if (bos != null) try {
                          bos.close();
                                 } catch (IOException ex) {
                                     Logger.getLogger(MultiClient.class.getName()).log(Level.SEVERE, null, ex);
                                 }
		          if (s!=null) try {
                              s.close();
                                 } catch (IOException ex) {
                                     Logger.getLogger(MultiClient.class.getName()).log(Level.SEVERE, null, ex);
                                 }
			}
		
	}

}

	

	
	




