/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operacionesmatrices;

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
        this.fil = matriz.length;
        this.col = matriz[0].length;
    }
    
    public void addElement(int posF, int posC, double val){
        matriz[posF][posC] = val;
    }
    
    @Override
    public String toString(){
        String mastringz = "";
        for (indexF = 0; indexF < this.fil; indexF++) {
            mastringz += "|";
            for (indexC = 0; indexC < this.col; indexC++) {
                mastringz += matriz[indexF][indexC];
                mastringz += (indexC != this.col-1)? ", ": "";
            }
            mastringz +="|\n";
        }
        return mastringz;
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

    public Matriz mult(Matriz m1){
        if(this.col == m1.fil){
            double[][] res = new double[this.fil][m1.col];
            for(indexF = 0; indexF < fil; indexF++){
                for (indexC = 0; indexC < col; indexC++) {
                    for (int i = 0; i < 3; i++) {
                        res[indexF][indexC] += this.matriz[indexF][i] * m1.matriz[i][indexC];
                    }
                }
            }
            return new Matriz(res);
        }else{
            return null;
        }
    }
    
    public Matriz traspuesta(){
        if(this.fil == this.col){
            double [][] res = new double[this.col][this.fil];
            for (indexF = 0; indexF < this.fil; indexF++) {
                for (indexC = 0; indexC < this.col; indexC++) {
                    res[indexF][indexC] = this.matriz[indexC][indexF];
                }
            }
            return new Matriz(res);
        }else{
            return null;
        }
    }
    
    public Matriz inversa(){
        if(this.fil == this.col){
            double [][] res = new double [this.fil][this.col*2];
            for (indexC = 0; indexC < this.col*2; indexC++) {
                for (indexF = 0; indexF < this.fil; indexF++) {
                    res[indexF][indexC] = (indexC < this.col) ? this.matriz[indexF][indexC] : (indexC - this.col != indexF) ? 0 : 1;
                }
            }
            
            System.out.println(new Matriz(res));
            System.out.println("");
            
            for (int i = 0; i< this.fil; i++) {
                double piv = res[i][i];
                double[][] mult = new double[this.fil][this.col];
                
                
                for (int j = 0; j < mult.length; j++) {
                    mult [j][0]= res[j][i];
                }
                
                //System.out.println(new Matriz(mult));
                for (indexC = 0; indexC < this.col*2; indexC++) {
                    res[i][indexC] = res[i][indexC] / piv;
                }
                
                
                for (indexF = 0; indexF < this.fil; indexF++) {
                    for ( indexC = 0; indexC < this.col * 2; indexC++) {
                        res[indexF][indexC] = (indexF != i) ? res[indexF][indexC] - res[i][indexC]*mult[indexF][0] : res[indexF][indexC];
                    }
                }
                System.out.println(new Matriz(res) + "\n");

            }
            
            return new Matriz(res);
        }else{
            return null;
        }
    }
    
    /*public double determinante(){
        double det;
        
    }*/
    
    public void luDecomposition(){
        if(this.fil == this.col){
            int n = this.fil;
            double[][] lower = new double[n][n];
            double[][] upper = new double[n][n];
            
            for (int i = 0; i < n; i++) { 
                for (int k = i; k < n; k++) { 
                    int sum = 0; 
                    for (int j = 0; j < i; j++) 
                        sum += (lower[i][j] * upper[j][k]); 
                    upper[i][k] = matriz[i][k] - sum; 
                } 

                for (int k = i; k < n; k++) { 
                    if (i == k){ 
                        lower[i][i] = 1;
                    }else { 
                        int sum = 0; 
                        for (int j = 0; j < i; j++){ 
                            sum += (lower[k][j] * upper[j][i]); 
                        }
                        lower[k][i] = (matriz[k][i] - sum) / upper[i][i]; 
                    } 
                }
            }
            System.out.println("Lower: \n" + new Matriz(lower));
            System.out.println("Upper: \n" + new Matriz(upper));
            double res = 0;
            for (int i = 0; i < n; i++) {
                res *= upper[i][i];
            }
            System.out.println("res: " + res);
        }
    }
    
    public void det(){
        
    }
}
