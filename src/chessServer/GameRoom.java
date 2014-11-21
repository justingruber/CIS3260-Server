package chessServer;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.Scanner;
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



public class GameRoom {
    public String name;
    private User host;
    private User playerTwo;
    Array settings;
    State gameState;
//    Type gameType;
    private ArrayList<User> currentUsers = new ArrayList<User>();

    public GameRoom(String roomName){
        this.name = roomName;
    }
    public GameRoom(String roomName, User hostName){
        this.name = roomName;
        this.host = hostName;
    }
    
    public void initGame(BufferedReader in, PrintWriter out, User client){
        client.canChangeSettings = true;
        currentUsers.add(client);
        this.gameState = GameRoom.State.NOT_STARTED;
        out.println("Game created. Waiting for another player...");
    }
    
    public void joinGame(BufferedReader in, PrintWriter out, User client){
        if(currentUsers.size() == 1){
            currentUsers.add(client);
            playerTwo = client;
            this.gameState = GameRoom.State.STARTED;
            startGame();
        }
        else{
            initGame(in,out,client);
        }
    }
    
    private void startGame(){
        //Need to start game from client here
        host.defaultOut.println("Another player has joined.");
        for(User c: currentUsers){
            c.defaultOut.println("Game is starting.");
        }
        host.out = new PrintWriter(System.out);
        playerTwo.out = new PrintWriter(System.out);
//        out = new PrintWriter(clientSocket.getOutputStream(), true);
//        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        Thread playerTwoSendInfoToHost = new Thread(){
          public synchronized void run(){
              while(true){
                  try{
//                      host.out.println(playerTwo.in.readLine());
                  }
                  catch(Exception e){
                  }
              }
          }  
        };
        playerTwoSendInfoToHost.start();
        
        Thread hostSendInfoToPlayerTwo = new Thread() {
            public synchronized void run() {
                while (true) {
                    try {
//                        playerTwo.out.println(host.in.readLine());
                    } catch (Exception e) {
                    }
                }
            }
        };
        hostSendInfoToPlayerTwo.start();
        
    }
    
    private void quitGame(User client){
        if(client == host){
            client.canChangeSettings = false;
            currentUsers.remove(client);
            host = playerTwo;
            host.canChangeSettings = true;
        }
        else{
            currentUsers.remove(client);
        }
    }
    
    
    public enum State{
        NOT_STARTED,
        STARTED,
        GAME_OVER
    }
    
    
}
