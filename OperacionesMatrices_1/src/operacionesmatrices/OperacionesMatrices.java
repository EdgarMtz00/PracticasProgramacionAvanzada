/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operacionesmatrices;

import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author Agustin
 */
public class OperacionesMatrices {
    Scanner scan = new Scanner(System.in);
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        OperacionesMatrices programa = new OperacionesMatrices();
        programa.run();
        
    }
    
    public void run (){
        Matriz m1;
        m1 = matrizInput("primera");
        System.out.println( m1.inversa() );       
    }
    
    public Matriz matrizInput(String texto){
        texto = (texto == null)? "" : texto;
        System.out.println("ingrese el tamaño de la " + texto + " matriz");
        String tamaño = scan.next();
        int [] tamañoInt = Arrays.stream(tamaño.split("x|\\*")).mapToInt(Integer::parseInt).toArray();
        Matriz matriz = new Matriz(tamañoInt[0], tamañoInt[1]);
        for (int indexF = 0; indexF < tamañoInt[0]; indexF++) {
            for (int indexC = 0; indexC < tamañoInt[1]; indexC++) {
                System.out.println("Ingrese el valor de la posicion [" + indexF + ", " + indexC + "]");
                double val = scan.nextInt();
                matriz.addElement(indexF, indexC, val);
            }
        }
        System.out.print(matriz);
        return matriz;
    }
}
