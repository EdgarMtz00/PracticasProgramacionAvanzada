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
    
    Matriz(Matriz m, int pivRow, int pivCol)
    {
        int rows=0, cols=0;
        fil = m.fil-1;
        col = m.col-1;
        matriz = new double[fil][col];
        for (int i = 0; i < m.fil; i++) 
        {
            cols = 0;
            for (int j = 0; j < m.col; j++) 
            {
                if(i!=pivRow && j!=pivCol)
                {
                    this.matriz[rows][cols] = m.matriz[i][j];
                    cols++;
                }
            }
            if(i!=pivRow)
            {
                rows++;
            }
        }
}
    
    public void addElement(int posF, int posC, double val){
        matriz[posF][posC] = val;
    }
    
    @Override
    public String toString(){
        String mastringz = "";
        if(this!=null){
            for (indexF = 0; indexF < this.fil; indexF++) {
                mastringz += "|";
                for (indexC = 0; indexC < this.col; indexC++) {
                    mastringz += matriz[indexF][indexC];
                    mastringz += (indexC != this.col-1)? ", ": "";
                }
                mastringz +="|\n";
            }
        }else{
            mastringz = "no valido";
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
                    for (int i = 0; i < m1.fil; i++) {
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
            
            for (int i = 0; i< this.fil; i++) {
                double piv = res[i][i];
                double[][] mult = new double[this.fil][this.col];
                
                for (int j = 0; j < mult.length; j++) {
                    mult [j][0]= res[j][i];
                }
                
                for (indexC = 0; indexC < this.col*2; indexC++) {
                    res[i][indexC] = res[i][indexC] / piv;
                }
                
                for (indexF = 0; indexF < this.fil; indexF++) {
                    for ( indexC = 0; indexC < this.col * 2; indexC++) {
                        res[indexF][indexC] = (indexF != i) ? res[indexF][indexC] - res[i][indexC]*mult[indexF][0] : res[indexF][indexC];
                        if(Double.isNaN(res[indexF][indexC]) || Double.isInfinite(res[indexF][indexC])){
                            return null;
                        }
                    }
                }
            }
            
            return new Matriz(res);
        }else{
            return null;
        }
    }
    
    /*public double determinante(){
        double det;
        
    }*/

    
    public double det(Matriz m1){
        
        if(m1.fil == 1 && m1.fil == m1.col){
            return m1.matriz[0][0];
        }else if(m1.fil==2 && fil == col){
            return m1.matriz[0][0] * m1.matriz[1][1] - m1.matriz[0][1] * m1.matriz[1][0];
        }else if(m1.fil == m1.col){
            double acumulate = 0.0;
            for(int i=0; i < m1.fil; i++){
                double sign=1.0;
                if(i%2!=0){
                    sign = -1.0; //Si el rowCount es impar Y el pivote está en posición 
                }
                Matriz temp = new Matriz(m1,0,i);
                acumulate += (sign * m1.matriz[0][i] * det(temp));
            }
            return acumulate;
        }
    return 0;
    }
}
