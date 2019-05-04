package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class ChatFrame extends JFrame {
    MsgPanel pChat;
    JTextField tMsg;
    JLabel lCurrentChat;
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
        this.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                System.out.println(e.getComponent().getBounds().getSize());
            }

            @Override
            public void componentMoved(ComponentEvent e) {

            }

            @Override
            public void componentShown(ComponentEvent e) {

            }

            @Override
            public void componentHidden(ComponentEvent e) {

            }
        });
    }

    private  void componentConfig(){
        pChat = new MsgPanel();
        tMsg = new JTextField("tMsg");
        lCurrentChat = new JLabel("CurrentChat");
        bSend = new JButton("send");
        tpControl = new JTabbedPane();
    }

    private void glConfig(){
        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup()
                    .addGroup(groupLayout.createSequentialGroup()
                        .addComponent(lCurrentChat, 25, 50,100)
                        .addComponent(pChat, 300, 550, 700)
                        .addGroup(groupLayout.createParallelGroup()
                            .addComponent(tMsg, 50, 75, 100)
                            .addComponent(bSend, 50, 75, 100)
                        )
                    )
                    .addComponent(tpControl, 700, 700, 700)

        );

        groupLayout.setHorizontalGroup(
                groupLayout.createSequentialGroup()
                    .addGroup(groupLayout.createParallelGroup()
                        .addComponent(lCurrentChat, 100, 300, 500)
                        .addComponent(pChat, 500, 750, 900)
                        .addGroup(groupLayout.createSequentialGroup()
                            .addComponent(tMsg, 400, 650, 800)
                            .addComponent(bSend, 50, 100, 150)
                        )
                    )
                    .addComponent(tpControl, 200, 400, 600)
        );

        groupLayout.setAutoCreateGaps(true);
        groupLayout.setAutoCreateContainerGaps(true);
        this.setLayout(groupLayout);
        this.pack();
    }
}
