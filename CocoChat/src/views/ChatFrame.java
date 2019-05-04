package views;

import javax.swing.*;
import java.awt.*;

public class ChatFrame extends JFrame {
    MsgPanel pChat;
    JTextField tMsg;
    JTextArea tCurrentChat;
    JButton bSend;
    JTabbedPane tpControl;

    GroupLayout groupLayout = new GroupLayout(this.getContentPane());
    public ChatFrame(){
        this.setTitle("CocoChat");
        this.getContentPane().setBackground(Color.lightGray);
        this.setVisible(true);
        this.setSize(new Dimension(1200, 700));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        componentConfig();
        glConfig();
    }

    private  void componentConfig(){
        pChat = new MsgPanel();
        tMsg = new JTextField();
        tCurrentChat = new JTextArea();
        bSend = new JButton();
        tpControl = new JTabbedPane();
    }

    private void glConfig(){
        groupLayout.setHorizontalGroup(
                groupLayout.createSequentialGroup()
                        .addComponent(tCurrentChat, 100, 100,100)
                        .addComponent(pChat, 100, 100, 100)
                        .addGroup(groupLayout.createParallelGroup()
                                .addComponent(tMsg, 100, 100, 100)
                                .addComponent(bSend, 100, 100, 100)
                        )
                        .addComponent(tpControl, 100, 100, 100)
        );

        groupLayout.setVerticalGroup(
                groupLayout.createSequentialGroup()
                        .addGroup(groupLayout.createParallelGroup()
                                .addComponent(tCurrentChat)
                                .addComponent(pChat)
                                .addGroup(groupLayout.createSequentialGroup()
                                        .addComponent(tMsg)
                                        .addComponent(bSend)
                                )
                                .addComponent(tpControl)
                        )
        );

        groupLayout.setAutoCreateGaps(true);
        groupLayout.setAutoCreateContainerGaps(true);
        this.setLayout(groupLayout);
        this.pack();
    }
}
