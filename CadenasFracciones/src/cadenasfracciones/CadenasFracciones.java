/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cadenasfracciones;

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
        System.out.println(fs.FracToString(new Fraccion(1, 16)));
    }
    
}
