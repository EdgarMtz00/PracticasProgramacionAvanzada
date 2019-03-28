/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculadora;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

/**
 *
 * @author agust
 */
public class Formulario extends JFrame implements ActionListener{
    JButton[] botonesNum = new JButton[11];
    JButton[] botonesOp  = new JButton[5];
    String[] numeros = {"0", ".", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
    String[] operaciones = {"=", "+", "-", "*", "/"};
    JTextField pantalla = new JTextField();
    String operacion;
    Color negro, gris, grisOsc;
    public Formulario(){
        colores();
        config();
        configBotones();
        configPantalla();
    }
    
    private void config(){
        this.setSize(500,500);
        this.setMinimumSize(new Dimension(500, 750));
        this.getContentPane().setBackground(grisOsc);
        this.setTitle("Calculadora");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
    }
    
    private void colores(){
        negro = new Color(43, 39, 39);
        gris = new Color(103, 101, 101);
        grisOsc = new Color(71, 68, 68);
    }
    
    private void configPantalla(){
        pantalla.setBackground(negro);
        pantalla.setForeground(Color.WHITE);
        pantalla.setEditable(false);
        pantalla.setBounds(0, 0, 500, 200);
        this.add(pantalla);
    }
    
    private void configBotones(){
        for(int i = 0; i < botonesNum.length; i++){
            botonesNum[i] = new JButton(numeros[i]);
            botonesNum[i].addActionListener(this);
            botonesNum[i].setBackground(gris);
            botonesNum[i].setForeground(Color.WHITE);
            this.add(botonesNum[i]);
        }
        
        botonesNum[0].setBounds(55, 610, 190, 90);
        botonesNum[1].setBounds(255, 610, 90, 90);
        
        for (int i = 2; i < botonesNum.length; i++) {
            botonesNum[i].setBounds(55 + (90 * (i % 3)), 300 + (90 * (i % 3)), 90, 90);
        }
        
        botonesOp[0] = new JButton(operaciones[0]);
        this.add(botonesOp[0]);
        botonesOp[0].addActionListener((ActionEvent e) -> {
            //code here
        });
        
        for (int i = 1; i < botonesOp.length; i++) {
            botonesOp[i] = new JButton(operaciones[i]);
            botonesOp[i].addActionListener(this);
            botonesOp[i].setBackground(gris);
            botonesOp[i].setForeground(Color.WHITE);
            this.add(botonesOp[i]);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        JButton boton = (JButton)o;
        operacion += boton.getText();
    }
    
}
