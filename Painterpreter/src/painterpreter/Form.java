/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package painterpreter;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 *
 * @author Agustin
 */
public class Form extends JFrame implements ActionListener{
    JButton run, compile;
    JTextArea console, output;
    int x = 0, y = 0;
    Color negro = new Color(43, 39, 39);
    Color gris = new Color(103, 101, 101);
    Color grisOsc = new Color(71, 68, 68);
    Interpreter intrp;
    Dummy dummy;
    
    public Form(){
        config();
        configTxt();
        configCtrlPanel();
    }
    
    private void configCtrlPanel(){
        run = new JButton("Run");
        run.setBounds(500,400, 150, 100);
        run.setBackground(gris);
        run.addActionListener(this);
        this.add(run);
        
        compile = new JButton("Compile");        
        compile.setBounds(650, 400, 150, 100);
        compile.setBackground(gris);
        compile.addActionListener(this);
        this.add(compile);
    }
    
    private void configTxt(){
        console = new JTextArea();
        console.setBounds(500,0,300,400);
        console.setBackground(negro);
        console.setForeground(Color.white);
        console.setVisible(true);
        console.setTabSize(4);
        console.setLineWrap(true);
        console.setCaretColor(Color.white);
        console.setWrapStyleWord(true);
        this.add(console);
        
        output = new JTextArea();
        output.setEditable(false);
        output.setBounds(0,500,800,210);
        output.setBackground(negro);
        output.setForeground(Color.white);
        output.setVisible(true);
        this.add(output);
    }
    
    private void config(){
        this.setSize(820, 730);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setBackground(grisOsc);
        this.setLayout(null);
    }
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        dummy.paint(g);
    }

    public void setInterpreter(Interpreter interpreter){
        this.intrp = interpreter;
    }
    
    public void setDummy(Dummy dummy){
        this.dummy = dummy;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        intrp.setCode(console.getText());
        if(e.getSource() == compile){
            intrp.compile();
        }else{
            new Thread(intrp).start();
        }
        output.setText(intrp.outputMsg);
    }
}
