/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newton.raphson;

/**
 *
 * @author Agustin
 */


public class Calculo {
   public double[][] numeros, derivada;
   public double error, inicio, maxPot;
   
   public Calculo(double[][] numeros, double maxPot, double error){
       this.numeros = numeros;
       this.derivada = new double[100][2];
       this.error = error;
       this.maxPot = maxPot;
       for(int i = 1; i <= maxPot; i++){
            derivada[i-1][0] = numeros[i][0] * numeros[i][1];
            derivada[i-1][1] = numeros[i][1] - 1;
        }
   }
   
   public double operacion(double xAnt){
        double x = xAnt;
        double opRes = 0;
        double deRes = 0;
        for(int i = 0; i <= maxPot; i++){
           opRes += numeros[i][0] * Math.pow(x, numeros[i][1]);
           deRes += derivada[i][0] * Math.pow(x, derivada[i][1]);
        }
            System.out.println("x = " + x + " opRes = " + opRes + " deRes = " + deRes);
        x = x - opRes/deRes;
        if(Math.abs(opRes) <= error){
            return xAnt;
        }else{
            return operacion(x);
        }
   }
}
