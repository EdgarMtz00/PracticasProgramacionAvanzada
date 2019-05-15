package com.company.views;

import com.company.connection.ClientSocket;
import com.google.gson.JsonObject;

import javax.swing.*;
import java.awt.*;

import static javax.swing.GroupLayout.Alignment.CENTER;

public class SignFrame extends JFrame{
    private boolean isLogin = true;
    private static String loginStr = "Log In";
    private static String signUpStr = "Sign Up";

    JTextField userField;
    JTextField passField;
    JTextField nameField;
    JTextField lastNameField;
    JTextField confirmField;
    JButton logInBtn;
    JButton signUpBtn;
    GroupLayout groupLayout;
    ClientSocket socket;
    JLabel userLabel;
    JLabel passLabel;
    JLabel confirmLabel;
    JLabel nameLabel;
    JLabel lastNameLabel;

    public SignFrame(ClientSocket socket){
        this.socket = socket;
        this.setTitle("CocoChat - LogIn");
        this.getContentPane().setBackground(Color.CYAN);
        this.setVisible(true);
        this.setMinimumSize(new Dimension(480,600));
        this.setPreferredSize(new Dimension(480, 600));
        this.revalidate();
        this.setResizable(false);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        groupLayout = new GroupLayout(this.getContentPane());
        componentConfig();
        groupConfig();
    }

    private void componentConfig(){
        userField = new JTextField("User");
        passField = new JTextField("Password");
        nameField = new JTextField("Name");
        lastNameField = new JTextField("Last name");
        confirmField = new JTextField("Confirm password");
        logInBtn = new JButton("Log In");
        signUpBtn = new JButton("Sign Up");
        userLabel = new JLabel("User:");
        passLabel = new JLabel("Password:");
        confirmLabel = new JLabel("Confirm password");
        nameLabel = new JLabel("Name");
        lastNameLabel = new JLabel("Last name");
        confirmField.setVisible(false);
        confirmLabel.setVisible(false);
        nameLabel.setVisible(false);
        nameField.setVisible(false);
        lastNameField.setVisible(false);
        lastNameLabel.setVisible(false);

        logInBtn.addActionListener(e -> {
            if(isLogin) {
                String user = userField.getText();
                String pwd = passField.getText();
                JsonObject d = new JsonObject();
                d.addProperty("username", user);
                d.addProperty("password", pwd);
                socket.setListener(this::logIn);
                socket.request("login", d);
            }else{
                String user = userField.getText();
                String pwd = passField.getText();
                String name = nameField.getText();
                String lasteName = lastNameField.getText();
                String confirm = confirmField.getText();
                if(pwd.equals(confirm)){
                    JsonObject d = new JsonObject();
                    d.addProperty("username", user);
                    d.addProperty("password", pwd);
                    d.addProperty("names", name);
                    d.addProperty("lastnames", lasteName);
                    socket.setListener(this::logIn);
                    socket.request("register", d);
                }else{
                    JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden");
                }
            }
        });

        signUpBtn.addActionListener(e -> {
            isLogin = !isLogin;
            String login = (isLogin)? loginStr : signUpStr;
            String signup = (isLogin)? signUpStr : loginStr;
            logInBtn.setText(login);
            signUpBtn.setText(signup);
            confirmLabel.setVisible(!isLogin);
            confirmField.setVisible(!isLogin);
            nameField.setVisible(!isLogin);
            nameLabel.setVisible(!isLogin);
            lastNameLabel.setVisible(!isLogin);
            lastNameField.setVisible(!isLogin);
        });
    }

    private void logIn(JsonObject response){
        System.out.println(response.toString());
        if(response.get("status").getAsString().equals("ok")){
           new ChatFrame(socket, response.get("user").getAsString());
            dispose();
        }else{
            JOptionPane.showMessageDialog(this, "Usuario invalido");
        }
    }

    private void groupConfig(){
        groupLayout.setHorizontalGroup(
            groupLayout.createParallelGroup(CENTER)
                .addComponent(userLabel)
                .addComponent(userField, 450,450,450)
                .addComponent(nameLabel)
                .addComponent(nameField, 450, 450, 450)
                .addComponent(lastNameLabel)
                .addComponent(lastNameField, 450, 450, 450)
                .addComponent(passLabel)
                .addComponent(passField, 450,450,450)
                .addComponent(confirmLabel)
                .addComponent(confirmField, 450, 450, 450)
                .addComponent(logInBtn, 250,250,250)
                .addComponent(signUpBtn,75,75,75)
        );

        groupLayout.setVerticalGroup(
            groupLayout.createSequentialGroup()
                .addComponent(userLabel)
                .addComponent(userField, 50,50,50)
                .addComponent(nameLabel)
                .addComponent(nameField, 50, 50, 50)
                .addComponent(lastNameLabel)
                .addComponent(lastNameField, 50 ,50, 50)
                .addComponent(passLabel)
                .addComponent(passField, 50,50,50)
                .addComponent(confirmLabel)
                .addComponent(confirmField, 50,50,50)
                .addComponent(logInBtn,75,75,75)
                .addComponent(signUpBtn,50,50,50)
        );
        groupLayout.setAutoCreateContainerGaps(true);
        groupLayout.setAutoCreateGaps(true);
        this.setLayout(groupLayout);
        this.pack();
    }
}