/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ventanas;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author Agustin
 */
public class FormularioB extends JFrame{
    JButton btnColor, btnResize, btnClose; 
    JTextField txtColor;
    
    public FormularioB(){
        btnColor = new JButton("Cambiar Color");
        btnResize = new JButton("Cambiar tamaño");
        btnClose = new JButton("Cerrar");
        txtColor = new JTextField();
        config();
        clickListeners();
    }
    
    private void config(){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500,500);
        this.setTitle("Ventanas");
        this.getContentPane().setBackground(Color.DARK_GRAY);
        this.add(btnColor);
        this.add(btnResize);
        this.add(btnClose);
        this.add(txtColor);
        this.setLayout(null);
        
        txtColor.setBounds(50, 50, 120, 30);
        btnColor.setBounds(10, 10, 120, 30);
        btnResize.setBounds(135, 10, 120, 30);
        btnClose.setBounds(260, 10, 120, 30);
    }

    private void clickListeners() {
        btnColor.addActionListener( new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(null,txtColor.getText());
            }     
        });
        
        btnResize.addActionListener( new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
            txtColor.setText("negro");
            }     
        });
        
        btnClose.addActionListener( new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
            System.exit(0);
            }     
        });      
    }
}
