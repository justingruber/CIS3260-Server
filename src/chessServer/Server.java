package chessServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Justin
 */
public class Server{
    private Lobby lobby = new Lobby();
    private ArrayList<Thread> clientConnections = new ArrayList<>();
    
    public static void main(String[] args){
        Server server = new Server();
        server.initializeServer();
    }
    
    private void initializeServer(){
        String userInput;
        
        
        try{
            InetAddress addr = InetAddress.getLocalHost();
            String hostname = addr.getHostName();
            System.out.println("Hostname: " + hostname);
            System.out.println("IP: " + addr.getHostAddress());
            ServerSocket serverSocket = new ServerSocket(Server.PORT);
            while (true) {
                this.acceptClients(serverSocket);
            }
        }
        catch(Exception e){}
        
        
    }
    
    private void acceptClients(ServerSocket serverSocket) throws IOException{
        while(true){
            Socket clientSocket = serverSocket.accept();
            System.out.println("Connected from: " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
            
            Thread t = new Thread(){
                public synchronized void run() {
                    User client = null;
                    String userInput = "";
                    Boolean nameIsUnique = false;
                    PrintWriter out = null;
                    BufferedReader in = null;
                    try{
                        out = new PrintWriter(clientSocket.getOutputStream(), true);
                        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        out.println("Connected to Server.");
                        userInput = in.readLine();
                        try {

                            while (!nameIsUnique) {
                                nameIsUnique = true;
                                try{
                                    for (User c : lobby.clientList) {
                                        if (c.name.equals(userInput)) {
                                            nameIsUnique = false;
                                            out.println("Name has been taken. Enter a new one.");
                                            userInput = in.readLine();
                                        }
                                    }
                                }
                                catch(Exception e){}
                                
                            }
                            nameIsUnique = false;
                            client = new User();
                            client.name = userInput;
                            client.defaultIn = in;
                            client.defaultOut = out;
                            client.in = in;
                            client.out = out;
                            client.userSocket = clientSocket;
                            lobby.clientList.add(client);
                            client.start();
                            out.println("Welcome, " + userInput + "!");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        out.println("[1] Join room"
                                + "\n[2] Create Room");
                        
                        int choice = -1;
                        Boolean valid = false;
                        
                        while(!valid){
                            userInput = in.readLine();
                            choice = Integer.parseInt(userInput);
                            if(choice == 1){
                                lobby.showGameRooms(in, out, client);
                            }
                            else if(choice == 2){
                                lobby.createGameRoom(in, out, client);
                            }
                        }
                    }
                    catch(Exception e){}
                }
            };
            t.start();
            clientConnections.add(t);
        }
    }
    private static final int PORT = 8080;
}
