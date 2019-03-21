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
public class Interfaces {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Figura x;
        x = new Circulo(10);
        System.out.println("area del " + x.quienEres() + ": " + x.area());
        x = new Cuadrado(5);
        System.out.println("area del " + x.quienEres() + ": " + x.area());
        x = new Cilindro(10,20);
        System.out.println("area del " + x.quienEres() + ": " + x.area());
        x = new Figura(){
            @Override
            public double area() {
                return 0; //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public String quienEres() {
                return "Nadie";
            }
        };
        System.out.println("area del " + x.quienEres() + ": " + x.area());
    }
}
