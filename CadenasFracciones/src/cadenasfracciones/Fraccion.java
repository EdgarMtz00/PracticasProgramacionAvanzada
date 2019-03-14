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
public class Fraccion {
    int num, den;
    
    public Fraccion(){
        num = 1;
        den = 1;
    }
    
    public Fraccion(int num, int den){
        if(den != 0){
            this.num = num;
            this.den = den;
        }else{
            System.out.println("");
        }
    }
    
    public Fraccion suma(Fraccion f1){
        int rNum = num * f1.den + f1.num * den;
        int rDen = den * f1.den;
        return new Fraccion(rNum, rDen);
    }
    
    public Fraccion resta(Fraccion f1){
        int rNum = num * f1.den - f1.num * den;
        int rDen = den * f1.den;
        return new Fraccion(rNum, rDen);
    }
    
    public Fraccion mult(Fraccion f1){
        int rNum = num * f1.num;
        int rDen = den * f1.den;
        return new Fraccion(rNum, rDen);
    }
    
    public Fraccion div(Fraccion f1){
        int rNum = num * f1.den;
        int rDen = den * f1.num;
        return new Fraccion(rNum, rDen);
    }
    
    @Override
    public String toString(){
        return num + " / " + den;
    }
}
