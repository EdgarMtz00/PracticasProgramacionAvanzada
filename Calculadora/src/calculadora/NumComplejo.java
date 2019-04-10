package calculadora;

public class NumComplejo {
    private double real, imaginario;
    private NumComplejo res;

    public NumComplejo(double real, double imaginario) {
        this.real = real;
        this.imaginario = imaginario;
    }

    public NumComplejo(){
        this.real = 0;
        this.imaginario = 0;
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

    public NumComplejo suma(NumComplejo b){
        res = new NumComplejo();
        res.real = this.real + b.real;
        res.imaginario = this.imaginario + b.imaginario;
        return res;
    }

    public NumComplejo resta(NumComplejo b){
        res = new NumComplejo();
        res.real = this.real - b.real;
        res.imaginario = this.imaginario - b.imaginario;
        return res;
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
