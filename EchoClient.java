
package get_and_put;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.Random;
import java.util.Scanner;


/**
 *
 * @author Pavan
 */


/*
I'm using a separate port for Sending and receiving.

For simplicity purpose!!

*/
public class EchoClient {
	
	public final static String USER_AGENT = "Mozilla/5.0";
    	public static void main(String args[]) throws IOException {

        int sendingPort     = 9001;
        int receivingPort   = 9000;
              
        try {
        	
        	File sendFile;
            System.out.println("This is the Client Side");

            System.out.println("Input:");
            Scanner sc = new Scanner(System.in);
	
	    // creating sockets

            DatagramSocket clientSocket1 = new DatagramSocket();
            DatagramSocket clientSocket2 = new DatagramSocket(receivingPort);
         
            String str = sc.nextLine();   
            byte[] strToBytes = str.getBytes();
            InetAddress IPAddress = InetAddress.getLocalHost();// gettin IP address
            DatagramPacket clientSend = new DatagramPacket(strToBytes, strToBytes.length, IPAddress, sendingPort);
            clientSocket1.send(clientSend);						// sending GET request to server
            if(str.contains("GET")) {    						// funtion for GET request            
            	byte[] buffer = new byte[32000];
                DatagramPacket clientReceive = new DatagramPacket(buffer, buffer.length);
                clientSocket2.receive(clientReceive);
                String dataToStr = new String(buffer);
                System.out.println("Echo reply from server: " + dataToStr);		// data from the text file is converted to string
                try(  PrintWriter out = new PrintWriter( "Client_file.txt" )  ){	//Creating a text file Client_file.txt 
                out.println( dataToStr );
                
		//////////////////////////////////////
                //reveive a 200 OK
                    
                String url = "http://127.0.0.1:5007";					//sending GET request to port 5007
            	URL obj = new URL(url);
            	HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            	// optional default is GET
            	con.setRequestMethod("GET");

            	//add request header
            	con.setRequestProperty("User-Agent", USER_AGENT);
           	int responseCode = con.getResponseCode();
            	System.out.println("\nSending 'GET' request to URL : " + url);
            	System.out.println("Response Code : " + responseCode);			//getting response code to HTTP request
            	BufferedReader in = new BufferedReader(
            	new InputStreamReader(con.getInputStream()));
            	String inputLine;
            	StringBuffer response = new StringBuffer();
            	while ((inputLine = in.readLine()) != null) {
            		response.append(inputLine);
            		}
            	in.close();

            	//print result
		System.out.println(response.toString());				//printing the response
                /////////////////////////////////////      
                }catch(Exception e) {
                	e.printStackTrace();
                }

                }    
                else if(str.contains("PUT")) {
                	
                	Thread.sleep(5000);
                	String fileName = str.substring(4, str.lastIndexOf(" "));	//accepting the file name
                	String prt = str.substring(str.lastIndexOf(" "));
                	//prt = str.substring(str.indexOf(" "));
                	prt = prt.replaceAll(" ", "");
                	int port = Integer.parseInt(prt);				//accepting port number from the command line
                	System.out.println("port =" + port);
                	System.out.println("Filename = " +fileName);
                	sendFile = new File(fileName);
                	try {
                	BufferedReader b = new BufferedReader(new FileReader(fileName));//sending the file
                	String readLine = "";
                	                    
                    while((readLine = b.readLine() )!= null) {
                		byte[] tobytes = readLine.getBytes();             		
                		DatagramPacket fileSend = new DatagramPacket(tobytes, tobytes.length, IPAddress, port);
                		clientSocket1.send(fileSend);          
                    }
                                    	
                }catch(Exception e) {
                	e.printStackTrace();
                }

                }
                
               
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}      

                
                
                
               
                
                
                
