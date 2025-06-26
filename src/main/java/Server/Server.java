package Server;

import DB.ClientDataBase;
import DB.DataBase;
import Exceptions.PortException;
import Exceptions.ServerIsDownException;
import Exceptions.UnknownAccountException;
import Exceptions.WrongServerException;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import Client.Client;
import repository.FileStorage;

public class Server {

    private static final String SERVER = "127.0.0.1";
    private final String[] ports = {"80", "17", "84"};
    private String[] busyPorts = {"0", "0", "0"};
    public static boolean isServerWorking = false;
    private static ServerView view;
    private static DataBase db;
    FileStorage fs;

    private static ArrayList<Client> clientsOnline = new ArrayList<>();

    public Server(ServerView view){
        Server.view = view;
        db = new ClientDataBase();
        fs = new FileStorage();
    }

    public boolean checkServer(String server, String port, Client client) throws RuntimeException {
        try {
            if( isServerRight(server) & checkWork() & isPortRight(port) & isPortFree(port) & checkLogin(client)){
                return true;
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
        return false;
    }

    private boolean checkLogin(Client client){
        return db.checkData(client.getName(), client.getPasswd()) & !isClientAlreadyOnline(client);
    }

    private boolean isClientAlreadyOnline(Client client) throws UnknownAccountException {
        for (int i = 0; i < clientsOnline.size(); i++) {
            if(clientsOnline.get(i).getName().equals(client.getName())){
                throw new UnknownAccountException("user is already online");
            }
        }
        System.out.println(clientsOnline.contains(client));
        return false;
    }

    public boolean connectToServer(Client client){
        if((checkServer(client.getIp(), client.getPort(), client))){
            clientsOnline.add(client);
            reservePort(client.getPort());
            sendMessage("\n" + client.getName() + " connected to server\n" + readInChat());
            return true;
        }
        return false;
    }

    private boolean isServerRight(String server) throws WrongServerException {
        if(SERVER.equals(server)){
            return true;
        }else throw new WrongServerException("server not found\ntry another\n");
    }

    public void disconnect(Client client, boolean isClientGuiClosed){
        String clientPort = client.getPort();
        for (int i = 0; i < clientsOnline.size(); i++) {
            if(client.equals(clientsOnline.get(i))){
                client.disconnect();
                if(!clientsOnline.isEmpty()){
                    clientsOnline.remove(clientsOnline.get(i));
                }
            }
        }
        for (int i = 0; i < busyPorts.length; i++) {
            if(clientPort.equals(busyPorts[i])){
                busyPorts[i] = "0";
            }
        }
    }

    private boolean checkWork() throws ServerIsDownException {
        if(isServerWorking){
            return true;
        }
        throw new ServerIsDownException("server is not running\ntry again later\n");
    }

    private boolean isPortRight(String port) throws PortException {
        if(Arrays.stream(ports).anyMatch(p -> Objects.equals(p, port))){
            return true;
        }else throw new PortException("port is not available\ntry another\n");
    }

    private boolean isPortFree(String port) throws PortException {
        if(Arrays.stream(busyPorts).noneMatch(p -> Objects.equals(p, port))){
            return true;
        }else {
            throw new PortException("the port is busy\n");
        }
    }
    

    public void reservePort(String port){
        for (int i = 0; i < busyPorts.length; i++) {
            if(busyPorts[i].equals("0")){
                busyPorts[i] = port;
                return;
            }
        }
    }

    public void stopServer(){
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

    private void printText(String msg){
        view.printText(msg);
    }

    public void writeInChat(String msg){
        fs.save(msg);
        answerAll(msg);
        sendMessage(msg);
    }

    private void answerAll(String msg){
        for (Client client : clientsOnline) {
            client.printText(msg);
        }
    }

    public String readInChat(){
        return fs.load();
    }

    public void startServer() {
        if(isServerWorking){
            printText("Server is already running" + "\n");
        }else{
            db.getDB();
            isServerWorking = true;
            printText("Server started " + true + "\n");
        }
    }

    public void sendMessage(String msg){
        view.printText(msg);
    }

    public void signUp(String name, String passwd) throws UnknownAccountException {
        db.signUp(name, passwd);
    }
}
