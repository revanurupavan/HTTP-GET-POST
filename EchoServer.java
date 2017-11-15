
package get_and_put;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;


/**
 *
 * @author Pavan
 */

public class EchoServer {

    public static void main(String[] args) {
        // TODO code application logic here

        //String str = null;
        int sendingPort     = 9000;
        int receivingPort   = 9001;
        
        try {
        	
            File receive;
            System.out.println("This is the Server Side");				//initializing the server
            byte[] buffer = new byte[1024];
            File file = new File ("C:\\Users\\pavanr\\eclipse-workspace\\trial\\test.txt");//location of test file stored
            BufferedReader b = new BufferedReader(new FileReader(file));
            String readLine = "";
            DatagramSocket serverSocket = new DatagramSocket(receivingPort);		//initializing the receiving port
            
            DatagramPacket serverReceive = new DatagramPacket(buffer, buffer.length);
            while (true) {

            serverSocket.receive(serverReceive);
            InetAddress IPAddress = serverReceive.getAddress();   
            String str = new String(buffer);
            System.out.println("Message from Client With IP " + IPAddress + " is "+str);
                
            if(str.contains("GET")) {							//function for operations to be performed for GET
            	if(str.contains("test.txt")) {
                	while((readLine = b.readLine() )!= null) {
                		byte[] tobytes = readLine.getBytes();
                		
                		DatagramPacket serverSend = new DatagramPacket(tobytes, tobytes.length, IPAddress, sendingPort);
                        	serverSocket.send(serverSend);
                        
                        	/////////////////////////////////////////////////////
                        	//Send back a HTTP 200 OK 
                        
                        	ServerSocket s1= new ServerSocket(5007);
                		Socket ss = s1.accept();
                		
                		// Get input and output streams to talk to the client
                		BufferedReader in = new BufferedReader(new InputStreamReader(ss.getInputStream()));//reading the GET response
                		PrintWriter out = new PrintWriter(ss.getOutputStream());
                		
                		// Start sending our reply, using the HTTP 1.1 protocol
                		out.print("HTTP/1.1 200 \r\n"); // Version & status code
                		out.print("Content-Type: text/plain\r\n"); // The type of data
                		out.print("Connection: close\r\n"); // Will close stream
                		out.print("\r\n"); // End of headers
                		out.print("Test");
                		String line;
                		while ((line = in.readLine()) != null) {
                		    if (line.length() == 0)
                		    	break;
                		        out.print(line + "\r\n");
                		}

                		        
                		out.close(); 							// Flush and close the output stream
                		in.close(); 							// Close the input stream
                        
                       		 ////////////////////////////////////////////////////
                        
                	}
                    
                	}else {
                		System.out.println("File not found");				//response if file not found
                		System.out.println("\n404");
                		//Send back a 404 response HTTP
                	}
                }
                
                else if(str.contains("PUT")) {							//operations to be performed for PUT request
                	String fileName = str.substring(4, str.lastIndexOf(" "));
                	String prt = str.substring(str.lastIndexOf(" "));
                	prt = prt.replaceAll(" ", "");
                	prt = prt.substring(0);
                	System.out.println("port =" + prt);					//opening the port mentioned in the command line
                	
                	
                	if(fileName == null) {
                		System.out.println("No file specified");			//if file not found
                		break;
                	}
                	System.out.println("File to be saved:" + fileName);
                	
                	byte[] buffer1 = new byte[32000];
                        DatagramPacket serverReceive1 = new DatagramPacket(buffer1, buffer1.length);
                        DatagramSocket clientSocket2 = new DatagramSocket(6400);		// socket at which the file is received
                        clientSocket2.receive(serverReceive1);
                        String dataToStr = new String(buffer1);
                                         
                    
                     try{
                    	 PrintWriter out = new PrintWriter(fileName);
                         out.println( dataToStr );						//printing the data in the file in string format
                     }catch(Exception e) {
                     	e.printStackTrace();
                     }
                                     	
                	System.out.println("File saved in the location:" + System.getProperty("user.dir") + "\\" + fileName + "\n200 OK");
                	clientSocket2.close();						       //successful cretion of text file
                	                	
                }

              
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
