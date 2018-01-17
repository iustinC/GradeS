package UI.GUI;

import Repository.NoteRepository;
import Repository.StudentRepository;
import Repository.TemeRepository;
import Service.NotaService;
import Service.StudentService;
import Service.TemeService;
import Validator.ValidatorNota;
import Validator.ValidatorStudent;
import Validator.ValidatorTeme;
import com.Main;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.sql.*;

public class ControllerLogin {

    static final String url = "jdbc:sqlserver://localhost\\DESKTOP-HLOMB7K:1433;database=LAB13;integratedSecurity=true";
    static final String jdbc_driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    static Connection conn = null;
    static Statement statement = null;
    String username, password, prioritate;
    static String email;
    boolean openDatabase = false;
    static TemeRepository repoTeme = new TemeRepository(new ValidatorTeme());
    static TemeService serviceTeme = new TemeService(repoTeme);

    //StudentFileRepository repoStud = new StudentFileRepository(new ValidatorStudent(),"Student.txt");
    static StudentRepository repoStud = new StudentRepository(new ValidatorStudent());
    static StudentService serviceStudent = new StudentService(repoStud);

    //NotaFileRepository repoNote = new NotaFileRepository(new ValidatorNota(), "Catalog.txt");
    static NoteRepository repoNote = new NoteRepository(new ValidatorNota());
    static NotaService serviceNote = new NotaService(repoNote,repoStud,repoTeme);

    @FXML
    AnchorPane anchorPane;

    @FXML
    private JFXTextField txtFieldUserLogin;

    @FXML
    private JFXPasswordField txtFieldPassLogin;

    @FXML
    private JFXTextField txtFieldUserRegister;

    @FXML
    private JFXPasswordField txtFieldPassRegister;

    @FXML
    private JFXTextField txtFieldEmailRegister;

    @FXML
    private JFXButton btnLogin;

    @FXML
    private JFXButton btnRegister;

    @FXML
    private JFXToggleButton btnToggle;

    public void initialize(){
        openDatabase();
    }

    public void openDatabase(){
        if(openDatabase == false){
            try{
                Class.forName(jdbc_driver);
                conn = DriverManager.getConnection(url);
                openDatabase = true;
            }catch (SQLException e){
                e.printStackTrace();
            }catch (ClassNotFoundException e){
                e.printStackTrace();
            }
        }
    }

    public boolean validate(){
        if(!email.matches("[a-zA-Z1-9]+@[a-zA-Z]+\\.[a-zA-Z]+")){
            showErrorMessage("Emailul nu este valid.");
            return false;
        }
        if(!username.matches("[a-z0-9A-Z]{8,}")){
            showErrorMessage("Username-ul trebuie sa contina minim 8 litere si cifre.");
            return false;
        }
        return true;
    }

    public void registerNewUser(){
        username = txtFieldUserRegister.getText();
        password = txtFieldPassRegister.getText();
        email = txtFieldEmailRegister.getText();
        if(btnToggle.isSelected())
            prioritate = "profesor";
        else
            prioritate = "student";

        if(validate() == true)
            try{
                statement = conn.createStatement();
                String sql = "INSERT INTO Utilizatori VALUES(" + "'" + username + "'" + "," +
                                                                 "'" + password + "'" + "," +
                                                                 "'" + email + "'" +","+
                                                                 "'" + prioritate + "'" + ")";
                statement.executeUpdate(sql);
            }catch (SQLException e){
                e.printStackTrace();
            }
    }


    public void login(){
        username = txtFieldUserLogin.getText();
        password = txtFieldPassLogin.getText();
        try{
            String sql = "SELECT * FROM Utilizatori WHERE username=" + "'" + username + "'" + " and  pass=" + "'" + password + "'";
            statement = conn.createStatement();
            ResultSet res = statement.executeQuery(sql);
            if(res.next() == false)
                showErrorMessage("Datele introduse sunt incorecte.");
            else
                {   email = res.getString(3);
                    Scene scene;
                    if(res.getString(4).equals("profesor")){
                        scene = new Scene(ControllerLogin.initView(res.getString(4)),800, 500);
                        scene.getStylesheets().add("Resources/CSS/stylesheets.css");
                    }
                    else {
                        scene = new Scene(ControllerLogin.initView(res.getString(4)), 545, 400);
                        scene.getStylesheets().add("Resources/CSS/stylesheets.css");
                    }
                    Main.st.setScene(scene);
                    Main.st.centerOnScreen();
                }
        }catch (SQLException e){
            System.out.println(e.getErrorCode());
        }
    }

    static void showErrorMessage(String text){
        Alert message=new Alert(Alert.AlertType.ERROR);
        message.setTitle("Mesaj eroare");
        message.setContentText(text);
        message.showAndWait();
    }



    public static AnchorPane initView(String prioritate)
    {   /*
        StudentController ctrl=new StudentController(controllerStudent);
        controllerStudent.addObserver(ctrl);
        StudentView view = new StudentView(ctrl);
        ctrl.setView(view);
        return view.getView();*/
        if(prioritate.equals("student")){
            return loadStudent();
        }
        else {
            return loadProfesor();
        }
    }

    public static AnchorPane loadStudent(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/UI/GUI/StudentLayout.fxml"));
            AnchorPane anchorPane = (AnchorPane) loader.load();
            ControllerStudent controllerStudent = loader.getController();
            controllerStudent.setEmail(email);
            controllerStudent.setService(serviceTeme, serviceNote, email);
            return anchorPane;
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public static AnchorPane loadProfesor(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/UI/GUI/RootLayout.fxml"));
            AnchorPane anchorPane = (AnchorPane) loader.load();
            ControllerRootLayout controllerRootLayout = loader.getController();
            controllerRootLayout.setService(serviceStudent, serviceTeme, serviceNote);
            anchorPane.getStyleClass().add("pane");
            return anchorPane;
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public static AnchorPane initView()
    {   /*
        StudentController ctrl=new StudentController(controllerStudent);
        controllerStudent.addObserver(ctrl);
        StudentView view = new StudentView(ctrl);
        ctrl.setView(view);
        return view.getView();*/
        return loadProfesor();
    }


}
