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
             f = new Filosofo(i);
             p = new Palillo(i);
             filosofos.add(f);
             palillos.add(p);
        }
        Canvas canvas = new Canvas(filosofos, palillos);
    }
}
