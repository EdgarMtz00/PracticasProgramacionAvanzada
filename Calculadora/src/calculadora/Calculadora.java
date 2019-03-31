/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculadora;

/**
 *
 * @author agust
 */
public class Calculadora {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Formulario formulario = new Formulario();
        formulario.setVisible(true);
        Operacion operacion = new Operacion();
        operacion.parseOperacion("35 + 23 - 1");
    }
    
}
