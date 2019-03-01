/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operacionesmatrices;

import java.util.ArrayList;

/**
 *
 * @author Agustin
 */
public class Matriz {
    private double [][] matriz;
    private int indexF, indexC;
    public int fil, col;
    
    public Matriz(int fil, int col) {
        this.fil = fil;
        this.col = col;
        matriz = new double[fil][col];
    }
    
    public Matriz(double[][] matriz){
        this.matriz = matriz;
    }
    
    public Matriz suma(Matriz m1) {
        if(m1.fil == this.fil && m1.col == this.col){
            double[][] res = new double[this.fil][this.col];
            for(indexF = 0; indexF < fil; indexF++){
                for (indexC = 0; indexC < col; indexC++) {
                    res[indexF][indexC] = matriz[indexF][indexC] + m1.matriz[indexF][indexC];
                }
            }
            return new Matriz(res);
        }else{
            return null;
        }
    }
    
    public Matriz resta(Matriz m1){
        if(m1.fil == this.fil && m1.col == this.col){
            double[][] res = new double[this.fil][this.col];
            for(indexF = 0; indexF < fil; indexF++){
                for (indexC = 0; indexC < col; indexC++) {
                    res[indexF][indexC] = matriz[indexF][indexC] - m1.matriz[indexF][indexC];
                }
            }
            return new Matriz(res);
        }else{
            return null;
        }
    }
    /**
     * TODO: Arreglar esto
     * @param m1
     * @return 
     */
    public Matriz mult(Matriz m1){
        if(this.col == m1.fil){
            double[][] res = new double[this.fil][m1.col];
            for(indexF = 0; indexF < fil; indexF++){
                for (indexC = 0; indexC < col; indexC++) {
                    for (int i = 0; i < this.fil; i++) {
                        
                    }
                }
            }
            return new Matriz(res);
        }else{
            return null;
        }
    }
}
