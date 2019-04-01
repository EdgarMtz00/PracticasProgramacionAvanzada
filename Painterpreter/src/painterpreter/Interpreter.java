/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package painterpreter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Agustin
 */
public class Interpreter {
    String codigo;
    HashMap vars;
    String outputMsg;
    String [] commands;
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
        for(String command : commands){
            command = command.trim();
            String[] terms = command.split(" ");
            if(Arrays.stream(functions).anyMatch(terms[0]::equals)){
                switch(command.split(" ")[0]){
                    case "if":
                        if(command.endsWith(":")){
                            String c = command.substring(3, command.indexOf(":"));
                            System.out.println(c);
                            if(condition(c)==2){
                                outputMsg = "ERROR AT:\n" + command;
                                return false;
                            }
                        }
                        break;
                    case "for":
                        if(command.endsWith(":")){
                            String n = command.substring(4, command.indexOf(":"));
                            System.out.println(n);
                            if(!isIntOrVar(n)){
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
                        || !terms[1].equals("=")) || !isIntOrVar(terms[2])){
                    outputMsg = "UNKNOWN COMMAND AT:\n" + command;
                    return false;
                }else{
                    vars.put(terms[0], Integer.parseInt(terms[2]));
                }
            }
            
        }
        outputMsg = "BUILD SUCCESSFUL";
        return true;
    }
    
    public void run(){
        if(compile()){
            dummy = new Dummy();
            form.setDummy(dummy);
                for(Integer i = 0; i < commands.length; i++){
                    String[] terms = commands[i].split(" ");
                    switch(commands[i].split(" ")[0]){
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
                            System.out.println(condition(c));
                            if(condition(c) == 0){
                                System.out.println(i);
                                int index = indexOfEndif(i);
                                i = index;
                                System.out.println(i);
                            }
                            break;
                        case "up":
                            dummy.move(Dummy.VERTICAL, Dummy.DECREASE);
                            form.repaint();
                            System.out.println("up");
                            break;
                        case "down":
                            dummy.move(Dummy.VERTICAL, Dummy.INCREASE);
                            System.out.println("down");
                            form.repaint();
                            break;
                        case "left":
                            dummy.move(Dummy.HORIZONTAL, Dummy.DECREASE);
                            form.repaint();
                            System.out.println("left");
                            break;
                        case "right":
                            dummy.move(Dummy.HORIZONTAL, Dummy.INCREASE);
                            form.repaint();
                            break;
                        case "moveLeftArm":
                            dummy.moveLeftArm();
                            form.repaint();
                            break;
                        case "moveRightArm":
                            dummy.moveRightArm();
                            form.repaint();
                            break;
                        case "moveLeftLeg":
                            dummy.moveLeftLeg();
                            form.repaint();
                            break;
                        case "moveRightLeg":
                            dummy.moveRightLeg();
                            form.repaint();
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
                }
        }
    }
    
    private void delay() throws InterruptedException{
        Thread.sleep(500);
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
    
    private int condition(String condition){
        String[] terms = condition.split(" ");
        int t1, t2;
        if(terms.length == 3){
            if(terms[1].matches("[<>=]") && isIntOrVar(terms[0]) && isIntOrVar(terms[2])){
                t1 = (vars.containsKey(terms[0])?
                        (int)vars.get(terms[0]) : Integer.parseInt(terms[0]));
                t2 = Integer.parseInt(terms[2]);
                switch(terms[1]){
                    case "<":
                        return (t1 < t2)? 1 : 0;
                    case ">":
                        return (t1 > t2)? 1 : 0;
                    case "=":
                        return (t1 == t2)? 1 : 0;
                }
            }
        }
        return 2;
    }
    
    private boolean isIntOrVar(String x){
        if(x.matches("-?\\d+(\\.\\d+)?")){
            return true;
        }else{
            return vars.containsKey(x);
        }
    }
}


