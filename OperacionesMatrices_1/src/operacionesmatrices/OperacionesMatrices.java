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
    Matriz m1, m2;
    int menu = 1;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        OperacionesMatrices programa = new OperacionesMatrices();
        programa.run();
        
    }
    
    public void run (){
        while(menu!=0){
            System.out.println("1.-Suma\n2.-Resta\n3.-Multiplicacion\n4.-Traspuesta\n5.-Inversa\n6.-Determinante\n0.-Salir");       
            menu = scan.nextInt();
            switch(menu){
                case 1:
                    m1 = matrizInput("primera");
                    m2 = matrizInput("segunda");
                    System.out.println("\n res:\n" + m1.suma(m2));
                    break;
                case 2:
                    m1 = matrizInput("primera");
                    m2 = matrizInput("segunda");
                    System.out.println("\n res:\n" + m1.resta(m2));
                    break;
                case 3:
                    m1 = matrizInput("primera");
                    m2 = matrizInput("segunda");
                    System.out.println("\n res:\n" + m1.mult(m2));
                    break;
                case 4:
                    m1 = matrizInput(null);
                    System.out.println("\n res:\n" + m1.traspuesta());
                    break;
                case 5:
                    m1 = matrizInput(null);
                    System.out.println("\n res:\n" + m1.inversa());
                    break;
                case 6:
                    m1 = matrizInput(null);
                    System.out.println("\n res:\n" + m1.det(m1));
                    break;
            }
        }
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
