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


public class ClientGUI extends JFrame {
    private static final int POS_X = 500;
    private static final int POS_Y = 550;
    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;


    private final String SERVER_IS_DOWN = "connection failed\ntry again later\n";
    private final JTextField server = new JTextField("127.0.0.1");
//    private JTextField port = new JTextField(20);
    private JComboBox<String> port = new JComboBox<>();
    private  JTextField nickName = new JTextField(20);
    private  JPasswordField passwd = new JPasswordField(20);
    private  JButton logInBtn = new JButton("login");
    private  JButton signUpBtn = new JButton("sign up");
    private  JTextField sendField = new JTextField(100);
    private  JButton sendBtn = new JButton("send");
    public JTextArea log = new JTextArea();
    private  final String LOGIN_SUCCESS = "loged in successfully\n";
    ArrayList<Client> clients = ClientDataBase.getClients();


    private JPanel loginPanel;


    @Setter
    private boolean logged = false;


    public boolean getLogged(){
        return logged;
    }


    public ClientGUI(ServerWindow serverWindow){
        setBackground(Color.WHITE);
        setBounds(POS_X+400, POS_Y, WIDTH, HEIGHT);
        setTitle("Chat Client");
        setVisible(true);
        createPanel();
    }

    private void connectToServer(){
        try {
            if(checkLogin(server.getText(), port.getItemAt(port.getSelectedIndex()), nickName.getText(), passwd.getPassword())){
                Server.reservePort(port.getItemAt(port.getSelectedIndex()));
//                ClientDataBase.getClientByNickName(nickName.getText()).setOnline();
                logged = true;
                loginPanel.setVisible(false);
                System.out.println(LOGIN_SUCCESS);
                log.append(LOGIN_SUCCESS);
                log.setText(String.valueOf(Server.readInChat()));
            }
        } catch (RuntimeException e) {
            log.append(e.getMessage());
        }
    }

    public void disconnectFromServer(boolean isClientGuiClosed){
        if(logged){
            logged = false;
            ClientDataBase.getClientByNickName(nickName.getText()).setOffline();
            loginPanel.setVisible(true);
            Server.disconnect(this, isClientGuiClosed);
            log.setText("disconnect\n");
        }
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
                if(signUp(new Client(nickName.getText(), passwd.getPassword()))) log.append("signed up successfully");
            }
        });

        port.setEditable(true);
        port.addItem("80");
        port.addItem("17");
        port.addItem("84");

        loginPanel.add(server);
        loginPanel.add(port);
        loginPanel.add(signUpBtn);
        loginPanel.add(nickName);
        loginPanel.add(passwd);
        loginPanel.add(logInBtn);
        return loginPanel;
    }

    private boolean signUp(Client client){
        try {
            ClientDataBase.signUp(client);
            return true;
        } catch (RuntimeException e) {
            log.append(e.getMessage());
            return false;
        }
    }

    private Component createFooter(){
        sendBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                message();
            }
        });
        sendField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                message();
            }
        });
        JPanel sendPanel = new JPanel(new GridLayout(1, 2));
        sendPanel.add(sendField);
        sendPanel.add(sendBtn);
        return  sendPanel;

    }

    private void message(){
        if(logged){
            String text = sendField.getText();
            if(!text.equals("")){
                Server.writeInChat(nickName.getText() + ": " + text);
                sendField.setText("");
            }else log.append(SERVER_IS_DOWN);
        }
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

    public void serverStopped(){
        if(this.isVisible()){
            this.setVisible(false);
        }
    }

    public void answer(String msg){
        log.append(msg + "\n");
    }

    public boolean checkLogin(String server, String port, String name, char[] passwd) throws RuntimeException {
        try {
            if(Server.checkServer(server, port, this)& checkPasswd(name, getPassword(passwd)) & !checkOnline(name)){
                Server.connectToServer(this);
                ClientDataBase.getClientByNickName(name).setOnline();
                return true;
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
        return false;
    }

    private boolean checkOnline(String name) throws RuntimeException {
        if(ClientDataBase.getClientByNickName(name).isOnline()){
            throw new RuntimeException("user is already online");
        }
        return false;
    }

    public String getPassword(char[] p){
        String s = new String(p);
        System.out.println(s);
        return s;
    }

    private boolean checkNickName(String name) throws UnknownAccountException {
        for (int i = 0; i < ClientDataBase.getClients().size(); i++) {
            if(clients.get(i).getNickName().equals(name)){
                return true;
            }
        }
        throw new UnknownAccountException("create the account first\n");
    }

    private boolean checkPasswd(String name, String passwd){
        if(checkNickName(name)){
            if(ClientDataBase.getClientByNickName(name).getPasswd().equals(passwd)){
                return true;
            }
        }
        throw new WrongPasswdException("wrong password\n");
    }

    public String getNickName(){
        return nickName.getText();
    }

    @Override
    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING){
            disconnectFromServer(true);
        }
        super.processWindowEvent(e);
    }

}
