/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ecuacion2dogrado;

/**
 *
 * @author Agustin
 * Clase para almacenar y mostrar numeros complejos formados por dos float
 */
public class NumComplex {
    float real, imag;
    
    public NumComplex(){
        real = 0;
        imag = 0;
    }
    
    /**
    *    Da formato e imprime sin saltar de linea el numero complejo
    */
    public void printComplex(){
        if(imag != 0.0f){
            if(imag > 0){
                System.out.print(real + " + " + imag + "i");
            }else{
                System.out.println(real + " " + imag + "i");
            }
        }else{
            System.out.print(real);
        }
    }
}
