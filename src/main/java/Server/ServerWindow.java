package Server;


import DB.ClientDataBase;
import lombok.Data;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import static Server.Server.isServerWorking;

@Data
public class ServerWindow extends JFrame {
    private static final int POS_X = 500;
    private static final int POS_Y = 550;
    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;


    private JButton btnStart;
    private JButton btnStop;
    private static final JTextArea log = new JTextArea();



    public ServerWindow(){
        isServerWorking = false;

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
        log.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(log);

        add(createButtons(), BorderLayout.SOUTH);
        add(scrollPane);
    }

    private Component createButtons(){
        btnStart = new JButton("Start");
        btnStop = new JButton("Stop");

        btnStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isServerWorking){
                    appendLog("Server is not running" + "\n");
                } else {
                    ClientDataBase.pushDB();
                    isServerWorking = false;
                    appendLog("Server stopped " + false + "\n");
                    Server.stopServer();
                }
            }
        });

        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(isServerWorking){
                    appendLog("Server is already running" + "\n");
                }else{
                    ClientDataBase.getDB();
                    isServerWorking = true;
                    appendLog("Server started " + true + "\n");
                }

            }
        });

        JPanel serverBtns = new JPanel(new GridLayout(1, 2));
        serverBtns.add(btnStart);
        serverBtns.add(btnStop);
        return serverBtns;
    }
}
