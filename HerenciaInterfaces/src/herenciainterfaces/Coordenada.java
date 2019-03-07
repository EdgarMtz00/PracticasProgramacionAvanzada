/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package herenciainterfaces;

/**
 *
 * @author Agustin
 */
public class Coordenada {
    protected int x, y;

    public Coordenada() {
        x = 0;
        y = 0;
    }

    public Coordenada(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")"; //To change body of generated methods, choose Tools | Templates.
    }
    
    

}
