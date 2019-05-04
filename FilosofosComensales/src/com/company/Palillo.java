package com.company;

import java.awt.*;

public class Palillo {
    private Graphics g;
    private int x, y;
    private Canvas c;

    /*  Status:
        1: Blanco, disponible.
        2: Rojo, Ocupado.
     */
    private int status;
    private static Color[] colStatus = {Color.white, Color.red};
    private boolean uso;

    public void setCanvas(Canvas c) {
        this.c = c;
    }

    public Palillo(){
        status = 0;
        uso = false;
    }

    public void paint(Graphics g, int x, int y) {
        this.g = g;
        this.x = x;
        this.y = y;
        g.setColor(colStatus[status]);
        g.drawRect(x, y,5, 35);
    }

    private void paint() {
        g.setColor(colStatus[status]);
        g.drawRect(x, y,5, 35);
    }

    public void tomar(){
        if(!uso) {
            status = 1;
            uso = true;
            c.repaint();
            notifyAll();
        }
    }

    public void soltar(){
        status = 0;
        uso = false;
        c.repaint();
        notifyAll();
    }

}
