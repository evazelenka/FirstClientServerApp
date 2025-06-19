package Server;

import Client.ClientGUI;
import DB.ClientDataBase;
import DB.DataBase;
import Exceptions.PortException;
import Exceptions.ServerIsDownException;
import Exceptions.WrongServerException;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import Client.Client;

public class Server {

    private static final String SERVER = "127.0.0.1";
    private final String[] ports = {"80", "17", "84"};
    private String[] busyPorts = {"0", "0", "0"};
    public static boolean isServerWorking = false;
    private static ServerView view;
    private static DataBase db;

    private static List<Client> clientsOnline = new ArrayList<>();

    public Server(ServerView view){
        Server.view = view;
        db = new ClientDataBase();
    }

    public boolean checkServer(String server, String port, Client client) throws RuntimeException {
        try {
            if( isServerRight(server) & checkWork() & isPortRight(port) & isPortFree(port)){
                return true;
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
        return false;
    }

    private boolean checkLogin(Client client){

    }

    public boolean connectToServer(Client client){
        if(checkServer(client.getIp(), client.getPort(), client)){

        }
        if(!clientsOnline.contains(client)){
            clientsOnline.add(client);
        }
        ServerWindow.appendLog("\n" + client.getName() + " connected to server\n" + readInChat());
    }

    private static boolean isServerRight(String server) throws WrongServerException {
        if(SERVER.equals(server)){
            return true;
        }else throw new WrongServerException("server not found\ntry another\n");
    }

    public static void disconnect(Client client, boolean isClientGuiClosed){
        for (int i = 0; i < clientsOnline.size(); i++) {
            if(client.equals(clientsOnline.get(i))){
                if(!isClientGuiClosed) client.disconnect(false);
//                clientsOnline.get(i).setLogged(false);
                ServerWindow.appendLog("\n" + client.getNickName() + " disconnected from server\n");
            }
        }
        for (int i = 0; i < busyPorts.length; i++) {
            if(client.getPort().equals(busyPorts[i])){
                busyPorts[i] = "0";
            }
        }
    }

    public static void disconnect(Client client){
        for (int i = 0; i < clientsOnline.size(); i++) {
            if(client.equals(clientsOnline.get(i))){
//                if(!isClientGuiClosed) client.disconnectFromServer(false);
//                clientsOnline.get(i).setLogged(false);
                ServerWindow.appendLog("\n" + client.getName() + " disconnected from server\n");
            }
        }
        for (int i = 0; i < busyPorts.length; i++) {
            if(client.getPort().equals(busyPorts[i])){
                busyPorts[i] = "0";
            }
        }
    }

    private static boolean checkWork() throws ServerIsDownException {
        if(isServerWorking){
            return true;
        }
        throw new ServerIsDownException("server is not running\ntry again later\n");
    }

    private static boolean isPortRight(String port) throws PortException {
        if(Arrays.stream(ports).anyMatch(p -> Objects.equals(p, port))){
            return true;
        }else throw new PortException("port is not available\ntry another\n");
    }

    private static boolean isPortFree(String port) throws PortException {
        if(Arrays.stream(busyPorts).noneMatch(p -> Objects.equals(p, port))){
            return true;
        }else {
            throw new PortException("the port is busy\n");
        }
    }
    

    public static void reservePort(String port){
        for (int i = 0; i < busyPorts.length; i++) {
            if(busyPorts[i].equals("0")){
                busyPorts[i] = port;
                return;
            }
        }
    }

    public static void stopServer(){
        if (!isServerWorking){
            printText("Server is not running" + "\n");
        } else {
            isServerWorking = false;
            printText("Server stopped " + false + "\n");
            Arrays.fill(busyPorts, "0");
            for (int i = 0; i < clientsOnline.size(); i++) {
                disconnect(clientsOnline.get(i), false);
            }
        }
    }

    private static void printText(String msg){
        view.printText(msg);
    }

    public static void writeInChat(String msg){
        try(BufferedWriter fw =new BufferedWriter(new FileWriter("src/main/java/test.txt", true))){
            fw.write(msg);
            fw.newLine();
            fw.flush();
            answerAll(msg);
            ServerWindow.appendLog(msg);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void answerAll(String msg){
        for (Client client : clientsOnline) {
            client.printText(msg);
        }
    }

    public static StringBuilder readInChat(){
        StringBuilder sb = new StringBuilder();
        try( BufferedReader fr = new BufferedReader(new FileReader("src/main/java/test.txt"))){
            String line;
            while((line = fr.readLine()) != null){
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sb;
    }

    public static void startServer() {
        if(isServerWorking){
            printText("Server is already running" + "\n");
        }else{
            db.getDB();
            isServerWorking = true;
            printText("Server started " + true + "\n");
        }
    }
}
