package UI.GUI;

import Domain.Student;
import Repository.*;
import Service.NotaService;
import Service.StudentService;
import Service.TemeService;
import Validator.ValidatorNota;
import Validator.ValidatorStudent;
import Validator.ValidatorTeme;
import com.Main;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerRootLayout {

    //TemeFileRepository repoTeme = new TemeFileRepository(new ValidatorTeme(),"Teme.txt");
    /*TemeRepository repoTeme = new TemeRepository(new ValidatorTeme());
    TemeService serviceTeme = new TemeService(repoTeme);*/
    TemeService serviceTeme;


    //StudentFileRepository repoStud = new StudentFileRepository(new ValidatorStudent(),"Student.txt");
   /* StudentRepository repoStud = new StudentRepository(new ValidatorStudent());
    StudentService serviceStudent = new StudentService(repoStud);*/
   StudentService serviceStudent;


    //NotaFileRepository repoNote = new NotaFileRepository(new ValidatorNota(), "Catalog.txt");
    /*NoteRepository repoNote = new NoteRepository(new ValidatorNota());
    NotaService serviceNote = new NotaService(repoNote,repoStud,repoTeme);*/
    NotaService serviceNote;


    @FXML
    public  AnchorPane anchorPane;

    @FXML
    Button btnStudenti ;

    @FXML
    Button btnTeme;

    @FXML
    Button btnNote;

    @FXML
    HBox hBox;


    public void initialize(){
        hBox.setHgrow(btnNote, Priority.ALWAYS);
        hBox.setHgrow(btnStudenti, Priority.ALWAYS);
        hBox.setHgrow(btnTeme, Priority.ALWAYS);
        btnNote.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        btnStudenti.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        btnTeme.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        btnStudenti.getStyleClass().add("button_home");
        btnTeme.getStyleClass().add("button_home");
        btnNote.getStyleClass().add("button_home");
    }

    @FXML
    public void initStudentiView(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/UI/GUI/StudentsLayout.fxml"));
            AnchorPane studentsPane = (AnchorPane) loader.load();
            anchorPane.getChildren().setAll(studentsPane);
            ControllerStudents controllerStudents = (ControllerStudents) loader.getController();
            controllerStudents.setController(serviceStudent, serviceNote);
            serviceStudent.addObserver(controllerStudents);

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void initTemeView(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/UI/GUI/TemeLayout.fxml"));
            AnchorPane temePane = (AnchorPane) loader.load();
            anchorPane.getChildren().setAll(temePane);
            ControllerTeme controllerTeme = (ControllerTeme) loader.getController();
            controllerTeme.setService(serviceTeme, serviceNote);
            serviceTeme.addObserver(controllerTeme);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void initNoteView(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/UI/GUI/NoteLayout.fxml"));
            AnchorPane notePane = (AnchorPane) loader.load();
            anchorPane.getChildren().setAll(notePane);
            ControllerNote controllerNote = (ControllerNote) loader.getController();
            controllerNote.setService(serviceNote);
            controllerNote.initNote();
            serviceNote.addObserver(controllerNote);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void setService(StudentService st, TemeService tm, NotaService nt){
        this.serviceStudent = st;
        this.serviceTeme = tm;
        this.serviceNote = nt;
    }

}
