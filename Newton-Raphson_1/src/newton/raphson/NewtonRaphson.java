/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newton.raphson;

import java.util.Scanner;

/**
 *
 * @author Agustin
 */
public class NewtonRaphson {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        double[][] Numeros = new double[100][2];
        double MaxPot, Inicio, Error;
        Scanner scan = new Scanner(System.in);
        System.out.println("¿Cual es la potencia mas grande en su ecuacion?");
        MaxPot = scan.nextDouble();
        
        for (int i = 0; i <= MaxPot; i++) {
            System.out.println("Ingrese el valor de x^" + i);
            Numeros[i][0] = scan.nextDouble();
            Numeros[i][1] = i;
            
        }
        System.out.println("Ingrese el punto de partida para la ecuacion");
        Inicio = scan.nextDouble();
        System.out.println("Ingrese el error maximo");
        Error = scan.nextDouble();
        
        Calculo calculo = new Calculo(Numeros, MaxPot, Error);
        double x = calculo.operacion(Inicio);
        System.out.println(x);
    }
}
