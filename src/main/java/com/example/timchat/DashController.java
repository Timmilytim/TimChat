package com.example.timchat;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalTime;

public class DashController {
    LocalTime currentTime = LocalTime.now();


    @FXML
    private TextField name;

    @FXML
    private TextField output;

    @FXML
    private Label outputGreeting;

    String greetings =  getGreeting(currentTime);


    @FXML
    public void initialize(){
        outputGreeting.setText(greetings);
    }



    public static String getGreeting( LocalTime currentTime){
        if (currentTime.isBefore(LocalTime.NOON)){
            return "Good Morning!";
        } else if (currentTime.isBefore(LocalTime.of(17,00))) {
            return "Good Afternoon!";
        }else {
            return "Good Evening!";
        }
    }
}