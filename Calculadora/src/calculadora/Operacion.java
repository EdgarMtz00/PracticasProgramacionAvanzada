/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculadora;

/**
 *
 * @author Agustin
 */
public class Operacion {
    double r1, r2, i1, i2;
    final String[] op = {"+", "-", "*", "/"};
    public Operacion(String operacion){
        
    }
    
    public void parseOperacion(){
        for (String operador : op) {
            String[] terminos = operador.split(operador);
        }
    }
}
