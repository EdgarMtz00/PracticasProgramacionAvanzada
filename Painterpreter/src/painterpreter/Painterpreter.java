/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package painterpreter;

/**
 *
 * @author Agustin
 */
public class Painterpreter {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Form form = new Form();
        form.setVisible(true);
        Interpreter interpreter = new Interpreter(form);
        form.setInterpreter(interpreter);
        
        //System.out.println(interpreter.compile());
        //interpreter.run();
        //System.out.println(interpreter.outputMsg);
    }
    
}
