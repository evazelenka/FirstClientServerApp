package Server;

import Client.ClientGUI;
import Exceptions.PortException;
import Exceptions.ServerIsDownException;
import Exceptions.WrongServerException;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Server {

    private static final String SERVER = "127.0.0.1";
    private static final String[] ports = {"80", "17", "84"};
    private static String[] busyPorts = {"0", "0", "0"};
    public static boolean isServerWorking = false;

    public static List<ClientGUI> clientsOnline = new ArrayList<ClientGUI>();

    public static boolean checkServer(String server, String port, ClientGUI client) throws RuntimeException {
        try {
            if( isServerRight(server) & checkWork() & isPortRight(port) & isPortFree(port)){
                return true;
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
        return false;
    }

    public static void connectToServer(ClientGUI client){
        if(!clientsOnline.contains(client)){
            clientsOnline.add(client);
        }
        ServerWindow.appendLog("\n" + client.getNickName() + " connected to server\n" + readInChat());
    }

    private static boolean isServerRight(String server) throws WrongServerException {
        if(SERVER.equals(server)){
            return true;
        }else throw new WrongServerException("server not found\ntry another\n");
    }

    public static void disconnect(ClientGUI client, boolean isClientGuiClosed){
        for (int i = 0; i < clientsOnline.size(); i++) {
            if(client.equals(clientsOnline.get(i))){
                if(!isClientGuiClosed) client.disconnectFromServer(false);
                clientsOnline.get(i).setLogged(false);
                ServerWindow.appendLog("\n" + client.getNickName() + " disconnected from server\n");
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
        Arrays.fill(busyPorts, "0");
        for (int i = 0; i < clientsOnline.size(); i++) {
            System.out.println("yes" + clientsOnline.size());
            disconnect(clientsOnline.get(i), false);
        }

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
        for (ClientGUI client : clientsOnline) {
            client.answer(msg);
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
}
