/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ecuacion2dogrado;

import java.util.Scanner;

/**
 *
 * @author Agustin
 */
public class Ecuacion2doGrado {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        float[] var = {1, 2, 3};
        Scanner scan = new Scanner(System.in);
        Ecuacion solver;
        NumComplex[] results;
        
        for(int i = 0; i < 3; i++){
            System.out.println("Ingrese el " + var[i] + "° dato: ");
            var[i] = scan.nextInt();
        }
        
        solver = new Ecuacion(var);
        results = solver.solve();
        
        int i = 1;
        for(NumComplex result : results){
            System.out.print("\nx" + i + " = ");
            result.printComplex();
            i++;
        }
    }
}
