package com.example.timchat;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.Objects;

public class HelloController {
    
    @FXML
    public TextField loginEmail;
    public PasswordField loginPassword;
    @FXML
    public Label loginerror;
    public Label outputGreeting;
    LocalTime currentTime = LocalTime.now();
    @FXML
    private Label error, error2;
    @FXML
    private TextField fname, lname, email;
    @FXML
    private PasswordField userpassword, confirmpassword;

    @FXML
    private ToggleGroup colorButton;

    @FXML
    private CheckBox java, python,js,kotlin,csharp,cplus;

    @FXML
    private ComboBox<String> frameworks;

    private Stage stage;
    private Scene scene;

    private String firstname;
    private String lastname;
    private String useremail;

    private  String passvalue;
    private String conpassvalue;
    private String genderValue;
    private String userFramework;
//    private String[] languages;
    private String language;

    String url = "jdbc:mysql://localhost:3306/timchat";
    String username = "root";
    String password = "";
    String greetings =  getGreeting(currentTime);



//    SWITCHING TO LOGIN
    public void switchToLogin(MouseEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("LogIn.fxml"));
        Parent root = loader.load();
        stage = (Stage)((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

//    SWITCHING TO SIGNUP
    public void switchToSignUp(MouseEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SignUp.fxml"));
        Parent root = loader.load();
        stage = (Stage)((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


//    LOGIN TO DASHBOARD
    public void switchToDashboard(MouseEvent e,String name) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("dashBoard.fxml"));
        Parent root = loader.load();
        HelloController controller = loader.getController();
        controller.outputGreeting.setText(greetings + name);
        stage = (Stage)((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


//    GETTING VALUES FROM SIGNUP PAGE
    public void SignUpSubmit(MouseEvent e) throws IOException{
//        text fields
        firstname = fname.getText();
        lastname = lname.getText();
        useremail = email.getText();
        passvalue = userpassword.getText();
        conpassvalue = confirmpassword.getText();

    //RADIO BUTTON
        RadioButton gender = (RadioButton) colorButton.getSelectedToggle();
    if (gender == null){
        System.out.println("error");
    }else {
        genderValue = gender.getText();

//        languages = new String[]{java.getText(),python.getText(),js.getText(),kotlin.getText(),csharp.getText(), cplus.getText()};

        language = java.getText();
        userFramework = frameworks.getValue();

        if(firstname.trim().isEmpty() || lastname.trim().isEmpty() ||
                useremail.trim().isEmpty() || passvalue.trim().isEmpty() ||
                conpassvalue.trim().isEmpty() || genderValue== null||
                language == null || userFramework == null){
            error2.setText("All fields must be filled");
        }else {
            if (passvalue.equals(conpassvalue)){

                if(passvalue.length() < 8 || passvalue.trim().isEmpty()){
                    error.setText("Password must be eight or more character long");
                }else {
                    HelloController controller = new HelloController();
                    controller.createUser(firstname, lastname, useremail, passvalue, genderValue, language, userFramework );
                    controller.switchToLogin(e);
                }
            }else {
                error.setText("Password is not same!!..");
                }
            }
        }

    }


//    SAVING DATA INTO DATABASE
    public void createUser(String firstname, String lastname, String useremail, String passvalue, String genderValue, String language, String userFramework) {
        try(Connection conn = DriverManager.getConnection(url, username, password)){
            String query = "INSERT INTO users (firstname, lastname, email, password, gender, language, framework) VALUES (?,?,?,?,?,?,?)";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, firstname);
            statement.setString(2, lastname);
            statement.setString(3, useremail);
            statement.setString(4, passvalue);
            statement.setString(5, genderValue);
            statement.setString(6, language);
            statement.setString(7, userFramework);
            statement.execute();
            System.out.println("Done");

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }


//    CHECKING DATA TO VALIDATE LOGIN
    public void validUser(String useremail, String passvalue, MouseEvent event){
        try(Connection con = DriverManager.getConnection(url, username, password)){
            Statement state = con.createStatement();
            String query = "SELECT * FROM users";
            state.execute(query);
            ResultSet resultSet = state.getResultSet();
            while (resultSet.next()){
                String chkemail = resultSet.getString("email");
                String chkpass = resultSet.getString("password");
                String chkfirstname = resultSet.getString("firstname");
                String chklastname = resultSet.getString("lastname");
                if (chkemail.equals(useremail) && chkpass.equals(passvalue)){
                    System.out.println("Validated");
                    HelloController controller = new HelloController();
                    String usName = chkfirstname + " " +chklastname;
                    controller.switchToDashboard(event, usName);
                } else{
                    System.out.println("Errorr");
//                    loginerror.setText("All fields must be filled");
                }

            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


//    LOGIN SECTION
    public void loginuser(MouseEvent mouseEvent) {
        useremail = loginEmail.getText();
        passvalue = loginPassword.getText();

        if (useremail.trim().isEmpty() || passvalue.trim().isEmpty()){
            loginerror.setText("All fields must be filled");
        }else {
            HelloController controller = new HelloController();
            controller.validUser(useremail,passvalue, mouseEvent);
        }
    }


//    THE GREETING METHOD
    public static String getGreeting( LocalTime currentTime){
        if (currentTime.isBefore(LocalTime.NOON)){
            return "Good Morning ";
        } else if (currentTime.isBefore(LocalTime.of(17,00))) {
            return "Good Afternoon ";
        }else {
            return "Good Evening ";
        }
    }
}
