package chessServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
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
public class Lobby implements Runnable{
    private ArrayList<GameRoom> gameList = new ArrayList<>();
    public ArrayList<User> clientList = new ArrayList<>();
    public void start(){
    }
    
    @Override
    public void run(){
        
    }
    
    public ArrayList getUsers(){
        return null;
    }
    public void removeUser(User client){
        for (User c : clientList) {
            if (c == client) {
                clientList.remove(c);
            }
        }
    }
    
    public void showGameRooms(BufferedReader in, PrintWriter out, User client) throws IOException{
        int i = 1;
        String userInput;
        int gameRoomChoice = -1;
        Boolean gameChoiceValid = false;
        if(!gameList.isEmpty()){
            for(GameRoom r : gameList) {
                out.println("[" + i + "]" + r.name +  " Status: "+ r.gameState.toString());
            }
            userInput = in.readLine();
            while(!gameChoiceValid){

                try{
                    gameRoomChoice = Integer.parseInt(userInput);
                    GameRoom tmpRoom = gameList.get(gameRoomChoice - 1);
                    
                    if(tmpRoom == null){
                        out.println("Option " + gameRoomChoice + " is not a valid room. Enter a new room number.");
                        userInput = in.readLine();
                    }
                    else{
                        out.println("Joining game...");
                        gameChoiceValid = true;
                        tmpRoom.joinGame(in, out, client);
                    }
                }
                catch(NumberFormatException e){
                    out.println("Number was not entered. Enter a valid number.");
                    userInput = in.readLine();
                }
                catch(NullPointerException ex){
                    System.out.println("Null");
                }
            }
            
        }
        else{
            out.println("No games are currently being played. Would you like to create a new one? Y/N");
            userInput = in.readLine();
            while(!userInput.equalsIgnoreCase("y") && !userInput.equalsIgnoreCase("n")){
                out.println("Please enter Y or N");
                userInput = in.readLine(); 
            }
            if(userInput.equalsIgnoreCase("y")){
                createGameRoom(in, out, client);
            }
            else{
                out.println("Goodbye");
                removeUser(client);
                return;
            }
        }
        
    }
    
    public void createGameRoom(BufferedReader in, PrintWriter out, User client){
        String roomName = "";
        GameRoom room = null;
        
        try{
            out.println("Enter name of the room:");
            roomName = in.readLine();
            room = new GameRoom(roomName, client);
            gameList.add(room);
            room.initGame(in, out, client);
        } catch (Exception e){}
        
    }
    
}
