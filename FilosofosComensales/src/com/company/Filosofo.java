package com.company;

import java.awt.*;

public class Filosofo implements Runnable{

    public int id;
    /*  Status:
        1: Blanco, Pensando.
        2: Amarillo, Esperando.
        3: Verde, Comiendo.
     */
    private int status;
    private static Color[] colStatus = {Color.white, Color.yellow, Color.green};

    public Filosofo(int id ){
        this.id = id;
        status = 0;
    }

    @Override
    public void run() {

    }

    public void paint(Graphics g, int x, int y) {
        int d = 35;
        g.setColor(colStatus[status]);
        g.drawOval(x - (d / 2),  y - (d / 2), d, d);
        System.out.println("x: " + x + "y: " + y);
    }

}
