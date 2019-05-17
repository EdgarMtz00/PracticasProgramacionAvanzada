/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package painterpreter;

import java.util.Arrays;
import java.util.HashMap;

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
    String[] codigos;
    String[] functions = {"arriba", "abajo", "izquierda", "derecha", "if", "endif",
        "for", "endfor", "moverBrazo1", "moverBrazo2", "moverPierna1", "moverPierna1"};
    HashMap<String, Integer> ciclos;
    Dummy dummy = new Dummy();
    Form form;
    
    public Interpreter(Form form){
        this.form = form;
        this.form.setDummy(dummy);
        vars = new HashMap();
        ciclos = new HashMap();
    }
    
    public void setCodigos(String codigo){
        codigos = codigo.split("\n");
    }

    @Override
    public void run(){
        int i = 0;
        for(String codigo : codigos){
            codigo = codigo.trim();
            String[] terms = codigo.split(" ");
            if(Arrays.stream(functions).anyMatch(terms[0]::equals)){
                switch(codigo.split(" ")[0]){
                    case "if":
                        if(codigo.endsWith(":")){
                            String c = codigo.substring(3, codigo.indexOf(":"));
                            if(condicion(c, i)==2){
                                return;
                            }
                        }
                        break;
                    case "for":
                        if(codigo.endsWith(":")){
                            String n = codigo.substring(4, codigo.indexOf(":"));
                            if(!numero(n.trim(), i)){
                                return;
                            }
                        }
                        break;
                    default:
                        if(terms.length > 1){
                            return;
                        }
                }
            }else{
                if((terms.length != 3 || terms[0].matches("-?\\d+(\\.\\d+)?")
                        || !terms[1].equals("=")) || !numero(terms[2], i)){
                    return;
                }else{
                    vars.put(terms[0], Integer.parseInt(terms[2]));
                }
            }
            i++;
        }

        for(i = 0; i < codigos.length; i++){
            String[] terms = codigos[i].split(" ");
            switch(terms[0]){
                case "for":
                    String n = codigos[i].substring(4, codigos[i].indexOf(":"));
                    Integer indexf = i;
                    if(vars.containsKey(n)){
                        ciclos.put(indexf.toString(), (Integer)vars.get(n));
                    }else{
                        ciclos.put(indexf.toString(), Integer.parseInt(n));
                    }
                    break;
                case "if":
                    String c = codigos[i].substring(3, codigos[i].indexOf(":"));
                    if(condicion(c, i) == 0){
                        int index = endIf(i);
                        i = index;
                    }
                    break;
                case "arriba":
                    dummy.move(Dummy.VERTICAL, Dummy.DECREASE);
                    break;
                case "abajo":
                    dummy.move(Dummy.VERTICAL, Dummy.INCREASE);
                    break;
                case "izquierda":
                    dummy.move(Dummy.HORIZONTAL, Dummy.DECREASE);;
                    break;
                case "derecha":
                    dummy.move(Dummy.HORIZONTAL, Dummy.INCREASE);
                    break;
                case "moverBrazo1":
                    dummy.moveLeftArm();
                    break;
                case "moverBrazo2":
                    dummy.moveRightArm();
                    break;
                case "moverPierna1":
                    dummy.moveLeftLeg();
                    break;
                case "movePierna2":
                    dummy.moveRightLeg();
                    break;
                case "endif":
                    break;
                case "endfor":
                    for(String wea : ciclos.keySet()){
                        if(ciclos.get(wea) != 0){
                            i = Integer.parseInt(wea);
                            System.out.println(i);
                            ciclos.put(wea, ciclos.get(wea) - 1);
                        }
                    }
            }
            form.repaint();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
    
    private int endIf(int indexIf){
        int ifCount = 0;
        indexIf++;
        for(int i = indexIf; i < codigos.length; i++){
            if(codigos[i].equals("endif")){
                if(ifCount == 0){
                    return i;
                }else{
                    ifCount--;
                }
            }else if(codigos[i].split(" ")[0].equals("if")){
                ifCount++;
            }
        }
        return codigos.length;
    }
    
    private int condicion(String condition, int i){
        for(String comparador : comparadores){
            if (condition.contains(comparador)) {
                int t1, t2;
                String[] terms = condition.split(comparador);
                System.out.println("temrs:" + Arrays.toString(terms));
                terms[0] = terms[0].trim();
                terms[1] = terms[1].trim();
                if(numero(terms[0], i) && numero(terms[1], i)) {
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

    private boolean numero(String x, int cPos){
        String res = null;
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
                    codigos[cPos] = codigos[cPos].replace(x, res);
                    return true;
                }
            }
        }
        if(x.matches("-?\\d+(\\.\\d+)?")){
            return true;
        }else{
            return vars.containsKey(x);
        }
    }
}