package com.company;

import java.awt.*;

public class Palillo {
    private Canvas c;

    private static Color[] color = {Color.black, Color.red};
    public boolean ocupado;

    public void setCanvas(Canvas c) {
        this.c = c;
    }

    public Palillo(){
        ocupado = false;
    }

    public void paint(Graphics g, int x, int y) {
        int i = (ocupado)? 1 : 0;
        g.setColor(color[i]);
        g.drawLine(x, y,x + 5, y + 35);
    }

    public void agarrar(){
        if(!ocupado) {
            ocupado = true;
            c.repaint();
            notify();
        }
    }

    public void dejar(){
        ocupado = false;
        c.repaint();
        notify();
    }

}
