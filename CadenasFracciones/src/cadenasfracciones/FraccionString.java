/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cadenasfracciones;

import java.util.Arrays;

/**
 *
 * @author Agustin
 */
public class FraccionString {
    
    String[] stringNum;
    String[] stringDen;
    int[] intNum;
    int[] intDen;
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
        stringDen = new String[]{"enteros","medios","tercios","cuartos","quintos","sextos","septimos","octavos",
                                "novenos","decimos","centesimos"};
        intDen = new int[]{1,2,3,4,5,6,7,8,9,10,100};
    }
    
    public String FracToString(Fraccion x){
        String res=IntToString(x.num, false) + IntToString(x.den, true);
        if(x.num>1){
            res += "s";
        }
        return res;
    }
    
    public Fraccion StringToFrac(String x){
        Fraccion res = new Fraccion();
        
        return res;
    }
    
    public String IntToString(int x, boolean den){
        double[] digitos = {-1, -1, -1};
        int maxDigit = 2, num = x;
        String res = "";
        
        if(x == 0){
            return "cero";
        }
        
        while(x > 0){
            digitos[maxDigit] = (x % 10)*(100/Math.pow(10,maxDigit));
            x = x/10;
            if(digitos[maxDigit] == 20 || digitos[maxDigit] == 10){
                digitos[maxDigit] += digitos[maxDigit+1];
                digitos[maxDigit+1] = -1;
            }       
            digitos[maxDigit] = (digitos[maxDigit] == 0)? -1 : digitos[maxDigit];
            maxDigit--;
        }
        
        for(int i = 0; i < 3; i++){
            for (int j = 0; j < intNum.length; j++) {
                if(digitos[i] == intNum[j]){
                    res += stringNum[j];
                    if(digitos[i] == 100){
                        res += "to";
                    }else if( digitos[i] >= 30 && digitos[i+1] != 0 ){
                        res += " y ";
                    }
                    res += " ";
                }
            }
        }
        
        if(den){
            res = res.substring(0, res.length()-1);
            if(res.endsWith("a")){
                res = res.substring(0, res.length()-1);
            }
            res += "avo";
            for (int i = 0; i < intDen.length; i++) {
                if(num == intDen[i]){
                    res = stringDen[i];
                }
            }
            res = res.replaceAll("\\s", "");
        }
        return res;
    }
}
