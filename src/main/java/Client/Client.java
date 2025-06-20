package Client;

import Exceptions.UnknownAccountException;
import Server.Server;
import lombok.Getter;

//логика
public class Client {
    private Server server;
    private boolean connected;
    @Getter
    private String name, passwd, ip, port;
    private ClientView view;

    public Client(ClientView view, Server server){
        this.view = view;
        this.server = server;
    }

    public boolean connectToServer(String name, String passwd, String ip, String port){
        this.name = name;
        this.passwd = passwd;
        this.ip = ip;
        this.port = port;
        try {
            if(server.connectToServer(this)){
                printText("connected successfully");
                connected = true;
                String log = server.readInChat();
                if(log != null){
                    printText(log);
                }
                return true;
            }else {
                printText("connection failed");
            }
        } catch (Exception e) {
            serverAnswer(e.getMessage());
        }
        return false;
    }

    public void disconnect(boolean isClientGuiClosed){
        if(connected){
            connected = false;
            view.disconnectFromServer();
            server.disconnect(this, isClientGuiClosed);
            server.sendMessage("\n" + getName() + " disconnected from server\n");
            printText("you was disconnected");
        }
    }

    public void disconnect(){
        if(connected){
            connected = false;
            view.disconnectFromServer();
            server.disconnect(this, false);
            server.sendMessage("\n" + getName() + " disconnected from server\n");
            printText("you was disconnected");
        }
    }

    public void sendMessage(String msg){
        if(connected){
            if(!msg.isEmpty()){
                server.writeInChat(name + ": " + msg);
            }
        }else {
            printText("connection failed");
        }
    }

    public void serverAnswer(String answer){
        printText(answer);
    }


    public void printText(String msg){
        view.sendMessage(msg);
    }

    public void signUp(String name, String passwd) {
        try {
            server.signUp(name, passwd);
            printText("signed up successfully");
        } catch (UnknownAccountException e) {
            serverAnswer(e.getMessage());
        }
    }
}
