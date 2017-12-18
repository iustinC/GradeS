package com;

import Domain.Student;
import Repository.NotaFileRepository;
import Repository.StudentFileRepository;
import Repository.TemeFileRepository;
import Service.NotaService;
import Service.StudentService;
import Service.TemeService;

import UI.GUI.ControllerRootLayout;
import UI.GUI.StudentController;
import UI.GUI.StudentView;
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
import sun.plugin.javascript.navig.Anchor;
import javafx.scene.control.TextField;

import javax.tools.Tool;
import java.io.IOException;


public class Main extends Application {

    //private StudentFileRepository repoStud = new StudentFileRepository(new ValidatorStudent(),"Student.txt");
    //private TemeFileRepository repoTeme = new TemeFileRepository(new ValidatorTeme(),"Teme.txt");
    //private NotaFileRepository repoNote = new NotaFileRepository(new ValidatorNota(), "Catalog.txt");
    public static Stage st;
    //private StudentService controllerStudent = new StudentService(repoStud);
    //private TemeService controllerTeme = new TemeService(repoTeme);
    //private NotaService controllerNote = new NotaService(repoNote,repoStud,repoTeme);


    @Override
    public void start(Stage primaryStage) throws Exception{
        st = primaryStage;
        primaryStage.setTitle("Hello World");
        AnchorPane root=initView();
        Scene scene = new Scene(root, 800, 500);
        scene.getStylesheets().add("Resources/CSS/stylesheets.css");
        primaryStage.setScene(scene);
        //primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static AnchorPane initView()
    {   /*
        StudentController ctrl=new StudentController(controllerStudent);
        controllerStudent.addObserver(ctrl);
        StudentView view = new StudentView(ctrl);
        ctrl.setView(view);
        return view.getView();*/
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/UI/GUI/RootLayout.fxml"));
            AnchorPane anchorPane = (AnchorPane) loader.load();
            ControllerRootLayout controllerRootLayout = loader.getController();
            anchorPane.getStyleClass().add("pane");
            return anchorPane;
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
	// write your code here
       /* Console ui = new Console();
        ui.runMenu();
       */
       launch(args);


    }
}
