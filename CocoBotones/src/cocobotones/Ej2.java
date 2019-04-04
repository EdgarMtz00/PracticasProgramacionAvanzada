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
public class Ej2 extends JFrame{
    JButton[] cosas = new JButton[12];
    GroupLayout orden = new GroupLayout(this.getContentPane());
    public Ej2(){
        for(JButton cosa : cosas){
            cosa = new JButton("cosa");
        }
        this.setTitle("CocoVentana");
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500,500);
        
        orden.setHorizontalGroup(
           orden.createSequentialGroup()
                .addGroup(orden.createParallelGroup()
                    .addGroup(orden.createSequentialGroup()
                        .addComponent(cosas[1], 300, 300, 300)
                        .addGroup(orden.createParallelGroup()
                            .addComponent(cosas[2])
                            .addComponent(cosas[5])
                            .addGroup(orden.createSequentialGroup()
                                .addComponent(cosas[7])
                                .addComponent(cosas[8])
                            )
                        )
                    )
                    .addComponent(cosas[11])
                )
                .addGroup(orden.createParallelGroup()
                    .addGroup(orden.createSequentialGroup()
                        .addGroup(orden.createParallelGroup()
                            .addComponent(cosas[3])
                            .addComponent(cosas[9])
                        )
                    .addComponent(cosas[12])
                    )
                )
        );
        
        orden.setVerticalGroup(
            orden.createSequentialGroup()
                .addGroup(orden.createParallelGroup()
                    .addComponent(cosas[1])
                    .addGroup(orden.createSequentialGroup()
                        .addComponent(cosas[2])
                        .addComponent(cosas[5])
                        .addGroup(orden.createParallelGroup()
                            .addComponent(cosas[7])
                            .addComponent(cosas[8])
                        )
                    )
                    .addGroup(orden.createSequentialGroup()
                        .addComponent(cosas[3])
                        .addComponent(cosas[9])
                    )
                    .addGroup(orden.createSequentialGroup()
                        .addComponent(cosas[4])
                        .addComponent(cosas[6])
                        .addComponent(cosas[10])
                    )
                )
                .addGroup(orden.createParallelGroup()
                    .addComponent(cosas[11])
                    .addComponent(cosas[12])
                )
        );
        
        orden.setAutoCreateGaps(true);
        orden.setAutoCreateContainerGaps(true);
        this.setLayout(orden);
        this.pack();
    }
    
}
