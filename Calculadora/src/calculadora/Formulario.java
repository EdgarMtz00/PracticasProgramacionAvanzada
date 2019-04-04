/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculadora;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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
    JButton[] botonesNum = new JButton[12];
    JButton[] botonesOp  = new JButton[5];
    String[] numeros = {"0", "i", ".", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
    String[] operaciones = {" = ", " + ", " - ", " * ", " / "};
    JTextField pantalla = new JTextField();

    String operacion = "";

    Color negro = new Color(43, 39, 39);
    Color gris = new Color(103, 101, 101);
    Color grisOsc = new Color(71, 68, 68);

    Font font = new Font("SansSerif", Font.BOLD, 35);
    
    public Formulario(){
        config();
        configBotones();
        configPantalla();
    }
    
    private void config(){
        this.setSize(500,700);
        this.setMinimumSize(new Dimension(500, 750));
        this.setMaximumSize(new Dimension(500, 750));
        this.getContentPane().setBackground(grisOsc);
        this.setTitle("Calculadora");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
    }
    
    private void configPantalla(){
        pantalla.setBackground(negro);
        pantalla.setForeground(Color.WHITE);
        pantalla.setEditable(false);
        pantalla.setBounds(20, 0, 450, 200);
        pantalla.setFont(font);
        this.add(pantalla);
    }
    
    private void configBotones(){
        for(int i = 0; i < botonesNum.length; i++){
            botonesNum[i] = new JButton(numeros[i]);
            botonesNum[i].addActionListener(this);
            botonesNum[i].setBackground(gris);
            botonesNum[i].setForeground(Color.WHITE);
            botonesNum[i].setFont(font);
            this.add(botonesNum[i]);
        }
        
        int iterador = 0;
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 3; x++) {
                botonesNum[iterador].setBounds(55 +(90 * x), 510 - (90 * y), 90, 90);
                iterador++;
            }
        }
        
        botonesOp[0] = new JButton(operaciones[0]);
        botonesOp[0].setBounds(55, 600, 360, 90);
        this.add(botonesOp[0]);
        botonesOp[0].setBackground(gris);
        botonesOp[0].setForeground(Color.WHITE);
        botonesOp[0].setFont(font);
        botonesOp[0].addActionListener((ActionEvent e) -> {
            //code here
        });
        
        for (int i = 1; i < botonesOp.length; i++) {
            botonesOp[i] = new JButton(operaciones[i]);
            botonesOp[i].addActionListener(this);
            botonesOp[i].setBackground(gris);
            botonesOp[i].setForeground(Color.WHITE);
            botonesOp[i].setBounds(325, 600 - (90 * i), 90, 90);
            botonesOp[i].setFont(font);
            this.add(botonesOp[i]);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        JButton boton = (JButton)o;
        operacion += boton.getText();
        pantalla.setText(operacion);
    }
    
}
