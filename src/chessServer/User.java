package chessServer;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.Scanner;
import java.net.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Justin
 */
public class User implements Runnable{
    private Thread player = null;
    String name;
    public Boolean canChangeSettings = false;
    public BufferedReader in;
    public PrintWriter out;
    public BufferedReader defaultIn;
    public PrintWriter defaultOut;
    public Socket userSocket;

    
    
    public void start(){
        this.run();
    }
    
    @Override
    public void run(){
//        if(player == null){
//            player = new Thread(this, name);
//            player.start();
//        }
        System.out.println("User's name is: " + this.name);
        return;
    }
    
    enum PlayerColour {
        White, 
        Black
    }
    
}
