package interprete;

public class Interprete {
    public static void main(String[] args) {
        Form form = new Form();
        form.setVisible(true);
        Compilador interpreter = new Compilador(form);
        form.setInterpreter(interpreter);
    }
    
}
