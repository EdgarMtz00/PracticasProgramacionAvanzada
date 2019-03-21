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
public class Cilindro implements Figura{
    double radio, altura;

    public Cilindro(double radio, double altura) {
        this.radio = radio;
        this.altura = altura;
    }
    
    public double getRadio() {
        return radio;
    }

    public void setRadio(double radio) {
        this.radio = radio;
    }

    public double getAltura() {
        return altura;
    }

    public void setAltura(double altura) {
        this.altura = altura;
    }

    @Override
    public double area() {
        return 2 * Math.PI * radio * altura;
    }

    public double volumen() {
        return Math.PI * radio * radio * altura;
    }
    @Override
    public String quienEres() {
        return "Cilindro";
    }
}
