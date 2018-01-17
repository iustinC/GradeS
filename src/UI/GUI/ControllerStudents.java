package UI.GUI;

import Domain.Student;
import Service.Filter;
import Service.NotaService;
import Service.StudentService;
import Utils.ListEvent;
import Utils.Observable;
import Utils.Observer;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleIntegerProperty;
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
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
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
    JFXButton btnBack;
    @FXML
    JFXButton btnAddStudent;
    @FXML
    JFXTextField textFieldId;
    @FXML
    JFXTextField textfieldNume;
    @FXML
    JFXTextField textfieldEmail;
    @FXML
    JFXTextField textfieldCadru;
    @FXML
    JFXComboBox comboBoxGrupa;
    @FXML
    JFXButton btnPrevious;
    @FXML
    JFXButton btnForward;
    @FXML
    JFXComboBox comboBoxItems;
    @FXML
    JFXButton btnSignOut;

    IntegerProperty index = new SimpleIntegerProperty(3);
    IntegerProperty page = new SimpleIntegerProperty(0);


    StudentService serviceStudent;
    NotaService noteService;
    ObservableList<Student> model;

    ObservableList<String> grupa =
            FXCollections.observableArrayList(
                    "0",
                    "221",
                    "222",
                    "223",
                    "224",
                    "225",
                    "226",
                    "227"
            );
    ObservableList<String> items =
            FXCollections.observableArrayList(
                    "3",
                    "5",
                    "10"
            );

    public void initialize(){
        setTableColumns();
        setButtons();
    }

    public ControllerStudents() {

    }

    public void setTableColumns(){
        tableColumnId.setCellValueFactory(new PropertyValueFactory<Student, String>("id"));
        tableViewStudents.setRowFactory(a->new TableRow<Student>(){
            private Tooltip tooltip = new Tooltip();
            @Override
            public void updateItem(Student person, boolean empty) {
                super.updateItem(person, empty);
                if (person == null) {
                    setTooltip(null);
                } else {
                    tooltip.setText(person.getNume()+" "+person.getCadruDidactic());
                    tooltip.setFont(Font.font(15));
                    setTooltip(tooltip);
                }
            }
        });
        tableColumnNume.setCellValueFactory(new PropertyValueFactory<Student, String>("nume"));
        tableColumnGrupa.setCellValueFactory(new PropertyValueFactory<Student, String>("grupa"));
        tableColumnEmail.setCellValueFactory(new PropertyValueFactory<Student, String>("email"));
        tableColumnCadru.setCellValueFactory(new PropertyValueFactory<Student, String>("cadruDidactic"));
        tableColumnActiune.setCellValueFactory(new PropertyValueFactory<Student, Student>("actiune"));
        tableColumnActiune.setCellValueFactory(e -> new ReadOnlyObjectWrapper<>(e.getValue()));
        tableColumnActiune.setCellFactory(e -> new TableCell<Student, Student>(){
            private JFXButton btnDelete = new JFXButton("Delete");
            private JFXButton btnUpdate = new JFXButton("Update");
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
                btnUpdate.setTextFill(Color.WHITE);
                btnDelete.setTextFill(Color.WHITE);
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
                            Scene addScene = new Scene(root, 300, 250);
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
    }

    public void setButtons(){
        comboBoxItems.setItems(items);
        comboBoxItems.getSelectionModel().selectFirst();
        comboBoxGrupa.setItems(grupa);
        comboBoxGrupa.getSelectionModel().selectFirst();

        btnBack.getStyleClass().add("button_home");
        btnPrevious.setDisable(true);
        btnBack.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Scene scene = new Scene(ControllerLogin.initView(), 800, 500);
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
                    Scene addScene = new Scene(root, 320, 235);
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
        comboBoxItems.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                index.set(Integer.valueOf(newValue.toString()));
                page.setValue(0);
                if(index.getValue() + page.getValue() >= serviceStudent.getAllStudents().size())
                    btnForward.setDisable(true);
                else
                    btnForward.setDisable(false);
                loadData();
            }
        });
        page.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if(newValue.intValue() == 0)
                    btnPrevious.setDisable(true);
                else
                    btnPrevious.setDisable(false);
                if(newValue.intValue() >= serviceStudent.getAllStudents().size() - index.getValue())
                    btnForward.setDisable(true);
                else
                    btnForward.setDisable(false);

            }
        });
        btnForward.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                page.setValue(index.getValue() + page.getValue());
                loadData();

            }
        });
        btnPrevious.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                page.setValue(page.getValue() - index.getValue());
                loadData();
            }
        });

        btnSignOut.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Main.logOut();
            }
        });
    }

    public void filter(){
        boolean ok1=false,ok2=false,ok3=false,ok4=false,ok5=false;
        Predicate<Student> predFinal = (student->true);
            if(!textFieldId.getText().equals(""))
                predFinal = predFinal.and(Filter.verificareId( textFieldId.getText()));
            else
                ok1 = true;
            if(!textfieldNume.getText().equals(""))
                predFinal = predFinal.and(Filter.verificareNume(textfieldNume.getText()));
            else
                ok2 = true;
            if(!textfieldEmail.getText().equals(""))
                predFinal = predFinal.and(Filter.verificareMail(textfieldEmail.getText()));
            else
                ok3 = true;
            if(!textfieldCadru.getText().equals(""))
                predFinal = predFinal.and(Filter.verificareIndrumator(textfieldCadru.getText()));
            else
                ok4 = true;
        if(!comboBoxGrupa.getSelectionModel().getSelectedItem().toString().equals("0"))
            predFinal = predFinal.and(Filter.verificareGrupa(comboBoxGrupa.getSelectionModel().getSelectedItem().toString()));
        else
            ok5=true;
        if(!(ok1 == true && ok2 == true && ok3 == true && ok4 == true && ok5 == true)){
            List<Student> filtered = this.serviceStudent.getAllStudents().stream().filter(predFinal).collect(Collectors.toList());
            model.setAll(FXCollections.observableArrayList(filtered));
            btnForward.setDisable(true);
            btnPrevious.setDisable(true);
            comboBoxItems.setDisable(true);
        }
        else{
            loadData();
            btnForward.setDisable(false);
            btnPrevious.setDisable(false);
            comboBoxItems.setDisable(false);
        }
    }


    @Override
    public void notifyEvent(ListEvent<Student> e) {
        loadData();
    }

    public void loadData(){
        if(index.getValue() + page.getValue() >= serviceStudent.getAllStudents().size())
            btnForward.setDisable(true);
        else
            btnForward.setDisable(false);
        model.setAll(FXCollections.observableArrayList(this.serviceStudent.getBetween(index.getValue(), page.getValue())));
    }

    public void setController(StudentService service,NotaService noteService){
        this.noteService = noteService;
        this.serviceStudent = service;
        this.model = FXCollections.observableArrayList(this.serviceStudent.getBetween(index.getValue(), page.getValue()));
        tableViewStudents.setItems(this.model);
        if(this.serviceStudent.getAllStudents().size() <= index.getValue())
            btnForward.setDisable(true);
    }

}
