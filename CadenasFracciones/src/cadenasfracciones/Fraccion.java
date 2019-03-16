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
    boolean divCero = false;
    
    public Fraccion(){
        num = 1;
        den = 1;
    }
    
    public Fraccion(int num, int den){
        if(den != 0){
            this.num = num;
            this.den = den;
        }else{
            System.out.println("error");
            divCero = true;
        }
    }
    
    public Fraccion suma(Fraccion f1){
        int rNum = (den != f1.den)?num * f1.den + f1.num * den : num + f1.num;
        int rDen = (den != f1.den)?den * f1.den : den;
        return new Fraccion(rNum, rDen);
    }
    
    public Fraccion resta(Fraccion f1){
        int rNum = (den != f1.den)?num * f1.den - f1.num * den : num - f1.num;
        int rDen = (den != f1.den)?den * f1.den : den;
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
