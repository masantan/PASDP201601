import java.io.*;
import java.util.*;
import java.lang.*;
import java.net.*;

public class HTTP_Server{
    public static void main(String[] args) throws Exception {
	try {
            // socket creation
            int port = 1989;
            ServerSocket serverSocket = new ServerSocket(port);
            System.err.println("Server launched on port : " + port);
            // repeatedly wait for connections, and process
            while (true) {
                // you are stuck on waiting for a client request
                Socket clientSocket = serverSocket.accept();
                System.err.println("New connected client");
                Connection c = new Connection(clientSocket);            
            } 
        }
        catch(IOException e) {
            System.out.println("Listen :"+e.getMessage());
	}
    }
}

class Connection extends Thread {
    BufferedReader in;
    BufferedWriter out;
    Socket clientSocket;
    public Connection (Socket aClientSocket) {
        try {
            // opening a conversation flow
            clientSocket = aClientSocket;
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            this.start();
        }
        catch(IOException e) {
            System.out.println("Connection:"+e.getMessage());
        }
    }
    public void run() {
        try {
            // each time data is read from the network is the reference
            // on the writing flow. the read data is returned to exactly
            // the same client.

            String s;
            while ((s = in.readLine()) != null) {
                System.out.println(s);
                if (s.isEmpty()) {
                    break;
                }
            }

            out.write("HTTP/1.0 200 OK\r\n");
            out.write("Date: Fri, 31 Dec 1999 23:59:59 GMT\r\n");
            out.write("Server: Apache/0.8.4\r\n");
            out.write("Content-Type: text/html\r\n");
            out.write("Content-Length: 59\r\n");
            out.write("Expires: Sat, 01 Jan 2000 00:59:59 GMT\r\n");
            out.write("Last-modified: Fri, 09 Aug 1996 14:21:40 GMT\r\n");
            out.write("\r\n");
            out.write("<TITLE>Test</TITLE>");
            out.write("<P>This is an test page.</P>");
            out.write("<P> This is a paragraph test </P>");
            
            // the flow is closed.
            System.err.println("Connecting with the customer completed");
        }
        catch(IOException e) {
            System.out.println("IO:"+e.getMessage());
        }
        finally{
            try {
                out.close();
                in.close();            
                clientSocket.close();
            }
            catch (IOException e){
                System.out.println("IO:"+e.getMessage());
            }
        }
    }
}
