package com.company;

import java.awt.*;

public class Filosofo implements Runnable{
    private Palillo izq, der;
    private Graphics g;
    private int x, y;
    private long r;
    private Canvas c;
    Thread hilo;
    /*  Status:
        1: Blanco, Pensando.
        2: Amarillo, Esperando.
        3: Verde, Comiendo.
     */
    private int status;
    private static Color[] colStatus = {Color.white, Color.yellow, Color.green};
    public int id;
    Filosofo(int id){
        status = 0;
        this.id = id;
    }

    public void setPalillos(Palillo der, Palillo izq, Canvas c){
        this.der = der;
        this.izq = izq;
        this.c = c;
        hilo = new Thread(this);
        hilo.start();
    }

    private void pensar() throws InterruptedException {
        status = 0;
        c.repaint();
        r = (long) (Math.random()*7000);
        System.out.println("piensa");
        Thread.sleep(r);
        status++;
        c.repaint();
    }

    private void comer() throws InterruptedException {
        status = 2;
        der.tomar();
        izq.tomar();
        c.repaint();
        r = (long) (Math.random() * 7000);
        System.out.println("come");
        Thread.sleep(r);
        der.soltar();
        izq.soltar();
    }

    @Override
    public void run() {
        int j = 10;
        while (true) {
            try {
                pensar();
                synchronized (der) {
                    synchronized (izq) {
                        comer();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //j--;
        }
    }

    public void paint(Graphics g, int x, int y) {
        this.g = g;
        this.x = x;
        this.y = y;
        int d = 35;
        g.setColor(colStatus[status]);
        g.drawOval(x - (d / 2),  y - (d / 2), d, d);
    }
}
