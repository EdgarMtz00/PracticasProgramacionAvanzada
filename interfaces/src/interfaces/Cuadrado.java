/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

/**
 *
 * @author Agustin
 */
public class Cuadrado implements Figura{
    double lado;

    public Cuadrado(double lado) {
        this.lado = lado;
    }
    
    public double getLado() {
        return lado;
    }

    public void setLado(double lado) {
        this.lado = lado;
    }
    
    @Override
    public double area() {
        return lado * lado;
    }

    @Override
    public String quienEres() {
        return "Cuadrado";
    }
    
}
