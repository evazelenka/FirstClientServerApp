package Server;


import lombok.Data;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Data
public class ServerWindow extends JFrame implements ServerView {
    private static final int POS_X = 500;
    private static final int POS_Y = 550;
    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;


    private JButton btnStart;
    private JButton btnStop;
    private static final JTextArea log = new JTextArea();
    @Getter
    private Server server;


    public ServerWindow(){
        server = new Server(this);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(POS_X, POS_Y, WIDTH, HEIGHT);
        setResizable(false);
        setTitle("Chat server");
        setBackground(Color.WHITE);
        setAlwaysOnTop(true);

        createPanel();

        setVisible(true);
    }

    public static void appendLog(String msg){
        log.append(msg);
    }

    private void createPanel(){
        add(createButtons(), BorderLayout.SOUTH);
        add(createLog());
    }

    private Component createLog(){
        log.setEditable(false);
        return new JScrollPane(log);
    }

    private Component createButtons(){
        btnStart = new JButton("Start");
        btnStop = new JButton("Stop");

        btnStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                server.stopServer();
            }
        });

        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                server.startServer();
            }
        });

        JPanel serverBtns = new JPanel(new GridLayout(1, 2));
        serverBtns.add(btnStart);
        serverBtns.add(btnStop);
        return serverBtns;
    }

    @Override
    public void printText(String msg) {
        appendLog(msg);
    }
}
