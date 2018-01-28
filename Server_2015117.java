import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

class App extends Thread{
    Socket connection;
    int id;
    Boolean isRunning;
    App(Socket connection, int id){
        super();
        this.connection = connection;
        this.id=id;
        this.isRunning = true;
        System.out.println("Client "+ this.id +" connected");
    }

    void writeToClient(int x, String msg){
        if(this.id!=x){
            DataOutputStream output = null;
            try{
                if(Server_2015117.connections.get(x).isRunning == true){
                    output = new DataOutputStream(Server_2015117.connections.get(x).connection.getOutputStream());
                    output.writeUTF(msg);
                    output.flush();
                }                
            }
            catch(IOException e2){
                System.out.println("Client " + Server_2015117.connections.get(x).id + "disconnected ");
                Server_2015117.connections.get(x).isRunning = false;
            }  
            catch(IndexOutOfBoundsException e){                
                giveErrorMessage("Client " + x + " doesn't exist");
            }
                
        }
        else if(Server_2015117.connections.get(x).isRunning == true){
            giveErrorMessage(x + " is self");
        }      
    }

    void writeToAllClients(String msg){
        for(App connect: Server_2015117.connections){
            if(this.id!=connect.id && connect.isRunning == true){
                DataOutputStream output = null;
                try{
                    output = new DataOutputStream(connect.connection.getOutputStream());
                    output.writeUTF(msg);
                    output.flush();
                }
                catch(Exception e){
                    System.out.println("Client " + connect.id + " disconnected ");
                    connect.isRunning = false;
                }  
            }
        }
    }

    void listAll(){
        for(App connect: Server_2015117.connections){
            if(connect.isRunning == true){
                String out = "";
                DataOutputStream output = null;
                try{
                    output = new DataOutputStream(this.connection.getOutputStream());
                    out = "" + connect.id;
                    output.writeUTF(out);
                    output.flush();
                }
                catch(Exception e){
                    System.out.println("Client " + this.id + "disconnected ");
                    return;
                }  
            }            
        }
    }

    void giveErrorMessage(String msg){
        DataOutputStream output = null;
        try{
            output = new DataOutputStream(this.connection.getOutputStream());
            output.writeUTF(msg);
            output.flush();
        }
        catch(Exception e){
            System.out.println("Client " + this.id + "disconnected ");
            return;
        }  
    }

    public void run(){
        DataInputStream input = null;
        while(true){
            try {   
                input = new DataInputStream(connection.getInputStream());
                String in = (String)input.readUTF();
                String[] words = in.split(" ");
                String[] msg = null;
                String out = "";

                if(in.equals("List All")){
                    this.listAll();
                }
                else if(words[0].equals("All:")){
                    msg = Arrays.copyOfRange(words,1,words.length);
                    for (String word : msg){
                       out = out + " " + word;
                    }
                    out = "Client " + this.id + " sent: " + out;                    
                    this.writeToAllClients(out);
                }
                else if(words[0].equals("Client") && words[1].indexOf(',')<0){
                    String stri = words[1].replaceAll("[^-?0-9]+", ""); 
                    int to = Integer.parseInt(stri);
                    msg = Arrays.copyOfRange(words,2,words.length);
                    for (String word : msg){
                        out = out + " " + word;
                    }
                    out = "Client " + this.id + " sent: " + out;
                    this.writeToClient(to,out);
                }
                else if(words[0].equals("Client") && words[1].indexOf(',')>=0){
                    String[] numbers = words[1].split(",");
                    for(String number: numbers){
                        out = "";
                        msg = Arrays.copyOfRange(words,2,words.length);                        
                        for (String word : msg){
                            out = out + " " + word;
                        }
                        out = "Client " + this.id + " sent: " + out;
                        this.writeToClient(Integer.parseInt(number.replaceAll("[^-?0-9]+", "")),out);
                    }
                }
                else{
                    giveErrorMessage("Format not valid");
                }
            } catch (IOException e) {
                System.out.println("Client " + this.id + " disconnected ");
                this.isRunning = false;
                return;
             }
        }
    }
}

public class Server_2015117{

    public static ArrayList<App> connections = new ArrayList<App>();

    public static void main(String[] args) {
        int port;
        if(args.length!=0)
            port = Integer.parseInt(args[0]);
        else
            port = 6665;

        ServerSocket socket_obj = null;
        try {
            socket_obj = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("error : socket cannot not attached to given port " + port);
            System.exit(0);
        }        

        int i = 0;
        while (true) {
            try {
                Socket temp = socket_obj.accept();
                App y = new App(temp,i);
                connections.add(y);
                connections.get(i).start();
                i++;
            }
            catch(Exception e){
                System.out.println(i);
            }
        }
    }
}

