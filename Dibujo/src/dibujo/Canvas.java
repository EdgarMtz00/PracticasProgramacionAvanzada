/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dibujo;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 *
 * @author Agustin
 */
public class Canvas extends JFrame{
    JButton[] botones = new JButton[4];
    String[] texto = {"Arriba", "Abajo", "Izquierda", "Derecha"};
    int x, y;

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.white);
        g.drawOval(150+x, 150+y, 80, 80);
        g.drawLine(190+x, 230+y, 190+x, 350+y);
        g.drawLine(115+x, 260+y, 265+x, 260+y);
        g.drawLine(190+x, 350+y, 120+x, 450+y);
        g.drawLine(190+x, 350+y, 260+x, 450+y);
        
    }
    
    
    
    public Canvas(){
        this.setLayout(null);
        x = 0;
        y = 0;
        for (int i = 0; i < 4; i++) {
            botones[i] = new JButton(texto[i]);
            botones[i].setBackground(Color.gray);
            botones[i].setForeground(Color.WHITE);
            botones[i].setBorderPainted(false);
            botones[i].setBounds((110*i) + 5, 20, 100, 40);
            
            this.add(botones[i]);
            switch(i){
                case 0:
                    botones[i].addActionListener(new ActionListener(){
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            y -= 10;
                            repaint();
                        }
                    }); 
                    break;
                case 1:
                    botones[i].addActionListener(new ActionListener(){
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            y += 10;
                            repaint();
                        }
                    }); 
                    break;
                case 2:
                    botones[i].addActionListener(new ActionListener(){
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            x -= 10;
                            repaint();
                        }
                    });
                    break;
                case 3:
                    botones[i].addActionListener(new ActionListener(){
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            x += 10;
                            repaint();
                        }
                    });
                    break;
                default:
                    break;
            }
        }
        config();
    }
    public void config(){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500,500);
        this.setMinimumSize(new Dimension(500,500));
        this.setTitle("Dibujo");
        this.getContentPane().setBackground(Color.darkGray);
    }   
}
