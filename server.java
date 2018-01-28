import java.net.*;
import java.io.*;
import java.util.ArrayList;

class App extends Thread{
    Socket connection;
    int id;
    App(Socket connection, int id){
        super();
        this.connection = connection;
        this.id=id;
        System.out.println("Client "+this.id +" connected");
    }

    public void run(){

        DataOutputStream output = null;
        DataInputStream input = null;

        try{
            input = new DataInputStream(connection.getInputStream());
            output = new DataOutputStream(connection.getOutputStream());
        }
        catch(Exception e){
            System.out.println("Client " + this.id + "disconnected ");
            return;
        }

        while(true){
            try {                
                String in = (String)input.readUTF();
                System.out.println("Received from Client " + this.id + ": " + in);
                String[] words = in.split(" ");
                String out = "";
                for (String word : words){
                    out = word + " " + out;
                }
                output.writeUTF(out);
                output.flush();

            } catch (IOException e) {
                 System.out.println("Client " + this.id + " disconnected ");
                return;
             }
        }
    }
}

public class server{
    public static void main(String[] args) {
        int port  = Integer.parseInt(args[0]);
        ServerSocket socket_obj = null;
        try {
            socket_obj = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("error : socket cannot not attached to port");
            System.exit(0);
        }

        ArrayList<App> connections = new ArrayList<App>();

        int i = 0;
        while (true) {
            try {
                Socket temp = socket_obj.accept();
                App y = new App(temp,i);
                connections.add(y);
                connections.get(i).start();
                i++;
                System.out.println("Main" + i);
            }
            catch(Exception e){
                System.out.println(i);
            }
        }
    }
}