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
            f = new Filosofo(i);
            filosofos.add(f);
            palillos.add(p);
        }

        Canvas canvas = new Canvas(filosofos, palillos);

        for (int i = 0; i < 5; i++){
            int prevI =(i != 0)? i - 1 : 4;
            filosofos.get(i).setPalillos(palillos.get(i), palillos.get(prevI), canvas);
            palillos.get(i).setCanvas(canvas);
        }
    }
}
