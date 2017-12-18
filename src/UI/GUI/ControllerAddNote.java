package UI.GUI;

import Domain.Nota;
import Domain.Student;
import Domain.TemaLaborator;
import Service.NotaService;
import Utils.Observable;
import Validator.ValidationException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ControllerAddNote {

    NotaService service;
    ObservableList<Student> modelStud;
    ObservableList<TemaLaborator> modelTema;

    @FXML
    TableView tableViewStudents;
    @FXML
    private TableColumn<Student, String> tableColumnIdForStud;
    @FXML
    private TableColumn<Student, String> tableColumnNumeForStud;
    @FXML
    private TableColumn<Student, String> tableColumnGrupaForStud;
    @FXML
    private TableColumn<Student, String> tableColumnEmailForStud;
    @FXML
    private TableColumn<Student, String> tableColumnCadruForStud;
    @FXML
    TableView tableViewTeme;
    @FXML
    TableColumn<TemaLaborator, String> tableColumnNrTemaForTema;
    @FXML
    TableColumn<TemaLaborator, String> tableColumnCerintaForTema;
    @FXML
    TableColumn<TemaLaborator, String> tableColumnDeadlineForTema;
    @FXML
    Button btnAddNota;
    @FXML
    ComboBox comboBoxNota;
    @FXML
    Button btnCancel;
    @FXML
    TextField textFieldObservatii;

    ObservableList<String> note =
            FXCollections.observableArrayList(
                    "1",
                    "2",
                    "3",
                    "4",
                    "5",
                    "6",
                    "7",
                    "8",
                    "9",
                    "10"
            );

    public void initialize(){
        tableColumnIdForStud.setCellValueFactory(new PropertyValueFactory<Student, String>("id"));
        tableColumnNumeForStud.setCellValueFactory(new PropertyValueFactory<Student, String>("nume"));
        tableColumnGrupaForStud.setCellValueFactory(new PropertyValueFactory<Student, String>("grupa"));
        tableColumnEmailForStud.setCellValueFactory(new PropertyValueFactory<Student, String>("email"));
        tableColumnCadruForStud.setCellValueFactory(new PropertyValueFactory<Student, String>("cadruDidactic"));


        tableColumnCerintaForTema.setCellValueFactory(new PropertyValueFactory<TemaLaborator, String>("cerinta"));
        tableColumnDeadlineForTema.setCellValueFactory(new PropertyValueFactory<TemaLaborator, String>("deadline"));
        tableColumnNrTemaForTema.setCellValueFactory(new PropertyValueFactory<TemaLaborator, String>("numarTema"));

        comboBoxNota.setItems(note);
        comboBoxNota.getSelectionModel().selectFirst();
    }

    public void addNota(){
        Student student = (Student) tableViewStudents.getSelectionModel().getSelectedItem();
        TemaLaborator temaLaborator = (TemaLaborator) tableViewTeme.getSelectionModel().getSelectedItem();
        try {
            if(service.add(new Nota(student.getId(), temaLaborator.getNumarTema(), Integer.valueOf(comboBoxNota.getSelectionModel().getSelectedItem().toString())), textFieldObservatii.getText()) != null)
                showMessage(Alert.AlertType.WARNING,"Warning","Studentul are nota la tema data.");
            else
                showMessage(Alert.AlertType.INFORMATION, "Add", "Nota a fost adaugata cu succes!");
        }catch (ValidationException ex){
            showErrorMessage(ex.getMessage());
        }
    }

    public void close(){
        Stage a  = (Stage) btnCancel.getScene().getWindow();
        a.close();
    }
    public void setService(NotaService service){
        this.service = service;
        modelStud = FXCollections.observableArrayList(this.service.getAllStuds());
        modelTema = FXCollections.observableArrayList(this.service.getAllTeme());
    }

    public void load(){
        tableViewTeme.setItems(FXCollections.observableArrayList(service.getAllTeme()));
        tableViewStudents.setItems(FXCollections.observableArrayList(service.getAllStuds()));
    }

    static void showMessage(Alert.AlertType type, String header, String text){
        Alert message=new Alert(type);
        message.setHeaderText(header);
        message.setContentText(text);
        message.showAndWait();
    }

    static void showErrorMessage(String text){
        Alert message=new Alert(Alert.AlertType.ERROR);
        message.setTitle("Mesaj eroare");
        message.setContentText(text);
        message.showAndWait();
    }

}
