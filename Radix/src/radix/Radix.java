/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package radix;

import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Agustin
 */
public class Radix {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        ArrayList<Integer> list = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingrese los numeros o n para salir");
        String input = "";
        do {
            input = scanner.next();
            if(input.matches("-?\\d+")){
                list.add(Integer.parseInt(input));
            }
        } while (!input.equals("n"));
        if(!list.isEmpty()){
            data sortedList = new data(list);
            System.out.println(sortedList);
        }
    }

    
    
}
