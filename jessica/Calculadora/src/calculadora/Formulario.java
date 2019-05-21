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
    private JButton[] botonesNum = new JButton[12];
    private JButton[] botonesOp  = new JButton[5];
    private String[] numeros = {"0", "i", ".", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
    private String[] operaciones = {"=", "+", "-", "*", "/"};
    private JTextField pantalla = new JTextField();
    private int contador = 0;
    private NumComplejo x, y;
    private String op, opAnt;
    private Color negro = new Color(43, 39, 39);
    private Color gris = new Color(103, 101, 101);
    private Color grisOsc = new Color(71, 68, 68);

    private Font font = new Font("SansSerif", Font.BOLD, 35);
    
    public Formulario(){
        x = new NumComplejo();
        y = new NumComplejo();
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
        this.setResizable(false);
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
            if(contador != 0) {
                String t = pantalla.getText();
                if (contador == 2) {
                    if (!t.contains("i")) {
                        y.setReal(Integer.parseInt(t));
                    } else {
                        y.setImaginario(t);
                    }
                } else if (contador == 3) {
                    if (t.contains("i")) {
                        y.setImaginario(t);
                    }
                }
                contador = 0;
                switch (op) {
                    case "+":
                        pantalla.setText(x.suma(y).toString());
                        break;
                    case "-":
                        pantalla.setText(x.resta(y).toString());
                        break;
                case "*":
                    pantalla.setText(x.multiplicar(y).toString());
                    break;
                case "/":
                    pantalla.setText(x.dividir(y).toString());
                    break;
                }
                x = new NumComplejo();
                y = new NumComplejo();
            }else{
                pantalla.setText("");
            }
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
        JButton button = (JButton) e.getSource();
        String btnText = button.getText();
        for (String operacion : operaciones){
            if(btnText.equals(operacion)){
                String t = pantalla.getText();
                pantalla.setText("");
                switch (contador){
                    case 0:
                        if(!t.contains("i")){
                            x.setReal(Integer.parseInt(t));
                        }else{
                            x.setImaginario(t);
                        }
                        op = btnText;
                        break;
                    case 1:
                        if(!t.contains("i")){
                            y.setReal(Integer.parseInt(t));
                        }else{
                            x.setImaginario(opAnt + t);
                            op = button.getText();
                        }
                        break;
                    case 2:
                        if(!t.contains("i")){
                            y.setReal(Integer.parseInt(t));
                        }else{
                            y.setImaginario(opAnt + t);
                        }
                        break;
                    case 3:
                        if(t.contains("i")){
                            y.setImaginario(opAnt + t);
                        }
                }
                System.out.println(x);
                System.out.println(y);
                contador++;
                opAnt = btnText;
                return;
            }
        }
        pantalla.setText(pantalla.getText() + btnText);
    }
}