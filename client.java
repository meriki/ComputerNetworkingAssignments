import java.io.*;
import java.net.*;
import java.util.Scanner;

public class client{
	public static void main(String[] args){
		Socket sock = null;
		int port;
		if(args.length!=0){
			port = Integer.parseInt(args[0]);
		}
		else
			port =6665;
		try{
			sock = new Socket("localhost", port);
			// System.out.println("connected sock");
		}
		catch(Exception e){
			System.out.println("Error: System cannot connect to server");
			System.exit(0);
		}

		DataOutputStream output = null;
		DataInputStream input = null;
		try{
			output = new DataOutputStream(sock.getOutputStream());
			input = new DataInputStream(sock.getInputStream());
		}
		catch(Exception e){
			System.out.println("Error: output/input stream coulf not be obtained");
			System.exit(0);
		}

		while(true){
			try {
							
				output.writeUTF("Hello Server");
				output.flush();
				
				String in = (String)input.readUTF();
				System.out.println("Received from Server: " + in);
			}
			catch(Exception e){
				System.out.println("Error : cannot send data");
				System.exit(0);
			}
		}
	}
}