/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculadora;

import java.util.Arrays;

/**
 *
 * @author Agustin
 */
public class Operacion {
    double realT1, imgT1, realT2, imgT2, realAns, imgAns;
    boolean issetT1, issetT2;
    final String[] op = {"+", "-", "*", "/"};
    
    public Operacion(){
        issetT1 = false;
        issetT2 = false;
    }

    public Operacion(double realT1, double imgT1, double realT2, double imgT2) {
        this.realT1 = realT1;
        this.imgT1 = imgT1;
        this.realT2 = realT2;
        this.imgT2 = imgT2;
        issetT1 = true;
        issetT2 = true;
    }
    
    public void parseOperacion(String operacion){
        String[] terminos;
        terminos = operacion.split(" ");
        if(terminos.length == 3){
            
        }
        System.out.println(Arrays.toString(terminos));
        
    }
    
    public boolean setT(double real, double img){
        if(!issetT1){
            realT1 = real;
            imgT1 = img;
            issetT1 = true;
            return true;
        }else if(!issetT2){
            realT2 = real;
            imgT2 = img;
            issetT2 = true;
            return true;
        }
        return false;
    }
}
