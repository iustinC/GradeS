package com;

import Domain.Nota;
import Domain.Student;
import Domain.TemaLaborator;
import Repository.*;
import Service.NotaService;
import Service.StudentService;
import Service.TemeService;

import UI.GUI.*;
import Validator.ValidationException;
import Validator.ValidatorNota;
import Validator.ValidatorTeme;
import Validator.ValidatorStudent;
import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.stage.StageStyle;

import javax.tools.Tool;
import java.io.IOException;
import java.util.List;



public class Main extends Application {

    public static Stage st;

    @Override
    public void start(Stage primaryStage) throws Exception{
        st = primaryStage;
        primaryStage.setTitle("");
        AnchorPane root = loginView();
        Scene scene = new Scene(root, 377, 381);
        scene.getStylesheets().add("Resources/CSS/stylesheets.css");
        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        //primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void logOut(){
        Scene scene = new Scene(Main.loginView(), 377, 381);
        scene.getStylesheets().add("Resources/CSS/stylesheets.css");
               /* Main.st.setWidth(1000);
                Main.st.setHeight(500);*/
        Main.st.setScene(scene);
        Main.st.centerOnScreen();}

    public static AnchorPane loginView(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/UI/GUI/LoginLayout.fxml"));
            AnchorPane anchorPane = (AnchorPane) loader.load();
            //anchorPane.getStyleClass().add("pane");
            return anchorPane;
        }catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
	// write your code here
       /* Console ui = new Console();
        ui.runMenu();
       */

        /*try {
            tm.update(new TemaLaborator(1, "in", 15));
        }catch (ValidationException e){
            System.out.println(e.getMessage());
        }*/

        launch(args);

    }
}
