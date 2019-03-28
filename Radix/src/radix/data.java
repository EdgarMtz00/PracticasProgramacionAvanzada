/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package radix;

import java.util.ArrayList;

/**
 *
 * @author agust
 */
public class data {
    ArrayList<Integer> coleccion = new ArrayList<>(); 
    
   public data(){
   }
   
   public data(ArrayList<Integer> coleccion){
       this.coleccion = coleccion;
       radixSort();
   }
   
   public void radixSort(){
       Integer max = coleccion.get(0), exponent = 1; 
       for (int data : coleccion) 
       {
           max = (data > max)?data:max;
       }
       do{
           exponent*=10;
           ArrayList<Integer> temp = new ArrayList<>();
           for (int i = 0; i <=9; i++) {
               for (int data : coleccion){
                   if((data % exponent - (data %(exponent/10)))/(exponent/10) ==i ){
                       temp.add(data);
                   }
               }
           }
           this.coleccion = temp;
        }while((max%exponent)!=max);
   }
   
   @Override
   public String toString(){
       String res = "";
       res = coleccion.stream().map((data) -> data + "\n").reduce(res, String::concat);
       return res;
   }
}
