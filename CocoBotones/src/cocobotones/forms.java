/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cocobotones;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 *
 * @author Agustin
 */
public class forms extends JFrame{
    JButton cosa1, cosa2, cosa3, cosa4;
    GroupLayout orden = new GroupLayout(this.getContentPane());
    public forms(){
        cosa1 = new JButton("cosa1");
        cosa2 = new JButton("cosa2");
        cosa3 = new JButton("cosa3");
        cosa4 = new JButton("cosa4");
        this.setTitle("CocoVentana");
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500,500);
        
        orden.setHorizontalGroup(
           orden.createSequentialGroup()
                .addGroup(orden.createParallelGroup()
                    .addGroup(orden.createSequentialGroup()
                            .addComponent(cosa1, 300, 300, 300)
                            .addComponent(cosa2, 150, 150, 150)
                    )
                    .addGroup(orden.createSequentialGroup()
                        .addComponent(cosa3, 150, 150, 150)
                        .addComponent(cosa4, 300, 300, 300)
                    )
                )
        );
        
        orden.setVerticalGroup(
            orden.createSequentialGroup()
                .addGroup(orden.createParallelGroup()
                        .addComponent(cosa1)
                        .addComponent(cosa2)
                )
                .addGroup(orden.createParallelGroup()
                    .addComponent(cosa3)
                    .addComponent(cosa4)
                )
        );
        
        orden.setAutoCreateGaps(true);
        orden.setAutoCreateContainerGaps(true);
        this.setLayout(orden);
        this.pack();
    }
}
