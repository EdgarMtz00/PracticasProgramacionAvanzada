package cena;

import java.awt.*;

public class Filosofo implements Runnable{
    private Palillo izq, der;
    private Graphics g;
    private int x, y;
    private long r;
    private Form c;
    private boolean hambre;
    Thread hilo;

    private int status;
    private static Color[] color = {Color.BLACK, Color.GRAY, Color.RED};

    Filosofo(){
        hambre = false;
        status = 0;
    }

    public void setPalillos(Palillo der, Palillo izq, Form c){
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
        hambre = true;
        status++;
        c.repaint();
    }

    private void comer() throws InterruptedException {
        status = 2;
        der.agarrar();
        izq.agarrar();
        c.repaint();
        r = (long) (Math.random() * 5000);
        Thread.sleep(r);
        hambre = false;
        der.dejar();
        izq.dejar();
    }

    @Override
    public void run() {
        while (true) {
            try {
                if(!hambre) {
                    pensar();
                }
                synchronized (der) {
                    if(!izq.ocupado) {
                        synchronized (izq) {
                            comer();
                        }
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void paint(Graphics g, int x, int y) {
        this.g = g;
        this.x = x;
        this.y = y;
        int d = 35;
        g.setColor(color[status]);
        g.drawOval(x - (d / 2),  y - (d / 2), d, d);
    }
}
