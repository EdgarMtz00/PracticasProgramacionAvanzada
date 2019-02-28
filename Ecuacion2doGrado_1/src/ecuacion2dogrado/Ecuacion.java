/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ecuacion2dogrado;

/**
 *Clase para resolver la chicharronera y regresar los resultados en un arreglo
 * @author Agustin
 */
public class Ecuacion {
    float[] var;
    NumComplex x1, x2;
    
    /**
     * @param var Arreglo que contiene los datos A, B y C
     * Inicializa x1 y x2
     */
    public Ecuacion(float [] var){
        this.var = var;
        x1 = new NumComplex();
        x2 = new NumComplex();
    }
    
    /**
     * Utiliza la formula cuadratica ya sea para obtener los resultados reales o imaginarios
     * @return 
     * regresa un arreglo de numeros complejos cono x1 y x2 en ese orden
     */
    public NumComplex[] solve(){
        NumComplex[] results = new NumComplex[2];
        float disc = var[1] * var[1] - 4 * var[0] * var[2];
        if(disc >= 0){
            x1.real = (float)(-var[1] + Math.pow(disc, 0.5)) / (2 * var[0]);
            x2.real = (float)(-var[1] - Math.pow(disc, 0.5)) / (2 * var[0]);
        }else{
            x1.real = -var[1] / (2 * var[0]);
            x1.imag = (float) Math.pow(Math.abs(disc), 0.5) / (2 * var[0]);
            x2.real = -var[1] / (2 * var[0]);
            x2.imag = (float) -(Math.pow(Math.abs(disc), 0.5) / (2 * var[0]));
        }
        results[0] = x1;
        results[1] = x2;
        return results;
    }
}
