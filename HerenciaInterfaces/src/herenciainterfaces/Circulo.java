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
public class Circulo extends Coordenada{
    double r;

    public Circulo(double r, int x, int y) {
        super(x, y);
        this.r = r;
    }

    public Circulo() {
        this.r = 0;
    }

    public double getR() {
        return r;
    }

    public void setR(double r) {
        this.r = r;
    } 
    
    public double perimetro(){
        return (r!=0)?2*Math.PI*r:null;
    }
    
    public double area(){
        return (r!=0)?Math.PI*r*r:null;
    }
    
}
