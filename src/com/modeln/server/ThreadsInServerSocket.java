/**
 * WORKING
 */

package com.modeln.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.Executors;

public class ThreadsInServerSocket{

    int port, currentConnection = 0;

    public ThreadsInServerSocket(int port){
        this.port = port;
    }

    static int maxConnections;

    public static void main(String[] args) throws IOException{
        new ThreadsInServerSocket(9993).connectToServer();
    }

    public void connectToServer() throws IOException{
        ServerSocket ss = new ServerSocket(port);
        if(maxConnections == -1)
            maxConnections = Integer.MAX_VALUE;
        else if(maxConnections == 0)
            maxConnections = 2;
        while(true){
            System.out.println("First this. "  + currentConnection);
            Socket s = null;
            try{
                if(currentConnection < maxConnections) {
                    System.out.println("In this. "  + currentConnection);
                    s = ss.accept();
                    DataInputStream dis = new DataInputStream(s.getInputStream());
                    DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                    Executors.newCachedThreadPool().execute(new ClientHandler(s, dis, dos, this));
                    currentConnection++;
                }else{
                    Thread.sleep(10000);
                }

            }catch(Exception e){
                s.close();
                e.printStackTrace();
            }
        }
    }

}

class ClientHandler implements Runnable{
    final DataInputStream dis;
    final DataOutputStream dos;
    final Socket s;
    final ThreadsInServerSocket t;

    public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos, ThreadsInServerSocket t)
    {
        this.s = s;
        this.dis = dis;
        this.dos = dos;
        this.t = t;
    }

    @Override
    public void run() {
        System.out.println("In client");
        String received;
        Scanner s  = new Scanner(dis, "UTF-8");
        try {
            dos.writeUTF("Welcome !!. Type exit to exit\n");
            while(true && s.hasNextLine()){
                received = s.nextLine();
                if(received.equalsIgnoreCase("exit")){
                    System.out.println("Bye");
                    dos.writeUTF("See you later! BYE!!!");
                    s.close();
                    break;
                }
                dos.writeUTF("\nYou wrote: " + received + "\n");
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        try{
            t.currentConnection--;
            System.out.println(t.currentConnection);
            s.close();
            dis.close();
            dos.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}