package UI.GUI;

import Domain.Nota;
import Domain.Student;
import Domain.TemaLaborator;
import Service.NotaService;
import Utils.Observable;
import Validator.ValidationException;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
    JFXButton btnAddNota;
    @FXML
    JFXComboBox comboBoxNota;
    @FXML
    JFXButton btnCancel;
    @FXML
    JFXTextField textFieldObservatii;

    @FXML
    JFXButton btnPreviousSt;
    @FXML
    JFXButton btnForwardSt;
    @FXML
    JFXComboBox comboBoxItemsSt;

    @FXML
    JFXButton btnPreviousTeme;
    @FXML
    JFXButton btnForwardTeme;
    @FXML
    JFXComboBox comboBoxItemsTeme;
    @FXML
    JFXComboBox comboBoxSaptPredarii;

    IntegerProperty indexTeme = new SimpleIntegerProperty(3);
    IntegerProperty pageTeme = new SimpleIntegerProperty(0);

    IntegerProperty indexStudenti = new SimpleIntegerProperty(3);
    IntegerProperty pageStudenti = new SimpleIntegerProperty(0);

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
    ObservableList<String> sapt =
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
                    "10",
                    "11",
                    "12",
                    "13",
                    "14"
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

    public void setButtons(){
        comboBoxNota.setItems(note);
        comboBoxNota.getSelectionModel().selectFirst();
        comboBoxSaptPredarii.setItems(sapt);
        comboBoxSaptPredarii.getSelectionModel().selectFirst();

        comboBoxItemsSt.setItems(items);
        comboBoxItemsTeme.setItems(items);
        comboBoxItemsTeme.getSelectionModel().selectFirst();
        comboBoxItemsSt.getSelectionModel().selectFirst();
        btnPreviousSt.setDisable(true);
        btnPreviousTeme.setDisable(true);

        comboBoxItemsSt.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                indexStudenti.set(Integer.valueOf(newValue.toString()));
                pageStudenti.setValue(0);
                if(indexStudenti.getValue() + pageStudenti.getValue() >= service.getAllStuds().size())
                    btnForwardSt.setDisable(true);
                else
                    btnForwardSt.setDisable(false);
                loadDataStudenti();
            }
        });
        comboBoxItemsTeme.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                indexTeme.set(Integer.valueOf(newValue.toString()));
                pageTeme.setValue(0);
                if(indexTeme.getValue() + pageTeme.getValue() >= service.getAllTeme().size())
                    btnForwardTeme.setDisable(true);
                else
                    btnForwardTeme.setDisable(false);
                loadDataTeme();
            }
        });

        pageStudenti.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if(newValue.intValue() == 0)
                    btnPreviousSt.setDisable(true);
                else
                    btnPreviousSt.setDisable(false);
                if(newValue.intValue() >= service.getAllStuds().size() - indexStudenti.getValue())
                    btnForwardSt.setDisable(true);
                else
                    btnForwardSt.setDisable(false);
            }
        });
        pageTeme.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if(newValue.intValue() == 0)
                    btnPreviousTeme.setDisable(true);
                else{
                    btnPreviousTeme.setDisable(false);
                }
                if(newValue.intValue() >= service.getAllTeme().size() - indexTeme.getValue())
                    btnForwardTeme.setDisable(true);
                else
                    btnForwardTeme.setDisable(false);
            }
        });

        btnForwardSt.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                pageStudenti.setValue(indexStudenti.getValue() + pageStudenti.getValue());
                loadDataStudenti();
            }
        });
        btnForwardTeme.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                pageTeme.setValue(indexTeme.getValue() + pageTeme.getValue());
                loadDataTeme();
            }
        });

        btnPreviousSt.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                pageStudenti.setValue(pageStudenti.getValue() - indexStudenti.getValue());
                loadDataStudenti();
            }
        });

        btnPreviousTeme.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                pageTeme.setValue(pageTeme.getValue() - indexTeme.getValue());
                loadDataTeme();
            }
        });
    }

    public void setTableColumns(){
        tableColumnIdForStud.setCellValueFactory(new PropertyValueFactory<Student, String>("id"));
        tableColumnNumeForStud.setCellValueFactory(new PropertyValueFactory<Student, String>("nume"));
        tableColumnGrupaForStud.setCellValueFactory(new PropertyValueFactory<Student, String>("grupa"));
        tableColumnEmailForStud.setCellValueFactory(new PropertyValueFactory<Student, String>("email"));
        tableColumnCadruForStud.setCellValueFactory(new PropertyValueFactory<Student, String>("cadruDidactic"));


        tableColumnCerintaForTema.setCellValueFactory(new PropertyValueFactory<TemaLaborator, String>("cerinta"));
        tableColumnDeadlineForTema.setCellValueFactory(new PropertyValueFactory<TemaLaborator, String>("deadline"));
        tableColumnNrTemaForTema.setCellValueFactory(new PropertyValueFactory<TemaLaborator, String>("numarTema"));

    }

    public void addNota(){
        if(tableViewTeme.getSelectionModel().getSelectedItem() == null || tableViewStudents.getSelectionModel().getSelectedItem() == null)
            showErrorMessage("Trebuie selectata o tema si un student");
        Student student = (Student) tableViewStudents.getSelectionModel().getSelectedItem();
        TemaLaborator temaLaborator = (TemaLaborator) tableViewTeme.getSelectionModel().getSelectedItem();
        try {
            if(service.add(new Nota(student.getId(), temaLaborator.getNumarTema(), Integer.valueOf(comboBoxNota.getSelectionModel().getSelectedItem().toString()),Integer.valueOf(comboBoxSaptPredarii.getSelectionModel().getSelectedItem().toString())), textFieldObservatii.getText()) != null)
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
        modelStud = FXCollections.observableArrayList(this.service.getBetweenStudent(indexStudenti.getValue(), pageStudenti.getValue()));
        modelTema = FXCollections.observableArrayList(this.service.getBetweenTeme(indexTeme.getValue(), pageTeme.getValue()));
        tableViewTeme.setItems(modelTema);
        tableViewStudents.setItems(modelStud);
        if(this.service.getAllTeme().size() <= indexTeme.getValue())
            btnForwardTeme.setDisable(true);
        if(this.service.getAllStuds().size() <= indexTeme.getValue())
            btnForwardSt.setDisable(true);

    }

    public void load(){
        //tableViewTeme.setItems(FXCollections.observableArrayList(service.getAllTeme()));
        //tableViewStudents.setItems(FXCollections.observableArrayList(service.getAllStuds()));
        loadDataStudenti();
        loadDataTeme();
    }

    public void loadDataStudenti(){
        if(indexStudenti.getValue() + pageStudenti.getValue() >= service.getAllStuds().size())
            btnForwardSt.setDisable(true);
        else
            btnForwardSt.setDisable(false);
        modelStud.setAll(FXCollections.observableArrayList(this.service.getBetweenStudent(indexStudenti.getValue(), pageStudenti.getValue())));
    }

    public void loadDataTeme(){
        if(indexTeme.getValue() + pageTeme.getValue() >= service.getAllTeme().size())
            btnForwardTeme.setDisable(true);
        else
            btnForwardTeme.setDisable(false);
        modelTema.setAll(FXCollections.observableArrayList(this.service.getBetweenTeme(indexTeme.getValue(), pageTeme.getValue())));
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
