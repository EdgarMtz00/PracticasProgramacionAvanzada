package cena;

import java.util.ArrayList;

public class Cena {
    public static void main(String[] args) {
        ArrayList<Filosofo> filosofos = new ArrayList<>();
        ArrayList<Palillo> palillos = new ArrayList<>();
        Filosofo f;
        Palillo p;

        for (int i = 0; i < 5; i++){
            p = new Palillo();
            f = new Filosofo();
            filosofos.add(f);
            palillos.add(p);
        }

        Form canvas = new Form(filosofos, palillos);
        int anterior;
        for (int i = 0; i < 5; i++){
            if(i != 0){
                anterior = i - 1;
            }else{
                anterior = 4;
            }
            filosofos.get(i).setPalillos(palillos.get(i), palillos.get(anterior), canvas);
            palillos.get(i).setCanvas(canvas);
        }
    }  
}
