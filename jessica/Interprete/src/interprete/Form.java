package interprete;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class Form extends JFrame implements ActionListener{
    JButton ejecutar;
    JTextArea textArea;
    Color color1 = new Color(121, 54, 104);
    Color color2 = new Color(214, 180, 231);
    Color grisOsc = new Color(172, 104, 204);
    Compilador compilador;
    Dummy dummy;
    
    public Form(){
        this.setSize(800, 550);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setBackground(grisOsc);
        this.setLayout(null);
        textArea = new JTextArea();
        textArea.setBounds(0,0,300,400);
        textArea.setBackground(color1);
        textArea.setForeground(Color.white);
        textArea.setVisible(true);
        textArea.setTabSize(4);
        textArea.setLineWrap(true);
        textArea.setCaretColor(Color.white);
        textArea.setWrapStyleWord(true);
        ejecutar = new JButton("Compilar");
        ejecutar.setBounds(0,400, 300, 100);
        ejecutar.setBackground(color2);
        ejecutar.addActionListener(this);
        this.add(ejecutar);
        this.add(textArea);
    }
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        dummy.paint(g);
    }

    public void setInterpreter(Compilador compilador){
        this.compilador = compilador;
    }
    
    public void setDummy(Dummy dummy){
        this.dummy = dummy;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        compilador.setCodigos(textArea.getText());
        new Thread(compilador).start();
    }
}