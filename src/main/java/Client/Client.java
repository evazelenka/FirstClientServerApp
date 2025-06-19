package Client;

import DB.ClientDataBase;
import Exceptions.UnknownAccountException;
import Exceptions.WrongPasswdException;
import Server.Server;
import Server.ServerWindow;
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
        if(server.connectToServer(this)){
            printText("connected successfully");
            connected = true;
            String log = server.getChat();
            if(log != null){
                printText(log);
            }
            return true;
        }else {
            printText("connection failed");
            return false;
        }
    }

    public void disconnect(boolean isClientGuiClosed){
        if(connected){
            connected = false;
            view.disconnectFromServer();
            Server.disconnect(this, false);
            printText("you was disconnected");
        }
    }

    public void sendMessage(String msg){
        if(connected){
            if(!msg.isEmpty()){
                server.sendMessage(name + ": " + msg);
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

}
