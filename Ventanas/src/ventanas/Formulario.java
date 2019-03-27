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
public class Formulario extends JFrame implements ActionListener{
    JButton btnColor, btnResize, btnClose; 
    JTextField txtColor; 
    public Formulario(){
        btnColor = new JButton("Cambiar Color");
        btnResize = new JButton("Cambiar tamaño");
        btnClose = new JButton("Cerrar");
        txtColor = new JTextField("Blanco");
        config();
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
        
        btnColor.addActionListener(this);
        btnResize.addActionListener(this);
        btnClose.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == btnColor){
            cambiarColor();
        }else if(e.getSource() == btnResize){
            resize();
        }else{
            close();
        }
    }
    
    public void cambiarColor(){
        this.getContentPane().setBackground(Color.WHITE);
        JOptionPane.showMessageDialog(null,txtColor.getText());
    }
    
    public void resize(){
        this.setSize(800, 700);
        txtColor.setText("negro");
    }
    
    public void close(){
        System.exit(0);
    }
}
