package Client;

import DB.ClientDataBase;
import Exceptions.UnknownAccountException;
import Exceptions.WrongPasswdException;
import Server.Server;
import Server.ServerWindow;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;


public class ClientGUI extends JFrame implements ClientView {
    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;

    private JTextArea log;
    private JTextField ip = new JTextField();
    private JTextField nickName = new JTextField();
    private JTextField sendField = new JTextField();
    private JPanel sendPanel;

    private JComboBox<String> port = new JComboBox<>();
    private  JPasswordField passwd = new JPasswordField(20);
    private  JButton logInBtn = new JButton("login");
    private  JButton signUpBtn = new JButton("sign up");
    private  JButton sendBtn = new JButton("send");


    private Client client;

    private JPanel loginPanel;

    public ClientGUI(ServerWindow serverWindow){
        client = new Client(this, serverWindow.getServer());
        setBounds(serverWindow.getX()+400, serverWindow.getY(), WIDTH, HEIGHT);
        setResizable(false);
        setTitle("Chat Client");
        setVisible(true);
        createPanel();
    }

    public String getPort(){
        return port.getItemAt(port.getSelectedIndex());
    }

    private Component createHeader(){
        loginPanel = new JPanel(new GridLayout(2, 3));
        System.out.println("header");
        logInBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connectToServer();
            }
        });
        signUpBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(signUp(nickName.getText(),
                        getPasswd(passwd.getPassword()))){
                    appendLog("signed up successfully");
                };
            }
        });

        port.setEditable(true);
        port.addItem("80");
        port.addItem("17");
        port.addItem("84");

        loginPanel.add(ip);
        loginPanel.add(port);
        loginPanel.add(signUpBtn);
        loginPanel.add(nickName);
        loginPanel.add(passwd);
        loginPanel.add(logInBtn);
        return loginPanel;
    }

    private String getPasswd(char[] passwd){
        StringBuilder p = new StringBuilder();
        for(char c : passwd){
            p.append(c);
        }
        return p.toString();
    }

    private Component createFooter(){
        System.out.println("footer");
        sendBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                message(sendField.getText());
                sendField.setText("");
            }
        });
        sendField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                message(sendField.getText());
                sendField.setText("");
            }
        });
        sendPanel = new JPanel(new GridLayout(1, 2));
        sendPanel.add(sendField);
        sendPanel.add(sendBtn);
        return sendPanel;

    }

    private void createPanel(){
        System.out.println("panel");
        add(createHeader(), BorderLayout.NORTH);
        add(createLog());
        add(createFooter(), BorderLayout.SOUTH);
    }

    private Component createLog(){
        System.out.println("log");
        log = new JTextArea();
        log.setEditable(false);
        return new JScrollPane(log);
    }

    public void answer(String msg){
        log.append(msg + "\n");
    }

    public String getPassword(char[] p){
        String s = new String(p);
        System.out.println(s);
        return s;
    }

    public String getNickName(){
        return nickName.getText();
    }

    @Override
    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING){
//            disconnectFromServer(true);
            client.disconnect(true);
        }
        super.processWindowEvent(e);
    }

    private void connectToServer(){
        if(client.connectToServer(getNickName(), getPassword(passwd.getPassword()), ip.getText(), getPort())){
            setVisibleHeaderPanel(false);
        }
    }

    private void setVisibleHeaderPanel(boolean visible){
        loginPanel.setVisible(visible);
    }

    private boolean signUp(String name, String passwd){
        try {
            client.signUp(name, passwd);
            return true;
        } catch (RuntimeException e) {
            log.append(e.getMessage());
            return false;
        }
    }

    private void message(String msg){
        client.sendMessage(msg);
    }

//    public boolean checkLogin(String server, String port, String name, char[] passwd) throws RuntimeException {
//        try {
//            if(Server.checkServer(server, port, this)& checkPasswd(name, getPassword(passwd)) & !checkOnline(name)){
////                Server.connectToServer(this);
//                ClientDataBase.getClientByNickName(name).setOnline();
//                return true;
//            }
//        } catch (RuntimeException e) {
//            throw new RuntimeException(e.getMessage());
//        }
//        return false;
//    }

//    private boolean checkOnline(String name) throws RuntimeException {
//        if(ClientDataBase.getClientByNickName(name).isOnline()){
//            throw new RuntimeException("user is already online");
//        }
//        return false;
//    }
//
//    private boolean checkNickName(String name) throws UnknownAccountException {
//        for (int i = 0; i < ClientDataBase.getUsers().size(); i++) {
//            if(users.get(i).getNickName().equals(name)){
//                return true;
//            }
//        }
//        throw new UnknownAccountException("create the account first\n");
//    }
//
//    private boolean checkPasswd(String name, String passwd){
//        if(checkNickName(name)){
//            if(ClientDataBase.getClientByNickName(name).getPasswd().equals(passwd)){
//                return true;
//            }
//        }
//        throw new WrongPasswdException("wrong password\n");
//    }

    public void appendLog(String message){
        log.append(message + "\n");
    }

    @Override
    public void sendMessage(String message) {
        appendLog(message);
    }

    @Override
    public void disconnectFromServer() {
        loginPanel.setVisible(true);
    }
}
