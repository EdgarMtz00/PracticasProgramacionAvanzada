package calculadora;

public class NumComplejo {
    private double real, imaginario;

    public NumComplejo(double real, double imaginario) {
        this.real = real;
        this.imaginario = imaginario;
    }

    public double getReal() {
        return real;
    }

    public void setReal(double real) {
        this.real = real;
    }

    public double getImaginario() {
        return imaginario;
    }

    public void setImaginario(double imaginario) {
        this.imaginario = imaginario;
    }

    public void printComplex(){
        if(imaginario != 0.0f){
            if(imaginario > 0){
                System.out.print(real + " + " + imaginario + "i");
            }else{
                System.out.println(real + " " + imaginario + "i");
            }
        }else{
            System.out.print(real);
        }
    }
}
