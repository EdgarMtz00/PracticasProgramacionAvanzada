package cena;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Form extends JFrame{

    private ArrayList<Filosofo> filosofos;
    private  ArrayList<Palillo> palillos;

    public Form(ArrayList filosofos, ArrayList palillos){
        this.filosofos = filosofos;
        this.palillos = palillos;
        this.setSize(500,500);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        filosofos.get(0).paint(g, 225, 150);
        palillos.get(0).paint(g, 167, 175);
        filosofos.get(1).paint(g, 125, 250 );
        palillos.get(1).paint(g, 167, 275);
        filosofos.get(2).paint(g, 175, 350);
        palillos.get(2).paint(g, 225, 350);
        filosofos.get(3).paint(g, 275, 350);
        palillos.get(3).paint(g, 287, 275);
        filosofos.get(4).paint(g, 325, 250);
        palillos.get(4).paint(g, 287, 175);
    }
}
