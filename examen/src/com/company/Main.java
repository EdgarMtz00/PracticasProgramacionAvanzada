package com.company;

import java.awt.*;

class Hilo extends Thread
{
    public void run()
    {
        for (int i = 0; i < 100; i++){
            System.out.println("Hilo" + i);
        }
    }
}

public class Main {

    public static void main(String[] args) {
	// write your code here
        Frame frame = new  frame();
    }
}
