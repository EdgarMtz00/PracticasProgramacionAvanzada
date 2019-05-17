/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package painterpreter;

import java.awt.Color;
import java.awt.Graphics;

public class Dummy {
    static public int VERTICAL = 0;
    static public int HORIZONTAL = -1;
    static public int INCREASE = 2;
    static public int DECREASE = 1;
    
    private int[] head = {450, 150, 80, 80};
    private int[] body = {490, 230, 490, 350};
    private int[] leftArm = {415, 260, 490, 260};
    private int[] rightArm = {565, 260, 490, 260};
    private int[] leftLeg = {490, 350, 420, 450};
    private int[] rightLeg = {490, 350, 560, 450};
    private int[][] dummy = {body, leftArm, rightArm, leftLeg, rightLeg};
    private boolean leftLegSwitch, rightLegSwitch;
    private int leftArmStatus, rightArmStatus;
    private int[] armOffset ={0, 25, -25};

    public Dummy() {
        this.leftLegSwitch = false;
        this.leftArmStatus = 0;
        this.rightLegSwitch = false;
        this.rightArmStatus = 0;
    }
    
    public void paint(Graphics g) {
        g.setColor(Color.white);
        g.drawOval(head[0], head[1], head[2], head[3]);
        for (int[] bodyPart : dummy) {
            g.drawLine(bodyPart[0], bodyPart[1], bodyPart[2], bodyPart[3]);
        }
    }
    
    public void move(int dir, int change){
        int offset = (change == INCREASE)? 5 : (change == DECREASE)? -5: 0;
        int index = (dir == HORIZONTAL)? 0 : (dir == VERTICAL)? 1 : 4;
        head[index] += offset;
        for(int[] bodyPart : dummy){
            for(int j = index;
                    j < bodyPart.length;
                    j = j + 2){
                bodyPart[j] += offset;
            }
        }
    }
    
    public void moveLeftLeg(){
        leftLeg[2] =(leftLegSwitch)? leftLeg[2] + 30 : leftLeg[2] - 30;
        leftLegSwitch = !leftLegSwitch;
    }
    
    public void moveRightLeg(){
        rightLeg[2] =(rightLegSwitch)? rightLeg[2] - 30 : rightLeg[2] + 30;
        rightLegSwitch = !rightLegSwitch;
    }
    
    public void moveRightArm(){
        rightArmStatus = (rightArmStatus + 1) % 3;
        rightArm[1] = rightArm[3] + armOffset[rightArmStatus];
    }
    
    public void moveLeftArm(){
        leftArmStatus = (leftArmStatus + 1) % 3;
        leftArm[1] = leftArm[3] + armOffset[leftArmStatus];
    }
}
