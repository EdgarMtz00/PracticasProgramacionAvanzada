/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package painterpreter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import javax.swing.Timer;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Agustin
 */
public class Interpreter implements Runnable{
    private final String[] operadores = new String[]{"+", "-", "*", "/"};
    private final String[] comparadores = new String[]{"<", ">", "=="};
    String codigo;
    HashMap vars;
    String outputMsg;
    String[] commands;
    String[] functions = {"up", "down", "left", "right", "if", "endif",
        "for", "endfor", "dab", "squat", "moveLeftArm", "moveRightArm",
        "moveLeftLeg", "moveRightLeg"};
    HashMap<String, Integer> cicles;
    Dummy dummy = new Dummy();
    Form form;

    public Interpreter(String code, Form form){
        this.form = form;
        form.setDummy(dummy);
        commands = code.split("\n");
        System.out.println(Arrays.toString(commands));
        vars = new HashMap();
        cicles = new HashMap();
    }
    
    public Interpreter(Form form){
        this.form = form;
        this.form.setDummy(dummy);
        vars = new HashMap();
        cicles = new HashMap();
    }
    
    public void setCode(String code){
        commands = code.split("\n");
    }
    
    public boolean compile(){
        int i = 0;
        for(String command : commands){
            command = command.trim();
            String[] terms = command.split(" ");
            if(Arrays.stream(functions).anyMatch(terms[0]::equals)){
                switch(command.split(" ")[0]){
                    case "if":
                        if(command.endsWith(":")){
                            String c = command.substring(3, command.indexOf(":"));
                            System.out.println(c);
                            if(condition(c, i)==2){
                                outputMsg = "ERROR AT:\n" + command;
                                return false;
                            }
                        }
                        break;
                    case "for":
                        if(command.endsWith(":")){
                            String n = command.substring(4, command.indexOf(":"));
                            System.out.println(n);
                            if(!isIntOrVar(n.trim(), i)){
                                outputMsg = "ERROR AT:\n" + command;
                                return false;
                            }
                        }
                        break;
                    default:
                        if(terms.length > 1){
                            outputMsg = "UNKNOWN FUNCTION AT:\n" + command;
                            return false;
                        }
                }
            }else{
                if((terms.length != 3 || terms[0].matches("-?\\d+(\\.\\d+)?") 
                        || !terms[1].equals("=")) || !isIntOrVar(terms[2], i)){
                    outputMsg = "UNKNOWN COMMAND AT:\n" + command;
                    return false;
                }else{
                    vars.put(terms[0], Integer.parseInt(terms[2]));
                }
            }
            i++;
        }
        outputMsg = "BUILD SUCCESSFUL";
        return true;
    }

    @Override
    public void run(){
        System.out.println("run");
        if(compile()){
            for(int i = 0; i < commands.length; i++){
                String[] terms = commands[i].split(" ");
                System.out.println(Arrays.toString(terms));
                switch(terms[0]){
                    case "for":
                        String n = commands[i].substring(4, commands[i].indexOf(":"));
                        Integer indexf = i;
                        if(vars.containsKey(n)){
                            cicles.put(indexf.toString(), (Integer)vars.get(n));
                        }else{
                            cicles.put(indexf.toString(), Integer.parseInt(n));
                        }
                        break;
                    case "if":
                        String c = commands[i].substring(3, commands[i].indexOf(":"));
                        System.out.println(c);
                        if(condition(c, i) == 0){
                            System.out.println(i);
                            int index = indexOfEndif(i);
                            i = index;
                            System.out.println(i);
                        }
                        break;
                    case "up":
                        dummy.move(Dummy.VERTICAL, Dummy.DECREASE);
                        System.out.println("up");
                        break;
                    case "down":
                        dummy.move(Dummy.VERTICAL, Dummy.INCREASE);
                        form.repaint();
                        System.out.println("down");
                        break;
                    case "left":
                        dummy.move(Dummy.HORIZONTAL, Dummy.DECREASE);
                        System.out.println("left");
                        break;
                    case "right":
                        dummy.move(Dummy.HORIZONTAL, Dummy.INCREASE);
                        break;
                    case "moveLeftArm":
                        dummy.moveLeftArm();
                        break;
                    case "moveRightArm":
                        dummy.moveRightArm();
                        break;
                    case "moveLeftLeg":
                        dummy.moveLeftLeg();
                        break;
                    case "moveRightLeg":
                        dummy.moveRightLeg();
                        break;
                    case "endif":
                        break;
                    case "endfor":
                        for(String key : cicles.keySet()){
                            if(cicles.get(key) != 0){
                                i = Integer.parseInt(key);
                                System.out.println(i);
                                cicles.put(key, cicles.get(key) - 1);
                            }
                        }
                    default:
                        break;
                }
                form.repaint();
                System.out.println("form repaint");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    private int indexOfEndif(int indexIf){
        int ifCount = 0;
        indexIf++;
        for(int i = indexIf; i < commands.length; i++){
            if(commands[i].equals("endif")){
                if(ifCount == 0){
                    return i;
                }else{
                    ifCount--;
                }
            }else if(commands[i].split(" ")[0].equals("if")){
                ifCount++;
            }
        }
        return commands.length;
    }
    
    private int condition(String condition, int i){
        for(String comparador : comparadores){
            if (condition.contains(comparador)) {
                int t1, t2;
                String[] terms = condition.split(comparador);
                System.out.println("temrs:" + Arrays.toString(terms));
                terms[0] = terms[0].trim();
                terms[1] = terms[1].trim();
                if(isIntOrVar(terms[0], i) && isIntOrVar(terms[1], i)) {
                    t1 = (vars.containsKey(terms[0]) ?
                            (int) vars.get(terms[0]) : Integer.parseInt(terms[0]));
                    t2 = (vars.containsKey(terms[1]) ?
                            (int) vars.get(terms[1]) : Integer.parseInt(terms[1]));
                    switch (comparador) {
                        case "<":
                            return (t1 < t2) ? 1 : 0;
                        case ">":
                            return (t1 > t2) ? 1 : 0;
                        case "=":
                            return (t1 == t2) ? 1 : 0;
                    }
                }
            }
        }
        return 2;
    }
    //TODO: Remplazar operacion y aceptar suma
    private boolean isIntOrVar(String x, int cPos){
        String res = null;
        System.out.println("is int or var" + x);
        for(String operador : operadores){
            if(x.contains(operador)){
                String op = operador;
                String[] operacion = x.split(op);
                operacion[0] = operacion[0].trim();
                operacion[1] = operacion[1].trim();
                switch (operador){
                    case "+":
                        res = String.valueOf((Integer.parseInt(operacion[0]) + Integer.parseInt(operacion[1])));
                        break;
                    case "-":
                        res = String.valueOf((Integer.parseInt(operacion[0]) - Integer.parseInt(operacion[1])));
                        break;
                    case "*":
                        res = String.valueOf((Integer.parseInt(operacion[0]) * Integer.parseInt(operacion[1])));
                        break;
                    case "/":
                        res = String.valueOf((Integer.parseInt(operacion[0]) / Integer.parseInt(operacion[1])));
                        break;
                }
                if(res != null) {
                    commands [cPos] = commands[cPos].replace(x, res);
                    System.out.println("numero" + commands[cPos]);
                    return true;
                }
            }
        }
        if(x.matches("-?\\d+(\\.\\d+)?")){
            System.out.println("true");
            return true;
        }else{
            return vars.containsKey(x);
        }
    }
}