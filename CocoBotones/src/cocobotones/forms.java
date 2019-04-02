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
                .addComponent(cosa1)
                .addGroup(orden.createParallelGroup()
                    .addComponent(cosa2)
                    .addComponent(cosa3))
                .addComponent(cosa4));
        
        orden.setVerticalGroup(
            orden.createSequentialGroup()
                .addGroup(orden.createParallelGroup()
                    .addComponent(cosa1)
                    .addGroup(orden.createSequentialGroup()
                        .addComponent(cosa2)
                        .addComponent(cosa3))
                    .addComponent(cosa4)));
        this.setLayout(orden);
        this.pack();
    }
}
