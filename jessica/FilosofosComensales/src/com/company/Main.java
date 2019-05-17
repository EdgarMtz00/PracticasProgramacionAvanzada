package com.company;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
	// write your code here
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

        Canvas canvas = new Canvas(filosofos, palillos);
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
