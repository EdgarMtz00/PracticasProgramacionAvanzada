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
public class Cilindro extends Circulo{
    double h;

    public Cilindro(double h, double r, int x, int y) {
        super(r, x, y);
        this.h = h;
    }
    
    public Cilindro(){
        this.h = 0;
        r = 0;
    }
    
    
    @Override
    public double area(){
        return (r != 0 || h != 0) ? (super.perimetro() * h) + (2 * super.area()) : null;
    }
    
    public double volumen(){
        return (r != 0 || h != 0) ? (super.area()* h) : null;
    }
    
}
