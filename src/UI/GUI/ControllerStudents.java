package UI.GUI;

import Domain.Student;
import Service.Filter;
import Service.NotaService;
import Service.StudentService;
import Utils.ListEvent;
import Utils.Observable;
import Utils.Observer;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.Window;
import com.Main;


import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.Main.st;


public class ControllerStudents implements Observer<Student> {

    @FXML
    private TableView<Student> tableViewStudents;
    @FXML
    private TableColumn<Student, String> tableColumnId;
    @FXML
    private TableColumn<Student, String> tableColumnNume;
    @FXML
    private TableColumn<Student, String> tableColumnGrupa;
    @FXML
    private TableColumn<Student, String> tableColumnEmail;
    @FXML
    private TableColumn<Student, String> tableColumnCadru;
    @FXML
    private TableColumn<Student, Student> tableColumnActiune;
    @FXML
    AnchorPane anchorPaneStudents;
    @FXML
    Button btnBack;
    @FXML
    Button btnAddStudent;
    @FXML
    CheckBox checkBoxId;
    @FXML
    TextField textFieldId;
    @FXML
    CheckBox checkBoxNume;
    @FXML
    TextField textfieldNume;
    @FXML
    CheckBox checkBoxEmail;
    @FXML
    TextField textfieldEmail;
    @FXML
    CheckBox checkBoxCadru;
    @FXML
    TextField textfieldCadru;
    @FXML
    CheckBox checkBoxGrupa;
    @FXML
    ComboBox comboBoxGrupa;


    StudentService serviceStudent;
    NotaService noteService;
    ObservableList<Student> model;

    ObservableList<String> grupa =
            FXCollections.observableArrayList(
                    "220",
                    "221",
                    "222",
                    "223",
                    "224",
                    "225",
                    "226",
                    "227"
            );

    public ControllerStudents() {

    }

    public void initialize(){

        tableColumnId.setCellValueFactory(new PropertyValueFactory<Student, String>("id"));
        tableColumnNume.setCellValueFactory(new PropertyValueFactory<Student, String>("nume"));
        tableColumnGrupa.setCellValueFactory(new PropertyValueFactory<Student, String>("grupa"));
        tableColumnEmail.setCellValueFactory(new PropertyValueFactory<Student, String>("email"));
        tableColumnCadru.setCellValueFactory(new PropertyValueFactory<Student, String>("cadruDidactic"));
        tableColumnActiune.setCellValueFactory(new PropertyValueFactory<Student, Student>("actiune"));
        tableColumnActiune.setCellValueFactory(e -> new ReadOnlyObjectWrapper<>(e.getValue()));
        tableColumnActiune.setCellFactory(e -> new TableCell<Student, Student>(){
            private Button btnDelete = new Button("Delete");
            private Button btnUpdate = new Button("Update");
            private HBox hbox;

            @Override
            protected void updateItem(Student student, boolean emtpty){
                super.updateItem(student, emtpty);

                hbox = new HBox(btnDelete, btnUpdate);
                hbox.setSpacing(5f);
                hbox.setPadding(new Insets(5f));

                if(student == null){
                    setGraphic(null);
                    return;
                }

                setGraphic(hbox);
                btnDelete.setOnAction(e ->
                {   if(noteService.findStud(String.valueOf(student.getId())))
                    {
                        Alert message=new Alert(Alert.AlertType.ERROR);
                        message.setTitle("Mesaj eroare");
                        message.setContentText("Studentul are nota si nu poate fi sters");
                        message.showAndWait();
                    }
                    else
                        serviceStudent.delete(student.getId());
                });

                btnUpdate.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        Parent root;
                        try{
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(Main.class.getResource("/UI/GUI/UpdateLayout.fxml"));
                            root =  loader.load();
                            root.getStyleClass().add("pane");
                            ControllerAdd controller = loader.getController();
                            controller.initUpdate();
                            controller.loadStudent(student);
                            controller.setService(serviceStudent);
                            Stage stage = new Stage();
                            stage.setTitle("Update");
                            Scene addScene = new Scene(root, 450, 210);
                            addScene.getStylesheets().add("Resources/CSS/stylesheets.css");
                            stage.setScene(addScene);
                            stage.setResizable(false);
                            stage.show();
                            // Hide this current window (if this is what you want)
                            //((Node)(event.getSource())).getScene().getWindow().hide();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                });
                btnDelete.getStyleClass().add("button_home");
                btnUpdate.getStyleClass().add("button_home");
            }
        });
        btnBack.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Scene scene = new Scene(Main.initView(), 800, 500);
                scene.getStylesheets().add("Resources/CSS/stylesheets.css");
               /* Main.st.setWidth(1000);
                Main.st.setHeight(500);*/
                Main.st.setScene(scene);            }
        });
        btnAddStudent.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Parent root;
                try{
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(Main.class.getResource("/UI/GUI/AddLayout.fxml"));
                    root =  loader.load();
                    root.getStyleClass().add("pane");
                    ControllerAdd controller = loader.getController();
                    controller.initAdd();
                    controller.setService(serviceStudent);
                    Stage stage = new Stage();
                    stage.setTitle("My New Stage Title");
                    Scene addScene = new Scene(root, 450, 210);
                    addScene.getStylesheets().add("Resources/CSS/stylesheets.css");
                    stage.setScene(addScene);
                    stage.setResizable(false);
                    stage.show();
                    // Hide this current window (if this is what you want)
                    //((Node)(event.getSource())).getScene().getWindow().hide();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        });
        comboBoxGrupa.setItems(grupa);
        comboBoxGrupa.getSelectionModel().selectFirst();
        btnBack.getStyleClass().add("button_home");
    }

    public void filter(){
        Predicate<Student> predFinal = (student->true);
        if(checkBoxId.isSelected())
            if(!textFieldId.getText().equals(""))
                predFinal = predFinal.and(Filter.verificareId( textFieldId.getText()));
        if(checkBoxNume.isSelected())
            if(!textfieldNume.getText().equals(""))
                predFinal = predFinal.and(Filter.verificareNume(textfieldNume.getText()));
        if(checkBoxEmail.isSelected())
            if(!textfieldEmail.getText().equals(""))
                predFinal = predFinal.and(Filter.verificareMail(textfieldEmail.getText()));
        if(checkBoxCadru.isSelected())
            if(!textfieldCadru.getText().equals(""))
                predFinal = predFinal.and(Filter.verificareIndrumator(textfieldCadru.getText()));
        if(checkBoxGrupa.isSelected())
            predFinal = predFinal.and(Filter.verificareGrupa(comboBoxGrupa.getSelectionModel().getSelectedItem().toString()));
        List<Student> filtered = this.serviceStudent.getAllStudents().stream().filter(predFinal).collect(Collectors.toList());
        tableViewStudents.setItems(FXCollections.observableArrayList(filtered));
    }


    @Override
    public void notifyEvent(ListEvent<Student> e) {
        model.setAll(StreamSupport.stream(e.getList().spliterator(),false)
                .collect(Collectors.toList()));
    }

    public void setController(StudentService service,NotaService noteService){
        this.noteService = noteService;
        this.serviceStudent = service;
        this.model = FXCollections.observableArrayList(this.serviceStudent.getAllStudents());
        tableViewStudents.setItems(this.model);
    }

}
