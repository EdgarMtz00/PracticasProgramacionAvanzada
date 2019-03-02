/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cadenasfracciones;

import java.util.HashMap;

/**
 *
 * @author Agustin
 */
public class FraccionString {
    HashMap<String, Integer> numeros;
    
    public FraccionString(){
        numeros.put("cero", 0);
        numeros.put("uno", 1);
        numeros.put("dos", 2);
        numeros.put("tres", 3);
        numeros.put("cuatro", 4);
        numeros.put("cinco", 5);
        numeros.put("seis", 6);
        numeros.put("siete", 7);
        numeros.put("ocho", 8);
        numeros.put("nueve", 9);
        numeros.put("diez", 10);
        
        // 11-20
        numeros.put("once", 11);
        numeros.put("doce", 12);
        numeros.put("trece", 13);
        numeros.put("catorce", 14);
        numeros.put("quince", 15);
        numeros.put("dieci", 10);
        numeros.put("veinte", 20);
        
        numeros.put("veinti", 20);
        
        // 30-100
        numeros.put("treinta", 30);
        numeros.put("cuarenta", 40);
        numeros.put("cincuenta", 50);
        numeros.put("sesenta", 60);
        numeros.put("setenta", 70);
        numeros.put("ochenta", 80);
        numeros.put("noventa", 90);
        
        numeros.put("quinientos", 500);
        numeros.put("milesimos", 1000);
        
        // multiplicativos
        numeros.put("cientos", 100);
        numeros.put("mil", 1000);
        numeros.put("centesimo", 100);
        numeros.put("milesimo", 1000);
        
        // 100
        numeros.put("ciento", 100);
        numeros.put("cien", 100);
        
        
        // denominadores
        numeros.put("entero", 1);
        numeros.put("medio", 2);
        numeros.put("tercio", 3);
        numeros.put("cuarto", 4);
        numeros.put("quinto", 5);
        numeros.put("sexto", 6);
        numeros.put("septimo", 7);
        numeros.put("octavo", 8);
        numeros.put("noveno", 9);
        numeros.put("decimo", 10);
        
    }
}
