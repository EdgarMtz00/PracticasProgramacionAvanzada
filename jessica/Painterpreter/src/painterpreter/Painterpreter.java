/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package painterpreter;

public class Painterpreter {
    public static void main(String[] args) {
        // TODO code application logic here
        Form form = new Form();
        form.setVisible(true);
        Interpreter interpreter = new Interpreter(form);
        form.setInterpreter(interpreter);
    }
}
