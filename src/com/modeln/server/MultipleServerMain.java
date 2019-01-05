package com.modeln.server;

import java.io.IOException;
import java.util.concurrent.Executors;

public class MultipleServerMain{
    public static void main(String[] args) throws IOException {
        int[] ports = new int[]{9991, 9990};
        for(int i=0; i< ports.length; i++) {
            System.out.println(ports[i] + " and " + ports.length);
            Executors.newCachedThreadPool().execute(new MultipleServers(ports[i]));
        }
    }
}

class MultipleServers implements Runnable{

    final int port;

    public MultipleServers(int port){
        this.port = port;
    }

    @Override
    public void run() {
        try {
            new ThreadsInServerSocket(port).connectToServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}