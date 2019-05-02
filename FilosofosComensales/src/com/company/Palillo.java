package com.company;

import java.awt.*;

public class Palillo {
    public int id;
    /*  Status:
        1: Blanco, disponible.
        2: Rojo, Ocupado.
     */
    private int status;
    private static Color[] colStatus = {Color.white, Color.red};

    public Palillo(int id ){
        this.id = id;
        status = 0;
    }

    public void paint(Graphics g, int x, int y) {
        g.setColor(colStatus[status]);
        g.drawRect(x, y,5, 35);
        System.out.println("x: " + x + "y: " + y);
    }
}
