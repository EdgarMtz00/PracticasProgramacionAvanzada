/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cadenasfracciones;

import java.util.Scanner;

/**
 *
 * @author Agustin
 */
public class CadenasFracciones {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        FraccionString fs = new FraccionString();
        String operacion;
        Scanner scan = new Scanner(System.in);
        System.out.println("Ingrese la operacion:");
        operacion = scan.nextLine();
        System.out.println(fs.resolver(operacion));
    }
    
}
