import java.io.*;
import java.net.*;
import java.util.Scanner;

class Listen extends Thread{
	Socket connection;
	Listen(Socket connection){
		super();
		this.connection = connection;
	}
	public void run(){
		DataInputStream input = null;
		try{
			input = new DataInputStream(this.connection.getInputStream());
		}
		catch(Exception e){
			// System.out.println("Error: input stream could not be obtained");
			e.printStackTrace();
			System.exit(0);
		}
		while(true){
			try{
				String in = (String)input.readUTF();
				System.out.println(in);
			}
			catch(Exception e){
				System.out.println("Error: Can't receive data");
				System.exit(0);
			}
		}
	}
}

class Send extends Thread{
	Socket connection;
	Send(Socket connection){
		super();
		this.connection = connection;
	}
	public void run(){
		DataOutputStream output = null;
		try{
			output = new DataOutputStream(this.connection.getOutputStream());
		}
		catch(Exception e){
			System.out.println("Error: output stream could not be obtained");
			System.exit(0);
		}
		BufferedReader bi = new BufferedReader(new InputStreamReader(System.in));
		String out = null;
		
		while(true){
			try{
				out = bi.readLine();
				output.writeUTF(out);
				output.flush();
			}
			catch(Exception e){
				System.out.println("Error: Can't send data");
				System.exit(0);
			}
		}
	}
}

public class Client_2015117{
	public static void main(String[] args){
		Socket sock = null;
		int port;
		if(args.length!=0){
			port = Integer.parseInt(args[0]);
		}
		else
			port = 6665;
		try{
			sock = new Socket("localhost", port);
		}
		catch(Exception e){
			System.out.println("Error: System cannot connect to server");
			System.exit(0);
		}

		Listen listen = new Listen(sock);
		Send send = new Send(sock);

		listen.start();
		send.start();

		try{
		listen.join();
		send.join();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}