package com.company;

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

    public void setImaginario(String imaginario) {
        imaginario = imaginario.replace("i", "");
        this.imaginario = Integer.parseInt(imaginario);
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

    public NumComplejo multiplicar(NumComplejo b){
        res = new NumComplejo();
        res.real = (this.real * b.real) - (this.imaginario * b.imaginario);
        res.imaginario = (this.real * b.imaginario) + (this.imaginario * b.real);
        return res;
    }

    public String dividir(NumComplejo b)
    {
        NumComplejo conj = new NumComplejo(b.imaginario, (b.imaginario * -1.0));
        NumComplejo aNew = this.multiplicar(conj);
        NumComplejo bNew = b.multiplicar(conj);
        String out =  "("+aNew.getReal()+"/"+bNew.getReal();
        if(aNew.imaginario >= 0)
            out+="+";
        out += aNew.imaginario+"i/"+bNew.real+")";
        return out;
    }

    @Override
    public String toString(){
        // Los operadores ternarios son lo maximo
        return (imaginario == 0.0f)? Double.toString(real) : (imaginario > 0) ? real + " + " + imaginario + "i" : real + " " + imaginario + "i";
    }
}