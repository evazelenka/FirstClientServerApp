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
        logInBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connectToServer();
            }
        });
        signUpBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                signUp(getNickName(), getPasswd(passwd.getPassword()));
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
        add(createHeader(), BorderLayout.NORTH);
        add(createLog());
        add(createFooter(), BorderLayout.SOUTH);
    }

    private Component createLog(){
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
