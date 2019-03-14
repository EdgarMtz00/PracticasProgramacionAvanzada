/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cadenasfracciones;

import java.util.Arrays;
import java.util.HashMap;

/**
 *
 * @author Agustin
 */
public class FraccionString {
    
    String[] partes;
    String[] stringNum;
    String[] numeros_sD;
    int[] intNum;
    int[] numeros_iD;
    int x;
    

    public FraccionString() {
        stringNum = new String[]{"cero","un","dos","tres","cuatro","cinco","seis",
                                "siete","ocho","nueve","diez","once","doce",
                                "trece","catorce","quince","dieciseis","diecisiete","dieciocho","diecinueve","veinte",
                                "veintiun","veintidos","veintitres","veinticuatro","veinticinco","veintiseis","veintisiete",
                                "veintiocho","veintinueve","treinta","cuarenta","cincuenta","sesenta","setenta",
                                "ochenta","noventa","cien"};
        intNum = new int[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,
                                22,23,24,25,26,27,28,29,30,40,50,60,70,80,90,100};
        numeros_sD = new String[]{"enteros","medios","tercios","cuartos","quintos","sextos","septimos","octavos",
                                "novenos","decimos","centesimos"};
        numeros_iD = new int[]{1,2,3,4,5,6,7,8,9,10,100};
    }
    
    public String FracToString(Fraccion x){
        String res="";
        
        return res;
    }
    
    public Fraccion StringToFrac(String x){
        Fraccion res;
        
        return res;
    }
    
    public String IntToString(int x, boolean den){
        double[] digitos = new double[3];
        int i=0;
        String res = "";
        
        while(x > 0){
            digitos[i] = (x % 10)*(100/Math.pow(10,i));
            x = x/10;
            i++;
        }
        System.out.println(Arrays.toString(digitos));
        
        if(!den){
            for(i = 0; i < digitos.length; i++){
                for (int j = 0; j < stringNum.length; j++) {
                    
                }
            }
        }
    }
}
