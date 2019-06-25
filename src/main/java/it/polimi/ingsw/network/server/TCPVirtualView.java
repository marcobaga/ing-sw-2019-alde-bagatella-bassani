package it.polimi.ingsw.network.server;

import com.google.gson.*;
import it.polimi.ingsw.view.ClientModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

/**
 * Implementation of VirtualView communicating through a socket
 *
 * @author marcobaga
 */
public class TCPVirtualView extends VirtualView {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private boolean waiting;
    private String answer;


    public TCPVirtualView(Socket socket){
        super();
        this.socket = socket;
        this.waiting = false;
        this.answer = "default";
    }

    /**
     * Initializes the connection (this happens on a separate thread from the main one so that the server can manage multiple requests in short time)
     */
    @Override
    public void run (){
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
            socket.setSoTimeout(100);
            super.run();
        }catch(Exception ex){
            LOGGER.log(Level.SEVERE, "Cannot create TCPVirtualView", ex);
            suspend();
        }
    }

    /**
     * Forwards the messages through the connection and receives them. Detects disconnected clients and allows for non-blocking send and receive methods.
     */
    @Override
    public synchronized void refresh() {
        if(!suspended) {
            try {
                String message = in.readLine();
                if (message == null) {
                    suspend();
                } else {
                    LOGGER.log(Level.FINE, "Received a message over TCP connection");
                    if(waiting){
                        answer = message;
                        waiting = false;
                        return;
                    }
                    if(timeout){
                        timeout = false;
                        if(System.currentTimeMillis()>timestamp) {
                            return;
                        }
                    }
                    if(!busy) return;
                    busy = false;
                    notifyObservers(message);
                }
            } catch (SocketTimeoutException ex) {
                LOGGER.log(Level.FINEST, "No incoming message from TCPVirtualView", ex);
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, "Cannot reach client", ex);
                suspend();
            }
        }
    }

    @Override
    public void choose(String msg, List<?> options){
        if(busy){
            return;
        }
        busy = true;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("head", "OPT");
        jsonObject.addProperty("text", msg);

        JsonArray array = new JsonArray();
        for (Object o : options) {
            array.add(new JsonPrimitive(o.toString()));
        }
        jsonObject.add("options", array);

        send(jsonObject);
    }

    @Override
    public void choose(String msg, List<?> options, int timeoutSec){
        choose(msg, options);
        timeout = true;
        timestamp = timeoutSec*1000 + System.currentTimeMillis();
    }

    @Override
    public int chooseNow(String msg, List<?> options){
        choose(msg, options);
        waiting = true;
        return Integer.parseInt(receive());
    }

    @Override
    public void display(String msg){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("head", "MSG");
        jsonObject.addProperty("text", msg);
        send(jsonObject);
    }

    @Override
    public String getInputNow(String msg, int max){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("head", "REQ");
        jsonObject.addProperty("text", msg);
        jsonObject.addProperty("length", String.valueOf(max));
        send(jsonObject);
        waiting = true;
        return receive();
    }

    /**
     * Receives a message from the client (BLOCKING)
     *
     * @return              the message received
     */
    private String receive() {
        while(waiting){
            refresh();
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException ex) {
                LOGGER.log(Level.INFO, "Skipped waiting time.");
                Thread.currentThread().interrupt();
            }
        }
        return answer;
    }

    /**
     * Sends a message serialized as a JsonObject through the socket
     *
     * @param jmessage  message to send
     */
    private void send (JsonObject jmessage){
        try {
            synchronized (game.getNotifications()){
                game.getNotifications().remove(this);
            }
        }catch(NullPointerException ex){
            LOGGER.log(Level.FINEST, "No old notifications to remove", ex);
        }
        out.println(jmessage.toString());
        out.flush();
        LOGGER.log(Level.FINE, "Sending a message over TCP connection");
    }

    @Override
    public void update (JsonObject jsonObject){
        send(jsonObject);
    }

    @Override
    public void suspend(){
        super.suspend();
        try {
            socket.close();
        }catch (IOException ex){
            //manage
        }
    }

    @Override
    public void shutdown(){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("head", "KILL");
        send(jsonObject);
    }
}